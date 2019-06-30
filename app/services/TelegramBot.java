package services;

import bussiness.Venues;
import models.User;
import models.VenueList;
import models.communication.LoginResult;
import models.exceptions.FoursquareException;
import models.telegram.Update;
import models.venues.FSVenueSearch;
import utils.TelegramCommunicator;
import utils.TelegramState;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class TelegramBot {


    //Business Layer Injects
    private final Venues venuesBusiness;
    private final UsersService usersService;
    private final ListsService listsService;

    private TelegramCommunicator communicator;
    private TelegramState state;

    public TelegramBot(TelegramCommunicator comm,
                       TelegramState state,
                       UsersService usersService,
                       Venues bVenues,
                       ListsService listsService) {
        this.communicator = comm;
        this.state = state;

        this.usersService = usersService;
        this.venuesBusiness = bVenues;
        this.listsService = listsService;
    }

    private boolean userLoggedIn(Long chatId) {
        return state.loggedUserTokens.containsKey(chatId);
    }
    private Optional<String> getUserToken(Long chatId) {
        return userLoggedIn(chatId)
                ? Optional.of(state.loggedUserTokens.get(chatId))
                : Optional.empty();
    }

    public Consumer<TelegramBot> routeUpdate(Long chatId, String command, String message, Update update) {

        String[] parameters;
        if (state.pendingOperations.containsKey(chatId)) {
            command = state.pendingOperations.get(chatId);
            parameters = message.trim().split(" ");
            state.pendingOperations.remove(chatId);
        } else {
            parameters = message.substring(command.length()).trim().split(" ");
        }

        if (command.length() <= 0) {
            System.out.println("No command found on Telegram Update ");
            return telegramBot -> telegramBot.properOptions(chatId,false);
        }

        Consumer<TelegramBot> result;
        System.out.println("Switching for command "+ command);

        if (userLoggedIn(chatId)) {
            switch (command) {
                case "/logout":
                    result = telegramBot -> telegramBot.logout(chatId);
                    break;
                case "/lists":
                    result = telegramBot -> telegramBot.sendListsTo(chatId);
                    break;
                case "/displayList":
                    result = telegramBot -> telegramBot.displayList(chatId, message);
                    break;
                case "/search":
                    result = telegramBot -> telegramBot.startSearchVenueFor(chatId);
                    break;
                case "/searchVenueWhere":
                    result = telegramBot -> telegramBot.storeNearAndPromptForFilter(chatId, message, update);
                    break;
                case "/searchVenueNearLocation":
                    result = telegramBot -> telegramBot.searchVenuesAndDisplay(chatId, message);
                    break;
                case "/selectListToAddVenue":
                    result = telegramBot -> telegramBot.selectListToWhichAddVenue(chatId, parameters);
                    break;
                case "/submitVenueToList":
                    result = telegramBot -> telegramBot.submitVenueFromOptionsToList(chatId, message);
                    break;
                case "/start":
                    result = telegramBot -> telegramBot.startSpeech(chatId);
                    break;
                default:
                    return telegramBot -> telegramBot.properOptions(chatId,true);
            }
        } else {
            switch (command) {
                case "/login":
                    if (parameters.length != 2) {
                        return telegramBot -> {
                            telegramBot.communicator.sendMessage(chatId, "Invalid username or password");
                        };
                    }
                    var email = parameters[0];
                    var password = parameters[1];
                    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                        return telegramBot -> {
                            telegramBot.communicator.sendMessage(chatId, "Invalid username or password");
                        };
                    } else {
                        result = telegramBot -> telegramBot.login(chatId, parameters[0], parameters[1]);
                    }
                    break;
                case "/start":
                    result = telegramBot -> telegramBot.startSpeech(chatId);
                    break;
                default:
                    return telegramBot -> telegramBot.properOptions(chatId, false);
            }
        }

        return result;
    }

    private void properOptions(Long chatId, boolean loggedIn) {
        var options = loggedIn ?
                Stream.of( "/start", "/logout","/lists","/search" )
                 : Stream.of( "/start" );

        this.communicator.sendMessageWithOptions(
                chatId,
                "Sorry, i don't undertand that instruction, please try one of the following",
                options.iterator()
                );
    }

    private void startSpeech(Long chatId) {
        var reply = this.getUserToken(chatId)
                .map(s -> "Hey you!, you are logged in and your token is " + s)
                .orElse("Who are you, do we know each other? Introduce yourself using /login {email} {password}");

        this.communicator.sendMessage(chatId, reply);
    }


    private void echo(Long chatId, String message) {
        this.communicator.sendMessage(chatId, message);
    }

    private void login(Long chatId, String email, String password) {
        this.communicator.sendMessage(chatId, MESSAGES_PERFORMING_LOGIN);

        LoginResult result = usersService.login(email,password);

        if (result.success()) {
            this.communicator.sendMessage(chatId, MESSAGES_LOGIN_SUCCESS);
            state.loggedUserTokens.put(chatId, result.token);
        } else {
            postErrorToChat(chatId, "Invalid Username or Password");
        }
    }

    private void logout(Long chatId) {

        if (state.loggedUserTokens.containsKey(chatId)) {

            this.communicator.sendMessage(chatId, MESSAGES_LOGOUT_SUCCESS);
            state.loggedUserTokens.remove(chatId);

        }

    }

    private void sendListsTo(Long chatId) {

        getLoggedUserLists(chatId).ifPresent( lists -> {
            if (lists.size() > 0) {
                state.pendingOperations.put(chatId, "/displayList");

                sendListOptionsTo(chatId, lists);
            } else {
                this.communicator.sendMessage(chatId, "You don't have created lists");
            }
        });


    }

    private void displayList(Long chatId, String listChatId) {

        var list = (VenueList)state.pendingOperationOptions.get(chatId).get(listChatId);

        var text = new StringBuilder();
        text.append("*Venues at ");
        text.append(list.getName());
        text.append("*\n");

        list.getVenues().forEach(userVenue -> text.append("+").append(userVenue.getName()).append("\n"));

        this.communicator.sendMessage(chatId, text.toString());

    }

    private void startSearchVenueFor(Long chatId) {
        this.state.pendingOperations.put(chatId, "/searchVenueWhere");

        this.communicator.sendMessageRequestingLocation(chatId, "Where can we search the venues?", BUTTON_OPTION_NEAR_LOCATION);
    }

    private void storeNearAndPromptForFilter(Long chatId, String nearParameter, Update update) {

        if (nearParameter.equals("") && update.message.location != null){
            state.pendingOperations.put(chatId, "/searchVenueNearLocation");
            state.lastKnownLocations.put(chatId, update.message.location);

            communicator.sendMessage(chatId, "Please input the search filter");
        } else {

            state.pendingOperations.put(chatId, "/searchVenueWhere");
            communicator.sendMessage(chatId, "\"near\" parameter not recognized, please provide your location using the provided keyboard");
        }
    }

    private void searchVenuesAndDisplay(Long chatId, String filter) {

        if (state.lastKnownLocations.containsKey(chatId)) {

            var location = state.lastKnownLocations.get(chatId);

            try {
                var venues = venuesBusiness.search(filter,Venues.LAT_LONG_PARAM, location.latitude + "," + location.longitude);
                state.pendingOperationOptions.put(chatId, new HashMap<>());
                state.pendingOperations.put(chatId, "/selectListToAddVenue");
                var text = new StringBuilder();
                text.append(" Venues found: \n");

                for (int j = 0; j < venues.size(); j++) {
                    var jn = venues.get(j);
                    var addBuild = new StringBuilder();

                    jn.location.formattedAddress.forEach(al -> addBuild.append(al).append(" "));

                    text.append("/")
                            .append(j)
                            .append(" ")
                            .append(jn.name)
                            .append(" :")
                            .append(addBuild.toString())
                            .append("\n");

                    state.pendingOperationOptions.get(chatId).put("/"+ j, jn);
                }

                text.append("\n")
                    .append("Select one or input several (/x /y /z ...) if you want to add them at one of your lists");

                communicator.sendMessage(chatId, text.toString());

            } catch (FoursquareException e) {
                e.printStackTrace();
                communicator.sendMessage(chatId, "There was an error searching for the venues, please contact the admins");
            }


        } else {
            communicator.sendMessage(chatId, "No location provided for the user, please start the process with /search");
        }

    }

    private void selectListToWhichAddVenue(Long chatId, String[] venueIndexes) {



        getLoggedUserLists(chatId).ifPresent( lists -> {
            if (lists.size() > 0) {
                state.pendingOperations.put(chatId, "/submitVenueToList");

                var venues = Arrays.stream(venueIndexes).map(x -> (FSVenueSearch) state.pendingOperationOptions.get(chatId).get(x)).collect(Collectors.toList());

                state.workingVenues.put(chatId, venues);

                this.communicator.sendMessage(chatId, "Please select a list to add the venue"+ (venues.size() > 1 ? "s" : ""));

                this.sendListOptionsTo(chatId, lists);
            } else {
                this.communicator.sendMessage(chatId, "You don't have created lists");
            }
        });
    }

    private void sendListOptionsTo(Long chatId, List<VenueList> lists) {
        state.pendingOperationOptions.put(chatId, new HashMap<>());

        var text = new StringBuilder();
        text.append(" You have the following lists: \n");

        for (int j = 0; j < lists.size(); j++) {
            VenueList jn = lists.get(j);

            text.append("/")
                    .append(j)
                    .append(" ")
                    .append(jn.getName())
                    .append("\n");

            state.pendingOperationOptions.get(chatId).put("/"+ j, jn);
        }

        communicator.sendMessage(chatId, text.toString());
    }

    private void submitVenueFromOptionsToList(Long chatId, String listKey) {

        var list = (VenueList) state.pendingOperationOptions.get(chatId).get(listKey);
        var venues = state.workingVenues.get(chatId);

        var user = getLoggedUser(chatId);
        if (user.isEmpty()) {
            return;
        }

        var success = usersService.addVenuesToList(user.get(), list, venues);

        if (success) {
            this.communicator.sendMessage(chatId,"Item"+ (venues.size() > 1 ? "s":"")+" added!");
        } else {
            this.communicator.sendMessage(chatId, "Invalid username or password");
        }
    }


    private Optional<List<VenueList>> getLoggedUserLists(Long chatId) {
        var user = getLoggedUser(chatId);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(user.get().getAllLists());
    }

    private Optional<User> getLoggedUser(Long chatId) {
        var token = this.getUserToken(chatId);

        if (token.isEmpty()) {
            postErrorToChat(chatId, "Token was empty");
            return Optional.empty();
        }


        Map<String, Object> map;
        try {
            map = CodesService.decodeMapFromToken(token.get());
        } catch (Exception e) {
            postErrorToChat(chatId, e);
            return Optional.empty();
        }

        var user = usersService.findById(map.get("userId").toString());

        if (user.isEmpty()) {
            postErrorToChat(chatId, MESSAGES_USER_NOT_FOUND);
            return Optional.empty();
        }
        return user;

    }

    private void postErrorToChat(Long chatId, String message) {
        System.out.println(message);
        this.communicator.sendMessage(chatId, MESSAGES_ERROR_FOR_USER);
    }

    private void postErrorToChat(Long chatId, Exception e) {
        e.printStackTrace();
        this.communicator.sendMessage(chatId, MESSAGES_ERROR_FOR_USER);
    }

    public static String MESSAGES_PERFORMING_LOGIN = "Thanks!, loggin in";
    public static String MESSAGES_LOGIN_SUCCESS = "Logged in successfully!";
    public static String MESSAGES_LOGOUT_SUCCESS = "See you next time!";
    public static String MESSAGES_ERROR_FOR_USER = "Sorry, there was an error, please try again or contact the admins";
    public static String MESSAGES_USER_NOT_FOUND = "User not found, please communicate with the admins";
    public static String BUTTON_OPTION_NEAR_LOCATION = "Near my location";



}
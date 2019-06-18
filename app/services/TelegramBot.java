package services;

import bussiness.Venues;
import models.User;
import models.VenueList;
import models.communication.LoginResult;
import models.telegram.Update;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import utils.TelegramComunicator;
import utils.TelegramState;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;



public final class TelegramBot {


    //Business Layer Injects
    private final Venues venuesBusiness;
    private final UsersService usersService;
    private final ListsService listsService;

    private TelegramComunicator comunicator;
    private TelegramState state;

    public TelegramBot(TelegramComunicator comm,
                       TelegramState state,
                       UsersService usersService,
                       Venues bVenues,
                       ListsService listsService) {
        this.comunicator = comm;
        this.state = state;

        this.usersService = usersService;
        this.venuesBusiness = bVenues;
        this.listsService = listsService;
    }

    public Optional<String> getUserToken(Integer chatId) {
        return state.loggedUserTokens.containsKey(chatId)
                ? Optional.of(state.loggedUserTokens.get(chatId))
                : Optional.empty();
    }

    public Consumer<TelegramBot> routeUpdate(Integer chatId, String command, String message, Update update) {

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
            return telegramBot -> { };
        }

        Consumer<TelegramBot> result;

        switch (command) {
            case "/login":

                if (parameters.length != 2) {
                    return telegramBot -> { };
                }
                var email = parameters[0];
                var password = parameters[1];
                if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                    return telegramBot -> { };
                } else {
                    result = telegramBot -> telegramBot.login(chatId, parameters[0], parameters[1]);
                }

                break;

            case "/logout":
                result = telegramBot -> telegramBot.logout(chatId);
                break;

            case "/lists":
                result = telegramBot -> telegramBot.sendListsTo(chatId);
                break;

            case "/displayList":

                result = telegramBot -> telegramBot.displayList(chatId, message);
                break;

            case "/start":
            default:
                return telegramBot -> { };
        }

        return result;
    }

    private void echo(Integer chatId, String message) {
        this.comunicator.sendMessage(chatId, message);
    }

    private void login(Integer chatId, String email, String password) {
        this.comunicator.sendMessage(chatId, MESSAGES_PERFORMING_LOGIN);

        LoginResult result = usersService.login(email,password);

        if (result.success()) {
            this.comunicator.sendMessage(chatId, MESSAGES_LOGIN_SUCCESS);
            state.loggedUserTokens.put(chatId, result.token);
        } else {
            this.comunicator.sendMessage(chatId, MESSAGES_ERROR_FOR_USER);
        }
    }

    private void logout(Integer chatId) {

        if (state.loggedUserTokens.containsKey(chatId)) {

            this.comunicator.sendMessage(chatId, MESSAGES_LOGOUT_SUCCESS);
            state.loggedUserTokens.remove(chatId);

        }

    }

    private void sendListsTo(Integer chatId) {

        var token = this.getUserToken(chatId);

        if (token.isEmpty()) {
            this.comunicator.sendMessage(chatId, MESSAGES_ERROR_FOR_USER);
            return;
        }


        Map<String, Object> map = null;
        try {
            map = CodesService.decodeMapFromToken(token.get());
        } catch (Exception e) {
            this.comunicator.sendMessage(chatId, MESSAGES_ERROR_FOR_USER);
            return;
        }

        Optional<User> user = usersService.findById(map.get("userId").toString());

        if (user.isEmpty()) {
            this.comunicator.sendMessage(chatId, MESSAGES_USER_NOT_FOUND);
            return;
        }

        var lists = user.get().getAllLists();
        if (lists.size() > 0) {
            state.pendingOperations.put(chatId, "/displayList");
            var listNames = lists.stream().map(VenueList::getChatId).iterator();

            this.comunicator.sendMessageWithOptions(chatId,"You have the following lists: ",listNames);
        } else {
            this.comunicator.sendMessage(chatId, "You don't have created lists");
        }
    }

    private void displayList(Integer chatId, String listChatId) {

        var list = listsService.getById(VenueList.getIdFromChatId(listChatId)).get();


        var text = new StringBuilder();
        text.append("* Venues at ");
        text.append(list.getName());
        text.append("\n");

        list.getVenues().forEach(userVenue -> text.append("- ").append(userVenue.getName()).append("\n"));

        this.comunicator.sendMessage(chatId, text.toString());

    }


    @Deprecated()
    public Result handleUpdate(Update update, Http.Request request) {

/*

            case "/search":



                var latitude = update.message.location.get().latitude;
                var longitude = update.message.location.get().latitude;

                try {
                    var venues = venuesBusiness.search(parameters,Venues.LAT_LONG_PARAM, latitude + "," + longitude);

                    this.sendSelections(update.getChatId(), "Found Venues","/selectVenue", venues.elements());

                } catch (FoursquareException e) {
                    e.printStackTrace();

                    reply = "There was an error fetching venues: "+ e.errorType + " " + e.errorText;
                    this.sendMessage(update.getChatId(), reply);
                }

                break;
*/
        return Results.ok();

    }


    public static String MESSAGES_PERFORMING_LOGIN = "Thanks!, loggin in";
    public static String MESSAGES_LOGIN_SUCCESS = "Logged in successfully!";
    public static String MESSAGES_LOGOUT_SUCCESS = "See you next time!";
    public static String MESSAGES_ERROR_FOR_USER = "Sorry, there was an error, please try again or contact the admins";
    public static String MESSAGES_USER_NOT_FOUND = "User not found, please communicate with the admins";



}
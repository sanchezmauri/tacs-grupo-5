package services;

import bussiness.Venues;
import models.User;
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
import java.util.function.Function;


public final class TelegramBot {


    //Business Layer Injects
    private final Venues venuesBusiness;
    private final UsersService usersService;

    private TelegramComunicator comunicator;
    private TelegramState state;

    public TelegramBot(TelegramComunicator comm,
                       TelegramState state,
                       UsersService usersService,
                       Venues bVenues) {
        this.comunicator = comm;
        this.state = state;

        this.usersService = usersService;
        this.venuesBusiness = bVenues;
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

            case "/start":
            default:
                return telegramBot -> { };
        }

        return result;
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

    @Deprecated()
    public Result handleUpdate(Update update, Http.Request request) {

        String message = update.getMessageText();

        if (message.length() <= 0) {
            System.out.println("Empty message received on Telegram Update "+update.updateId);
            return Results.noContent();
        }

        System.out.println("Received Update from :" + update.getChatId() + ", message: " + message);
        System.out.println("Payload: {" + update.originalJson + "}");


        String command;
        String parameters;
        if (state.pendingOperations.containsKey(update.getChatId())) {
            command = state.pendingOperations.get(update.getChatId());
            parameters = message.trim();
            state.pendingOperations.remove(update.getChatId());
        } else {
            command = update.getCommand().orElse("");
            parameters = message.substring(command.length()).trim();
        }

        if (command.length() <= 0) {
            System.out.println("No command found on Telegram Update "+update.updateId);
            return Results.noContent();
        }



        String reply;
        switch(command) {
            case "/login":

                this.comunicator.maskMessage(update, "Thanks!, processing request");

                String email = parameters.split(" ")[0];
                String password = parameters.split(" ")[1];

                LoginResult result = usersService.login(email,password);

                if (result.success()) {
                    this.comunicator.maskMessage(update, "Logged in successfully");
                    return Results.ok().addingToSession(request,"token",result.token);
                } else {
                    this.comunicator.maskMessage(update, "There was an error :C");
                    return Results.noContent();
                }
            case "/start":
                reply = request.session().getOptional("token")
                        .map(s -> "Hey you!, you are logged in and your token is " + s)
                        .orElse("Who are you, do we know each other? Introduce yourself using /login {email} {password}");

                this.comunicator.sendMessage(update.getChatId(), reply);

                break;
            case "/lists":

                String token;
                if (request.session().data().containsKey("token")) {
                    token = request.session().data().get("token");
                } else {
                    this.comunicator.sendMessage(update.getChatId(),"Session doesn't include token");
                    return Results.ok();
                }
                Map<String, Object> map = null;
                try {
                    map = CodesService.decodeMapFromToken(token);
                } catch (Exception e) {
                    this.comunicator.sendMessage(update.getChatId(),"Session doesn't include token");
                    return Results.ok();
                }
                Optional<User> user = usersService.findById(map.get("userId").toString());

                if (user.isEmpty()) {
                    this.comunicator.sendMessage(update.getChatId(),"User not found, please communicate with the admins");
                    return Results.ok();
                }

                var lists = user.get().getAllLists();
                if (lists.size() > 0) {
                    state.pendingOperations.put(update.getChatId(), "/displayList");

                    this.comunicator.sendMessageWithOptions(update.getChatId(),"You have the following lists: ",lists.iterator());
                } else {
                    this.comunicator.sendMessage(update.getChatId(), "You don't have created lists");
                }

                break;

            case "/displayList":

                this.comunicator.sendMessage(update.getChatId(), "Echo Test:" + message);
                break;
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
            default:
                break;
        }

        return Results.ok();

    }


    public static String MESSAGES_PERFORMING_LOGIN = "Thanks!, loggin in";
    public static String MESSAGES_LOGIN_SUCCESS = "Logged in successfully!";
    public static String MESSAGES_ERROR_FOR_USER = "Sorry, there was an error, please try again or contact the admins";


}
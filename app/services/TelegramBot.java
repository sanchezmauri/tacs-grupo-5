package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import controllers.Utils;
import controllers.VenuesController;
import models.communication.LoginResult;
import models.telegram.Message;
import models.telegram.Update;
import play.libs.ws.*;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;


public final class TelegramBot {
    private final String endpoint;
    public final String token;

    private final WSClient ws;

    //TODO: Refactor this, due to time constraints we couldn't extract the logic from this controller onto a proper business layer
    private final VenuesController vc;

    public TelegramBot(Config config, WSClient ws) {
        this.ws = ws;
        this.vc = new VenuesController(config,ws);
        this.endpoint = config.getString("telegram.url");
        this.token = config.getString("telegram.token");
    }

    private void sendMessage(Integer chatId, String text) {

        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("text", text);

        ws.url(endpoint + token + "/sendMessage")
                    .post(body)
                    .thenApply(Message::fromWSResponse);
    }

    private void maskMessage(Update update, String newText) {

        JsonNode body = Json.newObject()
                .put("chat_id", update.getChatId())
                .put("message_id",update.getMessageId())
                .put("text", newText);

        ws.url(endpoint + token + "/editMessageText")
                .post(body);
    }

    public Result handleUpdate(Update update, Http.Request request) {

        String message = update.getMessageText();

        if (message.length() <= 0) {
            System.out.println("Empty message received on Telegram Update "+update.updateId);
            return Results.noContent();
        }

        String command = Utils.getCommandFrom(message);

        if (command.length() <= 0) {
            System.out.println("No command found on Telegram Update "+update.updateId);
            return Results.noContent();
        }

        String parameters = message.substring(command.length(),message.length()).trim();

        String reply;
        switch(command) {
            case "/login":

                this.maskMessage(update, "Thanks!, processing request");

                String email = parameters.split(" ")[0];
                String password = parameters.split(" ")[1];

                LoginResult result = UsersService.login(email,password);

                if (result.success()) {
                    this.maskMessage(update, "Logged in successfully");
                    return Results.ok().addingToSession(request,"token",result.token);
                } else {
                    this.maskMessage(update, "There was an error :C");
                    return Results.unauthorized(Utils.createErrorMessage("Credenciales invalidas."));
                }
            case "/start":
                reply = request.session().getOptional("token")
                        .map(s -> "Hey you!, you are logged in and your token is " + s)
                        .orElse("Who are you, do we know each other? Introduce yourself using /login {email} {password}");

                this.sendMessage(update.getChatId(), reply);
            case "/users":
                reply = UsersService.index().toString();
                this.sendMessage(update.getChatId(), reply);
                break;
            case "/getUser":
                reply = UsersService.findById(parameters).toString();

                sendMessage(update.getChatId(), reply);
                break;
            case "/venuesSince":
                reply = vc.countVenuesAddedSince(parameters).toString();

                sendMessage(update.getChatId(), reply);
                break;
            default:
                break;
        }

        return Results.ok();

    }


}
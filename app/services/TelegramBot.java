package services;

import bussiness.Venues;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import controllers.Utils;
import models.communication.LoginResult;
import models.exceptions.FoursquareException;
import models.telegram.Message;
import models.telegram.Update;
import play.libs.ws.*;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import utils.TelegramUtils;

import java.util.Iterator;


public final class TelegramBot {
    private final String endpoint;
    public final String token;

    private final WSClient ws;



    //Business Layer Injects
    private final Venues venuesBusiness;

    public TelegramBot(Config config, WSClient ws, Venues bVenues) {
        this.ws = ws;

        this.venuesBusiness = bVenues;
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

    private void sendSelections(Integer chatId, String title, String responsePrefix, Iterator<JsonNode> elements) {

        var replyMarkup = TelegramUtils.getKeyboard(title, elements);

        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("parse_mode","Markdown")
                .put("text", replyMarkup.get("option-text").textValue())
                .set("reply_markup", replyMarkup);

        ws.url(endpoint + token + "/sendMessage")
                .post(body)
                .thenApply(Message::fromWSResponse);
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

        String parameters = message.substring(command.length()).trim();

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

            default:
                break;
        }

        return Results.ok();

    }



}
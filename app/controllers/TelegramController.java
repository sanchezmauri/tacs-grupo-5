package controllers;

import bussiness.Venues;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.typesafe.config.Config;
import models.telegram.Update;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.TelegramBot;

import javax.inject.Inject;

public class TelegramController extends Controller {

    private final TelegramBot bot;

    @Inject
    public TelegramController(Config config, WSClient ws, Venues bVenues) {
        this.bot = new TelegramBot(config, ws, bVenues);
    }

    public Result receiveUpdate(String token, Http.Request request) {

        Result result;
        if (token.equalsIgnoreCase(bot.token)) {

            try {

                Update update = Update.fromJson(request.body().asJson());

                return bot.handleUpdate(update, request);

            } catch (JsonProcessingException ex) {
                System.out.println("Error processing Update for Telegram bot");
                result = internalServerError(ex.getMessage());
            }

        } else {

            result = badRequest("Invalid Bot Token");

        }

        return result;

    }
}
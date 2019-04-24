package controllers;

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
    public TelegramController(Config config, WSClient ws) {
        this.bot = new TelegramBot(config,ws);
    }

    public Result receiveUpdate(String token, Http.Request request) {

        Result result;
        if (token.equalsIgnoreCase(bot.token)) {

            try {

                Update update = Update.fromJson(request.body().asJson());

                bot.handleUpdate(update);

                return noContent();

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
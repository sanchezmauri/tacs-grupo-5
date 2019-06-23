package controllers;

import bussiness.Venues;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.typesafe.config.Config;
import models.telegram.Update;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.ListsService;
import services.TelegramBot;
import services.UsersService;
import utils.TelegramCommunicator;
import utils.TelegramState;

import javax.inject.Inject;
import java.util.Arrays;

public class TelegramController extends Controller {

    private final TelegramBot bot;
    private final TelegramCommunicator comunicator;


    @Inject
    public TelegramController(Config config, WSClient ws,
                              TelegramState state,
                              Venues bVenues,
                              UsersService usersService,
                              ListsService listsService) {

        var endpoint = config.getString("telegram.url");
        var token = config.getString("telegram.token");

        this.comunicator = new TelegramCommunicator(ws,endpoint,token);
        this.bot = new TelegramBot(comunicator, state, usersService, bVenues, listsService);
    }

    public Result receiveUpdate(String token, Http.Request request) {

        Result result;
        if (token.equalsIgnoreCase(comunicator.token)) {

            try {

                Update update = Update.fromJson(request.body().asJson());

                var callback = bot.routeUpdate(update.getChatId(), update.getCommand().orElse(""), update.getMessageText(), update);

                callback.accept(bot);


                return ok();

            } catch (JsonProcessingException ex) {
                System.out.println("Error processing Update for Telegram bot");
                System.out.println(request.body().asJson().toString());
                result = noContent();//internalServerError(ex.getMessage());
            } catch (Exception e) {
                System.out.println("There was an unexpected error");
                System.out.println(request.body().asJson().toString());
                System.out.println(e.getMessage());
                System.err.print(Arrays.toString(e.getStackTrace()));
                result = noContent();
            }

        } else {
            System.out.println("Invalid Bot Token");
            result = noContent();

        }

        return result;

    }
}
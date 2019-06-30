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
import java.util.Optional;

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
        String stepStatus = "Starting";
        Optional<Long> chatId = Optional.empty();
        Integer updateId = 0;
        try {
            if (token.equalsIgnoreCase(comunicator.token)) {

                Update update = Update.fromJson(request.body().asJson());
                stepStatus = "Got Update";
                chatId = Optional.of(update.getChatId());
                updateId = update.updateId;

                stepStatus = "Identified Chat";
                var callback = bot.routeUpdate(chatId.get(), update.getCommand().orElse(""), update.getMessageText(), update);
                stepStatus = "Routed Update";
                callback.accept(bot);
                stepStatus = "Finished";
                result = ok();

            } else {
                System.out.println("Invalid Bot Token");
                result = noContent();
            }
        }
        catch (Exception e) {
            System.out.println("There was an unexpected error");
            System.out.println("Request Body: ");
            System.out.println(request.body().asJson().toString());
            System.out.println("Last Status: "+ stepStatus);

            if (chatId.isPresent()) {
                comunicator.sendMessage(
                        chatId.get(),
                        "There was an error, please contact the dev team with this number: "+updateId
                );
            }

            result = noContent();
        }

        return result;

    }
}
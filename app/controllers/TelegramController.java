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
import services.UsersService;
import utils.TelegramComunicator;

import javax.inject.Inject;
import java.util.Arrays;

public class TelegramController extends Controller {

    private final TelegramBot bot;
    private final TelegramComunicator comunicator;

    @Inject
    public TelegramController(Config config, WSClient ws, Venues bVenues, UsersService usersService) {

        var endpoint = config.getString("telegram.url");
        var token = config.getString("telegram.token");

        this.comunicator = new TelegramComunicator(ws,endpoint,token);

        this.bot = new TelegramBot(comunicator,usersService, bVenues);
    }

    public Result receiveUpdate(String token, Http.Request request) {

        Result result;
        if (token.equalsIgnoreCase(comunicator.token)) {

            try {



                Update update = Update.fromJson(request.body().asJson());

                if (update.getCommand().orElse("").equals("/start")) {
                    var reply = request.session().getOptional("token")
                            .map(s -> "Hey you!, you are logged in and your token is " + s)
                            .orElse("Who are you, do we know each other? Introduce yourself using /login {email} {password}");

                    comunicator.sendMessage(update.getChatId(), reply);
                    result = ok();
                }
                else {
                    var callback = bot.routeUpdate(update.getChatId(), update.getCommand().orElse(""), update.getMessageText(), update);

                    var response = callback.apply(bot);

                    if (response.has("token")) {
                        var userToken = response.get("token").textValue();

                        result = ok().addingToSession(request, "token", userToken);
                    } else {
                        result = ok();
                    }
                }

                return result;

            } catch (JsonProcessingException ex) {
                System.out.println("Error processing Update for Telegram bot");
                result = internalServerError(ex.getMessage());
            } catch (Exception e) {
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
package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import controllers.VenuesController;
import models.telegram.Message;
import models.telegram.Update;
import play.libs.ws.*;
import play.libs.Json;
import repos.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static play.mvc.Results.badRequest;

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

    private CompletionStage<Optional<Message>> sendMessage(Integer chatId, String text) {

        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("text", text);

            return ws.url(endpoint + token + "/sendMessage")
                    .post(body)
                    .thenApply(Message::fromWSResponse);
    }

    public void handleUpdate(Update update) {
        if (update.getMessageText().contains("/users")) {
            String reply = UserRepository.all().toString();
            sendMessage(update.getChatId(), reply);
        } else if (update.getMessageText().contains("/getUser")) {
            Long param = Long.parseLong(update.getMessageText().substring("/getUser".length(), update.getMessageText().length()));

            String reply = UserRepository.find(param).toString();

            sendMessage(update.getChatId(), reply);
        } else if (update.getMessageText().contains("/venuesSince")) {
            String param = update.getMessageText().substring("/venuesSince".length(), update.getMessageText().length());

            String reply = vc.countVenuesAddedSince(param).toString();


            sendMessage(update.getChatId(), reply);
        }
    }


}
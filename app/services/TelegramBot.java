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

public final class TelegramBot implements Runnable {
    private final String endpoint;
    private final String token;

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

    private CompletionStage<Optional<List<Update>>> getUpdates(Integer offset) {

        JsonNode body = Json.newObject()
                .put("offset", offset);

        return ws.url(endpoint + token + "/getUpdates")
                .setContentType("application/json")
                .post(body)
                .thenApply(Update::arrayFromWSResponse);
    }

    public void run() {
        int last_update_id = 0; // last processed command
        CompletionStage<Optional<List<Update>>> fetchingRequest;
        while (true) {

            fetchingRequest = getUpdates(last_update_id);

            try {

                Optional<List<Update>> updates = fetchingRequest.toCompletableFuture().get();

                if (updates.isPresent()) {

                    if (updates.get().size() > 0) {
                        last_update_id = updates.get().stream().mapToInt(x -> x.updateId).max().orElse(last_update_id) + 1;

                        updates.get().parallelStream().forEach(update -> {
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
                        });
                    }


                }

            }
            catch (InterruptedException | ExecutionException ignored)
            {

            }


        }
    }
}
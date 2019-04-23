package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import models.telegram.Message;
import models.telegram.Update;
import play.libs.ws.*;
import play.libs.Json;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static play.mvc.Results.badRequest;

public final class TelegramBot implements Runnable {
    private final String endpoint = "https://api.telegram.org/bot";
    private final String token = "804708943:AAHDX6vi8DJw-ZGUTA-wyTnfbThsYtdJ7eY";

    private final WSClient ws;

    public TelegramBot(Config config, WSClient ws) {
        this.ws = ws;
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
                            if (update.getMessageText().contains("/start")) {
                                String reply = "Hi, this is an example bot\n" +
                                        "Your chat_id is " + update.getChatId() + "\n" +
                                        "Your username is " + update.getUsername();
                                sendMessage(update.getChatId(), reply);
                            } else if (update.getMessageText().contains("/echo")) {
                                sendMessage(update.getChatId(), "Received " + update.getMessageText());
                            } else if (update.getMessageText().contains("/toupper")) {
                                String param = update.getMessageText().substring("/toupper".length(), update.getMessageText().length());
                                sendMessage(update.getChatId(), param.toUpperCase());
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
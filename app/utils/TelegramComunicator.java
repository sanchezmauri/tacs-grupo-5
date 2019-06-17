package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.telegram.Message;
import models.telegram.Update;
import play.libs.Json;
import play.libs.ws.WSClient;

import java.util.Iterator;

public class TelegramComunicator {

    private final WSClient ws;
    private final String endpoint;
    public final String token;

    public TelegramComunicator(WSClient ws, String endpoint, String token) {
        this.ws = ws;
        this.endpoint = endpoint;
        this.token = token;
    }

    public void sendMessage(Integer chatId, String text) {
        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("text", text);

        ws.url(endpoint + token + "/sendMessage")
                .post(body)
                .thenApply(Message::fromWSResponse);
    }

    public void maskMessage(Update update, String newText) {
        JsonNode body = Json.newObject()
                .put("chat_id", update.getChatId())
                .put("message_id",update.getMessageId())
                .put("text", newText);

        ws.url(endpoint + token + "/editMessageText")
                .post(body);
    }


    public <K> void sendMessageWithOptions(Integer chatId, String message, Iterator<K> elements) {

        var replyMarkup = TelegramUtils.getKeyboard(elements);

        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("parse_mode","Markdown")
                .put("text", message)
                .set("reply_markup", replyMarkup);

        ws.url(endpoint + token + "/sendMessage")
                .post(body)
                .thenApply(Message::fromWSResponse);
    }

}

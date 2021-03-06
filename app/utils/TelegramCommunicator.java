package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.telegram.Message;
import models.telegram.Update;
import play.libs.Json;
import play.libs.ws.WSClient;

import java.util.Iterator;

public class TelegramCommunicator {

    private final WSClient ws;
    private final String endpoint;
    public final String token;

    public TelegramCommunicator(WSClient ws, String endpoint, String token) {
        this.ws = ws;
        this.endpoint = endpoint;
        this.token = token;
    }

    public void sendMessage(Long chatId, String text) {
        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("parse_mode","Markdown")
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


    public <K> void sendMessageWithOptions(Long chatId, String message, Iterator<K> elements) {

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

    public void sendMessageRequestingLocation(Long chatId, String message, String buttonText) {

        var keyboard = Json.newArray();
        var row = Json.newArray();

        var button = Json.newObject()
                .put("text", buttonText)
                .put("request_location", true);

        row.add(button);
        keyboard.add(row);

        var replyMarkup = Json.newObject()
                .put("resize_keyboard",true)
                .put("one_time_keyboard", true)
                .set("keyboard", keyboard);


        JsonNode body = Json.newObject()
                .put("chat_id", chatId)
                .put("text", message)
                .set("reply_markup", replyMarkup);

        ws.url(endpoint + token + "/sendMessage")
                .post(body)
                .thenApply(Message::fromWSResponse);
    }

}

package models.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.ws.WSResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class Update {

    @JsonProperty("update_id")
    public Integer updateId;


    public Message message;

    @JsonProperty("edited_message")
    public Message editedMessage;


    public String getMessageText() {
        return message != null ? message.text : editedMessage.text;
    }

    public Integer getChatId() {
        return message != null ? message.chat.id : editedMessage.chat.id;
    }

    public String getUsername() {
        return message != null ? message.chat.username : editedMessage.chat.username;
    }

    static Optional<Update> fromWSResponse(WSResponse response) {

        if (response.getStatus() != 200) {
            return Optional.empty();
        }

        try {
            Update value = Update.fromJson(response.asJson());
            return Optional.of(value);
        } catch (JsonProcessingException ex){
            return Optional.empty();
        }
    }

    private static Update fromJson(JsonNode jsonNode) throws JsonProcessingException {
       return new ObjectMapper().treeToValue(jsonNode,Update.class);
    }

    public static Optional<List<Update>> arrayFromWSResponse(WSResponse response) {

        if (response.getStatus() != 200) {
            return Optional.empty();
        }

        try {
            JsonNode arrNode = response.asJson().get("result");
            if (arrNode.isArray()) {
                List<Update> values = new ArrayList<>();
                for (final JsonNode objNode : arrNode) values.add(Update.fromJson(objNode));
                return Optional.of(values);
            } else {
                return Optional.empty();
            }
        } catch (JsonProcessingException ex){

            System.out.println(ex.getMessage());
            return Optional.empty();
        }
    }
}
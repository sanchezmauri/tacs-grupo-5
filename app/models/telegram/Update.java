package models.telegram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.ws.WSResponse;

import java.io.IOException;
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

    @JsonIgnore
    public String originalJson;

    public String getMessageText() {
        return message != null ? message.text : editedMessage.text;
    }

    public Integer getChatId() {
        return message != null ? message.chat.id : editedMessage.chat.id;
    }

    public Integer getMessageId() {
        return message != null ? message.messageId : editedMessage.messageId;
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

    public static Update fromJson(JsonNode jsonNode) throws JsonProcessingException {
        Update update = new ObjectMapper().treeToValue(jsonNode,Update.class);
        update.originalJson = jsonNode.toString();
        return update;
    }

    public static Update fromJsonString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;

            node = mapper.readTree(json);

        return Update.fromJson(node);
    }

    public Optional<String> getCommand() {
        return this.message.entities
                .stream()
                .filter(e -> e.type.equals("bot_command"))
                .findAny()
                .map( e -> this.getMessageText().substring(e.offset, e.offset + e.length));

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
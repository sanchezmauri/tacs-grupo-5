package models.telegram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.ws.*;

import java.util.List;
import java.util.Optional;


@JsonIgnoreProperties({"forward_from", "forward_from_chat", "forward_from_message_id", "forward_signature", "forward_sender_name", "forward_date", "reply_to_message", "edit_date", "media_group_id", "author_signature", "caption_entities", "audio", "document", "animation", "game", "photo", "sticker", "video", "voice", "video_note", "caption", "contact", "venue", "poll", "new_chat_members", "left_chat_member", "new_chat_title", "new_chat_photo", "delete_chat_photo", "group_chat_created", "supergroup_chat_created", "channel_chat_created", "migrate_to_chat_id", "migrate_from_chat_id", "pinned_message", "invoice", "successful_payment", "connected_website", "passport_data"})
public class Message {


    /*
     * "message_id":4,
     *       "from":  {
     *           "id":277016262,
     *           "is_bot":false,
     *           "first_name":"Martin",
     *           "last_name":"Loguancio",
     *           "username":"maadlog",
     *           "language_code":"es"
     *       },
     *       "chat": {
     *           "id":277016262,
     *           "first_name":"Martin",
     *           "last_name":"Loguancio",
     *           "username":"maadlog",
     *           "type":"private"
     *       },
     *       "date":1556048035,
     *       "text":"/start",
     *       "entities":[{"offset":0,"length":6,"type":"bot_command"}]
    * */

    @JsonProperty("message_id") public Integer messageId;
    public String date;
    public String text;
    public User from;
    public Chat chat;
    public List<MessageEntity> entities;
    public Optional<Location> location;

    public static Optional<Message> fromWSResponse(WSResponse response) {
        if (response.getStatus() != 200) {
            System.out.println(response.asJson());
            return Optional.empty();
        }
        try {
            Message value = new ObjectMapper().treeToValue(response.asJson(),Message.class);
            return Optional.of(value);
        } catch (JsonProcessingException ex){
            return Optional.empty();
        }


    }
}

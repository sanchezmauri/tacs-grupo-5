package models.telegram;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateTests {

    @Test
    public void Update_FromJsonStart_SolvesCommandCorrectly(){

        try {
            String updateExample = "{\"update_id\":44388790,\"message\":{\"message_id\":169,\"from\":{\"id\":277016262,\"is_bot\":false,\"first_name\":\"Martin\",\"last_name\":\"Loguancio\",\"username\":\"maadlog\",\"language_code\":\"es\"},\"chat\":{\"id\":277016262,\"first_name\":\"Martin\",\"last_name\":\"Loguancio\",\"username\":\"maadlog\",\"type\":\"private\"},\"date\":1560783213,\"text\":\"/start\",\"entities\":[{\"offset\":0,\"length\":6,\"type\":\"bot_command\"}]}}";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(updateExample);


            var update = Update.fromJson(actualObj);

            assertEquals("/start", update.getCommand().orElse(""));

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void Update_FromJsonLogin_SolvesCommandCorrectly(){

        try {
            String updateLogin = "{\"update_id\":44388793,\"message\":{\"message_id\":173,\"from\":{\"id\":277016262,\"is_bot\":false,\"first_name\":\"Martin\",\"last_name\":\"Loguancio\",\"username\":\"maadlog\",\"language_code\":\"es\"},\"chat\":{\"id\":277016262,\"first_name\":\"Martin\",\"last_name\":\"Loguancio\",\"username\":\"maadlog\",\"type\":\"private\"},\"date\":1560784500,\"text\":\"/login root@root.com root\",\"entities\":[{\"offset\":0,\"length\":6,\"type\":\"bot_command\"},{\"offset\":7,\"length\":13,\"type\":\"email\"}]}}";

            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(updateLogin);

            var update = Update.fromJson(actualObj);

            assertEquals("/login", update.getCommand().orElse(""));


        } catch (Exception e){
            e.printStackTrace();
        }

    }


}

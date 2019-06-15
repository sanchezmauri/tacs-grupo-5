package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TelegramUtilsTest {

    @Test
    public void getKeyboardTest(){

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode actualObj = mapper.readTree("[ {\"name\":\"Jonh0\"}, {\"name\":\"Jonh1\"}, {\"name\":\"Jonh3\"}]");

            var keyboard = TelegramUtils.getKeyboard("Test", actualObj.elements());

            assertEquals("", mapper.writeValueAsString(keyboard));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}

package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collector;

import static org.junit.Assert.assertEquals;

public class TelegramUtilsTest {

    @Test
    public void getKeyboardTest(){

        ObjectMapper mapper = new ObjectMapper();
        try {
            var elements = new ArrayList<String>();
            elements.add("Element 1");
            elements.add("Element 2");
            elements.add("Element 3");
            elements.add("Element 4");

            var keyboard = TelegramUtils.getKeyboard(elements.iterator());

            assertEquals("{\"resize_keyboard\":true,\"keyboard\":[[\"Element 1\",\"Element 4\"],[\"Element 2\"],[\"Element 3\"]]}", mapper.writeValueAsString(keyboard));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}

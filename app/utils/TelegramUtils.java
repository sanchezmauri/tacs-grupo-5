package utils;

import bussiness.Venues;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import javax.inject.Inject;
import java.util.Iterator;

public class TelegramUtils {

    public static JsonNode getKeyboard(String titled, Iterator<JsonNode> elements) {

        var sb = new StringBuilder();
        sb.append("*").append(titled).append("*\n");

        var keyboard = Json.newArray();

        var row1 = Json.newArray();
        var row2 = Json.newArray();
        var row3 = Json.newArray();

        int idx1 = 0;
        int idx2 = 0;
        int idx3 = 0;

        var i = 0;
        do {

            var element = elements.next();

            sb.append("/")
                    .append(i)
                    .append(" :")
                    .append(element.get("name"))
                    .append("\n");

            var mod = i % 3;
            switch (mod) {
                case 0:
                    row1.add(String.valueOf(i));
                    idx1++;
                    break;
                case 1:
                    row2.add(String.valueOf(i));
                    idx2++;
                    break;
                case 2:
                    row3.add(String.valueOf(i));
                    idx3++;
                    break;
            }

            i++;
        } while (elements.hasNext() || i==9);

        if (idx1 != 0) {
            keyboard.add(row1);
        }
        if (idx2 != 0) {
            keyboard.add(row2);
        }
        if (idx3 != 0) {
            keyboard.add(row3);
        }


        sb.append("If your venue isn't any of these, please repeat the search with a more specific filter");

        return Json.newObject()
                .put("resize_keyboard",true)
                .put("option-text", sb.toString())
                .set("keyboard", keyboard);

    }

}

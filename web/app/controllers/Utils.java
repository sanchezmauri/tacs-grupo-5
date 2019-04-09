package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class Utils {

    public static JsonNode createErrorMessage(String error) {
        return JsonNodeFactory.instance.objectNode().put("error", error);
    }

}

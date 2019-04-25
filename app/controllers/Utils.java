package controllers;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Utils {

    public static JsonNode createErrorMessage(String message) {
        return JsonNodeFactory.instance.objectNode().put("message", message);
    }

    private static Pattern commandPattern = Pattern.compile( "^ */[A-z]* ?[A-z]* *$");
    public static String getCommandFrom(String text) {
        return commandPattern.
                matcher(text).
                results().
                findFirst().
                map(x -> text.substring(x.start(),x.end())).
                orElse("");
    }
}

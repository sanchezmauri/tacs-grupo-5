package controllers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Results.unauthorized;

public class Utils {

    public static JsonNode createErrorMessage(String message) {
        return JsonNodeFactory.instance.objectNode().put("message", message);
    }

    public static Result unauthorizedErrorJson(String errorMessage) {
        return unauthorized(createErrorMessage(errorMessage));
    }

    private static Pattern commandPattern = Pattern.compile( "^ */[A-z]* ?[A-z]* *$");
    public static String getCommandFrom(String text) {
        return commandPattern
                .matcher(text)
                .results()
                .findFirst()
                .map(x -> text.substring(x.start(),x.end()))
                .orElse("");
    }

    public static <T> Stream<T> toStream(Iterator<T> iterator) {

        // Convert the iterator to Spliterator
        Spliterator<T>
                spliterator = Spliterators
                .spliteratorUnknownSize(iterator, 0);

        // Get a Sequential Stream from spliterator
        return StreamSupport.stream(spliterator, false);

    }
}

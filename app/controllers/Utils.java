package controllers;

import java.util.regex.Pattern;

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

    public static F.Either<String, Long> parseLongQueryParam(Http.Request request, String name) {
        String queryParam = request.getQueryString(name);

        if (queryParam == null || queryParam.isEmpty()) {
            return F.Either.Left("Missing query param: " + name);
        }

        return F.Either.Right(Long.parseLong(queryParam));
    }
}

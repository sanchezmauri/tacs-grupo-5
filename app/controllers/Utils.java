package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.F;
import play.mvc.Http;

public class Utils {

    public static JsonNode createErrorMessage(String message) {
        return JsonNodeFactory.instance.objectNode().put("message", message);
    }


    public static F.Either<String, Long> parseLongQueryParam(Http.Request request, String name) {
        String queryParam = request.getQueryString(name);

        if (queryParam == null || queryParam.isEmpty()) {
            return F.Either.Left("Missing query param: " + name);
        }

        return F.Either.Right(Long.parseLong(queryParam));
    }
}

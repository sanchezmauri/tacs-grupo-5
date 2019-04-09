package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import play.libs.Json;
import play.mvc.*;
import repos.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public class UsersController extends Controller {
    public Result list() {
        JsonNode usersJson = Json.toJson(UserRepository.all());
        return ok(usersJson);
    }

    public Result create(Http.Request request) {
        JsonNode userToCreateJson = request.body().asJson();

        if (userToCreateJson == null) {
            return badRequest(Utils.createErrorMessage("Ill formatted json."));
        }

        for (String fieldName : Arrays.asList("name", "email", "password")) {
            if (!userToCreateJson.has(fieldName)) {
                return badRequest(
                    Utils.createErrorMessage("Missing field: " + fieldName)
                );
            }
        }

        User newUser = new User(
            UserRepository.nextId(),
            userToCreateJson.get("name").asText(),
            userToCreateJson.get("email").asText(),
            userToCreateJson.get("password").asText()
        );

        UserRepository.add(newUser);
        return created(Json.toJson(newUser));
    }

    public Result user(User user) {
        return ok(Json.toJson(user));
    }

    public Result listsCount(User user) {
        return ok(
            Json.newObject().put("listsCount", user.listsCount())
        );
    }

    public Result placesCount(User user, Optional<Boolean> visitedOpt) {
        // todo: mapear boolean visited a predicado visited
        /*Predicate<Object> predicate = visitedOpt.map(
            visited -> (Object place) -> place.visited.equals(visited)
        ).orElse(
            (Object place) -> true
        );*/
        Predicate<Object> predicate = (Object place) -> true;

        return ok(
            Json.newObject().put("placesCount", user.placesCount(predicate))
        );
    }

    public Result lastAccess(User user) {
        String lastAccessFormatted = user.getLastAccess().format(DateTimeFormatter.ISO_LOCAL_DATE);

        return ok(
            Json.newObject().put("lastAccess", lastAccessFormatted)
        );
    }
}

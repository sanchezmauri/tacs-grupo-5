package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import play.libs.Json;
import play.mvc.*;

public class UsersController extends Controller {
    private List<User> users = new ArrayList<>(
        Arrays.asList(
            new User(1L, "José"),
            new User(2L, "Pepe"),
            new User(3L, "Otro")
        )
    );
    public Result all() {
        JsonNode usersJson = Json.toJson(users);
        return ok(usersJson).as("application/json");
    }

    public Result create(Http.Request request) {
        JsonNode userToCreateJson = request.body().asJson();
        Long lastId = users.get(users.size() - 1).getId();

        User newUser = new User(
            lastId + 1,
            userToCreateJson.get("name").asText()
        );

        users.add(newUser);
        return ok(Json.toJson(newUser)).as("application/json");
    }


    public Result user(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(u -> ok(Json.toJson(u)).as("application/json"))
                .orElse(notFound("No user with id " + id.toString()));
    }
}

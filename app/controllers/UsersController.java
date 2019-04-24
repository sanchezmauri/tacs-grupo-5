package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;

import models.Rol;
import models.User;
import models.exceptions.UserException;
import play.libs.Json;
import play.mvc.*;
import repos.UserRepository;
import services.UsersService;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

public class UsersController extends Controller {
    @Authenticate(types = {"ROOT"})
    public Result list() {
        JsonNode usersJson = Json.toJson(UserRepository.all());
        return ok(usersJson);
    }

    @Authenticate(types = {"ROOT"})
    public Result create(Http.Request request) {
        try {
            JsonNode userToCreateJson = request.body().asJson();
            if (userToCreateJson == null) {
                return badRequest("ill formatted json.");
            }

            User newUser = new User(
                    UserRepository.nextId(),
                    userToCreateJson.get("name").asText(),
                    userToCreateJson.get("email").asText(),
                    userToCreateJson.get("password").asText(),
                    Rol.valueOf(userToCreateJson.get("rol").asText())
            );
            UsersService.create(newUser);
            return created(Json.toJson(newUser));
        } catch (UserException e) {
            return badRequest(Utils.createErrorMessage(e.getMessage()));
        }catch (Exception e)
        {
            return internalServerError(Utils.createErrorMessage(e.getMessage()));

        }

    }

    @Authenticate(types = {"ROOT"})
    public Result user(User user) {
        return ok(Json.toJson(user));
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result listsCount(User user) {
        return ok(
            Json.newObject().put("listsCount", user.listsCount())
        );
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result placesCount(User user, Optional<Boolean> visitedOpt) {
        // todo: mapear boolean visited a predicado visited
        /*Function<Object, Boolean> predicate = visitedOpt.map(
            visited -> (Object place) -> place.visited.equals(visited)
        ).orElse(
            (Object place) -> Boolean.TRUE
        );*/
        Function<Object, Boolean> predicate = (Object place) -> Boolean.TRUE;

        return ok(
            Json.newObject().put("placesCount", user.placesCount(predicate))
        );
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result lastAccess(User user) {
        String lastAccessFormatted = user.getLastAccess().format(DateTimeFormatter.ISO_LOCAL_DATE);

        return ok(
            Json.newObject().put("lastAccess", lastAccessFormatted)
        );
    }
}

package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;

import com.google.inject.Inject;
import models.Rol;
import models.User;
import models.UserVenue;
import models.exceptions.UserException;
import play.api.libs.ws.WSClient;
import play.libs.Json;
import play.mvc.*;
import repos.UserRepository;
import services.UsersService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Predicate;

public class UsersController extends Controller {
    @Authenticate(types = {"ROOT"})
    public Result list() {
        JsonNode usersJson = Json.toJson(UserRepository.all());
        return ok(usersJson);
    }

    public Result create(Http.Request request)  {
        try {
            JsonNode userToCreateJson = request.body().asJson();
            if (userToCreateJson == null) {
                return badRequest("ill formatted json.");
            }

            Rol role = Optional.ofNullable(userToCreateJson.get("rol"))
                        .map(jsonNode -> jsonNode.asText())
                        .map(Rol::valueOf)
                        .orElse(Rol.SYSUSER);

            User newUser = new User(
                UserRepository.nextId(),
                userToCreateJson.get("name").asText(),
                userToCreateJson.get("email").asText(),
                userToCreateJson.get("password").asText(),
                role
            );
            UsersService.create(newUser);
            return created(Json.toJson(newUser));
        } catch (UserException e) {
            return badRequest(Utils.createErrorMessage(e.getMessage()));
        } catch (Exception e) {
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

    private static Predicate<UserVenue> ANY_VENUE = venue -> true;
    private Predicate<UserVenue> makeVisitedPred(Boolean visited) {
        return venue -> venue.wasVisited() == visited;
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result venuesCount(User user, Optional<Boolean> visitedOpt) {
        Predicate<UserVenue> visitedPred = visitedOpt
                .map(this::makeVisitedPred)
                .orElse(ANY_VENUE);

        return ok(
            Json.newObject().put("venuesCount", user.venuesCount(visitedPred))
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

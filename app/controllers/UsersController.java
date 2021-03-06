package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;

import models.Rol;
import models.User;
import models.UserVenue;
import models.exceptions.UserException;
import play.libs.Json;
import play.mvc.*;
import services.UsersService;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UsersController extends Controller {


    private UsersService usersService;

    @Inject
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Authenticate(types = {"ROOT"})
    public Result list(Optional<String> name) {
        List<User> users = name.map(usersService::findByName)
                               .orElseGet(usersService::index);

        return ok(Json.toJson(users));
    }

    public Result create(Http.Request request) {
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
                userToCreateJson.get("name").asText(),
                userToCreateJson.get("email").asText(),
                userToCreateJson.get("password").asText(),
                role
            );
            usersService.create(newUser);
            return created(Json.toJson(newUser));
        } catch (UserException e) {
            return badRequest(Utils.createErrorMessage(e.getMessage()));
        } catch (Exception e) {
            return internalServerError(Utils.createErrorMessage(e.getMessage()));
        }
    }

    private User retrieveUserOrFail(String userId) {
        return usersService.findById(userId).orElseThrow(() -> new RuntimeException("Couldn't find user with id " + userId));
    }

    @Authenticate(types = {"ROOT"})
    public Result user(String userId) {

        var user = retrieveUserOrFail(userId);

        return ok(Json.toJson(user));
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result listsCount(String userId) {
        var user = retrieveUserOrFail(userId);
        return ok(
            Json.newObject().put("listsCount", user.listsCount())
        );
    }

    private static Predicate<UserVenue> ANY_VENUE = venue -> true;
    private Predicate<UserVenue> makeVisitedPred(Boolean visited) {
        return venue -> venue.wasVisited() == visited;
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result venuesCount(String userId, Optional<Boolean> visitedOpt) {

        var user = retrieveUserOrFail(userId);

        Predicate<UserVenue> visitedPred = visitedOpt
                .map(this::makeVisitedPred)
                .orElse(ANY_VENUE);

        return ok(
            Json.newObject().put("venuesCount", user.venuesCount(visitedPred))
        );
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result lastAccess(String userId) {

        var user = retrieveUserOrFail(userId);

        String lastAccessFormatted = user.getLastAccess().format(DateTimeFormatter.ISO_LOCAL_DATE);

        return ok(
            Json.newObject().put("lastAccess", lastAccessFormatted)
        );
    }
}

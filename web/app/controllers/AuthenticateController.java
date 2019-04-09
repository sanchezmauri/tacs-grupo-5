package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repos.UserRepository;
import services.CodesService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticateController extends Controller {

    private String initSession(Http.Request request, User user) {
        Map<String, Object> tokenGen = new HashMap<>();
        tokenGen.put("userId", user.getId());

        String token = CodesService.generateTokenFromMap(tokenGen);
        request.session().adding("token", token);

        return token;
    }

    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();

        if (!json.has("email") || !json.has("password")) {
            return badRequest(Utils.createErrorMessage("Por favor ingrese mail y contraseña para ingresar al sistema."));
        }

        String email = json.get("email").asText();
        String password = json.get("password").asText();

        Optional<User> userOpt = UserRepository.findByEmail(email);

        return userOpt
            .map(user -> {
                if (user.checkPassword(password)) {
                    String token = initSession(request, user);
                    ObjectNode tokenJson = JsonNodeFactory.instance.objectNode().put("token", token);

                    return ok(tokenJson);
                } else {
                    return unauthorized(Utils.createErrorMessage("Wrong password"));
                }
            })
            .orElse(unauthorized(Utils.createErrorMessage("No user with that email")));
    }

    public Result logout() {
        return ok().withNewSession();
    }
}

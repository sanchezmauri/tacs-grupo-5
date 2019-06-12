package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import models.communication.LoginResult;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.UsersService;


public class AuthenticateController extends Controller {
    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        JsonNode email = json.get("email");
        JsonNode password = json.get("password");

        if (email == null || password == null) {
            return badRequest(Utils.createErrorMessage("Por favor ingrese mail y contraseña para ingresar al sistema."));
        }

        LoginResult result = UsersService.login(email.textValue(),password.textValue());

        if (result.success()) {
            JsonNode userInfo = Json.newObject()
                    .put("role", result.user.get().getRol().toString())
                    .put("token", result.token);
            return ok(userInfo).addingToSession(request, "token", result.token);
        } else {
            return unauthorized(Utils.createErrorMessage("Credenciales invalidas."));
        }

    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result logout() {
        return ok().withNewSession();
    }
}

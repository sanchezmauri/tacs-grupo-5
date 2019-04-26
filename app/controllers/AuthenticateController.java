package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import models.communication.LoginResult;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repos.UserRepository;
import services.CodesService;
import services.UsersService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            return ok("Ingreso correctamente.").addingToSession(request, "token", result.token);
        } else {
            return unauthorized(Utils.createErrorMessage("Credenciales invalidas."));
        }

    }
    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result logout() {
        return ok("Deslogueo correcto").withNewSession();
    }

}

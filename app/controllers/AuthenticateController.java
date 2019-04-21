package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repos.UserRepository;
import services.CodesService;

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
        if(email != null && password != null)
        {
            Optional<User> user = UserRepository.findByEmail(email.textValue());
            if (BCrypt.checkpw(password.asText(),user.get().getPasswordHash()))
            {
                Map<String, Object> token = new HashMap<>();
                token.put("userId",user.get().getId());
                return ok("Ingreso correctamente.").addingToSession(request, "token", CodesService.generateTokenFromMap(token));
            }else
                return unauthorized(Utils.createErrorMessage("Credenciales invalidas."));
        }else
            return badRequest(Utils.createErrorMessage("Ningun campo puede estar vacio."));


    }
    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result logout() {
        return ok("Deslogueo correcto").withNewSession();
    }

}

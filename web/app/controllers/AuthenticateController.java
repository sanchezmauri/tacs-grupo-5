package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CodesService;

import java.util.HashMap;
import java.util.Map;

public class AuthenticateController extends Controller {


    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json.get("email") == null || json.get("password") == null) {
            return badRequest(Utils.createErrorMessage("Por favor ingrese mail y contraseña para ingresar al sistema."));
        }
//        Map<String, Object> token = new HashMap<>();
//        token.put("userId","n");
//        request.session().adding("token",CodesService.generateTokenFromMap(token));
        return ok("Ingreso correctamente.");
    }
    public Result logout() {
        return ok().withNewSession();
    }

}

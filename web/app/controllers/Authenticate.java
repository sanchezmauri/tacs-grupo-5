package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class Authenticate extends Controller {

    public static Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json.get("email") == null || json.get("password") == null) {
            return badRequest(Utils.createErrorMessage("Por favor ingrese mail y contraseña para ingresar al sistema."));
        }
        return ok("Ingreso correctamente.");
    }
    public static Result logout() {
        return ok().withNewSession();
    }

}

package annotations;

import controllers.RequestAttrs;
import controllers.Utils;
import models.Rol;
import models.User;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.CodesService;
import services.UsersService;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthenticateAction extends Action<Authenticate> {

    public CompletionStage<Result> call(Http.Request req) {
        try {
            String token;

            if (req.session().data().containsKey("token")) {
                token = req.session().data().get("token");
            } else {
                return CompletableFuture.completedFuture(Utils.unauthorizedErrorJson("no session token"));
            }

            Map<String, Object> map = CodesService.decodeMapFromToken(token);
            Optional<User> user = Optional.ofNullable(UsersService.findById((String) map.get("userId").toString())); //Aca deberia buscar el usuario segun id y traerlo con los PERMISOS QUE TIENE;

            if (user.isEmpty()) {
                return CompletableFuture.completedFuture(Utils.unauthorizedErrorJson("no user found"));
            }

            boolean hasRole = Arrays.stream(this.configuration.types())
                    .anyMatch(r -> Rol.valueOf(r).equals(user.get().getRol()));

            if (hasRole) {
                req = req.addAttr(RequestAttrs.USER, user.get());

                return delegate.call(req);
            }
            else {
                return CompletableFuture.completedFuture(
                    forbidden(Utils.createErrorMessage("no permissions"))
                );
            }

        } catch (Exception e) {
            return CompletableFuture.completedFuture(internalServerError(e.getMessage()));
        }
    }
}


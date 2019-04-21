package annotations;

import models.User;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import repos.UserRepository;
import services.CodesService;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthenticateAction extends Action<Authenticate> {

    public CompletionStage<Result> call(Http.Request req) {
        Security.Authenticator authenticator = new Security.Authenticator();
        try {
            String token;
            if (req.session().data().containsKey("token")) {
                token = req.session().data().get("token");
            } else {
                return CompletableFuture.completedFuture(authenticator.onUnauthorized(req));
            }
            Map<String, Object> map = CodesService.decodeMapFromToken(token);
//            ;
            Optional<User> user = UserRepository.find(Long.parseLong(map.get("userId").toString())); //Aca deberia buscar el usuario segun id y traerlo con los PERMISOS QUE TIENE;
            if (user == null) {
                CompletableFuture.completedFuture(authenticator.onUnauthorized(req));
            }
            if (Arrays.stream(this.configuration.types()).anyMatch(r -> User.Rol.valueOf(r).equals(user.get().getRol())))
                return delegate.call(req);
            else
                return CompletableFuture.completedFuture(authenticator.onUnauthorized(req));

        } catch (Exception e) {
            return CompletableFuture.completedFuture(authenticator.onUnauthorized(req));
        }
    }
}


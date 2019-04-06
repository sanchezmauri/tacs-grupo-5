package annotations;

import models.User;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Map;
import java.util.concurrent.CompletionStage;

public class AuthenticateAction extends Action<Authenticate> {
    @Override
    public CompletionStage<Result> call(Http.Request req) {
        try {
            String token;
            if (req.session().data().containsKey("token")) {
                token = req.session().data().get("token");
            } else {
                return (CompletionStage<Result>)unauthorized();
            }
            Map<String, Object> map = User.getDecodedMapFromToken(token);
            User user = null //Aca deberia buscar el usuario segun id y traerlo con los PERMISOS QUE TIENE;
            if (user == null) {
                return (CompletionStage<Result>) forbidden();
            }
            return delegate.call(req).thenApply(result -> result.addingToSession(req,"userId","Estatico"));
        } catch (Exception e) {
            return (CompletionStage<Result>) unauthorized();
        }

    }
}

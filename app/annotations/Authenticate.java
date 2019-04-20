package annotations;

import play.mvc.With;

import java.lang.annotation.*;

@With(AuthenticateAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Authenticate {
    String[] types();
}

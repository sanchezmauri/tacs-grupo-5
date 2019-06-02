package models.communication;

import models.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public class LoginResult {
    public static LoginResult Success(String token, User user) {
        return new LoginResult(LoginResultStatus.Success, token, Optional.of(user));
    }

    public static LoginResult InvalidUsernameOrPassword = new LoginResult(LoginResultStatus.InvalidUsernameOrPassword,"", Optional.empty());

    public LoginResultStatus status;
    public String token;
    public Optional<User> user;


    private LoginResult(LoginResultStatus status, String token, Optional<User> user) {
        this.status = status;
        this.token = token;
        this.user = user;
    }

    public Boolean success() {
        return this.status == LoginResultStatus.Success;
    }

}

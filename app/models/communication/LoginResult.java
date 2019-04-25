package models.communication;

public class LoginResult {



    public static LoginResult Success(String token) {
        return new LoginResult(LoginResultStatus.Success, token);
    }

    public static LoginResult InvalidUsernameOrPassword = new LoginResult(LoginResultStatus.InvalidUsernameOrPassword,"");

    public LoginResultStatus status;
    public String token;


    private LoginResult(LoginResultStatus status, String token) {
        this.status = status;
        this.token = token;
    }

    public Boolean success() {
        return this.status == LoginResultStatus.Success;
    }

}

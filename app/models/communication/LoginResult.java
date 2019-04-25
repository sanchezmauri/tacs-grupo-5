package models.communication;

public class LoginResult {

    public enum Status {
        Success, InvalidUsernameOrPassword
    }

    public static LoginResult Success(String token) {
        return new LoginResult(Status.Success, token);
    }

    public static LoginResult InvalidUsernameOrPassword = new LoginResult(Status.InvalidUsernameOrPassword,"");

    public Status status;
    public String token;


    private LoginResult(Status status, String token) {
        this.status = status;
        this.token = token;
    }

    public Boolean success() {
        return this.status == Status.Success;
    }

}

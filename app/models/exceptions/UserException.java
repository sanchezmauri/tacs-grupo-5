package models.exceptions;

public class UserException extends Exception {
    private String message;

    public UserException(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}

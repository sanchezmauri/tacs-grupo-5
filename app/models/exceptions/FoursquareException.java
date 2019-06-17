package models.exceptions;

public class FoursquareException extends Exception {

    public Integer statusCode;
    public String errorType;
    public String errorText;

    public FoursquareException(Integer _statusCode, String _errorType, String _errorText){
        statusCode = _statusCode;
        errorType = _errorType;
        errorText = _errorText;
    }
}

package pl.trimlogic.restapi.exception;

public class NotAuthenticatedException extends FilenetException {
    public NotAuthenticatedException(Exception e) {
        super(e);
    }
}

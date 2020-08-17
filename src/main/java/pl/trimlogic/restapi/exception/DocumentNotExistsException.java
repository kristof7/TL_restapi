package pl.trimlogic.restapi.exception;

public class DocumentNotExistsException extends FilenetException {
    public DocumentNotExistsException(Exception e) {
        super(e);
    }
}

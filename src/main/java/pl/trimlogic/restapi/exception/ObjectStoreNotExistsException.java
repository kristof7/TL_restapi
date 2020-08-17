package pl.trimlogic.restapi.exception;

public class ObjectStoreNotExistsException extends FilenetException {
    public ObjectStoreNotExistsException(Exception e){
        super(e);
    }
}

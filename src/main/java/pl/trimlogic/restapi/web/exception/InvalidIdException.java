package pl.trimlogic.restapi.web.exception;

public class InvalidIdException extends FilenetException {

    public InvalidIdException(String id){
        super("Cannot parse Id: " + id);
    }

    public InvalidIdException(Exception e){
        super(e);
    }
}

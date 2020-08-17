package pl.trimlogic.restapi.exception;

public class InvalidIdException extends FilenetException {

    public InvalidIdException(String id){
        super("Cannot parse Id: " + id);
    }

}

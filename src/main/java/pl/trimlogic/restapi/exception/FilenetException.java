package pl.trimlogic.restapi.exception;

public class FilenetException extends Exception {
    public FilenetException(){
        super();
    }
    public FilenetException(Exception e){
        super(e);
    }
    public FilenetException(String message) {
        super(message);
    }
}

package pl.trimlogic.restapi.config.sources;

public class PropertyParseException  extends Exception {
    public PropertyParseException(Exception e){
        super(e);
    }
    public PropertyParseException(String message) {
        super(message);
    }
    public PropertyParseException(String message,Exception e){
        super(message,e);
    }
}
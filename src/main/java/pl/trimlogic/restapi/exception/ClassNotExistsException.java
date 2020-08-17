package pl.trimlogic.restapi.exception;

import lombok.Getter;

public class ClassNotExistsException extends FilenetException {
    @Getter
    private String className;

    public ClassNotExistsException(String className, Exception e){
        super(e);
        this.className = className;
    }

    @Override
    public String getMessage() {
        return "Document class '" + className + "' is invalid";
    }
}

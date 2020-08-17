package pl.trimlogic.restapi.exception;

import lombok.Getter;

public class EmptyRequiredPropertyException extends FilenetException {

    @Getter
    private String propertyName;

    @Override
    public String getMessage() {
        return "Property " + propertyName + " is required";
    }
}

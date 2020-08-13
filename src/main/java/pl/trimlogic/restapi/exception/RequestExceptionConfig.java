package pl.trimlogic.restapi.exception;

import lombok.AllArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class RequestExceptionConfig {
    protected final ExceptionConfig defaultExceptionConfig,
            parseExceptionConfig,
            generationExceptionConfig;
    public final ExceptionConfig invalidId = getInvalidObjectExceptionConfig("Id"),
            objectStoreNotExists = getObjectNotExistsExceptionConfig("System"),
            documentNotExists = getObjectNotExistsExceptionConfig("Document"),
            objectClassNotExists = getObjectNotExistsExceptionConfig("Object class"),
            notAuthenticated = new ExceptionConfig("User is not authenticated", "You are not authenticated", HttpStatus.UNAUTHORIZED,LogLevel.WARN);



    public static final RequestExceptionConfig POST = new RequestExceptionConfig(
            new ExceptionConfig(ExceptionConfig.LogMessage.UNEXPECTED_ERROR,"", HttpStatus.INTERNAL_SERVER_ERROR),
            new ExceptionConfig(ExceptionConfig.LogMessage.PARSE_ERROR,ExceptionConfig.ResponseMessage.BAD_REQUEST,HttpStatus.BAD_REQUEST,LogLevel.WARN),
            new ExceptionConfig(ExceptionConfig.LogMessage.GENERATION_ERROR,"",HttpStatus.INTERNAL_SERVER_ERROR)
    );

    public static final RequestExceptionConfig GET = new RequestExceptionConfig(
            new ExceptionConfig(ExceptionConfig.LogMessage.UNEXPECTED_ERROR,"",HttpStatus.INTERNAL_SERVER_ERROR),
            new ExceptionConfig(ExceptionConfig.LogMessage.PARSE_ERROR,"",HttpStatus.INTERNAL_SERVER_ERROR),
            new ExceptionConfig(ExceptionConfig.LogMessage.GENERATION_ERROR,"",HttpStatus.INTERNAL_SERVER_ERROR)
    );

    private static ExceptionConfig getInvalidObjectExceptionConfig(String objectName){
        return new ExceptionConfig("Invalid property " + objectName,objectName + " is invalid",HttpStatus.BAD_REQUEST,LogLevel.WARN);
    }

    private static ExceptionConfig getObjectNotExistsExceptionConfig(String objectName){
        return new ExceptionConfig(objectName + " not exists",objectName + " not exists",HttpStatus.BAD_REQUEST,LogLevel.WARN);
    }
}

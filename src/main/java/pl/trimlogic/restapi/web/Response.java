package pl.trimlogic.restapi.web;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pl.trimlogic.restapi.config.sources.PropertyParseException;
import pl.trimlogic.restapi.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class Response {
    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private Object body;
    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private HttpHeaders headers;
    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private HttpStatus status;
    private final HttpServletRequest request;
    private final RequestExceptionConfig config;

    public Response(HttpServletRequest request, RequestExceptionConfig config){
        this.request = request;
        body = "{}";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.config = config;
    }
    public Response(HttpServletRequest request){
        this(request,null);
    }

    public ResponseEntity asResponseEntity(){
        return new ResponseEntity<>(body, headers, status);
    }

    public Response setErrorState(UUID errorUuid, String responseMessage, HttpStatus status){
        if(errorUuid == null)
            errorUuid = UUID.randomUUID();
        setStatus(status);

        StringBuilder bodyBuilder = new StringBuilder().append("{")
                .append("\"timestamp\": \"").append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date())).append("\",")
                .append("\"status\": ").append(getStatus().value()).append(",")
                .append("\"error\": \"").append(getStatus().getReasonPhrase()).append("\",")
                .append("\"errorId\": \"").append(errorUuid).append("\",");
        if(!Strings.isNullOrEmpty(responseMessage))
            bodyBuilder.append("\"message\": \"").append(responseMessage).append("\",");
        bodyBuilder.append("\"path\": \"").append(request.getRequestURI()).append("\"}");
        setBody(bodyBuilder.toString());
        return this;
    }

    public Response updateByException(Exception e) {
        if (config == null)
            return this;
        ExceptionConfig exceptionConfig = config.defaultExceptionConfig;
        if (e instanceof JsonParseException) {
            exceptionConfig = config.parseExceptionConfig;
        } else if (e instanceof JsonGenerationException) {
            exceptionConfig = config.generationExceptionConfig;
        } else if(e instanceof NotAuthenticatedException) {
            exceptionConfig = config.notAuthenticated;
        } else if(e instanceof ObjectStoreNotExistsException) {
            exceptionConfig = config.objectStoreNotExists;
        } else if(e instanceof ClassNotExistsException){
            exceptionConfig = new ExceptionConfig(e.getMessage(),HttpStatus.BAD_REQUEST,LogLevel.WARN);
        } else if(e instanceof InvalidIdException){
            exceptionConfig = config.invalidId;
        } else if(e instanceof DocumentNotExistsException){
            exceptionConfig = config.documentNotExists;
        } else if(e instanceof PropertyParseException) {
            exceptionConfig = new ExceptionConfig(e.getMessage(),HttpStatus.BAD_REQUEST,LogLevel.WARN);
        } else if(e instanceof EmptyRequiredPropertyException) {
            exceptionConfig = new ExceptionConfig(e.getMessage(),HttpStatus.BAD_REQUEST,LogLevel.WARN);
        }

        UUID errorUuid = UUID.randomUUID();
        setErrorState(errorUuid,exceptionConfig.getResponseMessage(),exceptionConfig.getHttpStatus());
        String logMessage = exceptionConfig.getLogMessage() + ", error code: " + errorUuid;

        switch (exceptionConfig.getLogLevel()) {
            case ERROR: {if(log.isErrorEnabled())   log.error(logMessage,e);    break;}
            case WARN:  {if(log.isWarnEnabled())    log.warn(logMessage);       break;}
            case INFO:  {if(log.isInfoEnabled())    log.info(logMessage);       break;}
            case DEBUG: {if(log.isDebugEnabled())   log.debug(logMessage);      break;}
            case TRACE: {if(log.isTraceEnabled())   log.trace(logMessage);      break;}
        }

        return this;
    }
}

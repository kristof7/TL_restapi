package pl.trimlogic.restapi.web.exception;

import lombok.*;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@RequiredArgsConstructor
public
class ExceptionConfig {
    @NonNull
    @Getter(AccessLevel.PACKAGE)
    private final String logMessage;
    @NonNull
    private String responseMessage;
    @NonNull
    @Getter(AccessLevel.PACKAGE)
    private final HttpStatus httpStatus;
    @Getter(AccessLevel.PACKAGE)
    private LogLevel logLevel = LogLevel.ERROR;

    ExceptionConfig(String message, HttpStatus httpStatus, LogLevel logLevel) {
        this(message, message, httpStatus, logLevel);
    }

    String getResponseMessage() {
        return (!responseMessage.isEmpty()) ?
                responseMessage
                        .replace("\n", "")
                        .replace("\"", "'")
                : ResponseMessage.INTERNAL_ERROR;
    }

    static class LogMessage {
        static final String UNEXPECTED_ERROR = "Unexpected error occurs";
        static final String PARSE_ERROR = "Cannot parse json";
        static final String GENERATION_ERROR = "Cannot create json response";
    }

    static class ResponseMessage {
        static final String INTERNAL_ERROR = "Internal server error";
        static final String BAD_REQUEST = "Cannot parse input data";
    }
}

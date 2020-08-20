package pl.trimlogic.restapi.filenet;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.util.UserContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.trimlogic.restapi.ahidden.UserAccount;
import pl.trimlogic.restapi.config.ConfigKeys;
import pl.trimlogic.restapi.exception.NotAuthenticatedException;
import pl.trimlogic.restapi.exception.ObjectStoreNotExistsException;

import javax.security.auth.Subject;

@Slf4j
public class FilenetConnection implements AutoCloseable {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private UserContext userContext;
    @Setter(AccessLevel.PRIVATE)
    private Connection connection;
    @Getter(AccessLevel.PACKAGE)
    private boolean connected;


    FilenetConnection connect() {
        String username = new UserAccount().getUsername();
        String password = new UserAccount().getPassword();
        return connect(username, password);
    }


    FilenetConnection connect(String username, String password) {
        if (connected)
            return this;
        if (log.isTraceEnabled()) log.trace("Initializing connection to Filenet");
        connection = Factory.Connection.getConnection(ConfigKeys.CE_URI);
        Subject subject = UserContext.createSubject(connection, username, password,
                ConfigKeys.STANZA_NAME);
        userContext = UserContext.get();
        userContext.pushSubject(subject);
        connected = true;
        return this;
    }

    @Override
    public void close() {

    }

    private Domain getDomain() {
        return Factory.Domain.getInstance(getConnection(), null);
    }

    private Connection getConnection() {
        if (isConnected()) {
            connect();
        }
        return connection;
    }

    ObjectStore getObjectStore() throws Exception {
        if(!isConnected()){
            connect();
        }
        try {
            return Factory.ObjectStore.fetchInstance(getDomain(), ConfigKeys.OBJECT_STORE_NAME, null);
        } catch(EngineRuntimeException e) {
            ExceptionCode exceptionCode = e.getAsErrorStack().getExceptionCode();
            if(ExceptionCode.E_NOT_AUTHENTICATED.equals(exceptionCode))
                throw new NotAuthenticatedException(e);
            if(ExceptionCode.E_OBJECT_NOT_FOUND.equals(exceptionCode))
                throw new ObjectStoreNotExistsException(e);
            throw e;
        }
    }
}

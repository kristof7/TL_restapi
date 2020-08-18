package pl.trimlogic.restapi.filenet;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;
import com.filenet.api.util.UserContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.trimlogic.restapi.hidden.UserAccount;

import javax.security.auth.Subject;

@Slf4j
public class FilenetConnection implements AutoCloseable {

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
        String uri = "http://192.168.1.140:9080/wsi/FNCEWS40MTOM";
        String stanzaName = "FileNetP8WSI";
        Connection connection = Factory.Connection.getConnection(uri);
        Subject subject = UserContext.createSubject(connection, username, password,
                stanzaName);
        UserContext userContext = UserContext.get();
        userContext.pushSubject(subject);
        return this;
    }

    @Override
    public void close() throws Exception {

    }
}

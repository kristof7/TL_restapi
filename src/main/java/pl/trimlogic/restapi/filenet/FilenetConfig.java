package pl.trimlogic.restapi.filenet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class FilenetConfig {

//    @Getter(AccessLevel.PACKAGE) private String uri;
//    @Getter(AccessLevel.PACKAGE) private String stanzaName;
//    @NonNull
//    private final AppConfig appConfig;
//
//
//    @PostConstruct
//    void init() throws Exception {
//        uri = appConfig.getRequiredProperty(ConfigKeys.Filenet.URL).toString();
//        stanzaName = appConfig.getRequiredProperty(ConfigKeys.Filenet.STANZA_NAME).toString();
//        if(log.isTraceEnabled()) {
//            log.trace("Stanza name: " + stanzaName);
//        }
//    }

    static class Properties {

        static final String DOCUMENT_TITLE = "DocumentTitle";

    }
}

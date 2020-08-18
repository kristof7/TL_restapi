package pl.trimlogic.restapi.filenet;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


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
        static final String CLASS_NAME = "ClassName";

        @Getter(AccessLevel.PACKAGE)
        private static Set<String> baseIncludedProperties = new HashSet<>(Arrays.asList(
                PropertyNames.CREATOR,
                PropertyNames.DATE_CREATED,
                PropertyNames.LAST_MODIFIER,
                PropertyNames.DATE_LAST_MODIFIED,
                PropertyNames.ID,
                DOCUMENT_TITLE,
                CLASS_NAME
        ));
        @Getter(AccessLevel.PACKAGE)
        private static Set<String> baseExcludedProperties = new HashSet<>(Arrays.asList(
                PropertyNames.SOURCE_DOCUMENT
        ));
        private static Set<String> documentCreationProperties = new HashSet<>(Arrays.asList(
                PropertyNames.CURRENT_VERSION
        ));

        static PropertyFilter getBaseDocumentPropertyFilter() {
            PropertyFilter propertyFilter = new PropertyFilter();
            for (String includedProperty : baseIncludedProperties) {
                propertyFilter.addIncludeProperty(new FilterElement(null, null, null, includedProperty, null));
            }
            for (String excludedProperty : baseExcludedProperties) {
                propertyFilter.addExcludeProperty(excludedProperty);
            }
            return propertyFilter;


        }
    }
}

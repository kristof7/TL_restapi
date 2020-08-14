package pl.trimlogic.restapi.filenet;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class FilenetConfig {

    static class Properties {

        static final String DOCUMENT_TITLE = "DocumentTitle";

    }
}

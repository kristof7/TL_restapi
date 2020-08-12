package pl.trimlogic.restapi.web.service;

import com.filenet.api.util.Id;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.trimlogic.restapi.web.repository.FilenetRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilenetService {

    @NonNull
    private FilenetRepository filenetRepository;

    public Map<String, Object> getDocument(String requestedGuid) {
        Id docId = getId(requestedGuid);
        return filenetRepository.getDocument(docId);
    }

    private Id getId(String requestedGuid) {
        Id docId = Id.asIdOrNull(requestedGuid);
        return docId;
    }

}

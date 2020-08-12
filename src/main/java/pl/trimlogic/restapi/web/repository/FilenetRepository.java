package pl.trimlogic.restapi.web.repository;

import com.filenet.api.util.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilenetRepository {


    public Map<String, Object> getDocument(Id docId) {
        return null;
    }

}

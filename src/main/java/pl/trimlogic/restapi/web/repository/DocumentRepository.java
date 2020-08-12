package pl.trimlogic.restapi.web.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.trimlogic.restapi.web.exception.DocumentNotFoundException;
import pl.trimlogic.restapi.web.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentRepository {


    private final Map<String, Document> documents;


    public DocumentRepository() {
        this.documents = new HashMap<>();
    }

    public List<Document> getDocuments() {
        return new ArrayList<>(documents.values());
    }

    public void saveDocument(Document document) {
        documents.put(document.getDocId(), document);
    }

    public Document getDocument(String id) {
        Document document = documents.get(id);

        if (document == null) {
            throw new DocumentNotFoundException(String.format("Document with id %s not " +
                    "found.", id));
        }

        Document documentToReturn = new Document();
        documentToReturn.setDocId(document.getDocId());
        documentToReturn.setUsername(document.getUsername());
        documentToReturn.setObjectStoreName(document.getObjectStoreName());
        documentToReturn.setCreated(document.getCreated());
        documentToReturn.setLastModified(document.getLastModified());

        return documentToReturn;
    }

    public Document getDocumentFilenet(String guid) {
        return null;
    }


}

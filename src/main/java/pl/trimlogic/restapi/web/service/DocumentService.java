package pl.trimlogic.restapi.web.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.trimlogic.restapi.web.model.Document;
import pl.trimlogic.restapi.web.model.DocumentDto;
import pl.trimlogic.restapi.web.model.NewDocumentDto;
import pl.trimlogic.restapi.web.repository.DocumentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentService {

    @NonNull
    private DocumentRepository documentRepository;

    public DocumentDto createDocument(NewDocumentDto newDocumentDto) {
        String id = UUID.randomUUID().toString();
        Long timestamp = System.currentTimeMillis();
        Document document = new Document();
        document.setDocId(id);
        document.setCreated(timestamp);
        document.setUsername(newDocumentDto.getUsername());
        document.setObjectStoreName(newDocumentDto.getObjectStoreName());

        documentRepository.saveDocument(document);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setUsername(newDocumentDto.getUsername());
        documentDto.setObjectStoreName(newDocumentDto.getObjectStoreName());
        documentDto.setCreated(timestamp);
        documentDto.setDocId(id);

        return documentDto;

    }

    public List<DocumentDto> getDocuments() {
        return documentRepository
                .getDocuments()
                .stream()
                .map(document -> {
                    DocumentDto documentDto = new DocumentDto();
                    documentDto.setDocId(document.getDocId());
                    documentDto.setUsername(document.getUsername());
                    documentDto.setObjectStoreName(document.getObjectStoreName());
                    documentDto.setCreated(document.getCreated());
                    documentDto.setLastModified(document.getLastModified());
                    return documentDto;
                })
                .collect(Collectors.toList());

    }

    public DocumentDto getDocument(String id) {
        Document document = documentRepository.getDocument(id);

        DocumentDto documentDto = new DocumentDto();
        documentDto.setDocId(document.getDocId());
        documentDto.setUsername(document.getUsername());
        documentDto.setObjectStoreName(document.getObjectStoreName());
        documentDto.setLastModified(document.getLastModified());
        documentDto.setCreated(document.getCreated());

        return documentDto;

    }
}

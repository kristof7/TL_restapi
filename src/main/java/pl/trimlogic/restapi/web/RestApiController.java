package pl.trimlogic.restapi.web;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.trimlogic.restapi.web.model.DocumentDto;
import pl.trimlogic.restapi.web.model.NewDocumentDto;
import pl.trimlogic.restapi.web.service.DocumentService;

import java.util.List;


@RestController
@RequestMapping("${api.request.context.root}${api.request.context.document.base}/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestApiController {

    @NonNull
    private DocumentService documentService;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/list")
    public List<DocumentDto> getDocuments() {
        return documentService.getDocuments();
    }

    @GetMapping("/list/{docId}")
    DocumentDto getDocument(@PathVariable String docId) {
        return documentService.getDocument(docId);
    }

    @PostMapping("/list")
    DocumentDto addDocument(@RequestBody NewDocumentDto document) {
        return documentService.createDocument(document);
    }
}
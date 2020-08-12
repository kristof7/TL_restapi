package pl.trimlogic.restapi.web;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.trimlogic.restapi.web.exception.FilenetException;
import pl.trimlogic.restapi.web.exception.RequestExceptionConfig;
import pl.trimlogic.restapi.web.filenet.FilenetService;
import pl.trimlogic.restapi.web.model.DocumentDto;
import pl.trimlogic.restapi.web.model.NewDocumentDto;
import pl.trimlogic.restapi.web.service.DocumentService;
import pl.trimlogic.restapi.web.service.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


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

    //----------------- Filenet --------------------------

    @NonNull
    private FilenetService filenetService;

    @GetMapping("/{docId}")
    public ResponseEntity getDocProperties(HttpServletRequest request,
                                           @PathVariable String docId
    ) throws FilenetException {
        Response response = new Response(request, RequestExceptionConfig.GET);

        Map<String, Object> responsePayload = filenetService.getDocument(docId);

        return response.asResponseEntity();
    }


}
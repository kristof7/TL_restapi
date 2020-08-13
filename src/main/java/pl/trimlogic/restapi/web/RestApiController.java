package pl.trimlogic.restapi.web;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.trimlogic.restapi.web.exception.FilenetException;
import pl.trimlogic.restapi.web.exception.RequestExceptionConfig;
import pl.trimlogic.restapi.web.filenet.FilenetService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("${api.request.context.root}${api.request.context.document.base}/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestApiController {

//    @NonNull
//    private DocumentService documentService;
//
//    @GetMapping("/hello")
//    public String hello(@RequestParam(value = "name", defaultValue = "World") String
//    name) {
//        return String.format("Hello %s!", name);
//    }
//
//    @GetMapping("/list")
//    public List<DocumentDto> getDocuments() {
//        return documentService.getDocuments();
//    }
//
//    @GetMapping("/list/{docId}")
//    DocumentDto getDocument(@PathVariable String docId) {
//        return documentService.getDocument(docId);
//    }
//
//    @PostMapping("/list")
//    DocumentDto addDocument(@RequestBody NewDocumentDto document) {
//        return documentService.createDocument(document);
//    }

    //----------------- Filenet --------------------------


    private static final ObjectMapper mapper =
            new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    @NonNull
    private FilenetService filenetService;

    @GetMapping("/filenet/{docId}")
    public ResponseEntity getDocProperties(HttpServletRequest request,
                                           @PathVariable String docId
    ) throws JsonProcessingException, FilenetException {
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);

        Map<String, Object> responsePayload = filenetService.getDocument(docId);
        String responseBody = mapper.writeValueAsString(responsePayload);

        return response;
    }
}
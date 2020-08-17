package pl.trimlogic.restapi.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filenet.api.util.Id;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.trimlogic.restapi.exception.FilenetException;
import pl.trimlogic.restapi.filenet.FilenetService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${api.request.context.root}${api.request.context.document.base}/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestApiController {


    private static final ObjectMapper mapper =
            new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    @NonNull
    private FilenetService filenetService;

    @GetMapping("{docId}")
    public ResponseEntity getDocProperties(@PathVariable String docId
    ) throws JsonProcessingException, FilenetException {
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);

        Map<String, Object> responsePayload = filenetService.getDocument(docId);
        String responseBody = mapper.writeValueAsString(responsePayload);


        return response;
    }

    @PostMapping("${api.request.context.document.creation}")
    public ResponseEntity upload(
            Map<String, String> properties
    ) throws FilenetException {


        Id docId = filenetService.createDocument(properties);
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);


        Map<String, Object> responsePayload = filenetService.getDocument(docId);

        return response;

    }

    //TODO post method - search for params, method parameter : Hashmap
}
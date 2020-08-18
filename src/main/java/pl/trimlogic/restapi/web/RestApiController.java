package pl.trimlogic.restapi.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filenet.api.util.Id;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.trimlogic.restapi.filenet.FilenetService;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity getDocProperties(
            HttpServletRequest request, @PathVariable String docId
    ) {
        Response response = new Response(request, RequestExceptionConfig.GET);
        try {
            Map<String, Object> responsePayload =
                    filenetService.getDocumentProperties(docId);
            String responseBody = mapper.writeValueAsString(responsePayload);

            response.setBody(responseBody);
            response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            response.updateByException(e);
        }
        return response.asResponseEntity();
    }

    @PostMapping("${api.request.context.document.creation}")
    public ResponseEntity upload(
            HttpServletRequest request,
            Map<String, Object> properties
    ) {
        Response response = new Response(request, RequestExceptionConfig.POST);

        try {
            Id docId = filenetService.createDocument(properties);
            if (docId == null) {
                return response.asResponseEntity();
            }

            Map<String, Object> responsePayload =
                    filenetService.getDocumentProperties(docId);
            String responseBody = mapper.writeValueAsString(responsePayload);
            response.setBody(responseBody);
            response.setStatus(HttpStatus.CREATED);
        } catch (Exception e) {
            response.updateByException(e);
        }
        return response.asResponseEntity();
    }

    //TODO post method - search for params, method parameter : Hashmap
}
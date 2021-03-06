package pl.trimlogic.restapi.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filenet.api.util.Id;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.trimlogic.restapi.filenet.FilenetService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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

    @RequestMapping(value = "${api.request.context.document.creation}", method = RequestMethod.POST, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity upload(
            @ModelAttribute("DocumentTitle") String documentTitle,
            HttpServletRequest request,
            Map<String, Object> properties
    ) {
        Response response = new Response(request, RequestExceptionConfig.POST);

        try {
            Id docId = filenetService.createDocument(documentTitle);
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

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Map> searchDocumentsByGetParams(HttpServletRequest request,
                                                               @RequestParam Map<String, String[]> customQuery) {

        Response response = new Response(request, RequestExceptionConfig.GET);

        try {
            customQuery = request.getParameterMap();
            List<Map> responsePayload;
            responsePayload = filenetService.getDocumentsByGetParams(customQuery);
            String responseBody = mapper.writeValueAsString(responsePayload);

            response.setBody(responseBody);
            return responsePayload;
        } catch (Exception e) {
            response.updateByException(e);
            return new ArrayList<>();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Map> searchDocumentsByPostParams(HttpServletRequest request,
                                                 @RequestBody Map<String, Object> customQuery) {

        Response response = new Response(request, RequestExceptionConfig.POST);
        try {

            List<Map> responsePayload;
            responsePayload = filenetService.getDocumentsByPostParams(customQuery);
            response.setBody(responsePayload);

            return responsePayload;
        } catch (Exception e) {
            response.updateByException(e);
            return new ArrayList<>();
        }
    }
}
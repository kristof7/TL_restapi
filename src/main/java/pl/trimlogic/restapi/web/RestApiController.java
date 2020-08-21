package pl.trimlogic.restapi.web;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filenet.api.util.Id;
import com.google.common.base.Strings;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
            Id docId = filenetService.createDocument(properties, documentTitle);
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
//-----------------------------------------------------------------------------------------------------

    @GetMapping("/search")
    public ResponseEntity searchForParams(HttpServletRequest request,
                                          @RequestParam("properties") String propertiesRaw) {
        Map params = request.getParameterMap();

        Response response = new Response(request, RequestExceptionConfig.GET);

        try {
            Map properties = request.getParameterMap();

            Set set = properties.entrySet();
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {

                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) iterator.next();

                String key = entry.getKey();
                String[] value = entry.getValue();
            }
            if (!Strings.isNullOrEmpty(propertiesRaw) && !propertiesRaw.trim().isEmpty()) {
                properties = mapper.readValue(propertiesRaw, Map.class);
            }


            Map<String, Object> responsePayload = filenetService.getDocumentsByParameters(params);
            String responseBody = mapper.writeValueAsString(responsePayload);
            response.setBody(responseBody);
            response.setStatus(HttpStatus.OK);

        } catch (Exception e) {
            response.updateByException(e);
        }
        return response.asResponseEntity();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/custom")
    public Set<Map.Entry<String, String[]>> searchByParamsTest(HttpServletRequest request,
                                                         @RequestParam Map<String, String[]> customQuery) throws JsonProcessingException {

        Response response = new Response(request, RequestExceptionConfig.GET);


        customQuery = request.getParameterMap();

        for (String key : customQuery.keySet()) {
            String[] strArr = customQuery.get(key);
            for (String val : strArr) {
                System.out.println(key + " " + val);
            }
        }
        String responseBody = mapper.writeValueAsString(customQuery);

        response.setBody(responseBody);
        response.setStatus(HttpStatus.OK);

        return customQuery.entrySet();

    }
}
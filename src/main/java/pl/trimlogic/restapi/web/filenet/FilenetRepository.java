package pl.trimlogic.restapi.web.filenet;

import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilenetRepository {


    public Map<String, Object> getDocument(Id docId) {
        String uri = "http://192.168.1.140:9080/wsi/FNCEWS40MTOM";
        String osName = "ArchNSPR";
        String username = "spichalskima";
        String password = "Trimlogic123";
        String stanzaName = "FileNetP8WSI";
        Connection connection = Factory.Connection.getConnection(uri);
        Subject subject = UserContext.createSubject(connection, username, password,
                stanzaName);
        UserContext userContext = UserContext.get();
        userContext.pushSubject(subject);

        Domain domain = Factory.Domain.getInstance(connection, null);
        ObjectStore os = Factory.ObjectStore.fetchInstance(domain, osName, null);
        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(new FilterElement(null, null, null, "objectStoreName",
                null));
        pf.addIncludeProperty(new FilterElement(null, null, null,
                PropertyNames.MIME_TYPE, null));
        Document doc = Factory.Document.getInstance(os, ClassNames.DOCUMENT, docId);

        doc.fetchProperties(pf);


        com.filenet.api.property.Properties props = doc.getProperties();

        Iterator iter = props.iterator();
        System.out.println("Property" + "\t" + "Value");
        System.out.println("----------------------------------");
        Map<String, Object> result = new HashMap<>();
        while (iter.hasNext()) {
            Property prop = (Property) iter.next();
            result.put(prop.getPropertyName(), prop.getObjectValue());
//            if (prop.getPropertyName().equals("objectStoreName"))
//                System.out.println(prop.getPropertyName() + "\t" + prop
//                .getStringValue());
//            else if (prop.getPropertyName().equals(PropertyNames.MIME_TYPE))
//                System.out.println(prop.getPropertyName() + "\t" + prop
//                .getStringValue());
        }

        return result;
    }

}

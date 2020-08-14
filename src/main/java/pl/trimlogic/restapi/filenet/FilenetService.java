package pl.trimlogic.restapi.filenet;

import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.trimlogic.restapi.exception.FilenetException;
import pl.trimlogic.restapi.exception.InvalidIdException;

import javax.security.auth.Subject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilenetService {

    private String uri = "http://192.168.1.140:9080/wsi/FNCEWS40MTOM";
    private String osName = "ArchNSPR";
    private String username = "spichalskima ";
    private String password = "Trimlogic123";
    private String stanzaName = "FileNetP8WSI";

    private Connection connection = Factory.Connection.getConnection(uri);
    private Domain domain = Factory.Domain.getInstance(connection, null);


    public Map<String, Object> getDocument(String requestedGuid) throws FilenetException {
        Id docId = getId(requestedGuid);
        return getDocument(docId);
    }

    public Map<String, Object> getDocument(Id docId) {

        connect(username, password);
        ObjectStore os = Factory.ObjectStore.fetchInstance(domain, osName, null);

        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(new FilterElement(null, null, null, "Creator",
                null));
        pf.addIncludeProperty(new FilterElement(null, null, null, "DateCreated",
                null));
        pf.addIncludeProperty(new FilterElement(null, null, null,
                PropertyNames.MIME_TYPE, null));
        Document doc = Factory.Document.getInstance(os, ClassNames.DOCUMENT, docId);

        doc.fetchProperties(pf);

        Properties props = doc.getProperties();

        Iterator iter = props.iterator();

        //TODO add LOGGERs to file, move to properties, search for documents
        // (properties-value)

        Map<String, Object> result = new HashMap<>();
        while (iter.hasNext()) {
            Property prop = (Property) iter.next();
            if (prop.getPropertyName().equals("Creator"))
                System.out.println(prop.getPropertyName() + "\t" + prop.getStringValue());
            else if (prop.getPropertyName().equals("DateCreated"))
                System.out.println(prop.getPropertyName() + "\t" + prop.getDateTimeValue());
            else if (prop.getPropertyName().equals(PropertyNames.MIME_TYPE))
                System.out.println(prop.getPropertyName() + "\t" + prop.getStringValue());
            result.put(prop.getPropertyName(), prop.getObjectValue());
        }

        return result;
    }


    public Id createDocument(Map<String, String> propsValues) {

        connect(username, password);

        ObjectStore os = Factory.ObjectStore.fetchInstance(domain, osName, null);

        Document doc = Factory.Document.createInstance(os, ClassNames.DOCUMENT);

        doc.getProperties().putObjectValue("DocumentTitle",
                propsValues.get(FilenetConfig.Properties.DOCUMENT_TITLE));
        doc.set_MimeType("text/plain");

        doc.save(RefreshMode.NO_REFRESH);
        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.NO_REFRESH);

        Folder folder = Factory.Folder.getInstance(os, ClassNames.FOLDER, new Id(
                "{C0009BDE-E819-49C5-88DF-ABA1E21D37E5}"));

        ReferentialContainmentRelationship rcr = folder.file(doc,
                AutoUniqueName.AUTO_UNIQUE, "New Document via Java Api",
                DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rcr.save(RefreshMode.NO_REFRESH);

        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(new FilterElement(null, null, null, "DocumentTitle",
                null));
        doc.fetchProperties(pf);
        Properties props = doc.getProperties();
        Iterator iter = props.iterator();
        Map<String, Object> result = new HashMap<>();
        while (iter.hasNext()) {
            Property prop = (Property) iter.next();
            if (prop.getPropertyName().equals("DocumentTitle"))
                System.out.println(prop.getPropertyName() + "\t" + prop.getStringValue());
            result.put(prop.getPropertyName(), prop.getObjectValue());
        }

        return doc.get_Id();
    }


    FilenetService connect(String username, String password) {
        Connection connection = Factory.Connection.getConnection(uri);
        Subject subject = UserContext.createSubject(connection, username, password,
                stanzaName);
        UserContext userContext = UserContext.get();
        userContext.pushSubject(subject);
        return this;
    }


    private Id getId(String requestedGuid) throws InvalidIdException {
        Id docId = Id.asIdOrNull(requestedGuid);
        if (docId == null) {
            throw new InvalidIdException(requestedGuid);
        }
        return docId;
    }

}

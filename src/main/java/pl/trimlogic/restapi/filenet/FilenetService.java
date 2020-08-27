package pl.trimlogic.restapi.filenet;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.trimlogic.restapi.exception.FilenetException;
import pl.trimlogic.restapi.exception.InvalidIdException;

import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilenetService {


    public Map<String, Object> getDocumentProperties(String requestedDocId) throws FilenetException {
        Id docId = getId(requestedDocId);
        return getDocumentProperties(docId);
    }

    public Map<String, Object> getDocumentProperties(Id docId) {

        Map<String, Object> propertyMap = new HashMap<>();
        try {

            new FilenetConnection().connect();
            ObjectStore os = new FilenetConnection().getObjectStore();
            PropertyFilter propertyFilter = new PropertyFilter();
            Document document = Factory.Document.getInstance(os, ClassNames.DOCUMENT, docId);
            propertyMap.put(FilenetConfig.Properties.CLASS_NAME, document.getClassName());


            propertyFilter.addIncludeProperty(new FilterElement(null, null, null, "DocumentTitle",
                    null));
            propertyFilter.addIncludeProperty(new FilterElement(null, null, null, "Creator",
                    null));
            propertyFilter.addIncludeProperty(new FilterElement(null, null, null, "DateCreated",
                    null));
            propertyFilter.addIncludeProperty(new FilterElement(null, null, null,
                    PropertyNames.MIME_TYPE, null));

            document.fetchProperties(propertyFilter);

            Iterator propertyIterator = document.getProperties().iterator();
            while (propertyIterator.hasNext()) {
                Property property = (Property) propertyIterator.next();
                Object value = property.getObjectValue();
                if (value == null)
                    value = "";
                else if (!(value instanceof List)) {
                    value = value.toString();
                }
                propertyMap.put(property.getPropertyName(), value);
                System.out.println(property.getPropertyName() + "\t" + value);
            }

        } catch (Exception e) {
            log.error("Cannot fetch document properties: ", e);
        }

        return propertyMap;
    }

    @SuppressWarnings("DuplicatedCode")
    public List<Map> getDocumentsByGetParams(Map customQuery) {

        List<Map> results = new ArrayList<>();

        try {
            new FilenetConnection().connect();
            ObjectStore objectStore = new FilenetConnection().getObjectStore();

            SearchSQL sqlObject = new SearchSQL();
            sqlObject.setSelectList("d.DocumentTitle, d.Creator, d.Id");
            sqlObject.setMaxRecords(20);
            sqlObject.setFromClauseInitialValue("Document", "d", true);
            String whereClause = "";

            int i = 0;
            for (Object key : customQuery.keySet()) {
                String[] strArr = (String[]) customQuery.get(key);
                for (String val : strArr) {
                    whereClause +=
                            "d." + key + "= '" + val + "'";
                    if (!(++i == customQuery.keySet().size())) {
                        whereClause += " AND ";
                    }
                }
            }
            sqlObject.setWhereClause(whereClause);

            SearchScope search = new SearchScope(objectStore);
            Integer myPageSize = 100;
            PropertyFilter myFilter = new PropertyFilter();
            int myFilterLevel = 1;
            myFilter.setMaxRecursion(myFilterLevel);
            myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
            Boolean continuable = new Boolean(true);
            RepositoryRowSet myRows = search.fetchRows(sqlObject, myPageSize, myFilter, continuable);

            Iterator iter = myRows.iterator();
            while (iter.hasNext()) {
                RepositoryRow row = (RepositoryRow) iter.next();
                Iterator propIt = row.getProperties().iterator();
                Map<String, Object> propertyMap = new HashMap<>();

                while (propIt.hasNext()) {
                    Property prop = (Property) propIt.next();
                    propertyMap.put(prop.getPropertyName(), "" + prop.getObjectValue());
                }
                results.add(propertyMap);
            }
        } catch (Exception e) {
            log.error("Cannot find documents by property", e);
        }
        return results;
    }


    @SuppressWarnings("DuplicatedCode")
    public List<Map> getDocumentsByPostParams(Map customQuery) {

        List<Map> results = new ArrayList<>();

        try {
            new FilenetConnection().connect();
            ObjectStore objectStore = new FilenetConnection().getObjectStore();

            SearchSQL sqlObject = new SearchSQL();
            sqlObject.setSelectList("d.DocumentTitle, d.Creator, d.Id, d.DateCreated");
            sqlObject.setMaxRecords(20);
            sqlObject.setFromClauseInitialValue("Document", "d", true);

            String whereClause = "";
            Iterator keyIt = customQuery.keySet().iterator();
            while (keyIt.hasNext()) {

                String key = (String) keyIt.next();
                LinkedHashMap<String, String> paramObject = (LinkedHashMap) customQuery.get(key);
                String dataType = paramObject.get("data_type");

                switch (dataType) {

                    case "STRING":

                        String operator = paramObject.get("operator");
                        String value = paramObject.get("value");

                        if (operator != null) {
                            if (operator.equals("equals")) {
                                whereClause += "d." + key + "= '" + value + "'";
                            }
                        }
                        if (keyIt.hasNext()) {
                            whereClause += " AND ";
                        }
                        if (!key.equals("DateCreated")) {
                            break;
                        }

                    case "DATE":
                        String operatorDate = paramObject.get("operator");
                        String date1 = paramObject.get("value1");
                        String date2 = paramObject.get("value2");
                        String operatorSign = "";

                        if (operatorDate != null) {
                            if (operatorDate.equals("gt")) {
                                operatorSign = " >= ";
                            } else if (operatorDate.equals("ls")) {
                                operatorSign = " <= ";
                            } else if (operatorDate.equals("gt-ls")) {
                                operatorSign = " >= ";
                                date1 += " AND " + "d." + key + " <= " + date2;
                            }
                        }
                        whereClause += "d." + key + operatorSign + date1;
                }
            }
            sqlObject.setWhereClause(whereClause);


            SearchScope search = new SearchScope(objectStore);
            Integer myPageSize = 100;
            PropertyFilter myFilter = new PropertyFilter();
            int myFilterLevel = 1;
            myFilter.setMaxRecursion(myFilterLevel);
            myFilter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
            Boolean continuable = Boolean.TRUE;
            RepositoryRowSet myRows = search.fetchRows(sqlObject, myPageSize, myFilter, continuable);

            Iterator iter = myRows.iterator();
            while (iter.hasNext()) {
                RepositoryRow row = (RepositoryRow) iter.next();
                Iterator propIt = row.getProperties().iterator();
                Map<String, Object> propertyMap = new HashMap<>();

                while (propIt.hasNext()) {
                    Property prop = (Property) propIt.next();
                    propertyMap.put(prop.getPropertyName(), "" + prop.getObjectValue());
                }
                results.add(propertyMap);
            }
        } catch (Exception e) {
            log.error("Cannot find documents by property", e);
        }
        return results;
    }


    public Id createDocument(String documentTitle) {


        try {

            new FilenetConnection().connect();
            ObjectStore objectStore = new FilenetConnection().getObjectStore();

            Document document = Factory.Document.createInstance(objectStore,
                    ClassNames.DOCUMENT);

            document.getProperties().putObjectValue("DocumentTitle",
                    documentTitle);

            document.set_MimeType("text/plain");

            document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY,
                    CheckinType.MAJOR_VERSION);
            document.save(RefreshMode.NO_REFRESH);
            PropertyFilter propertyFilter = new PropertyFilter();
            document.fetchProperties(propertyFilter);

            Folder folder = Factory.Folder.getInstance(objectStore, ClassNames.FOLDER,
                    new Id(
                    "{C0009BDE-E819-49C5-88DF-ABA1E21D37E5}"));

            ReferentialContainmentRelationship rcr = folder.file(document,
                    AutoUniqueName.AUTO_UNIQUE, "New Document via Java Api",
                    DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
            rcr.save(RefreshMode.NO_REFRESH);

            propertyFilter.addIncludeProperty(new FilterElement(null, null, null, "DocumentTitle",
                    null));
            document.fetchProperties(propertyFilter);
            Properties props = document.getProperties();
            Iterator iter = props.iterator();
            while (iter.hasNext()) {
                Property prop = (Property) iter.next();
                if (prop.getPropertyName().equals("DocumentTitle"))
                    System.out.println(prop.getPropertyName() + "\t" + prop
                            .getStringValue());
            }

            return document.get_Id();
        } catch (Exception e) {
            log.error("Cannot create document: ", e);
        }
        return null;
    }

    private void addDocumentContent(Document document, String fileName, String mimeType
            , InputStream inputStream) {
        ContentElementList contentElementList = Factory.ContentElement.createList();
        ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
        contentTransfer.set_RetrievalName(fileName);

        contentTransfer.setCaptureSource(inputStream);
        contentTransfer.set_ContentType(mimeType);
        contentElementList.add(contentTransfer);

        document.set_ContentElements(contentElementList);
        document.save(RefreshMode.NO_REFRESH);
    }

    private Id getId(String requestedGuid) throws InvalidIdException {
        Id docId = Id.asIdOrNull(requestedGuid);
        if (docId == null) {
            throw new InvalidIdException(requestedGuid);
        }
        return docId;
    }
}

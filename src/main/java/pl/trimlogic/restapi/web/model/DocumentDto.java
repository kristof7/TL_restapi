package pl.trimlogic.restapi.web.model;

public class DocumentDto {

    private String docId;
    private String username;
    private String objectStoreName;
    private Long created;
    private Long lastModified;

    //----------------- G & S -----------------------------

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getObjectStoreName() {
        return objectStoreName;
    }

    public void setObjectStoreName(String objectStoreName) {
        this.objectStoreName = objectStoreName;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }
}

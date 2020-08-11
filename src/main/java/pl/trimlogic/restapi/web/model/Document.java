package pl.trimlogic.restapi.web.model;

import java.util.Objects;

public class Document {

    private String docId;
    private String username;
    private String fileName;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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


    //----------------- Equals & Hashcode -----------------------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(docId, document.docId) &&
                Objects.equals(username, document.username) &&
                Objects.equals(fileName, document.fileName) &&
                Objects.equals(created, document.created) &&
                Objects.equals(lastModified, document.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, username, fileName, created, lastModified);
    }
}

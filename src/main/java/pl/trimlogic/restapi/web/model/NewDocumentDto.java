package pl.trimlogic.restapi.web.model;

import lombok.NonNull;

public class NewDocumentDto {

    @NonNull
    private String username;
    @NonNull
    private String objectStoreName;

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
}

package pl.trimlogic.restapi.web.model;

import lombok.NonNull;

public class NewDocumentDto {

    @NonNull
    private String username;
    @NonNull
    private String fileName;

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
}

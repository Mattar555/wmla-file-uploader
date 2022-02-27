package com.ibm.anz.wmla.sdk.ibmwmla.enumerations;

public enum Schema {

    FILENAME("fileName", "Key who's value contains the name of file in question"),
    FILESIZE("fileSize", "Key who's value contains the size of file in question"),
    STATUS("uploadStatus", "Keys who's value contains the upload status of the file in question"),
    ;

    private String value;
    private String description;

    Schema(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }
}

package com.ibm.anz.wmla.sdk.ibmwmla.enumerations;

public enum States {

    PENDING("Pending", "Files when initially uploaded will be in this state"),
    ERROR("Error", "If after a specified period of time, if the file is not uploaded, " +
            " we transition to this state eventually"),
    SUCCESS("Success", "Once the expected size is met, transition to this state."),
    ;

    private String value;
    private String description;

    States(String value, String description) {
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

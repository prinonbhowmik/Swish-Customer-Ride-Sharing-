package com.hydertechno.swishcustomer.ForApi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Distance {
    @JsonProperty("text")
    private String text;
    @JsonProperty("value")
    private Integer value;

    @JsonProperty("text")
    public String getText() {
        return text;
    }
    @JsonProperty("value")
    public Integer getValue() {
        return value;
    }
}

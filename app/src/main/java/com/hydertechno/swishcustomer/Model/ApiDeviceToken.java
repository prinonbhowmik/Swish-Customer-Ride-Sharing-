package com.hydertechno.swishcustomer.Model;

public class ApiDeviceToken {
    private String customer_id;
    private String device_token;

    public ApiDeviceToken(String customer_id, String device_token) {
        this.customer_id = customer_id;
        this.device_token = device_token;
    }

    public ApiDeviceToken(String customer_id) {
        this.customer_id = customer_id;
    }

    public ApiDeviceToken() {
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}

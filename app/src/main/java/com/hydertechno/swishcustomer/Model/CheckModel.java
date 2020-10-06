package com.hydertechno.swishcustomer.Model;

public class CheckModel {

    private String status;
    private String otp;
    private String customer_id;

    public CheckModel() {
    }

    public CheckModel(String status, String otp, String customer_id) {
        this.status = status;
        this.otp = otp;
        this.customer_id = customer_id;
    }

    public String getStatus() {
        return status;
    }

    public String getOtp() {
        return otp;
    }

    public String getCustomer_id() {
        return customer_id;
    }
}

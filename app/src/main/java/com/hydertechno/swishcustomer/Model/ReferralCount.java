package com.hydertechno.swishcustomer.Model;

public class ReferralCount {
    private String status;
    private int total;

    public ReferralCount(String status, int total) {
        this.status = status;
        this.total = total;
    }

    public ReferralCount() {
    }

    public String getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }
}

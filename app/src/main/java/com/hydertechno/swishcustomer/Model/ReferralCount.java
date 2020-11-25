package com.hydertechno.swishcustomer.Model;

public class ReferralCount {
    private int total;
    private int active;

    public ReferralCount(int total, int active) {
        this.total = total;
        this.active = active;
    }

    public ReferralCount() {
    }

    public int getTotal() {
        return total;
    }

    public int getActive() {
        return active;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setActive(int active) {
        this.active = active;
    }
}

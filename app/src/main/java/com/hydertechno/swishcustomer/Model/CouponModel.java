package com.hydertechno.swishcustomer.Model;

public class CouponModel {

    private String msg;
    private int amount;

    public CouponModel() {
    }

    public CouponModel(String msg, int amount) {
        this.msg = msg;
        this.amount = amount;
    }

    public String getMsg() {
        return msg;
    }

    public int getAmount() {
        return amount;
    }
}

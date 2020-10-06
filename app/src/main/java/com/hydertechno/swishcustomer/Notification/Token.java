package com.hydertechno.swishcustomer.Notification;

public class Token {
    private String token;
    private String userId;


    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


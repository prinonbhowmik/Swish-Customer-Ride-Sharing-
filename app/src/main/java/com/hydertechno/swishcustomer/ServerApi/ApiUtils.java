package com.hydertechno.swishcustomer.ServerApi;

public class ApiUtils {
    public static final String BASE_URL = "https://swish.com.bd/api/";

    public static ApiInterface getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}

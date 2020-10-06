package com.hydertechno.swishcustomer.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAXIzJqj4:APA91bFwkxwO3f6V5miXxnvguYlNWJZTVrJt1fumKLSISpqJWInO0Y-c32_JiQmdWsJJK6WHzj-In-cPchANiyo5xvH6EWF80sLEa4KR8vve3HvNDYpPY4z2XWarFNDDbcNzbQbpbkb1"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

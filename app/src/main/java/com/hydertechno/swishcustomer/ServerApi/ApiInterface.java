package com.hydertechno.swishcustomer.ServerApi;


import com.hydertechno.swishcustomer.Model.CheckModel;
import com.hydertechno.swishcustomer.Model.CouponModel;
import com.hydertechno.swishcustomer.Model.CouponShow;
import com.hydertechno.swishcustomer.Model.DriverInfo;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.Model.RidingRate;
import com.hydertechno.swishcustomer.Model.TripReportModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("customercheck")
    @FormUrlEncoded
    Call<List<CheckModel>> checkNo(@Field("phone_no") String phone_no);

    @POST("dforgotpassword")
    @FormUrlEncoded
    Call<List<CheckModel>> forgotPassword(@Field("phone_no") String phone_no);

    @PUT("cpasswordupdate/{customer_id}")
    @FormUrlEncoded
    Call<List<Profile>> resetPassword(@Path("customer_id") String customer_id,
                                          @Field("password") String password);

    @POST("customersave")
    @Multipart
    Call<List<Profile>> register(@Part("email") RequestBody email,
                                 @Part MultipartBody.Part image,
                                 @Part("name") RequestBody name,
                                 @Part("password") RequestBody password,
                                 @Part("phone") RequestBody phone,
                                 @Part("gender") RequestBody gender,
                                 @Part("remember_token") RequestBody remember_token,
                                 @Part("token") RequestBody token,
                                 @Part("wallet") int wallet,
                                 @Part("referral") RequestBody referral);

    @GET("customer?")
    Call<List<Profile>> getData(@Query("id") String customer_id);

    @GET("driver?")
    Call<List<DriverProfile>> getDriverData(@Query("id") String id);

    @Multipart
    @POST("customerupdate")
    Call<List<Profile>> updateData(@Part("customer_id") RequestBody customer_id,
                                   @Part MultipartBody.Part image,
                                   @Part("name") RequestBody full_name,
                                   @Part("email") RequestBody email,
                                   @Part("gender") RequestBody gender);

    @Multipart
    @POST("customerupdate")
    Call<List<Profile>> updateDatawithoutimage(@Part("customer_id") RequestBody customer_id,
                                               @Part("name") RequestBody full_name,
                                               @Part("email") RequestBody email,
                                               @Part("gender") RequestBody gender);


    @GET("customerbookinglist?")
    Call<List<RideModel>> rideHistory(@Query("id") String customer_id);

    @GET("customerhourlylist?")
    Call<List<HourlyRideModel>> insideHistory(@Query("id") String customer_id);

    @FormUrlEncoded
    @PUT("customerpassword/{customer_id}")
    Call<List<Profile>> changePassword(@Path("customer_id") String customer_id,
                                       @Field("password") String password);

    @GET("bookingrate?")
    Call<List<RidingRate>> getPrice(@Query("id") String car_type);

    @FormUrlEncoded
    @POST("bookforlater")
    Call<List<RideModel>> saveTripRequest(@Field("bookingId") String bookingId,
                                          @Field("bookingStatus") String bookingStatus,
                                          @Field("carType") String carType,
                                          @Field("customerId") String customerId,
                                          @Field("destinationLat") String destinationLat,
                                          @Field("destinationLon") String destinationLon,
                                          @Field("destinationPlace") String destinationPlace,
                                          @Field("driverId") String driverId,
                                          @Field("endTime") String endTime,
                                          @Field("pickUpDate") String pickupDate,
                                          @Field("pickUpLat") String pickupLat,
                                          @Field("pickUpLon") String pickupLon,
                                          @Field("pickUpPlace") String pickupPlace,
                                          @Field("pickUpTime") String pickupTime,
                                          @Field("price") String price,
                                          @Field("rideStatus") String rideStatus,
                                          @Field("paymentType") String payment);

    @FormUrlEncoded
    @POST("hourlyride")
    Call<List<HourlyRideModel>> saveHourlyTripRequest(@Field("bookingId") String bookingId,
                                                      @Field("bookingStatus") String bookingStatus,
                                                      @Field("carType") String carType,
                                                      @Field("customerId") String customerId,
                                                      @Field("driverId") String driverId,
                                                      @Field("endTime") String endTime,
                                                      @Field("pickUpDate") String pickupDate,
                                                      @Field("pickUpLat") String pickupLat,
                                                      @Field("pickUpLon") String pickupLon,
                                                      @Field("pickUpPlace") String pickupPlace,
                                                      @Field("pickUpTime") String pickupTime,
                                                      @Field("price") String price,
                                                      @Field("rideStatus") String rideStatus,
                                                      @Field("paymentType") String payment
    );

    @FormUrlEncoded
    @PUT("pickupupdate/{bookingId}")
    Call<List<RideModel>> updatePickUp(@Path("bookingId") String bookingId,
                                       @Field("pickUpLat") String pickupLat,
                                       @Field("pickUpLon") String pickupLon,
                                       @Field("pickUpPlace") String pickupPlace);

    @FormUrlEncoded
    @PUT("destinationupdate/{bookingId}")
    Call<List<RideModel>> updateDestination(@Path("bookingId") String bookingId,
                                            @Field("destinationLat") String destinationLat,
                                            @Field("destinationLon") String destinationLon,
                                            @Field("destinationPlace") String destinationPlace);

    @FormUrlEncoded
    @PUT("priceupdate/{bookingId}")
    Call<List<RideModel>> priceUpdate(@Path("bookingId") String bookingId,
                                      @Field("price") String price);

    @FormUrlEncoded
    @PUT("datetimeupdate/{bookingId}")
    Call<List<RideModel>> datetimeUpdate(@Path("bookingId") String bookingId,
                                         @Field("pickUpDate") String pickupDate,
                                         @Field("pickUpTime") String pickupTime);


    @DELETE("bookingdelete/{bookingId}")
    Call<Void> deleteTrip(@Path("bookingId") String bookingId);

    @FormUrlEncoded
    @PUT("rating/{driver_id}")
    Call<List<DriverProfile>> updateRating(@Path("driver_id") String driver_id,
                                           @Field("rating") float rating,
                                           @Field("ratingCount") int ratingCount);

    @FormUrlEncoded
    @PUT("bookingrating/{bookingId}")
    Call<List<RideModel>> addRating(@Path("bookingId") String bookingId,
                                    @Field("rating") float ratingCount);

    @FormUrlEncoded
    @PUT("hourlyrating/{bookingId}")
    Call<List<HourlyRideModel>> addHourRating(@Path("bookingId") String bookingId,
                                              @Field("rating") float ratingCount);

    @POST("couponset")
    @FormUrlEncoded
    Call<List<CouponModel>> checkCoupon(@Field("customer_id") String customer_id,
                                        @Field("coupons_code") String coupons_code);

    @GET("couponshow?")
    Call<List<CouponShow>> getValidCoupon(@Query("customer_id") String customer_id);

    @GET("driverinfo?")
    Call<List<DriverInfo>> getCarNumber(@Query("id") String driverid);

    @FormUrlEncoded
    @POST("report-an-issue")
    Call<List<TripReportModel>> report(@Field("r_id") String trip_id,
                                       @Field("d_id")String driver_id,
                                       @Field("c_id") String cust_id,
                                       @Field("issue") String reportmsg);

}

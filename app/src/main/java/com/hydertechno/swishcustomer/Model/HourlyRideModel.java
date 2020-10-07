package com.hydertechno.swishcustomer.Model;

public class HourlyRideModel {

    private String bookingId;
    private String bookingStatus;
    private String carType;
    private String pickUpLat;
    private String pickUpLon;
    private String endTime;
    private String customerId;
    private String driverId;
    private String pickUpPlace;
    private String pickUpDate;
    private String pickUpTime;
    private String price;
    private String rideStatus;
    private String ratingStatus;
    private String payment;
    private String discount;
    private String finalPrice;
    private String totalTime;


    public HourlyRideModel() {
    }

    public HourlyRideModel(String bookingId, String bookingStatus, String carType, String pickUpLat, String pickUpLon, String endTime, String customerId, String driverId, String pickUpPlace, String pickUpDate, String pickUpTime, String price, String rideStatus, String ratingStatus, String payment, String discount, String finalPrice, String totalTime) {
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.carType = carType;
        this.pickUpLat = pickUpLat;
        this.pickUpLon = pickUpLon;
        this.endTime = endTime;
        this.customerId = customerId;
        this.driverId = driverId;
        this.pickUpPlace = pickUpPlace;
        this.pickUpDate = pickUpDate;
        this.pickUpTime = pickUpTime;
        this.price = price;
        this.rideStatus = rideStatus;
        this.ratingStatus = ratingStatus;
        this.payment = payment;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.totalTime = totalTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getCarType() {
        return carType;
    }

    public String getPickUpLat() {
        return pickUpLat;
    }

    public String getPickUpLon() {
        return pickUpLon;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getPickUpPlace() {
        return pickUpPlace;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getPrice() {
        return price;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public String getRatingStatus() {
        return ratingStatus;
    }

    public String getPayment() {
        return payment;
    }

    public String getDiscount() {
        return discount;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public String getTotalTime() {
        return totalTime;
    }
}

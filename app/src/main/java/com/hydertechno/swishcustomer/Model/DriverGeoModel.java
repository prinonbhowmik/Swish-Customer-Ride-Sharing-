package com.hydertechno.swishcustomer.Model;

import com.firebase.geofire.GeoLocation;

public class DriverGeoModel {

    private String key;
    private GeoLocation geoLocation;
    private DriverInfoModel driverInfoModel;

    public DriverGeoModel() {
    }

    public DriverGeoModel(String key, GeoLocation geoLocation) {
        this.key = key;
        this.geoLocation = geoLocation;
    }

    public String getKey() {
        return key;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public DriverInfoModel getDriverInfoModel() {
        return driverInfoModel;
    }
}

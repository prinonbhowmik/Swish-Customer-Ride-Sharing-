package com.hydertechno.swishcustomer.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hydertechno.swishcustomer.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Home_Work_Address extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private AutocompleteSupportFragment autocompleteFragment;
    private Double currentLat, currentLon,latitude=0.0,longitude=0.0;
    private String type, userId ,apiKey = "AIzaSyCCqD0ogQ8adzJp_z2Y2W2ybSFItXYwFfI";
    private String placeName;
    private Locale locale;
    private Geocoder geocoder;
    private MarkerOptions marker;
    BitmapDescriptor markerIcon;
    private Button saveAddressBtn;
    private MarkerOptions addressMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__work__address);

        Intent intent = getIntent();
        currentLat = intent.getDoubleExtra("currentLat", 0.0);
        currentLon = intent.getDoubleExtra("currentLon", 0.0);
        type = intent.getStringExtra("type");
        userId = intent.getStringExtra("id");

        init();


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                placeName = place.getName();
                autocompleteFragment.setText(placeName);
                List<Address> latlonaddress = null;
                try {
                    latlonaddress = geocoder.getFromLocationName(place.getName(), 1);
                    if (latlonaddress.size() > 0) {
                        Address location = latlonaddress.get(0);

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        map.clear();
                        if (type.equals("home")){
                            markerIcon = vectorToBitmap(R.drawable.ic_pin);
                        }else{
                            markerIcon = vectorToBitmap(R.drawable.ic_destination);
                        }
                        marker = new MarkerOptions().position(new LatLng(latitude, longitude))
                                .icon(markerIcon).draggable(true);
                        map.addMarker(marker.title("Drag for suitable position")).showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));

                        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {
                            }
                            @Override
                            public void onMarkerDrag(Marker marker) {
                            }
                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                latitude = marker.getPosition().latitude;
                                longitude = marker.getPosition().longitude;

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    placeName = addresses.get(0).getAddressLine(0);
                                    autocompleteFragment.setText(placeName);
                                    if (type.equals("home")) {
                                        markerIcon = vectorToBitmap(R.drawable.ic_pin);
                                    }else{
                                        markerIcon = vectorToBitmap(R.drawable.ic_destination);
                                    }
                                    marker.setIcon(markerIcon);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(@NonNull Status status) {

            }
        });

        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude==0.0 && longitude==0.0){
                    latitude=currentLat;
                    longitude=currentLon;
                }

                DatabaseReference homeRef = FirebaseDatabase.getInstance().getReference("UserLocation").child(userId);
                HashMap<String, Object> rideInfo = new HashMap<>();
                rideInfo.put("place", placeName);
                rideInfo.put("lat", latitude);
                rideInfo.put("lon", longitude);

                if (type.equals("home")){
                    homeRef.child("Home").setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toasty.success(Home_Work_Address.this, "Home Address Saved!", Toasty.LENGTH_LONG).show();
                            startActivity(new Intent(Home_Work_Address.this,MainActivity.class));
                            finish();
                        }
                    });
                }
                else{
                    homeRef.child("Work").setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toasty.success(Home_Work_Address.this, "Work Address Saved!", Toasty.LENGTH_LONG).show();
                            startActivity(new Intent(Home_Work_Address.this,MainActivity.class));
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locale = new Locale("en");
        geocoder = new Geocoder(this, locale);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey, locale);
        }
        autocompleteFragment = (AutocompleteSupportFragment) this.getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("BD");
        if (type.equals("home")){
            autocompleteFragment.setHint("Search Your Home Address");
        }else{
            autocompleteFragment.setHint("Search Your Work Address");
        }
        saveAddressBtn = findViewById(R.id.saveAddressBtn);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLon), 16);
        map.moveCamera(point);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retro));

        if (type.equals("home")){
            markerIcon = vectorToBitmap(R.drawable.ic_pin);
        }else{
            markerIcon = vectorToBitmap(R.drawable.ic_destination);
        }

        addressMarker = new MarkerOptions().position(new LatLng(currentLat, currentLon)).icon(markerIcon).draggable(true);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(currentLat, currentLon, 1);
            if (addresses.size() > 0) {
                Address location = addresses.get(0);
                placeName = location.getAddressLine(0);
                autocompleteFragment.setText(placeName);
                map.addMarker(addressMarker.title("Drag for suitable position")).showInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLon), 19));

                map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        latitude = marker.getPosition().latitude;
                        longitude = marker.getPosition().longitude;

                        try {
                            List<Address> addresses = geocoder.getFromLocation(currentLat, currentLon, 1);
                            Address location = addresses.get(0);
                            placeName = location.getAddressLine(0);

                            autocompleteFragment.setText(placeName);

                            marker.setIcon(markerIcon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Toasty.error(Home_Work_Address.this, "Place Not Found!", Toasty.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Home_Work_Address.this,MainActivity.class));
        finish();
    }
}
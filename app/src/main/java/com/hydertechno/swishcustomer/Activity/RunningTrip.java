package com.hydertechno.swishcustomer.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.ForMap.FetchURL;
import com.hydertechno.swishcustomer.ForMap.TaskLoadedCallback;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.Remote.LatLngInterpolator;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.hydertechno.swishcustomer.Utils.AppConstants;
import com.hydertechno.swishcustomer.Utils.GpsUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RunningTrip extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap map;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;
    private boolean isGPS = false, isContinue = false;
    private StringBuilder stringBuilder;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private TextView placeNameTV;
    private int edit, check;
    private double destinationLat, destinationLon, currentLat, currentLon, pickUpLat, pickUpLon;
    private String userId, pickUpPlace, destinationPlace, tripId, carType;
    private Button doneBtn, detailsBtn,showFareBtn;
    private Locale locale;
    private String apiKey = "AIzaSyCCqD0ogQ8adzJp_z2Y2W2ybSFItXYwFfI";
    private String rideStatus;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;
    private String type;
    private ProgressDialog progressDialog;
    private String driverId;
    private float v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_trip);

        init();

        checkConnection();

        userId = sharedPreferences.getString("id", "");

        Intent intent = getIntent();
        check = intent.getIntExtra("check", 0);

        getData();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit == 1) {
                    if (check == 1) {  //change pickUp Point
                        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("CustomerRides")
                                .child(userId).child(tripId);
                        updateRef.child("pickUpLat").setValue(String.valueOf(pickUpLat));
                        updateRef.child("pickUpLon").setValue(String.valueOf(pickUpLon));
                        updateRef.child("pickUpPlace").setValue(pickUpPlace);

                        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("BookForLater")
                                .child(carType).child(tripId);
                        newRef.child("pickUpLat").setValue(String.valueOf(pickUpLat));
                        newRef.child("pickUpLon").setValue(String.valueOf(pickUpLon));
                        newRef.child("pickUpPlace").setValue(pickUpPlace);

                        Call<List<RideModel>> call = apiInterface.updatePickUp(tripId, String.valueOf(pickUpLat),
                                String.valueOf(pickUpLon), pickUpPlace);
                        call.enqueue(new Callback<List<RideModel>>() {
                            @Override
                            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                            }

                            @Override
                            public void onFailure(Call<List<RideModel>> call, Throwable t) {

                            }
                        });

                        Intent intent = new Intent(RunningTrip.this, MyRidesDetails.class);
                        intent.putExtra("bookingId", tripId);
                        intent.putExtra("check", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else if (check == 2) {
                        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("CustomerRides")
                                .child(userId).child(tripId);
                        updateRef.child("destinationLat").setValue(String.valueOf(destinationLat));
                        updateRef.child("destinationLon").setValue(String.valueOf(destinationLon));
                        updateRef.child("destinationPlace").setValue(destinationPlace);

                        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("BookForLater")
                                .child(carType).child(tripId);
                        newRef.child("destinationLat").setValue(String.valueOf(destinationLat));
                        newRef.child("destinationLon").setValue(String.valueOf(destinationLon));
                        newRef.child("destinationPlace").setValue(destinationPlace);

                        Call<List<RideModel>> call = apiInterface.updateDestination(tripId, String.valueOf(destinationLat),
                                String.valueOf(destinationLon), destinationPlace);
                        call.enqueue(new Callback<List<RideModel>>() {
                            @Override
                            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                            }

                            @Override
                            public void onFailure(Call<List<RideModel>> call, Throwable t) {

                            }
                        });

                        Intent intent = new Intent(RunningTrip.this, MyRidesDetails.class);
                        intent.putExtra("bookingId", tripId);
                        intent.putExtra("check", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else if (check==4){
                        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides")
                                .child(userId).child(tripId);
                        updateRef.child("pickUpLat").setValue(String.valueOf(pickUpLat));
                        updateRef.child("pickUpLon").setValue(String.valueOf(pickUpLon));
                        updateRef.child("pickUpPlace").setValue(pickUpPlace);

                        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("BookHourly")
                                .child(carType).child(tripId);
                        newRef.child("pickUpLat").setValue(String.valueOf(pickUpLat));
                        newRef.child("pickUpLon").setValue(String.valueOf(pickUpLon));
                        newRef.child("pickUpPlace").setValue(pickUpPlace);

                        Intent intent = new Intent(RunningTrip.this, HourlyRideDetails.class);
                        intent.putExtra("bookingId", tripId);
                        intent.putExtra("check", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("regular")){
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("CustomerRides").child(userId).child(tripId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                RideModel model = snapshot.getValue(RideModel.class);
                                String driverId = model.getDriverId();
                                String pickTime = model.getPickUpTime();
                                String price = model.getPrice();
                                String pickup = model.getPickUpPlace();
                                String destination = model.getDestinationPlace();


                                Bundle args = new Bundle();
                                args.putString("driverId", driverId);
                                args.putString("pickTime", pickTime);
                                args.putString("price", price);
                                args.putString("pickup", pickup);
                                args.putString("destination", destination);
                                args.putInt("check", check);

                                OnGoingDriverDetailsBottom bottom_sheet = new OnGoingDriverDetailsBottom();
                                bottom_sheet.setArguments(args);
                                bottom_sheet.show(getSupportFragmentManager(), "bottomSheet");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if (type.equals("hourly")){
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("CustomerHourRides").child(userId).child(tripId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                RideModel model = snapshot.getValue(RideModel.class);
                                String driverId = model.getDriverId();
                                String pickTime = model.getPickUpTime();
                                String price = model.getPrice();
                                String pickup = model.getPickUpPlace();
                                String destination = model.getDestinationPlace();


                                Bundle args = new Bundle();
                                args.putString("driverId", driverId);
                                args.putString("pickTime", pickTime);
                                args.putString("price", price);
                                args.putString("pickup", pickup);
                                args.putString("destination", destination);
                                args.putInt("check", check);

                                OnGoingDriverDetailsBottom bottom_sheet = new OnGoingDriverDetailsBottom();
                                bottom_sheet.setArguments(args);
                                bottom_sheet.show(getSupportFragmentManager(), "bottomSheet");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        checkTripStatus();

        checkHourlyTripStatus();

        showFareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkConnection();
                gotoShowCash(type);
            }
        });

    }

    private void checkHourlyTripStatus() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference().child("CustomerHourRides").child(userId).child(tripId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    rideStatus = snapshot.child("rideStatus").getValue().toString();
                    if (rideStatus.equals("End")) {
                        detailsBtn.setVisibility(View.GONE);
                        showFareBtn.setVisibility(View.VISIBLE);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkTripStatus() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference().child("CustomerRides").child(userId).child(tripId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    rideStatus = snapshot.child("rideStatus").getValue().toString();
                    if (rideStatus.equals("End")) {
                        detailsBtn.setVisibility(View.GONE);
                        showFareBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        if (check == 1) {
            //pick up edit
            tripId = intent.getStringExtra("tripId");
            carType = intent.getStringExtra("type");
            pickUpLat = Double.parseDouble(intent.getStringExtra("lat"));
            pickUpLon = Double.parseDouble(intent.getStringExtra("lon"));
            pickUpPlace = intent.getStringExtra("place");
            edit = intent.getIntExtra("edit", 0);
        }
        else if (check == 2) {
            //destination edit
            tripId = intent.getStringExtra("tripId");
            carType = intent.getStringExtra("type");
            destinationLat = Double.parseDouble(intent.getStringExtra("lat"));
            destinationLon = Double.parseDouble(intent.getStringExtra("lon"));
            destinationPlace = intent.getStringExtra("place");
            edit = intent.getIntExtra("edit", 0);
        }
        else if (check == 3) {
            //book for latter on going
            tripId = intent.getStringExtra("tripId");
            pickUpLat = Double.parseDouble(intent.getStringExtra("pLat"));
            pickUpLon = Double.parseDouble(intent.getStringExtra("pLon"));
            destinationLat = Double.parseDouble(intent.getStringExtra("dLat"));
            destinationLon = Double.parseDouble(intent.getStringExtra("dLon"));
            carType = intent.getStringExtra("carType");
            driverId = intent.getStringExtra("driverId");
            type = "regular";
        }
        else if (check == 4) {
            //hourly ride pick up edit
            tripId = intent.getStringExtra("tripId");
            carType = intent.getStringExtra("type");
            pickUpLat = Double.parseDouble(intent.getStringExtra("lat"));
            pickUpLon = Double.parseDouble(intent.getStringExtra("lon"));
            pickUpPlace = intent.getStringExtra("place");
            edit = intent.getIntExtra("edit", 0);
        }
        else if (check == 5){
            doneBtn.setVisibility(View.GONE);
            placeNameTV.setText("Your are on a hourly ride.");
            tripId = intent.getStringExtra("tripId");
            carType = intent.getStringExtra("carType");
            driverId = intent.getStringExtra("driverId");
            type = "hourly";
        }

    }

    private void gotoShowCash(String type) {

        progressDialog=new ProgressDialog(RunningTrip.this);
        progressDialog.setMessage("Calculating Fare!!");
        if (!isFinishing()) {
            progressDialog.show();

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (type.equals("regular")){
                    Intent intent = new Intent(RunningTrip.this, ShowCash.class);
                    intent.putExtra("tripId", tripId);
                    intent.putExtra("check", 3);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(RunningTrip.this, ShowCash.class);
                    intent.putExtra("tripId", tripId);
                    intent.putExtra("check", 4);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        },5000);

    }

    private void init() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locale = new Locale("en");
        geocoder = new Geocoder(this, locale);
        placeNameTV = findViewById(R.id.placeNameTV);
        doneBtn = findViewById(R.id.doneBtn);
        detailsBtn = findViewById(R.id.detailsBtn);
        showFareBtn = findViewById(R.id.showFareBtn);
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        apiInterface = ApiUtils.getUserService();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        locationCallback = new LocationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLon = location.getLongitude();
                        if (!isContinue) {
                            getCurrentLocation();
                        } else {
                            stringBuilder.append(currentLat);
                            stringBuilder.append("-");
                            stringBuilder.append(currentLon);
                            stringBuilder.append("\n\n");
                            Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        if (!isGPS) {
            Toast.makeText(this, "Please turn on your GPS!", Toast.LENGTH_SHORT).show();
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();
        Log.d("getResult", currentLat + "," + currentLon);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location1) {
                if (location1 != null) {
                    currentLat = location1.getLatitude();
                    currentLon = location1.getLongitude();
                } else {
                    if (ActivityCompat.checkSelfPermission(RunningTrip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunningTrip.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retro));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        //update every second
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        getCurrentLocation();

        if (check == 1) {
            if (edit == 1) {
                editablePickUpPoint();
            } else if (edit == 0) {
                doneBtn.setVisibility(View.GONE);
                showPickUpPoint();
            }
        }
        else if (check == 2) {
            if (edit == 1) {
                editDestinationPlace();
            } else if (edit == 0) {
                doneBtn.setVisibility(View.GONE);
                showDestinationPoint();
            }
        }
        else if (check == 3) {

            DatabaseReference mapRef = FirebaseDatabase.getInstance().getReference("OnLineDrivers")
                    .child(carType).child(driverId).child("l");
            mapRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pickUpLat = (double) snapshot.child("0").getValue();
                    pickUpLon = (double) snapshot.child("1").getValue();

                    showTripRoute(pickUpLat,pickUpLon);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            doneBtn.setVisibility(View.GONE);
            placeNameTV.setVisibility(View.GONE);
            detailsBtn.setVisibility(View.VISIBLE);
        }
        else if (check==4){
            if (edit == 1) {
                editablePickUpPoint();
            } else if (edit == 0) {
                doneBtn.setVisibility(View.GONE);
                showPickUpPoint();
            }
        }
        else if (check == 5){
            map.clear();
            detailsBtn.setVisibility(View.VISIBLE);
            DatabaseReference mapRef = FirebaseDatabase.getInstance().getReference("OnLineDrivers")
                    .child(carType).child(driverId).child("l");
            mapRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double lat = (double) snapshot.child("0").getValue();
                    double lon = (double) snapshot.child("1").getValue();

                    showHourlyLocation(lat,lon,tripId);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void showHourlyLocation(double lat, double lon, String tripId) {
        map.clear();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 10 seconds
        locationRequest.setFastestInterval(2000); // 5 seconds

        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).icon(markerIcon));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 16));
    }

    private void showTripRoute(double pickUpLat,double pickUpLon) {
        map.clear();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.ic_car_top_view);

        float bearing = CalculateBearingAngle(pickUpLat, pickUpLon,destinationLat, destinationLon);

        place1 = new MarkerOptions().icon(markerIcon).flat(true)
                .position(new LatLng(pickUpLat, pickUpLon)).title(pickUpPlace).anchor(0.5F, 0.5F);
        BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.ic_destination);
        place2 = new MarkerOptions().icon(markerIcon2)
                .position(new LatLng(destinationLat, destinationLon)).rotation(bearing).title(destinationPlace).flat(true);

        new FetchURL(RunningTrip.this).execute(getUrl(place1.getPosition(), place2.getPosition(),
                "driving"), "driving");

        map.addMarker(place1).showInfoWindow();
        map.addMarker(place2).showInfoWindow();
        LatLng latLng = new LatLng(pickUpLat, pickUpLon);
        MarkerAnimation.animateMarkerToGB(place1, latLng, new LatLngInterpolator.Spherical());

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickUpLat, pickUpLon), 18));

    }

    private void showDestinationPoint() {
        placeNameTV.setText(destinationPlace);
        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.ic_destination);
        map.addMarker(new MarkerOptions().position(new LatLng(destinationLat, destinationLon)).icon(markerIcon));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destinationLat, destinationLon), 16));
    }

    public float CalculateBearingAngle(double startLatitude,double startLongitude, double endLatitude, double endLongitude){
        double Phi1 = Math.toRadians(startLatitude);
        double Phi2 = Math.toRadians(endLatitude);
        double DeltaLambda = Math.toRadians(endLongitude - startLongitude);

        double Theta = atan2((sin(DeltaLambda)*cos(Phi2)) , (cos(Phi1)*sin(Phi2) - sin(Phi1)*cos(Phi2)*cos(DeltaLambda)));
        return (float)Math.toDegrees(Theta);
    }

    private void editDestinationPlace() {
        placeNameTV.setText(destinationPlace);
        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.ic_destination);
        map.addMarker(new MarkerOptions().position(new LatLng(destinationLat, destinationLon)).icon(markerIcon).draggable(true).title("Drag for suitable position")).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destinationLat, destinationLon), 16));

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                destinationLat = marker.getPosition().latitude;
                destinationLon = marker.getPosition().longitude;

                Geocoder geocoder = new Geocoder(RunningTrip.this, locale);
                try {
                    List<Address> addresses = geocoder.getFromLocation(destinationLat, destinationLon, 1);
                    destinationPlace = addresses.get(0).getAddressLine(0);
                    placeNameTV.setText(destinationPlace);
                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.ic_destination);
                    marker.setIcon(markerIcon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showPickUpPoint() {
        placeNameTV.setText(pickUpPlace);
        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
        map.addMarker(new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickUpLat, pickUpLon), 16));
    }

    private void editablePickUpPoint() {
        placeNameTV.setText(pickUpPlace);
        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
        map.addMarker(new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true).title("Drag for suitable position")).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickUpLat, pickUpLon), 16));

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                pickUpLat = marker.getPosition().latitude;
                pickUpLon = marker.getPosition().longitude;

                Geocoder geocoder = new Geocoder(RunningTrip.this, locale);
                try {
                    List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                    pickUpPlace = addresses.get(0).getAddressLine(0);
                    placeNameTV.setText(pickUpPlace);
                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                    marker.setIcon(markerIcon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        // DrawableCompat.setTint(vectorDrawable);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + apiKey;
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {

        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
       /* ValueAnimator animator = ValueAnimator.ofInt(0,100);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                List<LatLng> points = currentPolyline.getPoints();
                int percentValue =(int) animation.getAnimatedValue();
                int size = points.size();
                int newPoints = (int) (percentValue*size/100.0f);
                List<LatLng> p = points.subList(0,newPoints);
                currentPolyline.setPoints(p);
            }
        });
        animator.start();
*/
    }
}
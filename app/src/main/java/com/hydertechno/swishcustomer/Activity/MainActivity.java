package com.hydertechno.swishcustomer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hydertechno.swishcustomer.ForApi.DistanceApiClient;
import com.hydertechno.swishcustomer.ForApi.DistanceResponse;
import com.hydertechno.swishcustomer.ForApi.Element;
import com.hydertechno.swishcustomer.ForApi.RestUtil;
import com.hydertechno.swishcustomer.ForMap.FetchURL;
import com.hydertechno.swishcustomer.ForMap.TaskLoadedCallback;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.DriverInfo;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.Model.RidingRate;
import com.hydertechno.swishcustomer.Notification.APIService;
import com.hydertechno.swishcustomer.Notification.Client;
import com.hydertechno.swishcustomer.Notification.Data;
import com.hydertechno.swishcustomer.Notification.MyResponse;
import com.hydertechno.swishcustomer.Notification.Sender;
import com.hydertechno.swishcustomer.Notification.Token;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.hydertechno.swishcustomer.Utils.AppConstants;
import com.hydertechno.swishcustomer.Utils.Config;
import com.hydertechno.swishcustomer.Utils.GpsUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, TaskLoadedCallback {

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String userId;
    private NeomorphFrameLayout backNFL;
    private Animation btnanim;
    private String apiKey = "AIzaSyCCqD0ogQ8adzJp_z2Y2W2ybSFItXYwFfI", pickUpPlace, destinationPlace;
    private ImageView nav_icon;
    private NavigationView navigationView;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private double pickUpLat = 0.0, pickUpLon = 0.0;
    private double destinationLat = 0.0, destinationLon = 0.0;
    private DrawerLayout drawerLayout;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationClient;
    private Button pickUpBtn, destinationBtn, confirmRideBtn, hourlypickUpBtn;
    private Geocoder geocoder;
    private StringBuilder stringBuilder;
    private boolean isGPS = false;
    private boolean isContinue = false;
    private boolean dark, driverFound = false, ratingGiven = false;
    private AutocompleteSupportFragment autocompleteFragment;
    private AutocompleteSupportFragment autocompleteDestination;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private MarkerOptions pickUpMarker;
    private LinearLayout destinationLayout, timeDateLayout, chooseRideType, hourlyLayout, hourlyrideTypes, hourtimeDateLayout, timeselectLayout;
    private RelativeLayout PickUpLayout;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private ImageView circularImageView;
    private TextView UserName, userPhone, wallet_fromNavigation;
    private int kmdistance, travelduration;
    private String bookingId, driverId, hourlyTripId, paymentType;
    private RelativeLayout bottomsheet, searchLayout;
    private TextView sedanprice, premiereprice, micro7price, micro11price, sedanType, premiereType, hiaceType,
            microbusType, sedanbusinessprice, selectedPrice, rideDate, hourlysedanPrice, hourlysedanPremeirePrice,
            hourlysedanBusinessPrice, hourlyMicroPrice, hourlyMicro11Price;
    private CardView sedan, sedanpremiere, sedanbusiness, hiace, micro, hourlyMicro, hourly11Micro, hourlySedan, hourlySedanPremiere, hourlySedanBusiness;
    private TextView dateTv, timeTv, rideTypeTv, hourrideTime, hourrideDate, hourlyrideTypeTv;
    private String kilometer, km, min, minfare, duration, premierekm, premieremin, premiereminfare, businesskm,
            businessmin, businessminfare, pickUpCity, destinationCity;
    private Button rideHourly, rideLater, hourlyconfirmRideBtn, wantnowBtn, wantLaterBtn;
    private SharedPreferences sharedPreferences;
    private LottieAnimationView searchlottie;
    private Boolean notify = false, firstTime = true;
    private List<String> driverList;
    private int bottomSheetCount = 0, driverRatingCount = 0;
    private float driverRating = 0;
    private String price, type;
    private int estprice;
    private Locale locale;
    private String rideStatus;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    private String translatedPickCity, translatedDestCity;
    private int hourBottom = 0;
    private ApiInterface apiInterface;
    private Dialog dialog;
    private float rating1;
    private float totalRating;
    private int totalCount;
    private APIService apiService;
    private double radius = 2;
    private String tripId, picklat, pickLon, deslat, deslon, carType;
    private long doublePressToExit;
    private Toast backToasty;
    boolean singleBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        checkVersion();

        sharedPreferences = getSharedPreferences("MyRef", Context.MODE_PRIVATE);
        dark = sharedPreferences.getBoolean("dark", false);

        navHeaderData();
        hideKeyBoard(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                updateToken(instanceIdResult.getToken());
            }
        });
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
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            if (firstTime) {
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wayLatitude, wayLongitude), 19));
                                getLastLocation();
                                firstTime = false;
                            }
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                            Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                        }
                       /* if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }*/
                    }
                }
            }
        };
        if (!isGPS) {
            Toast.makeText(this, "Please turn on your GPS!", Toast.LENGTH_SHORT).show();
        }

        nav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyBoard(getApplicationContext());
            }
        });

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserProfile.class).putExtra("id", userId));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        getPickUpPlace();

        getDestinationPlace();

        pickUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                    if (addresses.size() > 0) {
                        Address location = addresses.get(0);
                        pickUpPlace = location.getAddressLine(0);
                        pickUpCity = addresses.get(0).getAddressLine(1);
                        map.addMarker(pickUpMarker.title("Drag for suitable position")).showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickUpLat, pickUpLon), 19));

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

                                Log.d("pickUp", String.valueOf(pickUpLat));
                                Log.d("pickUplon", String.valueOf(pickUpLon));

                                // Geocoder geocoder = new Geocoder(MainActivity.this, locale);
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                    Address location = addresses.get(0);
                                    pickUpPlace = location.getAddressLine(0);
                                    pickUpCity = location.getLocality();
                                    autocompleteFragment.setText(pickUpPlace);
                                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                                    marker.setIcon(markerIcon);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toasty.error(MainActivity.this, "Place Not Found!", Toasty.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                pickUpBtn.setVisibility(View.GONE);
                btnanim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein);
                destinationBtn.setVisibility(View.VISIBLE);
                destinationBtn.setAnimation(btnanim);
                destinationLayout.setVisibility(View.VISIBLE);
                Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
            }
        });

        destinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDestinationPlace();

                if (destinationLat == 0.0 && destinationLon == 0.0 || destinationLat == pickUpLat && destinationLon == pickUpLon) {
                    Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
                } else {
                    destinationBtn.setVisibility(View.GONE);
                    showDirections();
                }
            }
        });

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(dateTv);
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(timeTv);
            }
        });

        confirmRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify = true;

                if (dateTv.getText().equals("Select Ride Date")) {
                    Toast.makeText(MainActivity.this, "Please Enter Ride Date!", Toast.LENGTH_SHORT).show();
                } else if (timeTv.getText().equals("Select Ride Time")) {
                    Toast.makeText(MainActivity.this, "Please Select Ride Time!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.payment_type_select);
                    Button doneBtn = dialog.findViewById(R.id.doneBtn);
                    RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
                    RadioButton cash = dialog.findViewById(R.id.cash);
                    RadioButton wallet = dialog.findViewById(R.id.wallet);


                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (i) {
                                case R.id.cash:
                                    paymentType = "cash";
                                    break;
                                case R.id.wallet:
                                    paymentType = "wallet";
                                    break;
                            }
                        }
                    });
                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (paymentType != null) {
                                rideCheck();
                            } else {
                                Toast.makeText(MainActivity.this, "Select Payment Type!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.setCancelable(false);

                    dialog.show();


                    map.clear();

                    timeDateLayout.setVisibility(View.GONE);
                    destinationBtn.setVisibility(View.GONE);
                    pickUpBtn.setVisibility(View.GONE);
                    PickUpLayout.setVisibility(View.GONE);
                    chooseRideType.setVisibility(View.VISIBLE);

                }

            }
        });

        sedan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                /*chooseRideType.setVisibility(View.VISIBLE);*/
                timeDateLayout.setVisibility(View.VISIBLE);
                confirmRideBtn.setVisibility(View.VISIBLE);
                bottomSheetCount = 1;
                rideTypeTv.setText("Sedan");
            }
        });

        sedanpremiere.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                /*chooseRideType.setVisibility(View.VISIBLE);*/
                timeDateLayout.setVisibility(View.VISIBLE);
                confirmRideBtn.setVisibility(View.VISIBLE);
                bottomSheetCount = 2;
                rideTypeTv.setText("Sedan Premiere");
            }
        });

        sedanbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*chooseRideType.setVisibility(View.VISIBLE);*/
                timeDateLayout.setVisibility(View.VISIBLE);
                confirmRideBtn.setVisibility(View.VISIBLE);
                bottomSheetCount = 3;
                rideTypeTv.setText("Sedan Business");
            }
        });

        hiace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*chooseRideType.setVisibility(View.VISIBLE);*/
                timeDateLayout.setVisibility(View.VISIBLE);
                confirmRideBtn.setVisibility(View.VISIBLE);
                bottomSheetCount = 4;
                rideTypeTv.setText("Micro 7 Seat");
            }
        });

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*chooseRideType.setVisibility(View.VISIBLE);*/
                timeDateLayout.setVisibility(View.VISIBLE);
                confirmRideBtn.setVisibility(View.VISIBLE);
                bottomSheetCount = 5;
                rideTypeTv.setText("Micro 11 Seat");
            }
        });

        hourlySedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hourBottom == 0) {
                    timeselectLayout.setVisibility(View.VISIBLE);
                    hourBottom = 1;
                    type = "Sedan";
                    hourlyrideTypeTv.setText("Sedan");
                } else {
                    hourBottom = 1;
                    type = "Sedan";
                    hourlyrideTypeTv.setText("Sedan");
                    hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        hourlySedanPremiere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourBottom == 0) {
                    timeselectLayout.setVisibility(View.VISIBLE);
                    hourBottom = 2;
                    type = "SedanPremiere";
                    hourlyrideTypeTv.setText("Sedan Premiere");
                } else {
                    hourBottom = 2;
                    type = "SedanPremiere";
                    hourlyrideTypeTv.setText("Sedan Premiere");
                    hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        hourlySedanBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourBottom == 0) {
                    timeselectLayout.setVisibility(View.VISIBLE);
                    hourBottom = 3;
                    type = "SedanBusiness";
                    hourlyrideTypeTv.setText("Sedan Business");
                } else {
                    hourBottom = 3;
                    type = "SedanBusiness";
                    hourlyrideTypeTv.setText("Sedan Business");
                    hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        hourlyMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourBottom == 0) {
                    timeselectLayout.setVisibility(View.VISIBLE);
                    hourBottom = 4;
                    type = "Micro7";
                    hourlyrideTypeTv.setText("Micro");
                } else {
                    hourBottom = 4;
                    type = "Micro7";
                    hourlyrideTypeTv.setText("Micro");
                    hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        hourly11Micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourBottom == 0) {
                    timeselectLayout.setVisibility(View.VISIBLE);
                    hourBottom = 5;
                    type = "Micro11";
                    hourlyrideTypeTv.setText("Micro");
                } else {
                    hourBottom = 5;
                    type = "Micro11";
                    hourlyrideTypeTv.setText("Micro");
                    hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        wantLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeselectLayout.setVisibility(View.GONE);
                hourtimeDateLayout.setVisibility(View.VISIBLE);
                hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
            }
        });

        wantnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Alert!");
                dialog.setIcon(R.drawable.logo_circle);
                dialog.setMessage("Instant ride is not available right now!!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                androidx.appcompat.app.AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        hourrideDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate(hourrideDate);
            }
        });

        hourrideTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(hourrideTime);
            }
        });

        rideHourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Alert!!");
                dialog.setIcon(R.drawable.logo_circle);
                dialog.setMessage("Hourly service is available within Dhaka city only. Minimum booking required for 2 hours.");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        rideHourly.setVisibility(View.GONE);
                        rideLater.setVisibility(View.GONE);
                        hourlypickUpBtn.setVisibility(View.VISIBLE);
                    }
                });
                dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        hourlypickUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hourlyLayout.setVisibility(View.VISIBLE);
                hourlysedanPriceShow();
                hourlysedanPremierePriceShow();
                hourlysedanBusinessPriceShow();
                hourlyMicroPriceShow();
                hourlyMicro11PriceShow();


                BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                    if (addresses.size() > 0) {
                        Address location = addresses.get(0);
                        pickUpPlace = location.getAddressLine(0);
                        pickUpCity = addresses.get(0).getAddressLine(1);
                        map.addMarker(pickUpMarker.title("Drag for suitable position")).showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickUpLat, pickUpLon), 19));

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

                                Log.d("pickUp", String.valueOf(pickUpLat));
                                Log.d("pickUplon", String.valueOf(pickUpLon));

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                    Address location = addresses.get(0);
                                    pickUpPlace = location.getAddressLine(0);
                                    pickUpCity = location.getLocality();
                                    autocompleteFragment.setText(pickUpPlace);
                                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                                    marker.setIcon(markerIcon);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toasty.error(MainActivity.this, "Place Not Found!");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        rideLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickUpLayout.setVisibility(View.VISIBLE);
                pickUpBtn.setVisibility(View.VISIBLE);
                chooseRideType.setVisibility(View.GONE);
               /* timeDateLayout.setVisibility(View.VISIBLE);
                chooseRideType.setVisibility(View.GONE);
                confirmRideBtn.setVisibility(View.VISIBLE);*/
            }
        });

        hourlyconfirmRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hourrideDate.getText().equals("Select Ride Date")) {
                    Toast.makeText(MainActivity.this, "Please Enter Ride Date!", Toast.LENGTH_SHORT).show();
                } else if (hourrideTime.getText().equals("Select Ride Time")) {
                    Toast.makeText(MainActivity.this, "Please Selecet Ride Time!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.payment_type_select);
                    Button doneBtn = dialog.findViewById(R.id.doneBtn);
                    RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (i) {
                                case R.id.cash:
                                    paymentType = "cash";
                                    break;
                                case R.id.wallet:
                                    paymentType = "wallet";
                                    break;
                            }
                        }
                    });
                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (paymentType != null) {
                                hourlyRideCheck();
                            } else {
                                Toast.makeText(MainActivity.this, "Select Payment Type!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.setCancelable(false);

                    if (!isFinishing()) {
                        dialog.show();
                    }


                }
            }
        });

        checkRunningRides();

        checkRatingCall();

        checkHourlyRatingCall();
    }

    private void checkVersion() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pinfo.versionName;
        DatabaseReference versionRef = FirebaseDatabase.getInstance().getReference("Version").child("CustomerApp");
        versionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String getVersionName = snapshot.child("versionName").getValue().toString();
                    if (!versionName.equals(getVersionName)) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("New Version!");
                        dialog.setIcon(R.drawable.logo_circle);
                        dialog.setMessage("New Version is available. Please update for latest features!");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.hydertechno.swishcustomer")));
                                dialogInterface.dismiss();
                                System.exit(0);
                            }
                        });
                        dialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0);
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    protected void checkHourlyRatingCall() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides").child(userId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String rideStatus = data.child("rideStatus").getValue().toString();
                        String ratingStatus = data.child("ratingStatus").getValue().toString();
                        String cashReceived = data.child("cashReceived").getValue().toString();
                        if (rideStatus.equals("End") && ratingStatus.equals("false") && cashReceived.equals("yes")) {
                            RideModel model = data.getValue(RideModel.class);
                            String driver_id = model.getDriverId();
                            String tripId = model.getBookingId();
                            String carType = model.getCarType();
                            dialog = new Dialog(MainActivity.this);
                            dialog.setContentView(R.layout.driver_rating_popup);
                            TextView driveName = dialog.findViewById(R.id.driverNaamTv);
                            CircleImageView driverImage = dialog.findViewById(R.id.profileIV);
                            RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                            Button submitBTN = dialog.findViewById(R.id.submitBTN);

                            Call<List<DriverInfo>> call2 = apiInterface.getCarNumber(driver_id);
                            call2.enqueue(new Callback<List<DriverInfo>>() {
                                @Override
                                public void onResponse(Call<List<DriverInfo>> call, Response<List<DriverInfo>> response) {

                                    if (response.body().get(0).getSelfie() != null) {
                                        driverImage.setVisibility(View.VISIBLE);
                                        Picasso.get().load(Config.REG_LINE + response.body().get(0).getSelfie()).into(driverImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.d("kiKahini", e.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<DriverInfo>> call, Throwable t) {

                                }
                            });

                            Call<List<DriverProfile>> call = apiInterface.getDriverData(driver_id);
                            call.enqueue(new Callback<List<DriverProfile>>() {
                                @Override
                                public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                                    List<DriverProfile> list = response.body();

                                    driveName.setText(list.get(0).getFull_name());
                                    driverRating = list.get(0).getRating();
                                    driverRatingCount = list.get(0).getRatingCount();
                                }

                                @Override
                                public void onFailure(Call<List<DriverProfile>> call, Throwable t) {

                                }
                            });

                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                    rating1 = ratingBar.getRating();

                                    totalRating = rating1 + driverRating;
                                    totalCount = driverRatingCount + 1;
                                    ratingGiven = true;

                                }
                            });

                            submitBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ratingGiven == true) {
                                        Call<List<DriverProfile>> call1 = apiInterface.updateRating(driver_id, totalRating, totalCount);
                                        call1.enqueue(new Callback<List<DriverProfile>>() {
                                            @Override
                                            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {

                                            }
                                        });
                                        Call<List<HourlyRideModel>> ratingCall = apiInterface.addHourRating(tripId, rating1);
                                        ratingCall.enqueue(new Callback<List<HourlyRideModel>>() {
                                            @Override
                                            public void onResponse(Call<List<HourlyRideModel>> call, Response<List<HourlyRideModel>> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<List<HourlyRideModel>> call, Throwable t) {

                                            }
                                        });

                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("BookHourly").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                    } else {
                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("BookHourly").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.setCancelable(false);

                            if (!isFinishing()) {
                                dialog.show();
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hourlyMicroPriceShow() {
        DatabaseReference hourRef = FirebaseDatabase.getInstance().getReference("HourlyRate").child("Micro7");
        hourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String price = snapshot.getValue().toString();
                hourlyMicroPrice.setText(price + " Tk/Hr");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hourlyMicro11PriceShow() {
        DatabaseReference hourRef = FirebaseDatabase.getInstance().getReference("HourlyRate").child("Micro11");
        hourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String price = snapshot.getValue().toString();
                hourlyMicro11Price.setText(price + " Tk/Hr");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hourlysedanPriceShow() {
        DatabaseReference hourRef = FirebaseDatabase.getInstance().getReference("HourlyRate").child("Sedan");
        hourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String price = snapshot.getValue().toString();
                hourlysedanPrice.setText(price + " Tk/Hr");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hourlysedanPremierePriceShow() {
        DatabaseReference hourRef = FirebaseDatabase.getInstance().getReference("HourlyRate").child("SedanPremiere");
        hourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String price = snapshot.getValue().toString();
                hourlysedanPremeirePrice.setText(price + " Tk/Hr");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hourlysedanBusinessPriceShow() {
        DatabaseReference hourRef = FirebaseDatabase.getInstance().getReference("HourlyRate").child("SedanBusiness");
        hourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String price = snapshot.getValue().toString();
                hourlysedanBusinessPrice.setText(price + " Tk/Hr");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void checkRatingCall() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String rideStatus = data.child("rideStatus").getValue().toString();
                        String ratingStatus = data.child("ratingStatus").getValue().toString();
                        String cashReceived = data.child("cashReceived").getValue().toString();
                        if (rideStatus.equals("End") && ratingStatus.equals("false") && cashReceived.equals("yes")) {
                            RideModel model = data.getValue(RideModel.class);
                            String driver_id = model.getDriverId();
                            String tripId = model.getBookingId();
                            String carType = model.getCarType();
                            dialog = new Dialog(MainActivity.this);
                            dialog.setContentView(R.layout.driver_rating_popup);
                            TextView driveName = dialog.findViewById(R.id.driverNaamTv);
                            CircleImageView driverImage = dialog.findViewById(R.id.profileIV);
                            RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                            Button submitBTN = dialog.findViewById(R.id.submitBTN);
                            dialog.setCancelable(false);
                            if (!isFinishing()) {
                                dialog.show();
                            }

                            Call<List<DriverInfo>> call2 = apiInterface.getCarNumber(driver_id);
                            call2.enqueue(new Callback<List<DriverInfo>>() {
                                @Override
                                public void onResponse(Call<List<DriverInfo>> call, Response<List<DriverInfo>> response) {

                                    if (response.body().get(0).getSelfie() != null) {
                                        driverImage.setVisibility(View.VISIBLE);
                                        Picasso.get().load(Config.REG_LINE + response.body().get(0).getSelfie()).into(driverImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.d("kiKahini", e.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<DriverInfo>> call, Throwable t) {

                                }
                            });


                            Call<List<DriverProfile>> call = apiInterface.getDriverData(driver_id);
                            call.enqueue(new Callback<List<DriverProfile>>() {
                                @Override
                                public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                                    List<DriverProfile> list = response.body();

                                    driveName.setText(list.get(0).getFull_name());
                                    driverRating = list.get(0).getRating();
                                    driverRatingCount = list.get(0).getRatingCount();
                                }

                                @Override
                                public void onFailure(Call<List<DriverProfile>> call, Throwable t) {

                                }
                            });

                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                    rating1 = ratingBar.getRating();

                                    totalRating = rating1 + driverRating;
                                    totalCount = driverRatingCount + 1;
                                    ratingGiven = true;

                                }
                            });


                            submitBTN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ratingGiven == true) {
                                        Call<List<DriverProfile>> call1 = apiInterface.updateRating(driver_id, totalRating, totalCount);
                                        call1.enqueue(new Callback<List<DriverProfile>>() {
                                            @Override
                                            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {

                                            }
                                        });

                                        Call<List<RideModel>> ratingCall = apiInterface.addRating(tripId, rating1);
                                        ratingCall.enqueue(new Callback<List<RideModel>>() {
                                            @Override
                                            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<List<RideModel>> call, Throwable t) {

                                            }
                                        });

                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("BookForLater").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                    } else {

                                        Call<List<RideModel>> ratingCall = apiInterface.addRating(tripId, 0);
                                        ratingCall.enqueue(new Callback<List<RideModel>>() {
                                            @Override
                                            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<List<RideModel>> call, Throwable t) {

                                            }
                                        });
                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("BookForLater").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                    }
                                    dialog.dismiss();
                                }
                            });

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkRunningRides() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        rideStatus = data.child("rideStatus").getValue().toString();
                        if (rideStatus.equals("Start")) {
                            RideModel model = data.getValue(RideModel.class);
                            picklat = model.getPickUpLat();
                            pickLon = model.getPickUpLon();
                            deslat = model.getDestinationLat();
                            deslon = model.getDestinationLon();
                            tripId = model.getBookingId();
                            carType = model.getCarType();

                            Intent intent = new Intent(MainActivity.this, RunningTrip.class);
                            intent.putExtra("check", 3);
                            intent.putExtra("pLat", picklat);
                            intent.putExtra("pLon", pickLon);
                            intent.putExtra("dLat", deslat);
                            intent.putExtra("dLon", deslon);
                            intent.putExtra("tripId", tripId);
                            intent.putExtra("carType", carType);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void rideCheck() {
        if (bottomSheetCount == 1) {
            type = "Sedan";
            price = sedanprice.getText().toString();
        } else if (bottomSheetCount == 2) {
            type = "SedanPremiere";
            price = premiereprice.getText().toString();
        } else if (bottomSheetCount == 3) {
            type = "SedanBusiness";
            price = sedanbusinessprice.getText().toString();
        } else if (bottomSheetCount == 4) {
            type = "Micro7";
            price = sedanbusinessprice.getText().toString();
        } else if (bottomSheetCount == 5) {
            type = "Micro11";
            price = sedanbusinessprice.getText().toString();
        }
        upload();
    }

    private void upload() {

        confirmRideBtn.setEnabled(false);

        DatabaseReference rideLaterRef = FirebaseDatabase.getInstance().getReference()
                .child("BookForLater").child(type);
        bookingId = rideLaterRef.push().getKey();

        HashMap<String, Object> rideInfo = new HashMap<>();
        rideInfo.put("bookingId", bookingId);
        rideInfo.put("bookingStatus", "Pending");
        rideInfo.put("carType", type);
        rideInfo.put("customerId", userId);
        rideInfo.put("rideStatus", "Pending");
        rideInfo.put("driverId", "");
        rideInfo.put("destinationLat", String.valueOf(destinationLat));
        rideInfo.put("destinationLon", String.valueOf(destinationLon));
        rideInfo.put("destinationPlace", destinationPlace);
        rideInfo.put("pickUpDate", dateTv.getText().toString());
        rideInfo.put("pickUpLat", String.valueOf(pickUpLat));
        rideInfo.put("pickUpLon", String.valueOf(pickUpLon));
        rideInfo.put("pickUpPlace", pickUpPlace);
        rideInfo.put("pickUpTime", timeTv.getText().toString());
        rideInfo.put("endTime", "");
        rideInfo.put("price", price);
        rideInfo.put("ratingStatus", "false");
        rideInfo.put("payment", paymentType);
        rideInfo.put("discount", "");
        rideInfo.put("finalPrice", "");
        rideInfo.put("cashReceived", "no");
        rideInfo.put("totalDistance", "");
        rideInfo.put("totalTime", "");

        rideLaterRef.child(bookingId).setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            //  updateToken(instanceIdResult.getToken());
                        }
                    });

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.drawable.logo_circle);
                    builder.setTitle("Request Complete!");
                    builder.setMessage("Your ride request is complete! \n " +
                            "You will be notified when any driver accepts your request");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bottomsheet.setVisibility(View.GONE);
                        }
                    });

                    if(!isFinishing()){
                        builder.create().show();
                    }
                }
            }
        });

        if (notify) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(type);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        sendNotificationDriver(bookingId, postSnapshot.getKey(), type, "New Request!","Tap to see full details." , "booking_details");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            sendNotification(bookingId, userId, "Booking Create!", "Tap to see booking details.", "my_ride_details");
        }
        notify = false;
        DatabaseReference userRideRef = FirebaseDatabase.getInstance().getReference().child("CustomerRides");

        userRideRef.child(userId).child(bookingId).setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wayLatitude, wayLongitude), 19));
                }
            }
        });

        Call<List<RideModel>> call = apiInterface.saveTripRequest(bookingId, "Pending", type, userId, String.valueOf(destinationLat),
                String.valueOf(destinationLon), destinationPlace, "", "", dateTv.getText().toString(), String.valueOf(pickUpLat),
                String.valueOf(pickUpLon), pickUpPlace, timeTv.getText().toString(), price, "Pending", paymentType);
        call.enqueue(new Callback<List<RideModel>>() {
            @Override
            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {
                Toast.makeText(MainActivity.this, "Ride request complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<RideModel>> call, Throwable t) {

            }
        });

        finish();
        startActivity(getIntent());


    }

    private void hourlyRideCheck() {
        if (hourBottom == 1) {
            type = "Sedan";
            price = hourlysedanPrice.getText().toString();
        } else if (hourBottom == 2) {
            type = "SedanPremiere";
            price = hourlysedanPremeirePrice.getText().toString();
        } else if (hourBottom == 3) {
            type = "SedanBusiness";
            price = hourlysedanBusinessPrice.getText().toString();
        } else if (hourBottom == 4) {
            type = "Micro7";
            price = hourlyMicroPrice.getText().toString();
        } else if (hourBottom == 5) {
            type = "Micro11";
            price = hourlyMicro11Price.getText().toString();
        }


        uploadHourly();
    }

    private void uploadHourly() {
        notify = true;

        DatabaseReference hourlyLaterRef = FirebaseDatabase.getInstance().getReference()
                .child("BookHourly").child(type);

        hourlyTripId = hourlyLaterRef.push().getKey();

        HashMap<String, Object> rideInfo = new HashMap<>();
        rideInfo.put("bookingId", hourlyTripId);
        rideInfo.put("bookingStatus", "Pending");
        rideInfo.put("carType", type);
        rideInfo.put("customerId", userId);
        rideInfo.put("rideStatus", "Pending");
        rideInfo.put("pickUpLat", String.valueOf(pickUpLat));
        rideInfo.put("pickUpLon", String.valueOf(pickUpLon));
        rideInfo.put("pickUpPlace", pickUpPlace);
        rideInfo.put("driverId", "");
        rideInfo.put("pickUpDate", hourrideDate.getText().toString());
        rideInfo.put("pickUpTime", hourrideTime.getText().toString());
        rideInfo.put("endTime", "");
        rideInfo.put("price", "" + price);
        rideInfo.put("ratingStatus", "false");
        rideInfo.put("payment", paymentType);
        rideInfo.put("discount", "");
        rideInfo.put("finalPrice", "");
        rideInfo.put("cashReceived", "no");
        rideInfo.put("totalTime", "");

        hourlyLaterRef.child(hourlyTripId).setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            updateToken(instanceIdResult.getToken());
                        }
                    });


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.drawable.logo_circle);
                    builder.setTitle("Request Complete!");
                    builder.setMessage("Your hourly ride request is complete! \n " +
                            "You will be notified when any driver accepts your request");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            map.clear();
                            hourlyLayout.setVisibility(View.GONE);
                            chooseRideType.setVisibility(View.VISIBLE);
                        }
                    });

                    builder.create().show();
                }
            }
        });

        if (notify) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(type);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        sendNotificationDriver(bookingId, postSnapshot.getKey(), type, "New Request!","Tap to see full details." , "hourly_details");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            sendNotification(hourlyTripId, userId, "Hourly Ride", "Tap to see booking details.", "my_hourly_ride_details");
        }
        notify = false;
        DatabaseReference userRideRef = FirebaseDatabase.getInstance().getReference().child("CustomerHourRides");
        userRideRef.child(userId).child(hourlyTripId).setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wayLatitude, wayLongitude), 19));
                }
            }
        });

        Call<List<HourlyRideModel>> call = apiInterface.saveHourlyTripRequest(hourlyTripId, "Pending",
                type, userId, "", "", hourrideDate.getText().toString(), String.valueOf(pickUpLat),
                String.valueOf(pickUpLon), pickUpPlace, hourrideTime.getText().toString(), "0", "Pending", paymentType);
        call.enqueue(new Callback<List<HourlyRideModel>>() {
            @Override
            public void onResponse(Call<List<HourlyRideModel>> call, Response<List<HourlyRideModel>> response) {
                Toast.makeText(MainActivity.this, "Ride request complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<HourlyRideModel>> call, Throwable t) {

            }
        });

        finish();
        startActivity(getIntent());

    }

    private void showDirections() {
        map.clear();
        searchLayout.setVisibility(View.GONE);
        backNFL.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
        place1 = new MarkerOptions().icon(markerIcon)
                .position(new LatLng(pickUpLat, pickUpLon)).title(pickUpPlace);
        BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.ic_destination);
        place2 = new MarkerOptions().icon(markerIcon2)
                .position(new LatLng(destinationLat, destinationLon)).title(destinationPlace);

        new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(),
                "driving"), "driving");

        map.addMarker(place1);
        map.addMarker(place2);

        map.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

        calculateDirections(pickUpLat, pickUpLon, destinationLat, destinationLon);

    }

    private void calculateDirections(double pickUpLat, double pickUpLon, double destinationLat, double destinationLon) {

        String origins = pickUpLat + "," + pickUpLon;
        String destination = destinationLat + "," + destinationLon;

        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("units", "driving");
        mapQuery.put("origins", origins);
        mapQuery.put("destinations", destination);
        mapQuery.put("key", apiKey);

        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                if (response.body() != null &&
                        response.body().getRows() != null &&
                        response.body().getRows().size() > 0 &&
                        response.body().getRows().get(0) != null &&
                        response.body().getRows().get(0).getElements() != null &&
                        response.body().getRows().get(0).getElements().size() > 0 &&
                        response.body().getRows().get(0).getElements().get(0) != null &&
                        response.body().getRows().get(0).getElements().get(0).getDistance() != null &&
                        response.body().getRows().get(0).getElements().get(0).getDuration() != null) {
                    Element element = response.body().getRows().get(0).getElements().get(0);
                    int distance = element.getDistance().getValue();
                    int trduration = element.getDuration().getValue();

                    kmdistance = distance / 1000;
                    travelduration = trduration / 60;

                    Log.d("kmDist", kmdistance + "," + travelduration);
                    Log.d("trduration", String.valueOf(trduration));

                    bottomsheet.setVisibility(View.VISIBLE);

                    sedanPrice(kmdistance, travelduration);
                    sedanPremierePrice(kmdistance, travelduration);
                    sedanBusinessPrice(kmdistance, travelduration);
                    hiace7Price(kmdistance, travelduration);
                    hiace11Price(kmdistance, travelduration);

                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

            }
        });

    }

    private void hiace7Price(int kmdistance, int travelduration) {
        Call<List<RidingRate>> call = apiInterface.getPrice("Micro7");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();

                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRate = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    estprice = kmPrice + minPrice + minimumRate;
                    micro7price.setText("" + estprice);
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    private void hiace11Price(int kmdistance, int travelduration) {
        Call<List<RidingRate>> call = apiInterface.getPrice("Micro11");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();

                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRate = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    estprice = kmPrice + minPrice + minimumRate;
                    micro11price.setText("" + estprice);
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    private void sedanBusinessPrice(int kmdistance, int travelduration) {
        Call<List<RidingRate>> call = apiInterface.getPrice("SedanBusiness");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();

                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRate = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    estprice = kmPrice + minPrice + minimumRate;
                    sedanbusinessprice.setText("" + estprice);
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    public void sedanPremierePrice(int kmdistance, int travelduration) {
        Call<List<RidingRate>> call = apiInterface.getPrice("SedanPremiere");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();

                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRate = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    estprice = kmPrice + minPrice + minimumRate;
                    premiereprice.setText("" + estprice);
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    private void sedanPrice(int kmdistance, int travelduration) {

        Call<List<RidingRate>> call = apiInterface.getPrice("Sedan");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRate = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    estprice = kmPrice + minPrice + minimumRate;
                    sedanprice.setText("" + estprice);
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {
                Log.d("checkError", t.getMessage());
            }
        });

    }

    private void getTime(TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.timepicker, null);
        TimePicker timePicker = view.findViewById(R.id.timepicker);
        Button timeBtn = view.findViewById(R.id.timeBtn);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();

                Time time = new Time(hour, min, 0);
                textView.setText(timeFormat.format(time));
                dialog.dismiss();

            }
        });
    }

    private void getDate(TextView t) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String currentDate = day + "-" + month + "-" + year;
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;

                try {
                    date = dateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                t.setText(dateFormat.format(date));

            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    private void getDestinationPlace() {
        autocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (autocompleteDestination.equals("")) {
                    Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
                }
                autocompleteDestination.setText(place.getName());
                destinationPlace = place.getName();

                if (destinationPlace.equals(pickUpPlace)) {
                    Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
                } else {
                    List<Address> latlonaddress = null;
                    try {
                        latlonaddress = geocoder.getFromLocationName(place.getName(), 1);
                        if (latlonaddress.size() > 0) {
                            Address location = latlonaddress.get(0);
                            destinationLat = location.getLatitude();
                            destinationLon = location.getLongitude();
                            destinationCity = latlonaddress.get(0).getLocality();

                            map.clear();
                            BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                            pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon))
                                    .icon(markerIcon);

                            BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.ic_destination);
                            map.addMarker(new MarkerOptions().position(new LatLng(destinationLat, destinationLon)).icon(markerIcon2).draggable(true).title("Drag for suitable position")).showInfoWindow();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destinationLat, destinationLon), 16));

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

                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(destinationLat, destinationLon, 1);
                                        Address location = addresses.get(0);
                                        destinationPlace = location.getAddressLine(0);
                                        destinationCity = location.getLocality();

                                        autocompleteDestination.setText(destinationPlace);
                                        BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.ic_destination);
                                        marker.setIcon(markerIcon2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Toasty.error(MainActivity.this, "Place not found!", Toasty.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void getPickUpPlace() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //location_searchET.setText(place.getName());
                pickUpPlace = place.getName();
                autocompleteFragment.setText(pickUpPlace);
                List<Address> latlonaddress = null;
                try {
                    latlonaddress = geocoder.getFromLocationName(place.getName(), 1);

                    if (latlonaddress.size() > 0) {
                        Address location = latlonaddress.get(0);

                        pickUpLat = location.getLatitude();
                        pickUpLon = location.getLongitude();
                        pickUpCity = location.getLocality();

                        pickUpMarker = new MarkerOptions();

                        map.clear();
                        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                        pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon))
                                .icon(markerIcon).draggable(true);
                        map.addMarker(pickUpMarker.title("Drag for suitable position")).showInfoWindow();
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

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                    pickUpPlace = addresses.get(0).getAddressLine(0);
                                    pickUpCity = addresses.get(0).getLocality();
                                    autocompleteFragment.setText(pickUpPlace);
                                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                                    marker.setIcon(markerIcon);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toasty.error(MainActivity.this, "Place Not Found!", Toasty.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location1) {
                if (location1 != null) {
                    wayLatitude = location1.getLatitude();
                    wayLongitude = location1.getLongitude();
                    try {
                        List<Address> address = geocoder.getFromLocation(wayLatitude, wayLongitude, 1);
                        if (address.size() > 0) {
                            autocompleteFragment.setText(address.get(0).getFeatureName() + " " +
                                    address.get(0).getThoroughfare() + " " + address.get(0).getLocality() + " " +
                                    address.get(0).getSubLocality());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pickUpLat = wayLatitude;
                    pickUpLon = wayLongitude;

                } else {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            }
        });
    }

    private void init() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        auth = FirebaseAuth.getInstance();
        apiInterface = ApiUtils.getUserService();
        reference = FirebaseDatabase.getInstance().getReference("profile");
        nav_icon = findViewById(R.id.nav_icon);
        backNFL = findViewById(R.id.backNFL);
        drawerLayout = findViewById(R.id.drawer_layout);
        searchLayout = findViewById(R.id.searchLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        nav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                hideKeyBoard(getApplicationContext());
            }
        });
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", "");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locale = new Locale("en");

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey, locale);
        }
        autocompleteFragment = (AutocompleteSupportFragment) this.getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("BD");

        autocompleteDestination = (AutocompleteSupportFragment) this.getSupportFragmentManager().findFragmentById(R.id.autocomplete_destination);
        autocompleteDestination.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        autocompleteDestination.setHint("Search destination");
        autocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteDestination.setCountry("BD");

        geocoder = new Geocoder(this, locale);

        PickUpLayout = findViewById(R.id.PickUpLayout);
        pickUpBtn = findViewById(R.id.button_confirm_pickUp);
        destinationBtn = findViewById(R.id.button_destination);
        destinationLayout = findViewById(R.id.destinationLayout);
        circularImageView = navigationView.getHeaderView(0).findViewById(R.id.navImageView);
        UserName = navigationView.getHeaderView(0).findViewById(R.id.namefromNavigation);
        userPhone = navigationView.getHeaderView(0).findViewById(R.id.phone_fromNavigation);
        wallet_fromNavigation = navigationView.getHeaderView(0).findViewById(R.id.wallet_fromNavigation);
        hourlypickUpBtn = findViewById(R.id.hourlypickUpBtn);
        bottomsheet = findViewById(R.id.bottomsheet);
        sedan = findViewById(R.id.sedan);
        sedanpremiere = findViewById(R.id.sedanpremiere);
        hiace = findViewById(R.id.hiace);
        micro = findViewById(R.id.micro);
        sedanprice = findViewById(R.id.sedanprice);
        premiereprice = findViewById(R.id.sedanpremierePrice);
        micro7price = findViewById(R.id.hiaceprice);
        micro11price = findViewById(R.id.microprice);
        sedanType = findViewById(R.id.sedanType);
        premiereType = findViewById(R.id.premiereType);
        hiaceType = findViewById(R.id.hiaceType);
        microbusType = findViewById(R.id.microbusType);
        rideHourly = findViewById(R.id.bookNowBtn);
        rideLater = findViewById(R.id.bookLaterBtn);
        rideDate = findViewById(R.id.rideDate);
        bottomsheet = findViewById(R.id.bottomsheet);
        chooseRideType = findViewById(R.id.chooseRideType);
        timeDateLayout = findViewById(R.id.timeDateLayout);
        dateTv = findViewById(R.id.rideDate);
        timeTv = findViewById(R.id.rideTime);
        confirmRideBtn = findViewById(R.id.confirmRideBtn);
        sedanbusiness = findViewById(R.id.sedanbusiness);
        sedanbusinessprice = findViewById(R.id.sedanbusinessPrice);
        searchlottie = findViewById(R.id.searchlottie);
        rideTypeTv = findViewById(R.id.rideTypeTv);
        hourlyconfirmRideBtn = findViewById(R.id.hourlyconfirmRideBtn);
        hourlyLayout = findViewById(R.id.hourlyLayout);
        hourrideTime = findViewById(R.id.hourrideTime);
        hourrideDate = findViewById(R.id.hourrideDate);
        hourlyMicro = findViewById(R.id.hourlyMicro);
        hourlySedan = findViewById(R.id.hourlySedan);
        hourlySedanPremiere = findViewById(R.id.hourlySedanpremiere);
        hourlySedanBusiness = findViewById(R.id.hourlySedanbusiness);
        hourlySedan = findViewById(R.id.hourlySedan);
        hourlyMicroPrice = findViewById(R.id.hourlyMicroPrice);
        hourlyMicro11Price = findViewById(R.id.hourlyMicro11Price);
        hourly11Micro = findViewById(R.id.hourlyMicro11);
        hourlysedanPremeirePrice = findViewById(R.id.hourlysedanpremierePrice);
        hourlysedanBusinessPrice = findViewById(R.id.hourlysedanbusinessPrice);
        hourlyMicroPrice = findViewById(R.id.hourlyMicroPrice);
        hourlysedanPrice = findViewById(R.id.hourlysedanPrice);
        hourlyrideTypes = findViewById(R.id.hourlyrideTypes);
        hourtimeDateLayout = findViewById(R.id.hourtimeDateLayout);
        hourlyrideTypeTv = findViewById(R.id.hourlyrideTypeTv);
        timeselectLayout = findViewById(R.id.timeselectLayout);
        wantnowBtn = findViewById(R.id.wantnowBtn);
        wantLaterBtn = findViewById(R.id.wantLaterBtn);
    }

    private void navHeaderData() {

        Call<List<Profile>> call = apiInterface.getData(userId);
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.isSuccessful()) {
                    List<Profile> list = new ArrayList<>();
                    list = response.body();
                    if (Config.IMAGE_LINE + list.get(0).getImage() != null) {
                        Picasso.get().load(Config.IMAGE_LINE + list.get(0).getImage())
                                .into(circularImageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("kiKahini", e.getMessage());
                                    }
                                });
                    }
                    UserName.setText(list.get(0).getName());
                    userPhone.setText("+88" + list.get(0).getPhone());
                    wallet_fromNavigation.setText("৳ " + String.valueOf(list.get(0).getWallet()));
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void locationButton(View view) {
        getLastLocation();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wayLatitude, wayLongitude), 19));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.logout:

                DatabaseReference deleteTokenRef = FirebaseDatabase.getInstance().getReference("CustomersToken").child(userId);
                deleteTokenRef.removeValue();

                Intent intent = new Intent(MainActivity.this, SignIn.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn", false);
                editor.commit();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, UserProfile.class).putExtra("id", userId));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.rides:
                startActivity(new Intent(MainActivity.this, MyRides.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, settingsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.emergency:
                startActivity(new Intent(MainActivity.this, Emergency.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.referral:
                startActivity(new Intent(MainActivity.this, Referral.class).putExtra("id", userId));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.coupon:
                startActivity(new Intent(MainActivity.this, CouponActivity.class).putExtra("id", userId));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.history:
                startActivity(new Intent(MainActivity.this, History.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
        }
        return false;
    }

    private void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(new LatLng(23.9978113, 90.4651143),7);
        map.moveCamera(point);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (dark == true) {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_aubergine));
        } else {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.light));
        }

        //update in 5 seconds
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000); // 10 seconds
        locationRequest.setFastestInterval(1000); // 5 seconds

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        getLastLocation();
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
        if (dark == false) {
            currentPolyline.setColor(R.color.colorPrimary);
            currentPolyline.setWidth(11);
        } else {
            currentPolyline.setColor(R.color.red);
            currentPolyline.setWidth(11);
        }
    }


    private void updateToken(String token) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("CustomersToken").child(userId);
        userRef.child("token").setValue(token);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    map.setMyLocationEnabled(true);
                }
            }
        }
    }

    private void sendNotification(final String id, final String receiverId, final String title, final String message, final String toActivity) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("CustomersToken");
        Query query = tokens.orderByKey().equalTo(receiverId);
        String receiverId1 = receiverId;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(id, R.drawable.ic_car, message, title, receiverId1, toActivity);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> my_response) {
                                    if (my_response.code() == 200) {
                                        if (my_response.body().success != 1) {
                                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotificationDriver(final String bookingId, final String receiverId, final String car, final String title, final String message, final String toActivity) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(car);
        Query query = tokens.orderByKey().equalTo(receiverId);
        final String receiverId1 = receiverId;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(bookingId, R.mipmap.ic_noti_foreground, message, title, receiverId1, toActivity);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> my_response) {
                                    if (my_response.code() == 200) {
                                        if (my_response.body().success != 1) {
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendNotificationDriverw(final String id, final String type, final String title, final String message, final String toActivity) {
        DatabaseReference tokens1 = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(type);
        tokens1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> driverNotificationList = new ArrayList<>();
                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {

                        driverNotificationList.add(childSnapShot.child("id").getValue().toString());

                    }
                    if (driverNotificationList != null) {
                        for (int i = 0; i < driverNotificationList.size(); i++) {
                            DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(type);
                            Query query = tokens.orderByKey().equalTo(driverNotificationList.get(i));
                            int finalI = i;
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Token token = dataSnapshot.getValue(Token.class);
                                        Data data = new Data(id, R.drawable.ic_car, message, title, (String) driverNotificationList.get(finalI), toActivity);
                                        Sender sender = new Sender(data, token.getToken());

                                        apiService.sendNotification(sender)
                                                .enqueue(new Callback<MyResponse>() {
                                                    @Override
                                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> my_response) {
                                                        if (my_response.code() == 200) {
                                                            if (my_response.body().success != 1) {
                                                                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void backPressUp(View view) {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        if (singleBack) {
            super.onBackPressed();
            return;
        }

        this.singleBack = true;
        Toast.makeText(this, "Double Back to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                singleBack = false;
            }
        }, 2000);
    }
}
package com.hydertechno.swishcustomer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.widget.ProgressBar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import com.hydertechno.swishcustomer.Internet.Connection;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.ApiDeviceToken;
import com.hydertechno.swishcustomer.Model.CouponShow;
import com.hydertechno.swishcustomer.Model.DriverInfo;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.Model.RidingRate;
import com.hydertechno.swishcustomer.Model.UniqueId;
import com.hydertechno.swishcustomer.Notification.APIService;
import com.hydertechno.swishcustomer.Notification.Client;
import com.hydertechno.swishcustomer.Notification.Data;
import com.hydertechno.swishcustomer.Notification.MyResponse;
import com.hydertechno.swishcustomer.Notification.Sender;
import com.hydertechno.swishcustomer.Notification.Token;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.Remote.LatLngInterpolator;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.hydertechno.swishcustomer.Utils.AppConstants;
import com.hydertechno.swishcustomer.Utils.Config;
import com.hydertechno.swishcustomer.Utils.GpsUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
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

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, TaskLoadedCallback, ConnectivityReceiver.ConnectivityReceiverListener {

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String userId,rideType;
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
    private Button pickUpBtn, destinationBtn, confirmRideBtn, hourlypickUpBtn,instantRideBtn;
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
    private LinearLayout destinationLayout, timeDateLayout, chooseRideType, hourlyLayout, hourlyrideTypes,
            hourtimeDateLayout, timeselectLayout;
    private RelativeLayout PickUpLayout;
    private List<CouponShow> showList;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private ImageView circularImageView,profileIV,profileIV2,sizeIv;
    private TextView UserName, userPhone, wallet_fromNavigation,driverRatingBar,driverRatingBar2,rideCountTxt,rideCountTxt2,rideTxt2
            ,driverNameTV,driverNameTV2,driver_PhoneTV,carTv,carTv2,rideTxt,onGoingPickUp,onGoingDestination,onGoingPrice;
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
    private Button rideHourly, rideInterCity, hourlyconfirmRideBtn, wantnowBtn, wantLaterBtn;
    private SharedPreferences sharedPreferences;
    private Boolean notify = false, firstTime = true,isConnected;
    private List<String> driverList;
    private int bottomSheetCount = 0, driverRatingCount = 0,height;
    private float driverRating = 0;
    private String price, type;
    private int estprice;
    private Locale locale;
    private String rideStatus;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    private int hourBottom = 0;
    private ApiInterface apiInterface;
    private Dialog dialog;
    private float rating1;
    private float totalRating;
    private int totalCount,set_coupons,couponAmount,e_wallet;;
    private APIService apiService;
    private double radius = 1;
    private String tripId, picklat, pickLon, deslat, deslon, carType,destinationDivision;
    boolean singleBack = false;
    private ProgressBar progressBar;
    private FloatingActionButton homeBtn,workBtn;
    private String language = "en";
    private Date d1,d2;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private RelativeLayout rl1;
    private SlidingUpPanelLayout onGoingTripLayout,driverDetailsLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //change language
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        checkVersion();
        checkConnection();
        checkPermission();

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
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, UserProfile.class).putExtra("id", userId));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        getPickUpPlace();

        getDestinationPlace();

        pickUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                    pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                    List<Address> addresses = null;

                    try {
                        addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                        if (addresses.size() > 0) {
                            Address location = addresses.get(0);
                            pickUpPlace = location.getAddressLine(0);
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

                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                        Address location = addresses.get(0);
                                        pickUpPlace = location.getAddressLine(0);

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
            }
        });

        destinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    getDestinationPlace();
                    progressBar.setVisibility(View.VISIBLE);
                    if (destinationLat == 0.0 && destinationLon == 0.0 || destinationLat == pickUpLat && destinationLon == pickUpLon) {
                        Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
                    } else {
                        destinationBtn.setVisibility(View.GONE);
                        showDirections();
                    }
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
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {

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
                                    if (paymentType.equals("cash")) {
                                        Call<List<CouponShow>> couponCall = ApiUtils.getUserService().getValidCoupon(userId);
                                        couponCall.enqueue(new Callback<List<CouponShow>>() {
                                            @Override
                                            public void onResponse(Call<List<CouponShow>> call, Response<List<CouponShow>> response) {
                                                if (response.isSuccessful()) {
                                                    showList = response.body();
                                                    set_coupons = showList.get(0).getSetCoupons();
                                                    if (set_coupons == 1) {
                                                        String pickUpDate = rideDate.getText().toString();
                                                        String validityDate = showList.get(0).getEndDate();
                                                        Log.d("kahiniKi", pickUpDate);
                                                        Log.d("kahiniKi", validityDate);

                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                                        try {
                                                            d1 = dateFormat.parse(pickUpDate);
                                                            d2 = dateFormat.parse(validityDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (d2.compareTo(d1) > 0) {
                                                            couponAmount = showList.get(0).getAmount();

                                                            Log.d("kahiniKi", String.valueOf(couponAmount));

                                                        } else {
                                                            couponAmount = 0;
                                                        }

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<CouponShow>> call, Throwable t) {
                                            }
                                        });
                                    } else if (paymentType.equals("wallet")) {
                                        Call<List<Profile>> call = apiInterface.getData(userId);
                                        call.enqueue(new Callback<List<Profile>>() {
                                            @Override
                                            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                                                e_wallet = response.body().get(0).getE_wallet();
                                                Log.d("e-wallet", String.valueOf(e_wallet));

                                            }

                                            @Override
                                            public void onFailure(Call<List<Profile>> call, Throwable t) {

                                            }
                                        });
                                    }
                                    rideCheck();
                                    dialog.dismiss();
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
            }
        });

        instantRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    rideType = "instant";
                    PickUpLayout.setVisibility(View.VISIBLE);
                    pickUpBtn.setVisibility(View.VISIBLE);
                    chooseRideType.setVisibility(View.GONE);
                    homeBtn.show();
                    workBtn.show();
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
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    timeselectLayout.setVisibility(View.GONE);
                    hourtimeDateLayout.setVisibility(View.VISIBLE);
                    hourlyconfirmRideBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        wantnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Searching",
                            "Searching Nearby Drivers....", true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("Sorry!");
                            dialog.setIcon(R.drawable.logo_circle);
                            dialog.setMessage("No driver available! \nNo car found for your trip");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                            androidx.appcompat.app.AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        }
                    }, 3000);
                }
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
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    rideType = "hourly";
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Alert!!");
                    dialog.setIcon(R.drawable.logo_circle);
                    dialog.setMessage("Hourly service is available within Dhaka city only. Minimum booking required for 2 hours.");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            rideHourly.setVisibility(View.GONE);
                            rideInterCity.setVisibility(View.GONE);
                            hourlypickUpBtn.setVisibility(View.VISIBLE);
                            chooseRideType.setVisibility(View.GONE);
                            homeBtn.show();
                            workBtn.show();

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
            }
        });

        hourlypickUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    map.clear();
                    searchLayout.setVisibility(View.GONE);
                    backNFL.setVisibility(View.VISIBLE);

                    BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                    pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                    List<Address> addresses = null;

                    try {
                        addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                        if (addresses.size() > 0) {
                            Address location = addresses.get(0);
                            pickUpPlace = location.getAddressLine(0);
                            pickUpCity = location.getLocality();
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
                            Toasty.error(MainActivity.this, "Place Not Found!").show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("hourlyCity", pickUpCity);

                    if (pickUpCity.equals("????????????") || pickUpCity.equals("Dhaka")) {
                        hourlyLayout.setVisibility(View.VISIBLE);
                        hourlysedanPriceShow();
                        hourlysedanPriceShow();
                        hourlysedanPremierePriceShow();
                        hourlysedanBusinessPriceShow();
                        hourlyMicroPriceShow();
                        hourlyMicro11PriceShow();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Alert!!");
                        dialog.setIcon(R.drawable.logo_circle);
                        dialog.setMessage("Hourly service is available within Dhaka city only.");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        });

                        AlertDialog alertDialog = dialog.create();

                        if (!isFinishing()) {
                            alertDialog.show();
                        }
                    }
                }
            }
        });

        rideInterCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    rideType = "interCity";
                    PickUpLayout.setVisibility(View.VISIBLE);
                    pickUpBtn.setVisibility(View.VISIBLE);
                    chooseRideType.setVisibility(View.GONE);
                    homeBtn.show();
                    workBtn.show();
                }
            }
        });

        hourlyconfirmRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                } else {
                    if (hourrideDate.getText().equals("Select Ride Date")) {
                        Toast.makeText(MainActivity.this, "Please Enter Ride Date!", Toast.LENGTH_SHORT).show();
                    } else if (hourrideTime.getText().equals("Select Ride Time")) {
                        Toast.makeText(MainActivity.this, "Please Enter Ride Time!", Toast.LENGTH_SHORT).show();
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
                                    if (paymentType.equals("cash")) {
                                        Call<List<CouponShow>> couponCall = ApiUtils.getUserService().getValidCoupon(userId);
                                        couponCall.enqueue(new Callback<List<CouponShow>>() {
                                            @Override
                                            public void onResponse(Call<List<CouponShow>> call, Response<List<CouponShow>> response) {
                                                if (response.isSuccessful()) {
                                                    showList = response.body();
                                                    set_coupons = showList.get(0).getSetCoupons();
                                                    if (set_coupons == 1) {
                                                        String pickUpDate = hourrideDate.getText().toString();
                                                        String validityDate = showList.get(0).getEndDate();
                                                        Log.d("kahiniKi", pickUpDate);
                                                        Log.d("kahiniKi", validityDate);

                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                                        try {
                                                            d1 = dateFormat.parse(pickUpDate);
                                                            d2 = dateFormat.parse(validityDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (d2.compareTo(d1) > 0) {
                                                            couponAmount = showList.get(0).getAmount();

                                                            Log.d("kahiniKi", String.valueOf(couponAmount));

                                                        } else {
                                                            couponAmount = 0;
                                                        }

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<CouponShow>> call, Throwable t) {
                                            }
                                        });
                                    } else if (paymentType.equals("wallet")) {
                                        Call<List<Profile>> call = apiInterface.getData(userId);
                                        call.enqueue(new Callback<List<Profile>>() {
                                            @Override
                                            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                                                e_wallet = response.body().get(0).getE_wallet();
                                                Log.d("e-wallet", String.valueOf(e_wallet));

                                            }

                                            @Override
                                            public void onFailure(Call<List<Profile>> call, Throwable t) {

                                            }
                                        });
                                    }
                                    hourlyRideCheck();
                                    doneBtn.setEnabled(false);
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
            }
        });

        checkRunningRides();

        checkHourlyRunningRides();

        checkRatingCall();

        checkInstantRatingCall();

        checkHourlyRatingCall();

        showAcceptedDriverData();

        showOnGoingInstantTripData();

        checkTripStatus();
    }

    private void checkTripStatus() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
        tripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        String TAG = data.child("TAG").getValue().toString();
                        String cashReceived = data.child("cashReceived").getValue().toString();
                        if (TAG.equals("Ride Finished") && cashReceived.equals("no")){
                            tripId = data.child("bookingId").getValue().toString();
                            dialog = new Dialog(MainActivity.this);
                            dialog.setContentView(R.layout.end_trip_fare);
                            Button showCashBtn = dialog.findViewById(R.id.endTripDialogBtn);

                            showCashBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this,ShowCash.class);
                                    intent.putExtra("tripId",tripId);
                                    intent.putExtra("check",5);
                                    startActivity(intent);
                                }
                            });
                            dialog.setCancelable(false);
                            dialog.show();

                        }else if (TAG.equals("No Driver Found")){
                            tripId = data.child("bookingId").getValue().toString();
                            driverId = data.child("driverId").getValue().toString();
                            carType = data.child("carType").getValue().toString();
                            androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("Sorry!");
                            dialog.setIcon(R.drawable.logo_circle);
                            dialog.setMessage("No driver available! \nNo car found for your trip");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    DatabaseReference keyRef = FirebaseDatabase.getInstance().getReference("InstantRides").child(carType).child(tripId);
                                    keyRef.removeValue();
                                    DatabaseReference keyRef2 = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId).child(tripId);
                                    keyRef2.removeValue();
                                    finish();
                                    startActivity(getIntent());
                                    exit(0);

                                }
                            });
                            androidx.appcompat.app.AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      /*  DatabaseReference tripRef2 = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
        tripRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String TAG = data.child("TAG").getValue().toString();
                    String cashReceived = data.child("cashReceived").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    private void checkInstantRatingCall() {

        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
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
                            TextView skipTv = dialog.findViewById(R.id.skipTv);
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

                            Call<List<DriverProfile>> call1 = apiInterface.getDriverData(driver_id);
                            call1.enqueue(new Callback<List<DriverProfile>>() {
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
                                    if (ratingGiven==false){
                                        Toast.makeText(MainActivity.this, "Please give a rating!", Toast.LENGTH_SHORT).show();
                                    }else{
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

                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("InstantRides").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                        dialog.dismiss();
                                    }

                                }
                            });

                            skipTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Call<List<RideModel>> ratingCall = apiInterface.addRating(tripId, 0);
                                    ratingCall.enqueue(new Callback<List<RideModel>>() {
                                        @Override
                                        public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<List<RideModel>> call, Throwable t) {

                                        }
                                    });
                                    DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
                                    delRef.child(tripId).removeValue();

                                    DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("InstantRides").child(carType);
                                    del1Ref.child(tripId).removeValue();
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

    private void showOnGoingInstantTripData() {
        DatabaseReference onGoingTrip = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
        onGoingTrip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    String acptCustId = data.child("customerId").getValue().toString();
                    String rideStatus = data.child("rideStatus").getValue().toString();
                    if (acptCustId.equals(userId) && rideStatus.equals("Start")){
                        driverDetailsLayout.setVisibility(View.GONE);
                        onGoingTripLayout.setVisibility(View.VISIBLE);
                        chooseRideType.setVisibility(View.GONE);
                        searchLayout.setVisibility(View.GONE);

                        driverId = data.child("driverId").getValue().toString();
                        String tripId = data.child("bookingId").getValue().toString();
                        carType = data.child("carType").getValue().toString();
                        pickUpPlace = data.child("pickUpPlace").getValue().toString();
                        destinationPlace = data.child("destinationPlace").getValue().toString();
                        String getDesLat = data.child("destinationLat").getValue().toString();
                        String getDesLon = data.child("destinationLon").getValue().toString();
                        String price = data.child("price").getValue().toString();

                        onGoingPickUp.setText(pickUpPlace);
                        onGoingDestination.setText(destinationPlace);
                        onGoingPrice.setText(" "+price);

                        Call<List<DriverProfile>> call = apiInterface.getDriverData(driverId);
                        call.enqueue(new Callback<List<DriverProfile>>() {
                            @Override
                            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                                if (response.isSuccessful()) {
                                    List<DriverProfile> list = response.body();

                                    driverNameTV2.setText(list.get(0).getFull_name());

                                    int sRideCount=list.get(0).getRideCount();
                                    float sRating=list.get(0).getRating();
                                    int sRatingCount=list.get(0).getRatingCount();
                                    float rat=sRating/sRatingCount;
                                    if(sRideCount<6){
                                        driverRatingBar2.setVisibility(View.GONE);
                                        rideCountTxt2.setVisibility(View.GONE);
                                        rideTxt2.setVisibility(View.GONE);
                                    }
                                    driverRatingBar2.setText(" "+String.format("%.2f",rat));
                                    rideCountTxt2.setText(String.valueOf(list.get(0).getRideCount()));

                                }
                            }
                            @Override
                            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {
                                Log.d("kahiniKi", t.getMessage());
                            }
                        });

                        Call<List<DriverInfo>> call1 = apiInterface.getCarNumber(driverId);
                        call1.enqueue(new Callback<List<DriverInfo>>() {
                            @Override
                            public void onResponse(Call<List<DriverInfo>> call, Response<List<DriverInfo>> response) {
                                String carname = response.body().get(0).getCar_name();
                                String carmodel = response.body().get(0).getCar_model();
                                String platenum = response.body().get(0).getPlate_number();

                                if (response.body().get(0).getSelfie()!=null){
                                    Picasso.get().load(Config.REG_LINE + response.body().get(0).getSelfie()).into(profileIV2, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.d("kiKahini", e.getMessage());
                                        }
                                    });
                                }

                                carTv2.setText(carname+" "+carmodel+" "+platenum);

                            }

                            @Override
                            public void onFailure(Call<List<DriverInfo>> call, Throwable t) {

                            }
                        });

                        DatabaseReference mapRef = FirebaseDatabase.getInstance().getReference("OnLineDrivers")
                                .child(carType).child(driverId).child("l");
                        mapRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double driverLat = (double) snapshot.child("0").getValue();
                                double driverLon = (double) snapshot.child("1").getValue();
                                showDestinationRoute(driverLat,driverLon,destinationPlace,getDesLat,getDesLon);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDestinationRoute(double driverLat, double driverLon, String destinationPlace, String getDesLat, String getDesLon) {
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

        float bearing = CalculateBearingAngle(driverLat,driverLon,Double.parseDouble(getDesLat),Double.parseDouble(getDesLon));

        place1 = new MarkerOptions().icon(markerIcon).flat(true)
                .position(new LatLng(driverLat, driverLon)).rotation(bearing).anchor(0.5f,0.5f).flat(true);
        BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.ic_destination);
        place2 = new MarkerOptions().icon(markerIcon2)
                .position(new LatLng(Double.parseDouble(getDesLat), Double.parseDouble(getDesLon))).title(destinationPlace);

        new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(),
                "driving"), "driving");

        map.addMarker(place1).showInfoWindow();
        map.addMarker(place2).showInfoWindow();
        LatLng latLng = new LatLng(driverLat, driverLon);
        MarkerAnimation.animateMarkerToGB(place1, latLng, new LatLngInterpolator.Spherical());

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(driverLat, driverLon), 18));
    }

    private void checkHourlyRunningRides() {
        DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides").child(userId);
        tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        rideStatus = data.child("rideStatus").getValue().toString();
                        if (rideStatus.equals("Start")) {
                            HourlyRideModel model = data.getValue(HourlyRideModel.class);
                            driverId = model.getDriverId();
                            tripId = model.getBookingId();
                            carType = model.getCarType();

                            Log.d("checkData",driverId+","+tripId+","+carType);

                            Intent intent = new Intent(MainActivity.this, RunningTrip.class);
                            intent.putExtra("check", 5);
                            intent.putExtra("tripId", tripId);
                            intent.putExtra("carType", carType);
                            intent.putExtra("driverId", driverId);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            getLastLocation();
        }
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
                                exit(0);
                            }
                        });
                        dialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                exit(0);
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
                            TextView skipTv = dialog.findViewById(R.id.skipTv);
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

                            Call<List<DriverProfile>> call1 = apiInterface.getDriverData(driver_id);
                            call1.enqueue(new Callback<List<DriverProfile>>() {
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
                                    if (ratingGiven==false){
                                        Toast.makeText(MainActivity.this, "Please give a rating!", Toast.LENGTH_SHORT).show();
                                    }else{
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
                                        dialog.dismiss();
                                    }

                                }
                            });

                            skipTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Call<List<HourlyRideModel>> ratingCall = apiInterface.addHourRating(tripId, 0);
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
                hourlyMicroPrice.setText(price);
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
                hourlyMicro11Price.setText(price);
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
                hourlysedanPrice.setText(price);
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
                hourlysedanPremeirePrice.setText(price);
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
                hourlysedanBusinessPrice.setText(price);
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
                            TextView skipTv = dialog.findViewById(R.id.skipTv);
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

                            Call<List<DriverProfile>> call1 = apiInterface.getDriverData(driver_id);
                            call1.enqueue(new Callback<List<DriverProfile>>() {
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
                                    if (ratingGiven==false){
                                        Toast.makeText(MainActivity.this, "Please give a rating!", Toast.LENGTH_SHORT).show();
                                    }else{
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
                                        dialog.dismiss();
                                    }

                                }
                            });

                            skipTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
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
        tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            driverId = model.getDriverId();

                            Intent intent = new Intent(MainActivity.this, RunningTrip.class);
                            intent.putExtra("check", 3);
                            intent.putExtra("pLat", picklat);
                            intent.putExtra("pLon", pickLon);
                            intent.putExtra("dLat", deslat);
                            intent.putExtra("dLon", deslon);
                            intent.putExtra("tripId", tripId);
                            intent.putExtra("carType", carType);
                            intent.putExtra("driverId", driverId);
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

        if (rideType.equals("interCity")) {
            upload();
        }else{
            uploadInstant();
        }
    }

    private void uploadInstant() {
        confirmRideBtn.setEnabled(false);

        Call<List<UniqueId>> idcall = apiInterface.getTripId();
        idcall.enqueue(new Callback<List<UniqueId>>() {
            @Override
            public void onResponse(Call<List<UniqueId>> call, Response<List<UniqueId>> response) {
                bookingId = String.valueOf(response.body().get(0).getUniq_id());
                DatabaseReference rideLaterRef = FirebaseDatabase.getInstance().getReference()
                        .child("InstantRides").child(type);

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
                rideInfo.put("coupon", String.valueOf(couponAmount));
                rideInfo.put("e_wallet", String.valueOf(e_wallet));
                rideInfo.put("finalPrice", "");
                rideInfo.put("cashReceived", "no");
                rideInfo.put("totalDistance", "");
                rideInfo.put("totalTime", "");
                rideInfo.put("TAG", "");

                rideLaterRef.child(bookingId).setValue(rideInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    if (task.isSuccessful()){
                                        DatabaseReference userRideRef = FirebaseDatabase.getInstance().getReference().child("CustomerInstantRides");
                                        userRideRef.child(userId).child(bookingId).setValue(rideInfo);
                                        map.clear();
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(wayLatitude, wayLongitude), 19));

                                    }
                                }
                            });

                        }
                    }
                });

                /*if (notify) {
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
                notify = false;*/

                findDriver();
            }

            @Override
            public void onFailure(Call<List<UniqueId>> call, Throwable t) {

            }
        });

        bottomsheet.setVisibility(View.GONE);
        chooseRideType.setVisibility(View.GONE);
        loadingDialog = ProgressDialog.show(MainActivity.this, "Searching...",
                "Searching Nearby Drivers....", true);

    }

    private void findDriver() {
        DatabaseReference drivers = FirebaseDatabase.getInstance().getReference("AvailableDrivers").child(type);
        GeoFire gfDrivers = new GeoFire(drivers);

        GeoQuery geoQuery = gfDrivers.queryAtLocation(new GeoLocation(pickUpLat,pickUpLon),radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound){
                    if(radius>=5){
                        driverFound = true;
                        driverId = key;
                        DatabaseReference rideLaterRef = FirebaseDatabase.getInstance().getReference()
                                .child("InstantRides").child(type).child(bookingId);
                        rideLaterRef.child("driverId").setValue(driverId);
                        DatabaseReference rideLaterRef2 = FirebaseDatabase.getInstance().getReference()
                                .child("CustomerInstantRides").child(userId).child(bookingId);
                        rideLaterRef2.child("driverId").setValue(driverId);
                        sendNotificationDriver(bookingId,driverId,type,"New Request","New Trip Available","main_activity");
                        showAcceptedDriverData();
                        loadingDialog.dismiss();
                    }

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound){
                    if (radius<=5){
                        radius++;
                        findDriver();
                    }else{
                        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Sorry!");
                        dialog.setIcon(R.drawable.logo_circle);
                        dialog.setMessage("No driver available! \nNo car found for your trip");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                loadingDialog.dismiss();
                                DatabaseReference keyRef = FirebaseDatabase.getInstance().getReference("InstantRides").child(type).child(bookingId);
                                keyRef.removeValue();
                                DatabaseReference keyRef2 = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId).child(bookingId);
                                keyRef2.removeValue();
                                finish();
                                startActivity(getIntent());
                                exit(0);

                            }
                        });
                        androidx.appcompat.app.AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

                    }

                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    private void showAcceptedDriverData() {
//        loadingDialog.dismiss();
        DatabaseReference instantRideRef = FirebaseDatabase.getInstance().getReference("CustomerInstantRides").child(userId);
        instantRideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        String bookStatus = data.child("bookingStatus").getValue().toString();
                        if ( bookStatus.equals("Booked")){
                            String rideStatus = data.child("rideStatus").getValue().toString();
                            if(rideStatus.equals("Pending")){
                                driverDetailsLayout.setVisibility(View.VISIBLE);
                                RideModel model = data.getValue(RideModel.class);
                                String accptDriverId = model.getDriverId();
                                String reqPickLat = model.getPickUpLat();
                                String reqPickLon = model.getPickUpLon();
                                String tripCarType = model.getCarType();

                                DatabaseReference mapRef = FirebaseDatabase.getInstance().getReference("OnLineDrivers")
                                        .child(tripCarType).child(accptDriverId).child("l");
                                mapRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        double driverLat = (double) snapshot.child("0").getValue();
                                        double driverLon = (double) snapshot.child("1").getValue();

                                        showPickupRoute(reqPickLat,reqPickLon,driverLat,driverLon);
                                        ShowDriverDetails(accptDriverId);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                if(rideStatus.equals("Start")){
                                    showOnGoingInstantTripData();
                                }
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

    private void ShowDriverDetails(String driverId) {
        Call<List<DriverProfile>> call = apiInterface.getDriverData(driverId);
        call.enqueue(new Callback<List<DriverProfile>>() {
            @Override
            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                if (response.isSuccessful()) {
                    List<DriverProfile> list = response.body();

                    driverNameTV.setText(list.get(0).getFull_name());
                    driver_PhoneTV.setText(list.get(0).getPhone());

                    Log.d("kahini",list.get(0).getFull_name()+","+list.get(0).getPhone());

                    int sRideCount=list.get(0).getRideCount();
                    float sRating=list.get(0).getRating();
                    int sRatingCount=list.get(0).getRatingCount();
                    float rat=sRating/sRatingCount;
                    if(sRideCount<6){
                        driverRatingBar.setVisibility(View.GONE);
                        rideCountTxt.setVisibility(View.GONE);
                        rideTxt.setVisibility(View.GONE);
                    }
                    driverRatingBar.setText(" "+String.format("%.2f",rat));
                    rideCountTxt.setText(String.valueOf(list.get(0).getRideCount()));

                }
            }
            @Override
            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {
                Log.d("kahiniKi", t.getMessage());
            }
        });

        driver_PhoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "+88" + driver_PhoneTV.getText().toString()));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Toasty.error(MainActivity.this, "" + activityException.getMessage(), Toasty.LENGTH_SHORT).show();
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });


        Call<List<DriverInfo>> call1 = apiInterface.getCarNumber(driverId);
        call1.enqueue(new Callback<List<DriverInfo>>() {
            @Override
            public void onResponse(Call<List<DriverInfo>> call, Response<List<DriverInfo>> response) {
                String carname = response.body().get(0).getCar_name();
                String carmodel = response.body().get(0).getCar_model();
                String platenum = response.body().get(0).getPlate_number();

                if (response.body().get(0).getSelfie()!=null){
                    profileIV.setVisibility(View.VISIBLE);
                    Picasso.get().load(Config.REG_LINE + response.body().get(0).getSelfie()).into(profileIV, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("kiKahini", e.getMessage());
                        }
                    });
                }

                carTv.setText(carname+" "+carmodel+" "+platenum);

            }

            @Override
            public void onFailure(Call<List<DriverInfo>> call, Throwable t) {

            }
        });

    }

    private void showPickupRoute(String reqPickLat, String reqPickLon, double driverLat, double driverLon) {
        searchLayout.setVisibility(View.GONE);
        chooseRideType.setVisibility(View.GONE);
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



        float bearing = CalculateBearingAngle(driverLat,driverLon,Double.parseDouble(reqPickLat),Double.parseDouble(reqPickLon));

        place1 = new MarkerOptions().icon(markerIcon).flat(true)
                .position(new LatLng(driverLat, driverLon)).title("Driver").rotation(bearing).flat(true).anchor(0.5f, 0.5f)
                .alpha((float) 0.91);
        BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.userpickup);
        place2 = new MarkerOptions().icon(markerIcon2)
                .position(new LatLng(Double.parseDouble(reqPickLat), Double.parseDouble(reqPickLon))).title("PickUp Place");

        new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(),
                "driving"), "driving");

        map.addMarker(place1).showInfoWindow();
        map.addMarker(place2).showInfoWindow();
        LatLng latLng = new LatLng(driverLat, driverLon);
        MarkerAnimation.animateMarkerToGB(place1, latLng, new LatLngInterpolator.Spherical());

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(driverLat,driverLon), 18));
    }

    private void upload() {

        confirmRideBtn.setEnabled(false);
        Call<List<UniqueId>> idcall = apiInterface.getTripId();
        idcall.enqueue(new Callback<List<UniqueId>>() {
            @Override
            public void onResponse(Call<List<UniqueId>> call, Response<List<UniqueId>> response) {
                bookingId = String.valueOf(response.body().get(0).getUniq_id());
                DatabaseReference rideLaterRef = FirebaseDatabase.getInstance().getReference()
                        .child("BookForLater").child(type);

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
                rideInfo.put("coupon", String.valueOf(couponAmount));
                rideInfo.put("e_wallet", String.valueOf(e_wallet));
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

                Call<List<RideModel>> call1 = apiInterface.saveTripRequest(bookingId, "Pending", type, userId,
                        String.valueOf(destinationLat), String.valueOf(destinationLon), destinationPlace, "", "",
                        dateTv.getText().toString(), String.valueOf(pickUpLat), String.valueOf(pickUpLon), pickUpPlace,
                        timeTv.getText().toString(), price, String.valueOf(couponAmount),"Pending", paymentType);
                call1.enqueue(new Callback<List<RideModel>>() {
                    @Override
                    public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {
                        Toast.makeText(MainActivity.this, "Ride request complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<RideModel>> call, Throwable t) {

                    }
                });
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Ride Created");
                dialog.setIcon(R.drawable.logo_circle);
                dialog.setMessage("Your ride has been created. If any driver accept your ride you will be notified!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                        startActivity(getIntent());
                    }
                });
                AlertDialog alertDialog = dialog.create();
                if (!isFinishing()){
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<UniqueId>> call, Throwable t) {

            }
        });

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
        hourlyconfirmRideBtn.setEnabled(false);

        Call<List<UniqueId>> idcall = apiInterface.getTripId();
        idcall.enqueue(new Callback<List<UniqueId>>() {
            @Override
            public void onResponse(Call<List<UniqueId>> call, Response<List<UniqueId>> response) {
                hourlyTripId = String.valueOf(response.body().get(0).getUniq_id());

                DatabaseReference hourlyLaterRef = FirebaseDatabase.getInstance().getReference()
                        .child("BookHourly").child(type);
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
                rideInfo.put("coupon", String.valueOf(couponAmount));
                rideInfo.put("e_wallet", String.valueOf(e_wallet));
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

                Call<List<HourlyRideModel>> call1 = apiInterface.saveHourlyTripRequest(hourlyTripId, "Pending",
                        type, userId, "", "", hourrideDate.getText().toString(), String.valueOf(pickUpLat),
                        String.valueOf(pickUpLon), pickUpPlace, hourrideTime.getText().toString(), "0",
                        String.valueOf(couponAmount), "Pending", paymentType);
                call1.enqueue(new Callback<List<HourlyRideModel>>() {
                    @Override
                    public void onResponse(Call<List<HourlyRideModel>> call, Response<List<HourlyRideModel>> response) {
                        Toast.makeText(MainActivity.this, "Ride request complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<HourlyRideModel>> call, Throwable t) {

                    }
                });

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Ride Created");
                dialog.setIcon(R.drawable.logo_circle);
                dialog.setMessage("Your ride has been created. If any driver accept your ride you will be notified!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                        startActivity(getIntent());
                    }
                });
                AlertDialog alertDialog = dialog.create();
                if (!isFinishing()){
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<UniqueId>> call, Throwable t) {

            }
        });

    }

    private void showDirections() {
        map.clear();
        searchLayout.setVisibility(View.GONE);
        backNFL.setVisibility(View.VISIBLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Locale locale2 = new Locale("bn","BN");
        Geocoder geocoder2 = new Geocoder(this, locale2);

        try {
            List<Address> addresses = geocoder2.getFromLocation(pickUpLat, pickUpLon, 1);
            Address location = addresses.get(0);
            pickUpCity = location.getLocality();
            Log.d("division",location.getAdminArea());

            List<Address> addresses2 = geocoder2.getFromLocation(destinationLat, destinationLon, 1);
            Address location1 = addresses2.get(0);
            destinationCity = location1.getLocality();
            destinationDivision = location1.getAdminArea();
            Log.d("division",location1.getAdminArea());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("checkCity",pickUpCity+","+destinationCity);

        if (rideType.equals("interCity")){
            if (pickUpCity.equals(destinationCity)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Alert!!");
                dialog.setIcon(R.drawable.logo_circle);
                dialog.setMessage("Intercity service is available in outside of Dhaka city only!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });

                AlertDialog alertDialog = dialog.create();
                if (!isFinishing()) {
                    alertDialog.show();
                }
            }
            else {
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

                map.animateCamera(CameraUpdateFactory.zoomTo(10.0f));

                calculateDirections(pickUpLat, pickUpLon, destinationLat, destinationLon,destinationDivision,pickUpCity,destinationCity);
            }
        }
        else if(rideType.equals("instant")){

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

            calculateDirections(pickUpLat, pickUpLon, destinationLat, destinationLon,destinationDivision,pickUpCity,destinationCity);
        }
    }

    private void calculateDirections(double pickUpLat, double pickUpLon, double destinationLat, double destinationLon,
                                     String destinationDivision,String pickUpCity,String destinationCity) {

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

                    Log.d("kmDist", travelduration + "," + kmdistance);
                    Log.d("trduration", String.valueOf(trduration));

                    bottomsheet.setVisibility(View.VISIBLE);
                    if(rideType.equals("instant")){
                        dateTv.setVisibility(View.GONE);
                        timeTv.setVisibility(View.GONE);
                        String startTime = new SimpleDateFormat("hh:mm:ss aa").format(Calendar.getInstance().getTime());
                        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                        dateTv.setText(startDate);
                        timeTv.setText(startTime);
                    }
                    progressBar.setVisibility(View.GONE);

                    sedanPrice(kmdistance, travelduration,destinationDivision,pickUpCity,destinationCity);
                    sedanPremierePrice(kmdistance, travelduration,destinationDivision,pickUpCity,destinationCity);
                    sedanBusinessPrice(kmdistance, travelduration,destinationDivision,pickUpCity,destinationCity);
                    hiace7Price(kmdistance, travelduration,destinationDivision,pickUpCity,destinationCity);
                    hiace11Price(kmdistance, travelduration,destinationDivision,pickUpCity,destinationCity);

                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

            }
        });

    }

    private void hiace7Price(int kmdistance, int travelduration, String destinationDivision,String pickUpCity,String destinationCity) {
        Call<List<RidingRate>> call = apiInterface.getPrice("Micro7");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRateOutside = rate.get(0).getBase_fare_outside_dhaka();
                    int minimumRateInside = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Division").child(destinationDivision);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!pickUpCity.equals(destinationCity)){
                                int farePercent = Integer.parseInt(snapshot.child("Fare").getValue().toString());
                                estprice = kmPrice + minPrice + minimumRateOutside;
                                int divisionPercent = (estprice*farePercent)/100;
                                int finalPrice = estprice+divisionPercent;
                                micro7price.setText("" + finalPrice);
                            }else{
                                int finalPrice = kmPrice + minPrice + minimumRateInside;
                                micro7price.setText("" + finalPrice);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    private void hiace11Price(int kmdistance, int travelduration, String destinationDivision,String pickUpCity,String destinationCity) {
        Call<List<RidingRate>> call = apiInterface.getPrice("Micro11");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRateOutside = rate.get(0).getBase_fare_outside_dhaka();
                    int minimumRateInside = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Division").child(destinationDivision);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!pickUpCity.equals(destinationCity)){
                                int farePercent = Integer.parseInt(snapshot.child("Fare").getValue().toString());
                                estprice = kmPrice + minPrice + minimumRateOutside;
                                int divisionPercent = (estprice*farePercent)/100;
                                int finalPrice = estprice+divisionPercent;
                                micro11price.setText("" + finalPrice);
                            }else{
                                int finalPrice = kmPrice + minPrice + minimumRateInside;
                                micro11price.setText("" + finalPrice);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    private void sedanBusinessPrice(int kmdistance, int travelduration, String destinationDivision,String pickUpCity,String destinationCity) {
        Call<List<RidingRate>> call = apiInterface.getPrice("SedanBusiness");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRateOutside = rate.get(0).getBase_fare_outside_dhaka();
                    int minimumRateInside = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Division").child(destinationDivision);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!pickUpCity.equals(destinationCity)){
                                int farePercent = Integer.parseInt(snapshot.child("Fare").getValue().toString());
                                estprice = kmPrice + minPrice + minimumRateOutside;
                                int divisionPercent = (estprice*farePercent)/100;
                                int finalPrice = estprice+divisionPercent;
                                sedanbusinessprice.setText("" + finalPrice);
                            }else{
                                int finalPrice = kmPrice + minPrice + minimumRateInside;
                                sedanbusinessprice.setText("" + finalPrice);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    public void sedanPremierePrice(int kmdistance, int travelduration, String destinationDivision,String pickUpCity,String destinationCity) {
        Call<List<RidingRate>> call = apiInterface.getPrice("SedanPremiere");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRateOutside = rate.get(0).getBase_fare_outside_dhaka();
                    int minimumRateInside = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Division").child(destinationDivision);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!pickUpCity.equals(destinationCity)){
                                int farePercent = Integer.parseInt(snapshot.child("Fare").getValue().toString());
                                estprice = kmPrice + minPrice + minimumRateOutside;
                                int divisionPercent = (estprice*farePercent)/100;
                                int finalPrice = estprice+divisionPercent;
                                premiereprice.setText("" + finalPrice);
                            }else{
                                int finalPrice = kmPrice + minPrice + minimumRateInside;
                                premiereprice.setText("" + finalPrice);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

            }
        });
    }

    private void sedanPrice(int kmdistance, int travelduration, String destinationDivision,String pickUpCity,String destinationCity) {

        Call<List<RidingRate>> call = apiInterface.getPrice("Sedan");
        call.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()) {
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate = rate.get(0).getMin_charge();
                    int minimumRateOutside = rate.get(0).getBase_fare_outside_dhaka();
                    int minimumRateInside = rate.get(0).getBase_fare_inside_dhaka();

                    int kmPrice = kmRate * kmdistance;
                    int minPrice = minRate * travelduration;

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Division").child(destinationDivision);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!pickUpCity.equals(destinationCity)){
                                int farePercent = Integer.parseInt(snapshot.child("Fare").getValue().toString());
                                estprice = kmPrice + minPrice + minimumRateOutside;
                                int divisionPercent = (estprice*farePercent)/100;
                                int finalPrice = estprice+divisionPercent;
                                sedanprice.setText("" + finalPrice);
                            }else{
                                int finalPrice = kmPrice + minPrice + minimumRateInside;
                                sedanprice.setText("" + finalPrice);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
                String currentDate =  year + "-" + month+"-"+day ;
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        instantRideBtn = findViewById(R.id.instantRideBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        driverDetailsLayout = findViewById(R.id.driverDetailsLayout);
        driverRatingBar = findViewById(R.id.driverRatingBar);
        driverRatingBar2 = findViewById(R.id.driverRatingBar2);
        rideCountTxt = findViewById(R.id.rideCountTxt);
        rideCountTxt2 = findViewById(R.id.rideCountTxt2);
        rideTxt2 = findViewById(R.id.rideTxt2);
        driverNameTV = findViewById(R.id.driverNameTV);
        driverNameTV2 = findViewById(R.id.driverNameTV2);
        driver_PhoneTV = findViewById(R.id.driver_PhoneTV);
        rideTxt = findViewById(R.id.rideTxt);
        onGoingPickUp = findViewById(R.id.onGoingPickUp);
        onGoingDestination = findViewById(R.id.onGoingDestination);
        onGoingPrice = findViewById(R.id.onGoingPrice);
        carTv = findViewById(R.id.driver_CarTV);
        carTv2 = findViewById(R.id.driver_CarTV2);
        onGoingTripLayout = findViewById(R.id.slidingPanel);
        rl1=findViewById(R.id.rl1);
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
        profileIV = findViewById(R.id.profileIV);
        profileIV2 = findViewById(R.id.profileIV2);
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
        rideHourly = findViewById(R.id.rideHourly);
        rideInterCity = findViewById(R.id.intercityBtn);
        rideDate = findViewById(R.id.rideDate);
        bottomsheet = findViewById(R.id.bottomsheet);
        chooseRideType = findViewById(R.id.chooseRideType);
        timeDateLayout = findViewById(R.id.timeDateLayout);
        dateTv = findViewById(R.id.rideDate);
        timeTv = findViewById(R.id.rideTime);
        confirmRideBtn = findViewById(R.id.confirmRideBtn);
        sedanbusiness = findViewById(R.id.sedanbusiness);
        sedanbusinessprice = findViewById(R.id.sedanbusinessPrice);
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
        progressBar = findViewById(R.id.progressbar);
        homeBtn = findViewById(R.id.homeBtn);
        workBtn = findViewById(R.id.workBtn);
    }

    private void navHeaderData() {

        Call<List<Profile>> call = apiInterface.getData(userId);
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.isSuccessful()) {
                    List<Profile> list = new ArrayList<>();
                    list = response.body();
                    if (list.get(0).getImage() == null) {
                        String uri = "@drawable/logo_circle";  // where myresource (without the extension) is the file

                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                        Drawable res = getResources().getDrawable(imageResource);
                        circularImageView.setImageDrawable(res);
                    }else{
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
                    int totalWallet = list.get(0).getWallet()+list.get(0).getE_wallet();
                    wallet_fromNavigation.setText("??? " + totalWallet);
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
                Call<List<ApiDeviceToken>> call = apiInterface.deleteToken(userId);
                call.enqueue(new Callback<List<ApiDeviceToken>>() {
                    @Override
                    public void onResponse(Call<List<ApiDeviceToken>> call, Response<List<ApiDeviceToken>> response) {
                    }
                    @Override
                    public void onFailure(Call<List<ApiDeviceToken>> call, Throwable t) {
                        Log.d("kahiniki",t.getMessage());
                    }
                });

                DatabaseReference deleteTokenRef = FirebaseDatabase.getInstance().getReference("CustomersToken").child(userId);
                deleteTokenRef.removeValue();

                Intent intent = new Intent(MainActivity.this, SignIn.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn", false);
                editor.putString("id", "");
                editor.commit();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                break;
            case R.id.profile:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, UserProfile.class).putExtra("id", userId));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.rides:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, MyRides.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.settings:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, settingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.emergency:
                startActivity(new Intent(MainActivity.this, Emergency.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                drawerLayout.closeDrawers();
                finish();
                break;
            case R.id.referral:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, Referral.class).putExtra("id", userId));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.coupon:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, CouponActivity.class).putExtra("id", userId));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.notification:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.history:
                checkConnection();
                if (!isConnected) {
                    snackBar(isConnected);
                    drawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(MainActivity.this, History.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    drawerLayout.closeDrawers();
                    finish();
                }
                break;
            case R.id.support:
                try{
                    Intent intent1 = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "support@swish.com.bd"));
                    intent1.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent1.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent1);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retro));
        }

        //update in 5 seconds
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100); // 10 seconds
        locationRequest.setFastestInterval(10); // 5 seconds

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
    }


    private void updateToken(String token) {
        Call<List<ApiDeviceToken>> call = apiInterface.updateToken(userId,token);
        call.enqueue(new Callback<List<ApiDeviceToken>>() {
            @Override
            public void onResponse(Call<List<ApiDeviceToken>> call, Response<List<ApiDeviceToken>> response) {
            }
            @Override
            public void onFailure(Call<List<ApiDeviceToken>> call, Throwable t) {
                Log.d("kahiniki",t.getMessage());
            }
        });
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
                    Data data = new Data(id, "1", message, title, receiverId1, toActivity);

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

    private void sendNotificationDriver(final String bookingId, final String receiverId, final String car, final String title,
                                        final String message, final String toActivity) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(car);
        Query query = tokens.orderByKey().equalTo(receiverId);
        final String receiverId1 = receiverId;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(bookingId, "1", message, title, receiverId1, toActivity);

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
                                        Data data = new Data(id, "1", message, title, (String) driverNotificationList.get(finalI), toActivity);
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

    public void homeaddress(View view) {

        DatabaseReference homeRef = FirebaseDatabase.getInstance().getReference("UserLocation");
        homeRef.child(userId).child("Home").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if(rideType.equals("interCity") || rideType.equals("instant")){
                        if (pickUpMarker==null){
                            pickUpPlace = snapshot.child("place").getValue().toString();
                            pickUpLat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                            pickUpLon = Double.parseDouble(snapshot.child("lon").getValue().toString());
                            autocompleteFragment.setText(pickUpPlace);

                            BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                            pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                if (addresses.size() > 0) {
                                    Address location = addresses.get(0);
                                    pickUpPlace = location.getAddressLine(0);
                                    map.addMarker(pickUpMarker.title("Drag for suitable position")).showInfoWindow();
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickUpLat, pickUpLon), 19));

                                    showDirections();

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
                                                Address location = addresses.get(0);
                                                pickUpPlace = location.getAddressLine(0);
                                                autocompleteFragment.setText(pickUpPlace);
                                                BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                                                marker.setIcon(markerIcon);

                                                showDirections();

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
                        else {
                            destinationPlace = snapshot.child("place").getValue().toString();
                            destinationLat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                            destinationLon = Double.parseDouble(snapshot.child("lon").getValue().toString());
                            autocompleteDestination.setText(destinationPlace);

                            if (destinationPlace.equals(pickUpPlace)) {
                                Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
                            } else {
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
                            }
                        }
                    }else{
                        pickUpPlace = snapshot.child("place").getValue().toString();
                        pickUpLat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                        pickUpLon = Double.parseDouble(snapshot.child("lon").getValue().toString());
                        autocompleteFragment.setText(pickUpPlace);

                        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                        pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                            if (addresses.size() > 0) {
                                Address location = addresses.get(0);
                                pickUpPlace = location.getAddressLine(0);
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

                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                            Address location = addresses.get(0);
                                            pickUpPlace = location.getAddressLine(0);
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

                    }
                }
                else{
                    Intent intent = new Intent(MainActivity.this,Home_Work_Address.class);
                    intent.putExtra("type","home");
                    intent.putExtra("currentLat",wayLatitude);
                    intent.putExtra("currentLon",wayLongitude);
                    intent.putExtra("id",userId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void workAddress(View view) {
        DatabaseReference homeRef = FirebaseDatabase.getInstance().getReference("UserLocation");
        homeRef.child(userId).child("Work").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    if (rideType.equals("interCity") ||rideType.equals("instant") ){
                        if (pickUpMarker==null){
                            pickUpPlace = snapshot.child("place").getValue().toString();
                            pickUpLat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                            pickUpLon = Double.parseDouble(snapshot.child("lon").getValue().toString());
                            autocompleteFragment.setText(pickUpPlace);

                            BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                            pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                if (addresses.size() > 0) {
                                    Address location = addresses.get(0);
                                    pickUpPlace = location.getAddressLine(0);
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

                                            try {
                                                List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                                Address location = addresses.get(0);
                                                pickUpPlace = location.getAddressLine(0);
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

                        }
                        else {
                            destinationPlace = snapshot.child("place").getValue().toString();
                            destinationLat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                            destinationLon = Double.parseDouble(snapshot.child("lon").getValue().toString());
                            autocompleteDestination.setText(destinationPlace);

                            if (destinationPlace.equals(pickUpPlace)) {
                                Toasty.info(MainActivity.this, "Please select your destination", Toasty.LENGTH_LONG).show();
                            } else {
                                map.clear();
                                BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                                pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon))
                                        .icon(markerIcon);

                                BitmapDescriptor markerIcon2 = vectorToBitmap(R.drawable.ic_destination);
                                map.addMarker(new MarkerOptions().position(new LatLng(destinationLat, destinationLon)).icon(markerIcon2).draggable(true).title("Drag for suitable position")).showInfoWindow();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destinationLat, destinationLon), 16));

                                showDirections();

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

                                            showDirections();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }
                    else{
                        pickUpPlace = snapshot.child("place").getValue().toString();
                        pickUpLat = Double.parseDouble(snapshot.child("lat").getValue().toString());
                        pickUpLon = Double.parseDouble(snapshot.child("lon").getValue().toString());
                        autocompleteFragment.setText(pickUpPlace);

                        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.userpickup);
                        pickUpMarker = new MarkerOptions().position(new LatLng(pickUpLat, pickUpLon)).icon(markerIcon).draggable(true);
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                            if (addresses.size() > 0) {
                                Address location = addresses.get(0);
                                pickUpPlace = location.getAddressLine(0);
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

                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(pickUpLat, pickUpLon, 1);
                                            Address location = addresses.get(0);
                                            pickUpPlace = location.getAddressLine(0);
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
                    }
                }else{
                    Intent intent = new Intent(MainActivity.this,Home_Work_Address.class);
                    intent.putExtra("type","work");
                    intent.putExtra("currentLat",wayLatitude);
                    intent.putExtra("currentLon",wayLongitude);
                    intent.putExtra("id",userId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void rideInfo(View view) {
        switch (view.getId()){
            case R.id.sedanInfo:
                carType = "Sedan";
                break;
            case R.id.sedanpremiereInfo:
                carType = "SedanPremiere";
                break;
            case R.id.sedanbusinessInfo:
                carType = "SedanBusiness";
                break;
            case R.id.micro7Info:
                carType = "Micro7";
                break;
            case R.id.micro11Info:
                carType = "Micro11";
                break;
        }

        startActivity(new Intent(MainActivity.this, FareDetails.class)
                .putExtra("check",3)
                .putExtra("rideType",rideType)
                .putExtra("carType", carType));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        snackBar(isConnected);
    }
    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
    }
    private void snackBar(boolean isConnected) {
        if(!isConnected) {
            snackbar = Snackbar.make(drawerLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE).setAction("ReTry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            snackbar.setDuration(5000);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
        /*TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }*/
            snackbar.show();
            /*final Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
            for (int i = 0; i < snackBarLayout.getChildCount(); i++) {
                View parent = snackBarLayout.getChildAt(i);
                if (parent instanceof LinearLayout) {
                    ((LinearLayout) parent).setRotation(180);
                    break;
                }
            }*/
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, intentFilter);
    }
    @Override
    protected void onResume() {
        // register connection status listener
        Connection.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

    }

    @Override
    protected void onStop() {
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

        super.onStop();
    }

    public float CalculateBearingAngle(double startLatitude,double startLongitude, double endLatitude, double endLongitude){
        LatLng begin = new LatLng(startLatitude,startLongitude);
        LatLng end = new LatLng(endLatitude,endLongitude);
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

}
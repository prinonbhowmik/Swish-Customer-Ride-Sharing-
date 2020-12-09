package com.hydertechno.swishcustomer.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.ForApi.DistanceApiClient;
import com.hydertechno.swishcustomer.ForApi.DistanceResponse;
import com.hydertechno.swishcustomer.ForApi.Element;
import com.hydertechno.swishcustomer.ForApi.RestUtil;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.Model.RidingRate;
import com.hydertechno.swishcustomer.Model.TripReportModel;
import com.hydertechno.swishcustomer.Notification.APIService;
import com.hydertechno.swishcustomer.Notification.Client;
import com.hydertechno.swishcustomer.Notification.Data;
import com.hydertechno.swishcustomer.Notification.MyResponse;
import com.hydertechno.swishcustomer.Notification.Sender;
import com.hydertechno.swishcustomer.Notification.Token;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRidesDetails extends AppCompatActivity {

    private TextView pickupPlaceTV, destinationTV, pickupDateTV, pickupTimeTV, carTypeTV, takaTV,headerTitle;
    private String id, pickupPlace, destinationPlace, pickupDate, pickupTime, carType,
            driverId, userId, bookingStatus, tripId , pickUpLat, pickUpLon, destinationLat,
            destinationLon, apiKey = "AIzaSyDy8NWL5x_v5AyQkcM9-4wqAWBp27pe9Bk", rideStatus;
    private Button editBtn, deleteBtn, driverInfoBtn, saveBtn,cancelBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private NeomorphFrameLayout editNFL,cancelNFL;
    private Boolean editable = false;
    private String pickUpCity, destinationCity,discount,payment,totalTime,totalDistance,finalPrice,realPrice;
    private int kmdistance, travelduration, price, check;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private ImageView receiptIv;
    private APIService apiService;
    private TextView reportTrip,discountTv,paymentTv;
    private LinearLayout discountLayout;
    private NeomorphFrameLayout receiptCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides_details);

        init();

        Intent intent = getIntent();
        id = intent.getStringExtra("bookingId");
        check = intent.getIntExtra("check", 0);

        getData();


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                deleteAlertDialog();
            }
        });

        reportTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<List<TripReportModel>> call = ApiUtils.getUserService().reportCheck(tripId);
                call.enqueue(new Callback<List<TripReportModel>>() {
                    @Override
                    public void onResponse(Call<List<TripReportModel>> call, Response<List<TripReportModel>> response) {
                        String nextStep = response.body().get(0).getNext_action();
                        if (nextStep.equals("false")){
                            Intent intent1 = new Intent(MyRidesDetails.this,TripReportActivity.class);
                            intent1.putExtra("tripId",tripId);
                            intent1.putExtra("driverId",driverId);
                            intent1.putExtra("userId",userId);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            finish();
                        }
                        else if (nextStep.equals("true")){
                            String status = response.body().get(0).getStatus();
                            if (status.equals("0")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyRidesDetails.this);
                                builder.setIcon(R.drawable.logo_circle);
                                builder.setTitle("Complain Under Review!");
                                builder.setMessage("Your complain is taken under review! \n" +
                                        "Our Customer service team will contact you very shortly");

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                if(!isFinishing()){
                                    builder.create().show();
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyRidesDetails.this);
                                builder.setIcon(R.drawable.logo_circle);
                                builder.setTitle("Complain Review!");
                                builder.setMessage("We have taken neccesary steps against your driver! \n " +
                                        "Sorry for your issue and Thanks for staying with SWISH");

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                if(!isFinishing()){
                                    builder.create().show();
                                }                          }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripReportModel>> call, Throwable t) {

                    }
                });

            }
        });

        receiptCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyRidesDetails.this,ReceiptActivity.class)
                        .putExtra("price",realPrice)
                        .putExtra("carType",carType)
                        .putExtra("finalPrice",finalPrice)
                        .putExtra("distance",totalDistance)
                        .putExtra("time",totalTime)
                        .putExtra("discount",discount)
                        .putExtra("bookingId",tripId)
                        .putExtra("check",1));
            }
        });

        driverInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();

                Bundle args = new Bundle();
                args.putString("id", driverId);
                args.putInt("check", check);
                DriverDetailsBottomSheet bottom_sheet = new DriverDetailsBottomSheet();
                bottom_sheet.setArguments(args);
                bottom_sheet.show(getSupportFragmentManager(), "bottomSheet");

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyRidesDetails.this);
                builder.setIcon(R.drawable.logo_circle);
                builder.setTitle("Cancel Alert!");
                builder.setMessage("Do you want to cancel this ride?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference cancelRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId).child(tripId);
                        cancelRef.removeValue();

                        DatabaseReference canRef =  FirebaseDatabase.getInstance().getReference("BookForLater").child(carType).child(tripId);
                        canRef.removeValue();

                        Call<List<RideModel>> call = apiInterface.cancelTrip(id,"Cancel");
                        call.enqueue(new Callback<List<RideModel>>() {
                            @Override
                            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                            }

                            @Override
                            public void onFailure(Call<List<RideModel>> call, Throwable t) {

                            }
                        });


                        sendNotification(userId,driverId,carType,"Trip Cancelled","Your trip has cancelled by your passenger","main_activity");
                        dialog.dismiss();
                        Intent i = new Intent(MyRidesDetails.this,MyRides.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                if(!isFinishing()){
                    builder.create().show();
                }

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editable = true;
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
            }
        });

        pickupPlaceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1) {
                    if (editable == true) {
                        Intent pickintent = new Intent(MyRidesDetails.this, RunningTrip.class);
                        pickintent.putExtra("lat", pickUpLat);
                        pickintent.putExtra("lon", pickUpLon);
                        pickintent.putExtra("place", pickupPlace);
                        pickintent.putExtra("tripId", id);
                        pickintent.putExtra("type", carType);
                        pickintent.putExtra("edit", 1);
                        pickintent.putExtra("check", 1);
                        pickintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(pickintent);
                        finish();
                    } else if (editable == false) {
                        Intent pikIntent = new Intent(MyRidesDetails.this, RunningTrip.class);
                        pikIntent.putExtra("lat", pickUpLat);
                        pikIntent.putExtra("lon", pickUpLon);
                        pikIntent.putExtra("place", pickupPlace);
                        pikIntent.putExtra("edit", 0);
                        pikIntent.putExtra("check", 1);
                        pikIntent.putExtra("tripId", id);
                        startActivity(pikIntent);

                    }
                }
            }
        });

        destinationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1) {
                    if (editable == true) {
                        Intent pickintent = new Intent(MyRidesDetails.this, RunningTrip.class);
                        pickintent.putExtra("lat", destinationLat);
                        pickintent.putExtra("lon", destinationLon);
                        pickintent.putExtra("place", destinationPlace);
                        pickintent.putExtra("tripId", id);
                        pickintent.putExtra("type", carType);
                        pickintent.putExtra("edit", 1);
                        pickintent.putExtra("check", 2);
                        pickintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(pickintent);
                        finish();
                    } else if (editable == false) {
                        Intent pikIntent = new Intent(MyRidesDetails.this, RunningTrip.class);
                        pikIntent.putExtra("lat", destinationLat);
                        pikIntent.putExtra("lon", destinationLon);
                        pikIntent.putExtra("place", destinationPlace);
                        pikIntent.putExtra("edit", 0);
                        pikIntent.putExtra("check", 2);
                        pikIntent.putExtra("tripId", id);
                        startActivity(pikIntent);
                    }
                }
            }
        });

        pickupDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable == true) {
                    getDate();
                }
            }
        });

        pickupTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable == true) {
                    getTime();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("CustomerRides")
                        .child(userId).child(id);
                updateRef.child("pickUpDate").setValue(pickupDateTV.getText().toString());
                updateRef.child("pickUpTime").setValue(pickupTimeTV.getText().toString());

                DatabaseReference updateRef2 = FirebaseDatabase.getInstance().getReference("BookForLater")
                        .child(carType).child(id);
                updateRef2.child("pickUpDate").setValue(pickupDateTV.getText().toString());
                updateRef2.child("pickUpTime").setValue(pickupTimeTV.getText().toString());

                Call<List<RideModel>> call = apiInterface.datetimeUpdate(id, pickupDateTV.getText().toString(),
                        pickupTimeTV.getText().toString());
                call.enqueue(new Callback<List<RideModel>>() {
                    @Override
                    public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<RideModel>> call, Throwable t) {

                    }
                });

                saveBtn.setVisibility(View.GONE);
                editable = false;
            }
        });

    }

    private void getPrice(String pickUpLat, String pickUpLon, String destinationLat, String destinationLon) {
        Locale locale = new Locale("en");
        Geocoder geocoder = new Geocoder(MyRidesDetails.this, locale);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(pickUpLat), Double.parseDouble(pickUpLon), 1);
            pickUpCity = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(destinationLat), Double.parseDouble(destinationLon), 1);
            destinationCity = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

                    Call<List<RidingRate>> call1 = apiInterface.getPrice(carType);
                    call1.enqueue(new Callback<List<RidingRate>>() {
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

                                Log.d("kmPrice", kmPrice + "," + minPrice);
                                Log.d("minf", String.valueOf(minimumRate));

                                Log.d("checkCity", pickUpCity + "," + destinationCity);

                                price = kmPrice + minPrice + minimumRate;

                                takaTV.setText("৳ " + price);
                                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("CustomerRides")
                                        .child(userId).child(id);
                                updateRef.child("price").setValue(String.valueOf(price));

                                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("BookForLater")
                                        .child(carType).child(id);
                                newRef.child("price").setValue(String.valueOf(price));

                                Call<List<RideModel>> call2 = apiInterface.priceUpdate(id, String.valueOf(price));
                                call2.enqueue(new Callback<List<RideModel>>() {
                                    @Override
                                    public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<List<RideModel>> call, Throwable t) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<List<RidingRate>> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

            }
        });
    }

    private void getTime() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyRidesDetails.this);

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
                pickupTimeTV.setText(timeFormat.format(time));
                dialog.dismiss();

            }
        });
    }

    private void getDate() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String currentDate = day + "-" + month + "-" + year;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;

                try {
                    date = dateFormat.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pickupDateTV.setText(dateFormat.format(date));
                long milisec = date.getTime();
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MyRidesDetails.this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    private void buttonsShow() {
        if (check == 1) {
            if (!bookingStatus.matches("Booked")) {
                editBtn.setVisibility(View.VISIBLE);
                driverInfoBtn.setVisibility(View.GONE);
            } else if(rideStatus.equals("Start")){
                cancelBtn.setVisibility(View.GONE);
                editNFL.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
                driverInfoBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.GONE);
            }else if(rideStatus.equals("End")){
                cancelBtn.setVisibility(View.GONE);
                editNFL.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
                driverInfoBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.GONE);
            }else {
                editNFL.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                driverInfoBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.GONE);
            }
        }
        if (check == 2) {
            driverInfoBtn.setVisibility(View.VISIBLE);
            editNFL.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            if (driverId == null) {
                driverInfoBtn.setVisibility(View.GONE);
            }

        }
        if (check == 3) {
            driverInfoBtn.setVisibility(View.VISIBLE);
            editNFL.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);

        }
    }

    private void deleteAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm");
        dialog.setIcon(R.drawable.ic_baseline_directions_car_24);
        dialog.setMessage("Do you want to confirm this ride ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRide();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void deleteRide() {
        DatabaseReference deleteRef = databaseReference.child("BookForLater").child(carType).child(id);
        DatabaseReference deleteRef2 = databaseReference.child("CustomerRides").child(userId).child(id);
        deleteRef.removeValue();
        deleteRef2.removeValue();

        Call<Void> call = apiInterface.deleteTrip(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

        sendNotification(id, driverId, carType, "Booking Delete!", "Customer had deleted his ride.", "booking_details");

        if (check == 1) {
            startActivity(new Intent(MyRidesDetails.this, MyRides.class));
        }
        if (check == 2) {
            startActivity(new Intent(MyRidesDetails.this, History.class));
        }

        finish();
    }

    private void init() {
        headerTitle = findViewById(R.id.headerTitle);
        sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", "");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        pickupDateTV = findViewById(R.id.pickupDateTV);
        pickupPlaceTV = findViewById(R.id.pickupPlaceTV);
        destinationTV = findViewById(R.id.destinationTV);
        pickupTimeTV = findViewById(R.id.pickupTimeTV);
        carTypeTV = findViewById(R.id.carTypeTV);
        receiptIv = findViewById(R.id.receiptIv);
        takaTV = findViewById(R.id.takaTV);
        receiptCard = findViewById(R.id.receiptCard);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        cancelNFL = findViewById(R.id.cancelNFL);
        cancelBtn = findViewById(R.id.cancelBtn);
        driverInfoBtn = findViewById(R.id.driverDetailsBtn);
        saveBtn = findViewById(R.id.saveBtn);
        editNFL = findViewById(R.id.editNFL);
        apiInterface = ApiUtils.getUserService();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        reportTrip = findViewById(R.id.reportTrip);
    }

    private void getData() {
        if (check == 1) {
            DatabaseReference reference = databaseReference.child("CustomerRides").child(userId).child(id);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        RideModel model = snapshot.getValue(RideModel.class);
                        pickupPlace = model.getPickUpPlace();
                        destinationPlace = model.getDestinationPlace();
                        pickupDate = model.getPickUpDate();
                        pickupTime = model.getPickUpTime();
                        carType = model.getCarType();
                        tripId=model.getBookingId();
                        driverId = model.getDriverId();
                        //taka = model.getPrice();
                        bookingStatus = model.getBookingStatus();
                        rideStatus = model.getRideStatus();
                        pickUpLat = model.getPickUpLat();
                        pickUpLon = model.getPickUpLon();
                        destinationLat = model.getDestinationLat();
                        destinationLon = model.getDestinationLon();

                        pickupPlaceTV.setText(pickupPlace);
                        destinationTV.setText(destinationPlace);
                        pickupDateTV.setText(pickupDate);
                        pickupTimeTV.setText(pickupTime);
                        switch (carType) {
                            case "Sedan":
                                carTypeTV.setText("Sedan");
                                break;
                            case "SedanPremiere":
                                carTypeTV.setText("Sedan Premiere");
                                break;
                            case "SedanBusiness":
                                carTypeTV.setText("Sedan Business");
                                break;
                            case "Micro7":
                                carTypeTV.setText("Micro 7");
                                break;
                            case "Micro11":
                                carTypeTV.setText("Micro 11");
                                break;
                        }

                        getPrice(pickUpLat, pickUpLon, destinationLat, destinationLon);
                        buttonsShow();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (check == 2) {
            headerTitle.setText("Ride History");
            Intent intent = getIntent();
            carType = intent.getStringExtra("cartype");
            pickupPlaceTV.setText(intent.getStringExtra("pickplace"));
            destinationTV.setText(intent.getStringExtra("desplace"));
            pickupDateTV.setText(intent.getStringExtra("pickdate"));
            pickupTimeTV.setText(intent.getStringExtra("picktime"));
            carTypeTV.setText(carType);
            realPrice = intent.getStringExtra("price");
            finalPrice = intent.getStringExtra("finalPrice");
            rideStatus = intent.getStringExtra("rideStatus");
            if (rideStatus.equals("End")){
                receiptCard.setVisibility(View.VISIBLE);
                reportTrip.setVisibility(View.VISIBLE);
            }
            takaTV.setText("৳ "+finalPrice);
            tripId = intent.getStringExtra("tripId");
            driverId = intent.getStringExtra("custId");
            discount = intent.getStringExtra("discount");
            totalTime = intent.getStringExtra("totalTime");
            totalDistance = intent.getStringExtra("totalDistance");
            payment = intent.getStringExtra("payment");
            buttonsShow();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (check == 1) {
            startActivity(new Intent(MyRidesDetails.this, MyRides.class));
        }
        if (check == 2) {
            startActivity(new Intent(MyRidesDetails.this, History.class));
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    private void sendNotification(final String id, final String receiverId, final String carType, final String title, final String message, final String toActivity) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(carType);
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


}
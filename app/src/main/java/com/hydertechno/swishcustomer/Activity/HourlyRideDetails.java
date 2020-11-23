package com.hydertechno.swishcustomer.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.Model.RideModel;
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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HourlyRideDetails extends AppCompatActivity {

    private TextView pickupPlaceTV, pickupDateTV, pickupTimeTV, carTypeTV, takaTV,headerTitle,reportTrip;
    private String id, car_type, pickupPlace, destinationPlace, pickupDate, pickupTime, carType, taka,type,
            driverId,userId,bookingStatus, d_name, d_phone,pickUpLat,pickUpLon,destinationLat,
            destinationLon,apiKey = "AIzaSyDy8NWL5x_v5AyQkcM9-4wqAWBp27pe9Bk",rideStatus;
    private Button editBtn,deleteBtn,driverInfoBtn,saveBtn,cancelBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private NeomorphFrameLayout editNFL;
    private Boolean editable=false;
    private int check;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_ride_details);

        init();

        Intent intent = getIntent();
        id = intent.getStringExtra("bookingId");
        check = intent.getIntExtra("check",0);

        checkConnection();

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
                Call<List<TripReportModel>> call = ApiUtils.getUserService().reportCheck(id);
                call.enqueue(new Callback<List<TripReportModel>>() {
                    @Override
                    public void onResponse(Call<List<TripReportModel>> call, Response<List<TripReportModel>> response) {
                        String nextStep = response.body().get(0).getNext_action();
                        if (nextStep.equals("false")){
                            Intent intent1 = new Intent(HourlyRideDetails.this,TripReportActivity.class);
                            intent1.putExtra("tripId",id);
                            intent1.putExtra("driverId",driverId);
                            intent1.putExtra("userId",userId);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            finish();
                        }
                        else if (nextStep.equals("true")){
                            String status = response.body().get(0).getStatus();
                            if (status.equals("0")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(HourlyRideDetails.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(HourlyRideDetails.this);
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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HourlyRideDetails.this);
                builder.setIcon(R.drawable.logo_circle);
                builder.setTitle("Cancel Alert!");
                builder.setMessage("Do you want to cancel this ride?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference cancelRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides").child(userId).child(id);
                        cancelRef.removeValue();

                        DatabaseReference canRef =  FirebaseDatabase.getInstance().getReference("BookHourly").child(carType).child(id);
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

                        sendNotification(id,driverId,carType,"Trip Cancelled","Your Passenger has cancelled the ride!","main_activity");

                        dialog.dismiss();
                        Intent i = new Intent(HourlyRideDetails.this,MyRides.class);
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

        driverInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("id", driverId);
                args.putInt("check", check);
                DriverDetailsBottomSheet bottom_sheet = new DriverDetailsBottomSheet();
                bottom_sheet.setArguments(args);
                bottom_sheet.show(getSupportFragmentManager(), "bottomSheet");
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editable=true;
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
            }
        });

        pickupPlaceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check==1){
                    if (editable==true){
                        Intent pickintent = new Intent(HourlyRideDetails.this,RunningTrip.class);
                        pickintent.putExtra("lat",pickUpLat);
                        pickintent.putExtra("lon",pickUpLon);
                        pickintent.putExtra("place",pickupPlace);
                        pickintent.putExtra("tripId",id);
                        pickintent.putExtra("type",carType);
                        pickintent.putExtra("edit",1);
                        pickintent.putExtra("check",4);
                        pickintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(pickintent);
                        finish();
                    }
                    else if(editable==false){
                        Intent pikIntent = new Intent(HourlyRideDetails.this,RunningTrip.class);
                        pikIntent.putExtra("lat",pickUpLat);
                        pikIntent.putExtra("lon",pickUpLon);
                        pikIntent.putExtra("place",pickupPlace);
                        pikIntent.putExtra("edit",0);
                        pikIntent.putExtra("check",1);
                        pikIntent.putExtra("tripId",id);
                        startActivity(pikIntent);

                    }
                }
            }
        });

        pickupDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable==true){
                    getDate();
                }
            }
        });

        pickupTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editable==true){
                    getTime();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides")
                        .child(userId).child(id);
                updateRef.child("pickUpDate").setValue(pickupDateTV.getText().toString());
                updateRef.child("pickUpTime").setValue(pickupTimeTV.getText().toString());

                DatabaseReference updateRef2 = FirebaseDatabase.getInstance().getReference("BookHourly")
                        .child(carType).child(id);
                updateRef2.child("pickUpDate").setValue(pickupDateTV.getText().toString());
                updateRef2.child("pickUpTime").setValue(pickupTimeTV.getText().toString());

                Call<List<RideModel>> call = apiInterface.datetimeUpdate(id,pickupDateTV.getText().toString(),
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
                editable=false;
            }
        });
    }

    private void getTime() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HourlyRideDetails.this);

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
                String currentDate = day + "/" + month + "/" + year;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(HourlyRideDetails.this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
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
        DatabaseReference deleteRef=databaseReference.child("BookHourly").child(carType).child(id);
        DatabaseReference deleteRef2=databaseReference.child("CustomerHourRides").child(userId).child(id);
        deleteRef.removeValue();
        deleteRef2.removeValue();

        Call<Void> call = apiInterface.hourdeleteTrip(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

        sendNotification(id, driverId,carType, "Booking Delete!", "Customer has deleted his ride.", "hourly_details");

        if(check==1) {
            startActivity(new Intent(HourlyRideDetails.this, MyRides.class));
        }
        if(check==2){
            startActivity(new Intent(HourlyRideDetails.this, History.class));
        }
        finish();
    }

    private void getData() {
        if (check==1){
            DatabaseReference reference = databaseReference.child("CustomerHourRides").child(userId).child(id);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        HourlyRideModel model = snapshot.getValue(HourlyRideModel.class);
                        pickupPlace = model.getPickUpPlace();
                        pickupDate = model.getPickUpDate();
                        pickupTime = model.getPickUpTime();
                        carType = model.getCarType();
                        driverId=model.getDriverId();
                        taka = model.getPrice();
                        bookingStatus = model.getBookingStatus();
                        rideStatus = model.getRideStatus();
                        pickUpLat = model.getPickUpLat();
                        pickUpLon = model.getPickUpLon();
                        takaTV.setText(taka);
                        pickupPlaceTV.setText(pickupPlace);

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

                        buttonsShow();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (check == 2){
            headerTitle.setText("Hourly Ride History");
            reportTrip.setVisibility(View.VISIBLE);
            Intent intent = getIntent();
            driverId = intent.getStringExtra("custId");
            pickupPlaceTV.setText( intent.getStringExtra("pickplace"));
            pickupDateTV.setText( intent.getStringExtra("pickdate"));
            pickupTimeTV.setText( intent.getStringExtra("picktime"));
            carTypeTV.setText( intent.getStringExtra("cartype"));
            takaTV.setText( intent.getStringExtra("price"));

            buttonsShow();
        }

    }

    private void buttonsShow() {
        if (check==1){
            if(!bookingStatus.matches("Booked")){
                editBtn.setVisibility(View.VISIBLE);
                driverInfoBtn.setVisibility(View.GONE);
            }
            else{
                editNFL.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
                driverInfoBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
            }
        }
        if (check==2){

            driverInfoBtn.setVisibility(View.VISIBLE);
            editNFL.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);

            if (driverId==null){
                driverInfoBtn.setVisibility(View.GONE);
            }
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    private void init() {
        sharedPreferences = getSharedPreferences("MyRef",MODE_PRIVATE);
        userId = sharedPreferences.getString("id","");
        databaseReference= FirebaseDatabase.getInstance().getReference();
        pickupDateTV=findViewById(R.id.pickupDateTV);
        pickupPlaceTV=findViewById(R.id.pickupPlaceTV);
        headerTitle=findViewById(R.id.headerTitle);
        reportTrip=findViewById(R.id.reportTrip);
        pickupTimeTV=findViewById(R.id.pickupTimeTV);
        carTypeTV=findViewById(R.id.carTypeTV);
        takaTV=findViewById(R.id.takaTV);
        cancelBtn=findViewById(R.id.cancelBtn);
        editBtn=findViewById(R.id.editBtn);
        deleteBtn=findViewById(R.id.deleteBtn);
        driverInfoBtn=findViewById(R.id.driverDetailsBtn);
        saveBtn=findViewById(R.id.saveBtn);
        editNFL = findViewById(R.id.editNFL);
        apiInterface = ApiUtils.getUserService();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }

    private void sendNotification(final String id, final String receiverId,final String carType,  final String title, final String message, final String toActivity) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("DriversToken").child(carType);
        Query query = tokens.orderByKey().equalTo(receiverId);
        String receiverId1=receiverId;
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

}
package com.hydertechno.swishcustomer.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.hydertechno.swishcustomer.Utils.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowCash extends AppCompatActivity {
    Button payCasshBtn;
    private TextView pickupPlaceTV, destinationPlaceTV, cashTxt, distanceTv, durationTv, final_Txt, discountTv, hourTv;
    private int  check, realPrice, discount, addWalletBalance;
    private Integer setCoupon;
    private float hourPrice;
    private RelativeLayout hourLayout, kmLayout;
    private NeomorphFrameLayout cashNFL;
    private String pickUpPlace, destinationPlace, userId,driverId,price, carType, payment, tripId
            ,status,finalPrice,discountPrice,distance,duration;
    private ImageView info;
    private SharedPreferences sharedPreferences;
    private LottieAnimationView progrssbar;
    private Dialog dialog;
    private ApiInterface api;
    private boolean couponActive = false, walletLow = false, walletHigh = false;
    private int wallet;
    private int halfPrice;
    private int actualPrice;
    private int walletBalance;
    private int updatewallet;
    private float driverRating;
    private int driverRatingCount;
    private float rating1;
    private float totalRating;
    private int totalCount;
    private boolean ratingGiven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cash);

        init();

        Intent intent = getIntent();
        tripId = intent.getStringExtra("tripId");
        check = intent.getIntExtra("check",0);
        userId = sharedPreferences.getString("id","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cashNFL.setVisibility(View.VISIBLE);
                progrssbar.setVisibility(View.GONE);
            }
        },3000);

        if (check==3){
            DatabaseReference cashRef = FirebaseDatabase.getInstance().getReference("CustomerRides")
                    .child(userId).child(tripId);
            cashRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        RideModel model = snapshot.getValue(RideModel.class);
                        price = model.getPrice();
                        pickUpPlace = model.getPickUpPlace();
                        destinationPlace = model.getDestinationPlace();
                        driverId = model.getDriverId();
                        finalPrice = model.getFinalPrice();
                        discountPrice = model.getDiscount();
                        distance = model.getTotalDistance();
                        duration = model.getTotalTime();
                        carType = model.getCarType();

                        cashTxt.setText("৳ " + price);
                        pickupPlaceTV.setText(pickUpPlace);
                        destinationPlaceTV.setText(destinationPlace);
                        discountTv.setText(discountPrice);
                        final_Txt.setText(finalPrice);
                        kmLayout.setVisibility(View.VISIBLE);
                        distanceTv.setText("Distance : " + distance + " km");
                        durationTv.setText("Duration : " + duration + " min");
                    }else {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        if (check==4){
            DatabaseReference cashRef = FirebaseDatabase.getInstance().getReference("CustomerHourRides")
                    .child(userId).child(tripId);
            cashRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        RideModel model = snapshot.getValue(RideModel.class);
                        price = model.getPrice();
                        pickUpPlace = model.getPickUpPlace();
                        driverId = model.getDriverId();
                        finalPrice = model.getFinalPrice();
                        discountPrice = model.getDiscount();
                        carType = model.getCarType();

                        cashTxt.setText("৳ " + price);
                        pickupPlaceTV.setText(pickUpPlace);
                        destinationPlaceTV.setText(destinationPlace);
                        discountTv.setText(discountPrice);
                        final_Txt.setText(finalPrice);
                        hourLayout.setVisibility(View.VISIBLE);
                        hourTv.setText(snapshot.child("totalTime").getValue().toString());

                    }else {
                        startActivity(new Intent(ShowCash.this,History.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        payCasshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(ShowCash.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowCash.this, "" + carType, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShowCash.this, FareDetails.class).putExtra("carType", carType));
            }
        });

        checkRatingCall();

    }

    private void checkRatingCall()
    {
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
                            dialog = new Dialog(ShowCash.this);
                            dialog.setContentView(R.layout.driver_rating_popup);
                            TextView driveName = dialog.findViewById(R.id.driverNaamTv);
                            CircleImageView driverImage = dialog.findViewById(R.id.profileIV);
                            RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                            Button submitBTN = dialog.findViewById(R.id.submitBTN);

                            Call<List<DriverProfile>> call = ApiUtils.getUserService().getDriverData(driver_id);
                            call.enqueue(new Callback<List<DriverProfile>>() {
                                @Override
                                public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                                    List<DriverProfile> list = response.body();

                                    if (!list.get(0).getImage().equals("")) {
                                        Picasso.get().load(Config.DRIVER_IMAGE_LINE + list.get(0).getImage()).into(driverImage
                                                , new com.squareup.picasso.Callback() {
                                                    @Override
                                                    public void onSuccess() {
                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        Log.d("kiKahini", e.getMessage());
                                                    }
                                                });
                                    }
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
                                        Call<List<DriverProfile>> call1 = ApiUtils.getUserService().updateRating(driver_id, totalRating, totalCount);
                                        call1.enqueue(new Callback<List<DriverProfile>>() {
                                            @Override
                                            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {

                                            }
                                        });

                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("BookForLater").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                        startActivity(new Intent(ShowCash.this,MainActivity.class));
                                        finish();
                                    } else {
                                        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("CustomerRides").child(userId);
                                        delRef.child(tripId).removeValue();

                                        DatabaseReference del1Ref = FirebaseDatabase.getInstance().getReference("BookForLater").child(carType);
                                        del1Ref.child(tripId).removeValue();
                                        startActivity(new Intent(ShowCash.this,MainActivity.class));
                                        finish();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.setCancelable(false);

                            dialog.show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void init() {
        payCasshBtn = findViewById(R.id.payCashBtn);
        final_Txt = findViewById(R.id.final_Txt);
        distanceTv = findViewById(R.id.distance);
        durationTv = findViewById(R.id.duration);
        progrssbar = findViewById(R.id.progrssbar);
        discountTv = findViewById(R.id.discount_Txt);
        pickupPlaceTV = findViewById(R.id.pickupPlaceTV);
        destinationPlaceTV = findViewById(R.id.destinationPlaceTV);
        kmLayout = findViewById(R.id.kmLayout);
        hourTv = findViewById(R.id.hourTxt);
        cashNFL = findViewById(R.id.cashNFL);
        hourLayout = findViewById(R.id.hourLayout);
        cashTxt = findViewById(R.id.cashTxt);
        info = findViewById(R.id.infoIV);
        sharedPreferences = getSharedPreferences("MyRef", Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShowCash.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
package com.hydertechno.swishcustomer.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;

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
    private ApiInterface api;
    private boolean couponActive = false, walletLow = false, walletHigh = false;
    private int wallet;
    private int halfPrice;
    private int actualPrice;
    private int walletBalance;
    private int updatewallet;

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
                        durationTv.setText("Duration : " + duration + " km");
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
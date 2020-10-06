package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

public class ShowCash extends AppCompatActivity {
    Button payCasshBtn;
    TextView pickupPlaceTV,destinationPlaceTV,cashTxt;
    int price;
    String pickUpPlace,destinationPlace,tripId,userId,carType,driverId;
    private NeomorphFrameLayout cashNFL;
    private ImageView info;
    private float rating1 = 0,rating;
    private static int SPLASH_TIME_OUT=1000;
    private int count1 =0,count;
    private boolean ratingUsed =false;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private String driver_id;
    private LottieAnimationView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cash);

        init();

        Intent intent = getIntent();
        tripId = intent.getStringExtra("tripId");
        carType = intent.getStringExtra("carType");
        userId = sharedPreferences.getString("id","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cashNFL.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        },SPLASH_TIME_OUT);


        DatabaseReference cashRef = FirebaseDatabase.getInstance().getReference("CustomerRides")
                .child(userId).child(tripId);
        cashRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RideModel model = snapshot.getValue(RideModel.class);
                String price = model.getPrice();
                pickUpPlace = model.getPickUpPlace();
                destinationPlace = model.getDestinationPlace();
                driverId = model.getDriverId();
                cashTxt.setText("৳ " + price);
                pickupPlaceTV.setText(pickUpPlace);
                destinationPlaceTV.setText(destinationPlace);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        cashNFL=findViewById(R.id.cashNFL);
        payCasshBtn = findViewById(R.id.collectBtn);
        pickupPlaceTV = findViewById(R.id.pickupPlaceTV);
        destinationPlaceTV = findViewById(R.id.destinationPlaceTV);
        cashTxt = findViewById(R.id.cashTxt);
        info = findViewById(R.id.infoIV);
        sharedPreferences = getSharedPreferences("MyRef",MODE_PRIVATE);
        apiInterface = ApiUtils.getUserService();
        progressBar = findViewById(R.id.progrssbar);
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
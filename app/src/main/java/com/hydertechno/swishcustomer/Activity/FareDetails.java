package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.Rate;
import com.hydertechno.swishcustomer.R;

public class FareDetails extends AppCompatActivity {

    private TextView baseTv,kiloTv,minuteTv;
    private String baseFare,kiloFare,minuteFare,carType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_details);

        Intent intent = getIntent();
        carType = intent.getStringExtra("carType");

        init();

        checkConnection();

        DatabaseReference getRef = FirebaseDatabase.getInstance().getReference("RidingRate").child(carType);
        getRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Rate rate = snapshot.getValue(Rate.class);

                kiloFare = rate.getKm();
                minuteFare = rate.getMin();
                baseFare = rate.getMinimumfare();

                baseTv.setText(baseFare);
                kiloTv.setText(kiloFare);
                minuteTv.setText(minuteFare);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    private void init() {
        baseTv=findViewById(R.id.baseTv);
        kiloTv=findViewById(R.id.kiloTv);
        minuteTv=findViewById(R.id.minuteTv);
    }
}
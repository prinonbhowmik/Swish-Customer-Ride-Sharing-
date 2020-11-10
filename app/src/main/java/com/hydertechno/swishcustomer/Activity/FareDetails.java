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
import com.hydertechno.swishcustomer.Model.RidingRate;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Call<List<RidingRate>> call1 = ApiUtils.getUserService().getPrice(carType);
        call1.enqueue(new Callback<List<RidingRate>>() {
            @Override
            public void onResponse(Call<List<RidingRate>> call, Response<List<RidingRate>> response) {
                if (response.isSuccessful()){
                    List<RidingRate> rate = new ArrayList<>();
                    rate = response.body();
                    int kmRate = rate.get(0).getKm_charge();
                    int minRate =rate.get(0).getMin_charge();
                    int minimumRate = rate.get(0).getBase_fare_inside_dhaka();

                    baseTv.setText(""+minimumRate);
                    kiloTv.setText(""+kmRate);
                    minuteTv.setText(""+minRate);
                }
            }

            @Override
            public void onFailure(Call<List<RidingRate>> call, Throwable t) {

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
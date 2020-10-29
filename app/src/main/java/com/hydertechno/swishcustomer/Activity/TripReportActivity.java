package com.hydertechno.swishcustomer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hydertechno.swishcustomer.R;

public class TripReportActivity extends AppCompatActivity {

    private String userId,driverId,tripId,reportMessage;
    private RadioGroup radioGroup;
    private EditText otherEt;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_report);

        Intent i = getIntent();
        tripId = i.getStringExtra("tripId");
        userId = i.getStringExtra("userId");
        driverId = i.getStringExtra("driverId");

        init();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                
            }
        });


    }

    private void init() {
        radioGroup = findViewById(R.id.radioGroup);
        submitBtn=findViewById(R.id.submitBtn);
        otherEt = findViewById(R.id.otherEt);
    }
}
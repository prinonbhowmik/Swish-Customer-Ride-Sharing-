package com.hydertechno.swishcustomer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.Model.TripReportModel;
import com.hydertechno.swishcustomer.Notification.APIService;
import com.hydertechno.swishcustomer.Notification.Client;
import com.hydertechno.swishcustomer.Notification.Data;
import com.hydertechno.swishcustomer.Notification.MyResponse;
import com.hydertechno.swishcustomer.Notification.Sender;
import com.hydertechno.swishcustomer.Notification.Token;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripReportActivity extends AppCompatActivity {

    private String userId, driverId, tripId, reportMessage;
    private RadioGroup radioGroup;
    private EditText otherEt;
    private Button submitBtn;
    private APIService apiService;

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
                switch (i) {
                    case R.id.type1:
                        reportMessage = "Involved in an accident";
                        otherEt.setText(reportMessage);
                        otherEt.setVisibility(View.GONE);
                        break;
                    case R.id.type2:
                        reportMessage = "Paid extra cash";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type3:
                        reportMessage = "My fair doesn't match";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type4:
                        reportMessage = "My stuff was stolen";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type5:
                        reportMessage = "Rough driving issue";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type6:
                        reportMessage="Vehicle was not as expected";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type7:
                        reportMessage="Misbehaviour from my driver";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type8:
                        reportMessage = "Car information doesn't match";
                        otherEt.setVisibility(View.GONE);
                        otherEt.setText(reportMessage);
                        break;
                    case R.id.type9:
                        otherEt.setVisibility(View.VISIBLE);
                        otherEt.requestFocus();
                        otherEt.setText("");
                        break;
                }
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportMessage = otherEt.getText().toString();

                Call<List<TripReportModel>> call = ApiUtils.getUserService().report(tripId,driverId,userId,reportMessage);
                call.enqueue(new Callback<List<TripReportModel>>() {
                    @Override
                    public void onResponse(Call<List<TripReportModel>> call, Response<List<TripReportModel>> response) {
                        if (response.isSuccessful()){
                            if (response.body().get(0).getStatus().equals("done")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(TripReportActivity.this);
                                builder.setIcon(R.drawable.logo_circle);
                                builder.setTitle("Report!");
                                builder.setMessage("Your complain has taken under review. We will take action within 24hrs.");

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(TripReportActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                if(!isFinishing()){
                                    builder.create().show();
                                }



                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripReportModel>> call, Throwable t) {

                    }
                });

            }
        });


    }

    private void init() {
        radioGroup = findViewById(R.id.radioGroup);
        submitBtn = findViewById(R.id.submitBtn);
        otherEt = findViewById(R.id.otherEt);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }
}
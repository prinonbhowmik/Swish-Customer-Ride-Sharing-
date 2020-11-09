package com.hydertechno.swishcustomer.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.R;


public class settingsActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView modeTxt,versionCodeTv;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Double wayLatitude,wayLongitude;
    private static Switch switchBtn;
    public static boolean darkMode = false;
    private String userId;
    private CardView homeCardView,workCardView;
    private TextView homeTv,workTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        checkConnection();

        showSavedAddress();

        changeSavedAddress();

        if (darkMode==true){
            switchBtn.setChecked(true);
            switchBtn.setText("Dark");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(settingsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

    }

    private void changeSavedAddress() {

        homeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settingsActivity.this);
                builder.setIcon(R.drawable.home_icon_black);
                builder.setTitle("Home Address!");
                builder.setMessage("Do you want to change your Home Address?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(settingsActivity.this,Home_Work_Address.class);
                        intent.putExtra("type","home");
                        intent.putExtra("currentLat",wayLatitude);
                        intent.putExtra("currentLon",wayLongitude);
                        intent.putExtra("id",userId);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                if(!isFinishing()){
                    builder.create().show();
                }

            }
        });

        workCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(settingsActivity.this);
                builder.setIcon(R.drawable.work_icon_black);
                builder.setTitle("Work Address!");
                builder.setMessage("Do you want to change your Work Address?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(settingsActivity.this,Home_Work_Address.class);
                        intent.putExtra("type","work");
                        intent.putExtra("currentLat",wayLatitude);
                        intent.putExtra("currentLon",wayLongitude);
                        intent.putExtra("id",userId);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                if(!isFinishing()){
                    builder.create().show();
                }

            }
        });
    }

    private void showSavedAddress() {
        DatabaseReference homeRef = FirebaseDatabase.getInstance().getReference("UserLocation").child(userId).child("Home");
        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   String homeAddress = snapshot.child("place").getValue().toString();
                   wayLatitude = Double.valueOf(snapshot.child("lat").getValue().toString());
                   wayLongitude = Double.valueOf(snapshot.child("lon").getValue().toString());
                   homeTv.setText(homeAddress);
               }
               else{
                   homeTv.setHint("Add Home Address");
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference workRef = FirebaseDatabase.getInstance().getReference("UserLocation").child(userId).child("Work");
        workRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   String work = snapshot.child("place").getValue().toString();
                   wayLatitude = Double.valueOf(snapshot.child("lat").getValue().toString());
                   wayLongitude = Double.valueOf(snapshot.child("lon").getValue().toString());
                   workTv.setText(work);
               } else{
                   workTv.setHint("Add Work Address");

               }
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
        backBtn = findViewById(R.id.backBtn);
       /* modeTxt = findViewById(R.id.modeTxt);*/
        switchBtn = findViewById(R.id.switchBtn);
        versionCodeTv = findViewById(R.id.versionCodeTv);
        sharedpreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        editor = sharedpreferences.edit();

        sharedpreferences = getSharedPreferences("MyRef", Context.MODE_PRIVATE);
        darkMode = sharedpreferences.getBoolean("dark",false);
        userId = sharedpreferences.getString("id","");

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCodeTv.setText("Version "+pinfo.versionName);

        homeCardView = findViewById(R.id.homeCardView);
        workCardView = findViewById(R.id.workCardView);
        homeTv = findViewById(R.id.homeTv);
        workTv = findViewById(R.id.workTv);
    }

    public void switchChange(View view) {
        if (switchBtn.isChecked()){
            switchBtn.setText("Dark");
            darkMode = true;
            editor.putBoolean("dark",true);
            editor.commit();
        }
        else{
            switchBtn.setText("Light");
            switchBtn.setChecked(false);
            darkMode = false;
            editor.putBoolean("dark",false);
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(settingsActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    public void changePassword(View view) {
        startActivity(new Intent(settingsActivity.this, ChangePassword.class).putExtra("id", userId));
    }
}
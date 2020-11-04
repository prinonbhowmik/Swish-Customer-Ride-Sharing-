package com.hydertechno.swishcustomer.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.R;


public class settingsActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView modeTxt;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private static Switch switchBtn;
    public static boolean darkMode = false;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        checkConnection();

        sharedpreferences = getSharedPreferences("MyRef", Context.MODE_PRIVATE);
        darkMode = sharedpreferences.getBoolean("dark",false);
        userId = sharedpreferences.getString("id","");

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
        sharedpreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
        editor = sharedpreferences.edit();
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
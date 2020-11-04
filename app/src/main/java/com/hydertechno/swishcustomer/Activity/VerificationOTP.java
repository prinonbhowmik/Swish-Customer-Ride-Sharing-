package com.hydertechno.swishcustomer.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.R;

public class VerificationOTP extends AppCompatActivity {

    private TextInputEditText verifyEt;
    private TextInputLayout otp_LT;
    private Button verifyBtn;
    private String otpCode,userOtp,phone,id;
    private LottieAnimationView progressbar;
    private int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_o_t_p);

        init();

        checkConnection();

        Intent i = getIntent();
        check = i.getIntExtra("check",0);

        if (check==1){
            otpCode = i.getStringExtra("otp");
            phone = i.getStringExtra("phone");
        }else if (check==2){
            otpCode = i.getStringExtra("otp");
            id = i.getStringExtra("id");
        }

       // requestPermissions();

       // new OTPReceiver().setEditText_otp(verifyEt);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                userOtp = verifyEt.getText().toString();
                verifyBtn.setEnabled(false);

                if (TextUtils.isEmpty(userOtp)) {
                    otp_LT.setErrorEnabled(true);
                    otp_LT.setError("Please Enter OTP");
                    verifyEt.requestFocus();
                } else {
                       if (userOtp.matches(otpCode)) {
                           progressbar.setVisibility(View.VISIBLE);
                           progressbar.setAnimation("car_moving.json");
                           progressbar.playAnimation();
                           otp_LT.setErrorEnabled(false);
                           hideKeyboardFrom(getApplicationContext());
                          if (check==1){
                              startActivity(new Intent(VerificationOTP.this, SignUp.class).putExtra("phone", phone));
                          }
                          else{
                              startActivity(new Intent(VerificationOTP.this, ResetPassword.class).putExtra("id",id));
                          }
                           overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                           finish();
                       } else {
                           otp_LT.setErrorEnabled(true);
                           otp_LT.setError("Otp doesn't match!");
                           verifyEt.requestFocus();
                       }

                }
                verifyBtn.setEnabled(true);
            }
        });


    }


    private void init() {
        verifyEt = findViewById(R.id.verify_Et);
        verifyBtn = findViewById(R.id.submitBtn);
        progressbar=findViewById(R.id.progrssbar);
        otp_LT=findViewById(R.id.otp_LT);
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(VerificationOTP.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VerificationOTP.this,new String[]{
                    Manifest.permission.RECEIVE_SMS
            },100);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VerificationOTP.this,SignIn.class));
        finish();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }
}
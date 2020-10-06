package com.hydertechno.swishcustomer.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.swishcustomer.R;

public class VerificationOTP extends AppCompatActivity {

    private TextInputEditText verifyEt;
    private TextInputLayout otp_LT;
    private Button verifyBtn;
    private String otpCode,userOtp,phone;
    private LottieAnimationView progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_o_t_p);

        init();

        Intent i = getIntent();
        otpCode = i.getStringExtra("otp");
        phone = i.getStringExtra("phone");


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        startActivity(new Intent(VerificationOTP.this, SignUp.class).putExtra("phone", phone));
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

}
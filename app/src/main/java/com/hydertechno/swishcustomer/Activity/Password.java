package com.hydertechno.swishcustomer.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.swishcustomer.Model.CheckModel;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Password extends AppCompatActivity {

    private TextInputEditText passEt;
    private TextView forgotPassTv;
    private TextInputLayout password_LT;
    private Button loginBtn;
    private String id, password, matchpassword,phone;
    List<Profile> list;
    private ApiInterface api;
    private LottieAnimationView progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        init();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        phone = intent.getStringExtra("phone");

        forgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<CheckModel>> call = api.forgotPassword(phone);
                call.enqueue(new Callback<List<CheckModel>>() {
                    @Override
                    public void onResponse(Call<List<CheckModel>> call, Response<List<CheckModel>> response) {
                        String otp = response.body().get(0).getOtp();
                        Intent intent1 = new Intent(Password.this, VerificationOTP.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent1.putExtra("check",2);
                        intent1.putExtra("id",id);
                        intent1.putExtra("otp",otp);
                        startActivity(intent1);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<List<CheckModel>> call, Throwable t) {

                    }
                });


            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = passEt.getText().toString();
                loginBtn.setEnabled(false);
                if (TextUtils.isEmpty(password)) {
                    password_LT.setErrorEnabled(true);
                    password_LT.setError("Please Enter Password!");
                    passEt.requestFocus();

                } else {

                    Call<List<Profile>> call = api.getData(id);
                    call.enqueue(new Callback<List<Profile>>() {
                        @Override
                        public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                            if (response.isSuccessful()) {
                                list = response.body();
                                matchpassword = list.get(0).getPassword();
                                if (password.matches(matchpassword)) {
                                    password_LT.setErrorEnabled(false);
                                    progressbar.setVisibility(View.VISIBLE);
                                    progressbar.setAnimation("car_moving.json");
                                    progressbar.playAnimation();
                                    hideKeyboardFrom(getApplicationContext());
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyRef", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", id);
                                    editor.putBoolean("loggedIn", true);
                                    editor.commit();
                                    Intent intent1 = new Intent(Password.this, MainActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent1);
                                    finish();
                                } else {
                                    password_LT.setErrorEnabled(true);
                                    password_LT.setError("Password doesn't matched!");
                                    passEt.requestFocus();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<List<Profile>> call, Throwable t) {

                        }
                    });
                }
                loginBtn.setEnabled(true);
            }
        });

    }

    private void init() {
        passEt = findViewById(R.id.passEt);
        forgotPassTv = findViewById(R.id.forgotPassTv);
        password_LT = findViewById(R.id.password_LT);
        loginBtn = findViewById(R.id.loginBtn);
        list = new ArrayList<>();
        api = ApiUtils.getUserService();
        progressbar = findViewById(R.id.progrssbar);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}
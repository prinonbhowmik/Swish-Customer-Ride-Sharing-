package com.hydertechno.swishcustomer.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {
    private TextInputEditText passEt;
    private TextView forgotPassTv;
    private TextInputLayout password_LT;
    private Button changePasswordBtn;
    private String id, password,done;
    List<Profile> list;
    private ApiInterface api;
    private LottieAnimationView progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();

        Intent intent = getIntent();
        id=intent.getStringExtra("id");

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = passEt.getText().toString();
                changePasswordBtn.setEnabled(false);
                if (TextUtils.isEmpty(password)) {
                    password_LT.setErrorEnabled(true);
                    password_LT.setError("Please Enter Password!");
                    passEt.requestFocus();
                }else{
                    Call<List<Profile>> call = api.resetPassword(id,password);
                    call.enqueue(new Callback<List<Profile>>() {
                        @Override
                        public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                            done = response.body().get(0).getDone();
                            if (done.equals("1")){
                                Intent intent1 = new Intent(ResetPassword.this,SignIn.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent1);
                                finish();

                            }
                        }

                        @Override
                        public void onFailure(Call<List<Profile>> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }

    private void init() {
        passEt = findViewById(R.id.passEt);
        forgotPassTv = findViewById(R.id.forgotPassTv);
        password_LT = findViewById(R.id.password_LT);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
        list = new ArrayList<>();
        api = ApiUtils.getUserService();
        progressbar = findViewById(R.id.progrssbar);
    }
}
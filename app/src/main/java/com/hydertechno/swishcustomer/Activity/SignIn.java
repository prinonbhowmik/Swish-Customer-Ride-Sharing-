package com.hydertechno.swishcustomer.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hydertechno.swishcustomer.Internet.Connection;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.CheckModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private TextInputEditText Phn_Et;
    private TextInputLayout phn_LT;
    private Button nextBtn;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String phone,status,otp;
    private LottieAnimationView progressbar;
    private ConnectivityReceiver connectivityReceiver;
    private IntentFilter intentFilter;
    private Snackbar snackbar;
    private RelativeLayout singInActivity;
    private ApiInterface api;
    private SharedPreferences sharedPreferences;
    private boolean loggedIn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        loggedIn = sharedPreferences.getBoolean("loggedIn",false);

        if (loggedIn==true){
            startActivity(new Intent(SignIn.this,MainActivity.class));
            finish();
        }

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        Phn_Et.setText(phone);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=Phn_Et.getText().toString();
                nextBtn.setEnabled(false);
                if (TextUtils.isEmpty(phone)) {
                    phn_LT.setErrorEnabled(true);
                    phn_LT.setError("Please Enter Phone Number");
                    Phn_Et.requestFocus();
                } else if (phone.length() != 11) {
                    phn_LT.setErrorEnabled(true);
                    phn_LT.setError("Please Provide Correct Phone Number");
                    Phn_Et.requestFocus();
                }else {
                    progressbar.setVisibility(View.VISIBLE);
                    progressbar.setAnimation("car_moving.json");
                    progressbar.playAnimation();
                    phn_LT.setErrorEnabled(false);
                    hideKeyboardFrom(getApplicationContext());
                    signin(phone);
                }
                nextBtn.setEnabled(true);

            }
        });

    }

    private void signin(final String phone) {
        Call<List<CheckModel>> call = api.checkNo(phone);
        call.enqueue(new Callback<List<CheckModel>>() {
            @Override
            public void onResponse(Call<List<CheckModel>> call, Response<List<CheckModel>> response) {
                List<CheckModel> models = new ArrayList<>();
                if (response.isSuccessful()){
                    models = response.body();
                    status = models.get(0).getStatus();
                    if (status.equals("1")){
                        startActivity(new Intent(SignIn.this,Password.class)
                                .putExtra("id",models.get(0).getCustomer_id()).putExtra("phone",phone));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        progressbar.setVisibility(View.GONE);
                        finish();
                    }
                    else if(status.equals("0")){
                        startActivity(new Intent(SignIn.this,VerificationOTP.class).putExtra("phone",phone)
                                .putExtra("otp",models.get(0).getOtp()).putExtra("check",1));
                        progressbar.setVisibility(View.GONE);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<CheckModel>> call, Throwable t) {

            }
        });
    }


    private void init() {
        Phn_Et = findViewById(R.id.phn_Et);
        phn_LT=findViewById(R.id.phn_LT);
        nextBtn=findViewById(R.id.nextBtn);
        auth = FirebaseAuth.getInstance();
        progressbar=findViewById(R.id.progrssbar);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
      // registerReceiver(connectivityReceiver, intentFilter);
        singInActivity=findViewById(R.id.singInActivity);
        api = ApiUtils.getUserService();
        sharedPreferences = getSharedPreferences("MyRef",MODE_PRIVATE);

    }
    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {
            //message = "Connected to Internet";
            if (snackbar != null) {
                snackbar.dismiss();
               // SignIn.setEnabled(true);
            }
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            message = "No internet! Please connect to network.";
            snackBar(message);
            //singin.setEnabled(false);
        }
    }

    private void snackBar(String text) {
        snackbar = Snackbar.make(findViewById(R.id.logo), text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(5000);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    protected void onResume() {

       // register connection status listener
        Connection.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}

    }
    @Override
    protected void onStop() {
        try{
            if(connectivityReceiver!=null)
                unregisterReceiver(connectivityReceiver);

        }catch(Exception e){}
        super.onStop();
    }
}
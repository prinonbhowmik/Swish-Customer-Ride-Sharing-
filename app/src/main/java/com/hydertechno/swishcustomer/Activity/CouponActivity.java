package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.CouponModel;
import com.hydertechno.swishcustomer.Model.CouponShow;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponActivity extends AppCompatActivity {
    private String userId;
    private TextInputEditText coupon_Et;
    private TextView couponCode,couponDiscount,date;
    private Button submitBtn;
    private List<CouponModel> list;
    private List<CouponShow> showList;
    private SharedPreferences sharedPreferences;
    private RelativeLayout couponLayout;
    private String msg;
    private int amount,set_coupons;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        init();

        checkConnection();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                couponLayout.setVisibility(View.GONE);
                checkConnection();

                checkCoupon();
            }
        });
        showValidCoupon();
    }
    private void showValidCoupon() {
        Call<List<CouponShow>> call = ApiUtils.getUserService().getValidCoupon(userId);
        call.enqueue(new Callback<List<CouponShow>>() {
            @Override
            public void onResponse(Call<List<CouponShow>> call, Response<List<CouponShow>> response) {
                if (response.isSuccessful()){
                    showList = response.body();
                    coupon_Et.setText("");
                    set_coupons = showList.get(0).getSetCoupons();
                    if (set_coupons==1){
                        progressBar.setVisibility(View.GONE);
                        couponLayout.setVisibility(View.VISIBLE);
                        couponCode.setText(showList.get(0).getCouponsCode());
                        couponDiscount.setText(showList.get(0).getAmount()+"%");
                        date.setText(showList.get(0).getEndDate());
                    }else {
                        return;
                    }
                }
            }
            @Override
            public void onFailure(Call<List<CouponShow>> call, Throwable t) {
            }
        });
    }

    private void checkCoupon() {
        Call<List<CouponModel>> call = ApiUtils.getUserService().checkCoupon(userId,coupon_Et.getText().toString());
        call.enqueue(new Callback<List<CouponModel>>() {
            @Override
            public void onResponse(Call<List<CouponModel>> call, Response<List<CouponModel>> response) {
                list=response.body();
                if (list.get(0).getMsg().equals("true")){
                    amount = list.get(0).getAmount();
                    showValidCoupon();
                }else{
                    Toasty.error(CouponActivity.this,"Invalid Coupon",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CouponModel>> call, Throwable t) {
            }
        });
    }

    private void init() {
        coupon_Et= findViewById(R.id.coupon_Et);
        submitBtn= findViewById(R.id.submitBtn);
        couponCode= findViewById(R.id.couponCode);
        couponLayout= findViewById(R.id.couponLayout);
        couponCode= findViewById(R.id.couponCode);
        couponDiscount= findViewById(R.id.couponDiscount);
        date= findViewById(R.id.date);
        progressBar= findViewById(R.id.progressBar);
        list = new ArrayList<>();
        showList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("MyRef",MODE_PRIVATE);
        userId = sharedPreferences.getString("id","");

    }

    public void couponBack(View view) {
        startActivity(new Intent(CouponActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CouponActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }
}
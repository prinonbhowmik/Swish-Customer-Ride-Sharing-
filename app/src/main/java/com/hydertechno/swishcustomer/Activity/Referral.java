package com.hydertechno.swishcustomer.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.NotificationModel;
import com.hydertechno.swishcustomer.Model.ReferralCount;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Referral extends AppCompatActivity {

    private String userId;
    private TextView referralTV,takenReferralTv,activeReferralTv;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        init();
        Intent intent =getIntent();
        userId = intent.getStringExtra("id");

        checkConnection();

        referralTV.setText(userId);

        getReferralCount();

        referralTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) Referral.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(referralTV.getText());
                Toast.makeText(Referral.this, "Text Copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        referralTV = findViewById(R.id.referralTV);
        takenReferralTv = findViewById(R.id.takenReferralTv);
        activeReferralTv = findViewById(R.id.activeReferralTv);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getReferralCount() {
        Call<List<ReferralCount>> call = ApiUtils.getUserService().getReffarelCount(userId);
        call.enqueue(new Callback<List<ReferralCount>>() {
            @Override
            public void onResponse(Call<List<ReferralCount>> call, Response<List<ReferralCount>> response) {
                if (response.isSuccessful()){
                    int count = response.body().get(0).getTotal();
                    int active=response.body().get(0).getActive();
                    progressBar.setVisibility(View.GONE);
                    takenReferralTv.setText(""+count);
                    activeReferralTv.setText(""+active);
                }
            }

            @Override
            public void onFailure(Call<List<ReferralCount>> call, Throwable t) {
                Log.d("problemKi",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Referral.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    public void referralBack(View view) {
        startActivity(new Intent(Referral.this,MainActivity.class));
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
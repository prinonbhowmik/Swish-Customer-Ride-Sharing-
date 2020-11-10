package com.hydertechno.swishcustomer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hydertechno.swishcustomer.Adapter.NotificationAdapter;
import com.hydertechno.swishcustomer.Model.NotificationModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private NotificationAdapter adapter;
    private List<NotificationModel> list;
    private ApiInterface api;
    private String customerId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        init();
        getData();
    }
    private void init() {
        sharedPreferences = getSharedPreferences("MyRef", Context.MODE_PRIVATE);
        customerId=sharedPreferences.getString("id","");
        list = new ArrayList<>();
        api = ApiUtils.getUserService();
        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(list,this);
        recyclerView.setAdapter(adapter);
        emptyText=findViewById(R.id.emptyNotification);
    }
    private void getData() {
        list.clear();
        Call<List<NotificationModel>> call = api.getNotificationData(customerId);
        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    adapter = new NotificationAdapter(list, NotificationsActivity.this);
                    recyclerView.setAdapter(adapter);
                    if (list.size() == 0) {
                        emptyText.setVisibility(View.VISIBLE);
                        emptyText.setText("No History");
                        recyclerView.setVisibility(View.GONE);
                    }
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
            }
        });
    }
    public void notificationBack(View view) {
        startActivity(new Intent(NotificationsActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotificationsActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}
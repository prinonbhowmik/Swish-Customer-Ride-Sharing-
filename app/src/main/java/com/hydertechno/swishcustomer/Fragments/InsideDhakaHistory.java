package com.hydertechno.swishcustomer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hydertechno.swishcustomer.Adapter.InsideDhakaHistoryAdapter;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class InsideDhakaHistory extends Fragment {

    private List<HourlyRideModel> rideModels;
    private InsideDhakaHistoryAdapter adapter;
    private RecyclerView rideRecycler;
    private String userId;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String rideStatus, driverID;
    private TextView nohistorytxt;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_inside_dhaka_history, container, false);
        init(view);

        getHistoryData();
        return view;
    }

    private void init(View view) {

        rideModels = new ArrayList<>();
        rideRecycler =view.findViewById(R.id.historyHourlyRecyclerView);
        progressBar =view.findViewById(R.id.progressBar);
        rideRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new InsideDhakaHistoryAdapter(rideModels, getContext());
        rideRecycler.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", "");
        nohistorytxt = view.findViewById(R.id.nohistorytxt);
        apiInterface = ApiUtils.getUserService();
    }


    private void getHistoryData() {

        Call<List<HourlyRideModel>> call = apiInterface.insideHistory(userId);
        call.enqueue(new Callback<List<HourlyRideModel>>() {
            @Override
            public void onResponse(Call<List<HourlyRideModel>> call, Response<List<HourlyRideModel>> response) {
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    rideModels.clear();
                    rideModels = response.body();
                    adapter = new InsideDhakaHistoryAdapter(rideModels,getContext());
                    rideRecycler.setAdapter(adapter);
                    if (rideModels.size() == 0) {
                        nohistorytxt.setVisibility(View.VISIBLE);
                        rideRecycler.setVisibility(View.GONE);
                    }
                }
                Collections.reverse(rideModels);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<HourlyRideModel>> call, Throwable t) {
            }
        });

       /* DatabaseReference historyRef = reference.child("CustomerRides").child(userId);
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    rideModels.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        rideStatus = data.child("rideStatus").getValue().toString();
                        driverID = data.child("driverId").getValue().toString();

                        if (rideStatus.equals("End")) {
                            RideModel ride = data.getValue(RideModel.class);
                            rideModels.add(ride);
                        }
                    }
                    if (rideModels.size() == 0) {
                        nohistorytxt.setVisibility(View.VISIBLE);
                        rideRecycler.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/


    }
}
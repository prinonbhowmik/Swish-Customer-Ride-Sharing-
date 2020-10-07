package com.hydertechno.swishcustomer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hydertechno.swishcustomer.Adapter.OutsideDhakaHistoryAdapter;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class OutsideDhakaHistory extends Fragment {
    private List<RideModel> rideModels;
    private OutsideDhakaHistoryAdapter adapter;
    private RecyclerView rideRecycler;
    private String userId;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String rideStatus, driverID;
    private TextView nohistorytxt;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_outside_dhaka_history, container, false);
        init(view);

        getHistoryData();

        return view;
    }

    private void init(View view) {
        rideModels = new ArrayList<>();
        rideRecycler =view.findViewById(R.id.historyRecyclerView);
        rideRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getContext().getSharedPreferences("MyRef", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", "");
        nohistorytxt = view.findViewById(R.id.nohistorytxt);
        apiInterface = ApiUtils.getUserService();

    }


    private void getHistoryData() {
        rideModels.clear();
        Call<List<RideModel>> call = apiInterface.rideHistory(userId);
        call.enqueue(new Callback<List<RideModel>>() {
            @Override
            public void onResponse(Call<List<RideModel>> call, Response<List<RideModel>> response) {
                if (response.isSuccessful()){

                    rideModels = response.body();


                    adapter = new OutsideDhakaHistoryAdapter(rideModels, getActivity());
                    rideRecycler.setAdapter(adapter);
                    if (rideModels.size() == 0) {
                        nohistorytxt.setVisibility(View.VISIBLE);
                        rideRecycler.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<List<RideModel>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_LONG).show();
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
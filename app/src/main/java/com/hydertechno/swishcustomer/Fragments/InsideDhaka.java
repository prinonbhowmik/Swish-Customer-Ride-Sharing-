package com.hydertechno.swishcustomer.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hydertechno.swishcustomer.Adapter.HourlyRideAdapter;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class InsideDhaka extends Fragment {

    private TextView title_text,nodatatxt;
    private ImageView backBtn, rideType;
    private List<HourlyRideModel> rideModels;
    private HourlyRideAdapter adapter;
    private RecyclerView rideRecycler;
    private String userId;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String rideStatus;
    Date eDate = null;
    Date currentDate=null;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_dhaka, container, false);

        init(view);

        upcomingRides();


        return view;
    }

    private void upcomingRides() {
        DatabaseReference showRef = reference.child("CustomerHourRides").child(userId);
        showRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    rideModels.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        rideStatus=data.child("rideStatus").getValue().toString();
                        String date1 = data.child("pickUpDate").getValue().toString();
                        String time1 = data.child("pickUpTime").getValue().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
                        String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa").format(Calendar.getInstance().getTimeInMillis());

                        if (!rideStatus.equals("End")) {
                            HourlyRideModel ride = data.getValue(HourlyRideModel.class);
                            rideModels.add(ride);
                            if(rideModels.size()==0){
                                nodatatxt.setVisibility(View.VISIBLE);
                                rideRecycler.setVisibility(View.GONE);
                            }else {
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }
                    Collections.reverse(rideModels);
                    adapter.notifyDataSetChanged();
                }
                if(rideModels.size()<1){
                    nodatatxt.setVisibility(View.VISIBLE);
                    nodatatxt.setText("No Request Available");
                }else {
                    nodatatxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(View view) {
        backBtn = view.findViewById(R.id.backBtn);
        //rideType = findViewById(R.id.rideType);
        nodatatxt= view.findViewById(R.id.nodatatxt);
        rideModels = new ArrayList<>();
        rideRecycler =  view.findViewById(R.id.rideRecycler);
        rideRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HourlyRideAdapter(rideModels, getContext());
        rideRecycler.setAdapter(adapter);
        sharedPreferences = getContext().getSharedPreferences("MyRef",MODE_PRIVATE);
        userId = sharedPreferences.getString("id","");
        reference = FirebaseDatabase.getInstance().getReference();

    }

}
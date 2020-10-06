package com.hydertechno.swishcustomer.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnGoingDriverDetailsBottom extends BottomSheetDialogFragment {
    private View view;
    private LottieAnimationView animation;
    private TextView driverNameTV,plateTV,pickupPlaceTV, destinationTV, pickupTimeTV, takaTV;
    private String drivername,driverId,pickTime,price,pickup,destination;
    private ApiInterface api;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.on_going_driver_details, container, false);

        init(view);

        Bundle mArgs = getArguments();
        driverId = mArgs.getString("driverId");
        pickTime = mArgs.getString("pickTime");
        price = mArgs.getString("price");
        pickup = mArgs.getString("pickup");
        destination = mArgs.getString("destination");

        Call<List<DriverProfile>> call = api.getDriverData(driverId);
        call.enqueue(new Callback<List<DriverProfile>>() {
            @Override
            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                List<DriverProfile> list = response.body();
                drivername = list.get(0).getFull_name();
                driverNameTV.setText(drivername);
            }

            @Override
            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {

            }
        });


       /* DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("DriversProfile").child(driverId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drivername = snapshot.child("name").getValue().toString();
                driverNameTV.setText(drivername);
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("RegisteredDrivers").child(driverId);
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String model,plate;

                        model=snapshot.child("carModel").getValue().toString();
                        plate=snapshot.child("plateNumber").getValue().toString();
                        plateTV.setText(model+" "+plate);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/



        // plateTV.setText(pickTime);
        pickupPlaceTV.setText(pickup);
        destinationTV.setText(destination);
        pickupTimeTV.setText(pickTime);
        takaTV.setText("৳ " +price);

    return view;
    }

    private void init(View view) {
        driverNameTV = view.findViewById(R.id.driverNameTV);
        plateTV = view.findViewById(R.id.driver_PlateTV);
        pickupPlaceTV = view.findViewById(R.id.pickupPlaceTV);
        destinationTV = view.findViewById(R.id.destinationTV);
        pickupTimeTV = view.findViewById(R.id.pickupTimeTV);
        takaTV = view.findViewById(R.id.takaTV);
        api = ApiUtils.getUserService();

    }

}

package com.hydertechno.swishcustomer.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.DriverInfo;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;

import java.util.List;

import es.dmoral.toasty.Toasty;
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

        checkConnection();

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

        Call<List<DriverInfo>> call1 = api.getCarNumber(driverId);
        call1.enqueue(new Callback<List<DriverInfo>>() {
            @Override
            public void onResponse(Call<List<DriverInfo>> call, Response<List<DriverInfo>> response) {

                String platenum = response.body().get(0).getPlate_number();

                plateTV.setText(platenum);

            }
            @Override
            public void onFailure(Call<List<DriverInfo>> call, Throwable t) {

            }
        });

        // plateTV.setText(pickTime);
        pickupPlaceTV.setText(pickup);
        destinationTV.setText(destination);
        pickupTimeTV.setText(pickTime);
        takaTV.setText("৳ " +price);

        animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "999"));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Toasty.error(getContext(), "" + activityException.getMessage(), Toasty.LENGTH_SHORT).show();
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });


        return view;
    }

    private void init(View view) {
        driverNameTV = view.findViewById(R.id.driverNameTV);
        plateTV = view.findViewById(R.id.driver_PlateTV);
        pickupPlaceTV = view.findViewById(R.id.pickupPlaceTV);
        destinationTV = view.findViewById(R.id.destinationTV);
        pickupTimeTV = view.findViewById(R.id.pickupTimeTV);
        takaTV = view.findViewById(R.id.takaTV);
        animation = view.findViewById(R.id.sos);
        api = ApiUtils.getUserService();

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

}

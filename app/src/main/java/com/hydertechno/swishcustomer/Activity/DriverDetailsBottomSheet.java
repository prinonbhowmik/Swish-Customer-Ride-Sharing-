package com.hydertechno.swishcustomer.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hydertechno.swishcustomer.Model.DriverInfo;
import com.hydertechno.swishcustomer.Model.DriverProfile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.hydertechno.swishcustomer.Utils.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDetailsBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private TextView driverNameTV, driverPhoneTV,carTV,plateTV,rideCountTV,rideTxt;

    private Button callDriver;
    private DatabaseReference databaseReference;
    private String bookingId;
    private String carType;
    private String driverName;
    private String driverPhone;
    private String driverId;
    private String image;
    private int check;
    private CircleImageView userImage;
    private ImageView phoneIV;
    private TextView phoneTxt;
    private NeomorphFrameLayout card_view2;
    private RatingBar driverRatingBar;
    private List<DriverProfile> list;
    private ApiInterface api;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_driver_details_bottom_sheet, container, false);
        init(view);
        Bundle mArgs = getArguments();
        driverId = mArgs.getString("id");
        check=mArgs.getInt("check");

        if(check==2){
            callDriver.setVisibility(View.GONE);
            phoneTxt.setVisibility(View.GONE);
            phoneIV.setVisibility(View.GONE);
            driverPhoneTV.setVisibility(View.GONE);
            card_view2.setVisibility(View.GONE);
        }


        getData();



        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "+88" + driverPhoneTV.getText().toString()));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Toasty.error(getContext(), "" + activityException.getMessage(), Toasty.LENGTH_SHORT).show();
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        return view;
    }

    private void getData() {


        Call<List<DriverProfile>> call = api.getDriverData(driverId);
        call.enqueue(new Callback<List<DriverProfile>>() {
            @Override
            public void onResponse(Call<List<DriverProfile>> call, Response<List<DriverProfile>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                   if (!list.get(0).getImage().equals("")){
                       Picasso.get().load(Config.DRIVER_IMAGE_LINE+list.get(0).getImage()).into(userImage, new com.squareup.picasso.Callback() {
                           @Override
                           public void onSuccess() {}
                           @Override
                           public void onError(Exception e) {
                               Log.d("kiKahini", e.getMessage());
                           }
                       });
                   }
                    driverNameTV.setText(list.get(0).getFull_name());
                    driverPhoneTV.setText(list.get(0).getPhone());

                    int sRideCount=list.get(0).getRideCount();
                    float sRating=list.get(0).getRating();
                    int sRatingCount=list.get(0).getRatingCount();
                    float rat=sRating/sRatingCount;
                    if(sRideCount<6){
                        driverRatingBar.setVisibility(View.GONE);
                        rideTxt.setVisibility(View.GONE);
                        rideCountTV.setVisibility(View.GONE);
                    }
                    driverRatingBar.setRating(rat);
                    rideCountTV.setText(String.valueOf(list.get(0).getRideCount()));
                }
            }
            @Override
            public void onFailure(Call<List<DriverProfile>> call, Throwable t) {
                Log.d("kahiniKi", t.getMessage());
            }
        });


        Call<List<DriverInfo>> call1 = api.getCarNumber(driverId);
        call1.enqueue(new Callback<List<DriverInfo>>() {
            @Override
            public void onResponse(Call<List<DriverInfo>> call, Response<List<DriverInfo>> response) {
                String carname = response.body().get(0).getCar_name();
                String carmodel = response.body().get(0).getCar_model();
                String platenum = response.body().get(0).getPlate_number();

                carTV.setText(carname+" "+carmodel);
                plateTV.setText(platenum);

            }

            @Override
            public void onFailure(Call<List<DriverInfo>> call, Throwable t) {

            }
        });





    }

    private void init(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        driverNameTV = view.findViewById(R.id.driverNameTV);
        driverPhoneTV = view.findViewById(R.id.driver_PhoneTV);
        carTV = view.findViewById(R.id.driver_CarTV);
        plateTV = view.findViewById(R.id.driver_PlateTV);
        callDriver = view.findViewById(R.id.callDriverBtn);
        userImage = view.findViewById(R.id.profileIV);

        phoneIV=view.findViewById(R.id.phoneIV);
        phoneTxt=view.findViewById(R.id.phoneTxt);
        card_view2=view.findViewById(R.id.card_view2);
        driverRatingBar=view.findViewById(R.id.driverRatingBar);
        rideCountTV=view.findViewById(R.id.rideCountTxt);
        rideTxt=view.findViewById(R.id.rideTxt);
        list = new ArrayList<>();
        api = ApiUtils.getUserService();
    }
}
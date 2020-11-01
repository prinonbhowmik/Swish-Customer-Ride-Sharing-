package com.hydertechno.swishcustomer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.hydertechno.swishcustomer.Activity.HourlyRideDetails;
import com.hydertechno.swishcustomer.Model.HourlyRideModel;
import com.hydertechno.swishcustomer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HourlyRideAdapter extends RecyclerView.Adapter<HourlyRideAdapter.ViewHolder> {

    private List<HourlyRideModel> ride;
    private Context context;

    public HourlyRideAdapter(List<HourlyRideModel> ride, Context context) {
        this.ride = ride;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_hourly_list_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyRideAdapter.ViewHolder holder, int position) {
        HourlyRideModel rideModel = ride.get(position);

        holder.DateTv.setText(rideModel.getPickUpDate());
        holder.TimeTV.setText(rideModel.getPickUpTime());
        holder.startLocation.setText(rideModel.getPickUpPlace());
        holder.carType.setText(rideModel.getCarType());
        holder.ridePrice.setText(rideModel.getPrice());
        holder.bookingStatus.setText(rideModel.getBookingStatus());
        String status = holder.bookingStatus.getText().toString();

        if (status.equals("Pending")) {
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_blue));
        } else {
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_green));
        }

        String date1 = holder.DateTv.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date eDate = null;
        try {
            eDate = dateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (rideModel.getRideStatus().equals("End")) {
            holder.bookingStatus.setText("Ride Finished");
            //holder.relativeLayout1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorTextSecondary));
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, HourlyRideDetails.class);
                    intent.putExtra("bookingId", rideModel.getBookingId());
                    intent.putExtra("check", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ride.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayout1;
        private TextView startLocation, TimeTV, DateTv, carType, ridePrice, bookingStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startLocation = itemView.findViewById(R.id.startLocation);
            TimeTV = itemView.findViewById(R.id.rideTime);
            DateTv = itemView.findViewById(R.id.rideDate);
            carType = itemView.findViewById(R.id.rideType);
            ridePrice = itemView.findViewById(R.id.ridePrice);
            bookingStatus = itemView.findViewById(R.id.status);
            relativeLayout1 = itemView.findViewById(R.id.relative1);
        }
    }
}

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

public class InsideDhakaHistoryAdapter extends RecyclerView.Adapter<InsideDhakaHistoryAdapter.ViewHolder> {

    private List<HourlyRideModel> hourly;
    private Context context;

    public InsideDhakaHistoryAdapter(List<HourlyRideModel> hourly, Context context) {
        this.hourly = hourly;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.indide_history_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         HourlyRideModel rideModel= hourly.get(position);

        holder.DateTv.setText(rideModel.getPickUpDate());
        holder.TimeTV.setText(rideModel.getPickUpTime());
        holder.startLocation.setText(rideModel.getPickUpPlace());
        holder.carType.setText(rideModel.getCarType());
        holder.ridePrice.setText(rideModel.getPrice());
        holder.bookingStatus.setText(rideModel.getBookingStatus());
        String status = holder.bookingStatus.getText().toString();

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
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_gray));
        }
        if (!rideModel.getRideStatus().equals("End")) {
            holder.bookingStatus.setText("Ride Expire");
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, HourlyRideDetails.class);
                    intent.putExtra("bookingId", rideModel.getBookingId());
                    intent.putExtra("check",2);
                    intent.putExtra("pickplace",rideModel.getPickUpPlace());
                    intent.putExtra("pickdate",rideModel.getPickUpDate());
                    intent.putExtra("picktime",rideModel.getPickUpTime());
                    intent.putExtra("cartype",rideModel.getCarType());
                    intent.putExtra("price",rideModel.getPrice());
                    intent.putExtra("custId",rideModel.getDriverId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hourly.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private RelativeLayout relativeLayout1;
        private TextView startLocation,TimeTV, DateTv, carType, ridePrice, bookingStatus;
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

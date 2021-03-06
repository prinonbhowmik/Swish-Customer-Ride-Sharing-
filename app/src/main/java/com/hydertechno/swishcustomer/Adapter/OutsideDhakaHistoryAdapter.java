package com.hydertechno.swishcustomer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.hydertechno.swishcustomer.Activity.MyRidesDetails;
import com.hydertechno.swishcustomer.Model.RideModel;
import com.hydertechno.swishcustomer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OutsideDhakaHistoryAdapter extends RecyclerView.Adapter<OutsideDhakaHistoryAdapter.ViewHolder> {

    private List<RideModel> ride;
    private Context context;
    private String car_type,carType;
    public OutsideDhakaHistoryAdapter(List<RideModel> ride, Context context) {
        this.ride = ride;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rides_history_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RideModel rideModel = ride.get(position);

        holder.DateTv.setText(rideModel.getPickUpDate());
        holder.TimeTV.setText(rideModel.getPickUpTime());
        holder.startLocation.setText(rideModel.getPickUpPlace());
        holder.endLocation.setText(rideModel.getDestinationPlace());
        holder.ridePrice.setText(rideModel.getPrice());
        holder.bookingStatus.setText(rideModel.getBookingStatus());
        holder.ratingBar.setRating(rideModel.getRating());
        String status = holder.bookingStatus.getText().toString();

        car_type=rideModel.getCarType();
        switch (car_type) {
            case "Sedan":
                carType="Sedan";
                break;
            case "SedanPremiere":
                carType="Sedan Premiere";
                break;
            case "SedanBusiness":
                carType="Sedan Business";
                break;
            case "Micro7":
                carType="Micro 7";
                break;
            case "Micro11":
                carType="Micro 11";
                break;
        }
        holder.carTypeTv.setText(carType);

        String date1 = holder.DateTv.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date eDate = null;
        try {
            eDate = dateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (rideModel.getRideStatus().equals("End")) {
            holder.bookingStatus.setText("Ride Finished");
            //holder.relativeLayout1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorTextSecondary));
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_blue));
        }
        else if(rideModel.getRideStatus().equals("Cancel")){
            holder.bookingStatus.setText("Ride Cancelled");
            //holder.relativeLayout1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorTextSecondary));
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_red));
        }
        else {
            holder.bookingStatus.setText("Ride Expire");
            //holder.relativeLayout1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorTextSecondary));
            holder.relativeLayout1.setBackground(ContextCompat.getDrawable(context, R.drawable.my_ride_status_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, MyRidesDetails.class);
                    intent.putExtra("bookingId", rideModel.getBookingId());
                    intent.putExtra("check",2);
                    intent.putExtra("tripId",rideModel.getBookingId());
                    intent.putExtra("pickplace",rideModel.getPickUpPlace());
                    intent.putExtra("desplace",rideModel.getDestinationPlace());
                    intent.putExtra("pickdate",rideModel.getPickUpDate());
                    intent.putExtra("picktime",rideModel.getPickUpTime());
                    intent.putExtra("cartype",carType);
                    intent.putExtra("payment",rideModel.getPaymentType());
                    intent.putExtra("discount",rideModel.getDiscount());
                    intent.putExtra("totalTime",rideModel.getTotalTime());
                    intent.putExtra("totalDistance",rideModel.getTotalDistance());
                    intent.putExtra("price",rideModel.getPrice());
                    intent.putExtra("finalPrice",rideModel.getFinalPrice());
                    intent.putExtra("custId",rideModel.getDriverId());
                    intent.putExtra("rideStatus",rideModel.getRideStatus());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        private TextView startLocation, endLocation, TimeTV, DateTv, carTypeTv, ridePrice, bookingStatus;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            TimeTV = itemView.findViewById(R.id.rideTime);
            DateTv = itemView.findViewById(R.id.rideDate);
            carTypeTv = itemView.findViewById(R.id.rideType);
            ridePrice = itemView.findViewById(R.id.ridePrice);
            bookingStatus = itemView.findViewById(R.id.status);
            relativeLayout1 = itemView.findViewById(R.id.relative1);
        }
    }
}

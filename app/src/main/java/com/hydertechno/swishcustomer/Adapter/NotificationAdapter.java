package com.hydertechno.swishcustomer.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hydertechno.swishcustomer.Model.NotificationModel;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.Utils.Config;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notificationModels;
    private Context context;

    public NotificationAdapter(List<NotificationModel> notificationModels, Context context) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout_design, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = notificationModels.get(position);
        holder.title.setText(model.getTitle());
        holder.body.setText(model.getBody());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());
        String imageFile = model.getImage();
        Picasso.get().load(Config.NOTIFICATION_LINE + imageFile).into(holder.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Log.d("kiKahini", e.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title, body, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notificationLargeImage);
            title = itemView.findViewById(R.id.notificationTitle);
            body = itemView.findViewById(R.id.notificationBody);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);

        }
    }
}

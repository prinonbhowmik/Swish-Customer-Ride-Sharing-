package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hydertechno.swishcustomer.Internet.ConnectivityReceiver;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.hydertechno.swishcustomer.Utils.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String userId, name, email, phone, image,gender,dob;
    private TextView  nametv, emailtv, phonetv,genderTv;
    private CircleImageView userImage,editBtn;
    private List<Profile> list;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FrameLayout frame_layout1;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();

        checkConnection();

        userId = sharedPreferences.getString("id","");

        Call<List<Profile>> call = apiInterface.getData(userId);
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.isSuccessful()){
                    list = response.body();

                    Picasso.get().load(Config.IMAGE_LINE+list.get(0).getImage())
                            .into(userImage, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {}
                                @Override
                                public void onError(Exception e) {
                                    Log.d("kiKahini", e.getMessage());
                                }
                            });
                    nametv.setText(list.get(0).getName());
                    emailtv.setText(list.get(0).getEmail());
                    phonetv.setText(list.get(0).getPhone());
                    genderTv.setText(list.get(0).getGender());
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserProfile.this, EditProfile.class);
                intent.putExtra("id",userId);
                intent.putExtra("name", nametv.getText());
                intent.putExtra("email",emailtv.getText());
                intent.putExtra("gender",genderTv.getText());
                startActivity(intent);

            }
        });
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("profile");
        nametv = findViewById(R.id.nameTv);
        sharedPreferences = getSharedPreferences("MyRef",MODE_PRIVATE);
        emailtv = findViewById(R.id.emailTv);
        phonetv = findViewById(R.id.phoneTv);
        phonetv = findViewById(R.id.phoneTv);
        genderTv = findViewById(R.id.genderTv);
        userImage = findViewById(R.id.profileIV);
        editBtn = findViewById(R.id.editBtn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        frame_layout1 = findViewById(R.id.frame_layout1);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        apiInterface = ApiUtils.getUserService();
        list= new ArrayList<>();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserProfile.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    public void backPress(View view) {
        startActivity(new Intent(UserProfile.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }
}
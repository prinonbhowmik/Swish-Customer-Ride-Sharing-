package com.hydertechno.swishcustomer.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    private EditText nameEt, emailEt, genderEt;
    private CircleImageView updateBtn;
    private CircleImageView imageView;
    private int SPLASH_TIME_OUT2 =5000;
    private static int SPLASH_TIME_OUT=1000;
    private List<Profile> list;
    private FrameLayout frameLayout;
    private String userId, name, email, phone, gender, image, password,g;
    private String name1, email1, gender1, phone1, password1;
    private DatabaseReference reference;
    private Uri imageUri;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private LottieAnimationView progressBar;
    private ApiInterface apiInterface;
    private RadioGroup genderGroup;
    private RadioButton radioMale,radioFemale,radioOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();

        checkConnection();

        Intent intent = getIntent();
        userId = intent.getStringExtra("id");
        name1 = intent.getStringExtra("name");
        email1 = intent.getStringExtra("email");
        gender1 = intent.getStringExtra("gender");
        nameEt.setText(name1);
        emailEt.setText(email1);

        if(gender1.equals("Male")){
            radioMale.setChecked(true);
        }if(gender1.equals("Female")){
            radioFemale.setChecked(true);
        }if(gender1.equals("Other")){
            radioOther.setChecked(true);
        }

        genderEt.setText(gender1);

        hideKeyboardFrom(getApplicationContext());

        getUserInformation();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioMale:
                        gender = "Male";
                        break;
                    case R.id.radioFemale:
                        gender = "Female";
                        break;
                    case R.id.radioOther:
                        gender = "Other";
                        break;
                }
                 genderEt.setText(gender);

            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfile.this);
            }
        });

        nameEt.addTextChangedListener(textWatcher);
        emailEt.addTextChangedListener(textWatcher);
        genderEt.addTextChangedListener(textWatcher);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
                name = nameEt.getText().toString();
                email = emailEt.getText().toString();
                //gender = genderEt.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    nameEt.setError("Enter name");
                    nameEt.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    emailEt.setError("Enter email");
                    emailEt.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEt.setError("Enter valid email");
                    emailEt.requestFocus();
                } else {
                    g= genderEt.getText().toString();
                    updateInformation(name, email, g);
                }


            }
        });

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected){
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String updateName = nameEt.getText().toString().trim();
            String updateEmail = emailEt.getText().toString().trim();
            String updateGender = genderEt.getText().toString().trim();
            if (!updateName.equals(name1) || !updateEmail.equals(email1) || !updateGender.equals(gender1)) {
                updateBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick_green));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void updateInformation(String name, String email, String gender) {

        progressBar.setVisibility(View.VISIBLE);
        hideKeyboardFrom(getApplicationContext());

        if (imageUri!=null){
            File file = new File(imageUri.getPath());

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);


            RequestBody  fullName = RequestBody .create(MediaType.parse("text/plain"), name);
            RequestBody  idbody = RequestBody .create(MediaType.parse("text/plain"), userId);
            RequestBody  emailBody = RequestBody .create(MediaType.parse("text/plain"), email);
            RequestBody  genderbody = RequestBody .create(MediaType.parse("text/plain"), gender);


            Call<List<Profile>> call = apiInterface.updateData(idbody,body,fullName,emailBody,genderbody);
            call.enqueue(new Callback<List<Profile>>() {
                @Override
                public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                    if (response.isSuccessful()){
                        if (response.body().get(0).getDone().equals("1")){
                            Toasty.success(EditProfile.this,"Update Success", Toasty.LENGTH_SHORT).show();
                            startActivity(new Intent(EditProfile.this,UserProfile.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            finish();
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Profile>> call, Throwable t) {
                    Log.d("Dekhbo", t.getMessage());
                }
            });


        }
        else if (imageUri==null){
            RequestBody  fullName = RequestBody .create(MediaType.parse("text/plain"), name);
            RequestBody  idbody = RequestBody .create(MediaType.parse("text/plain"), userId);
            RequestBody  emailBody = RequestBody .create(MediaType.parse("text/plain"), email);
            RequestBody  genderbody = RequestBody .create(MediaType.parse("text/plain"), gender);

            Call<List<Profile>> call = apiInterface.updateDatawithoutimage(idbody,fullName,emailBody,genderbody);
            call.enqueue(new Callback<List<Profile>>() {
                @Override
                public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                    if (response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<List<Profile>> call, Throwable t) {

                }
            });


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toasty.success(EditProfile.this,"Update Success", Toasty.LENGTH_SHORT).show();
                    startActivity(new Intent(EditProfile.this,UserProfile.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            },SPLASH_TIME_OUT);

        }
    }

    private void getUserInformation() {

        Call<List<Profile>> call = apiInterface.getData(userId);
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    Picasso.get().load(Config.IMAGE_LINE + list.get(0).getImage())
                            .into(imageView, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.d("kiKahini", e.getMessage());
                                }
                            });

                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

            }
        });

    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        nameEt = findViewById(R.id.name_Et);
        emailEt = findViewById(R.id.email_Et);
        genderEt = findViewById(R.id.gender_Et);
        imageView = findViewById(R.id.profileIV);
        updateBtn = findViewById(R.id.updateBtn);

        userId = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("profile");
        frameLayout = findViewById(R.id.frame_layout1);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progrssbar);

        genderGroup=findViewById(R.id.radio);
        radioMale=findViewById(R.id.radioMale);
        radioFemale=findViewById(R.id.radioFemale);
        radioOther=findViewById(R.id.radioOther);
        apiInterface = ApiUtils.getUserService();
        list = new ArrayList<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri = resultUri;
                imageView.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //startActivity(new Intent(EditProfile.this,UserProfile.class));
    }
}
package com.hydertechno.swishcustomer.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.hydertechno.swishcustomer.Model.Profile;
import com.hydertechno.swishcustomer.R;
import com.hydertechno.swishcustomer.ServerApi.ApiInterface;
import com.hydertechno.swishcustomer.ServerApi.ApiUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText nameEt,emailEt,passEt,phoneEt;
    private RadioGroup genderGroup;
    private String name,email;
    private ImageView userImage;
    private Button loginBtn;
    private Uri imageUri;
    private String phone,gender="Male",password;
    private LottieAnimationView progressbar;
    private CheckBox terms;
    private TextView conditions;
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        phoneEt.setText(phone);

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
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(SignUp.this);

            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(terms.isChecked()){
                    terms.setTextColor(getResources().getColor(R.color.blue));
                    conditions.setTextColor(Color.BLUE);

                }else
                    terms.setTextColor(getResources().getColor(R.color.black));
                conditions.setTextColor(Color.BLACK);

            }
        });
        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,WebViewActivity.class);
                intent.putExtra("link","https://swish.com.bd/terms-and-conditions");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEt.getText().toString();
                email = emailEt.getText().toString();
                password = passEt.getText().toString();

                if (TextUtils.isEmpty(name)){
                    nameEt.setError("Enter name");
                    nameEt.requestFocus();
                }
                else if (TextUtils.isEmpty(email)) {
                    emailEt.setError("Enter email");
                    emailEt.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEt.setError("Enter valid email");
                    emailEt.requestFocus();
                } else if (TextUtils.isEmpty(name)) {
                    nameEt.setError("Enter User name");
                    nameEt.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passEt.setError("Enter Password!");
                    passEt.requestFocus();
                } else if (passEt.length() < 6) {
                    passEt.setError("At least 6 characters!", null);
                    passEt.requestFocus();
               }else if(!terms.isChecked()){
                    Toasty.info(SignUp.this,"Agree terms and conditions.",Toasty.LENGTH_SHORT).show();
                }else{
                    progressbar.setVisibility(View.VISIBLE);
                    hideKeyboardFrom(getApplicationContext());
                    progressbar.setAnimation("car_moving.json");
                    progressbar.playAnimation();
                    signup(name,email,password,phone,gender);
                }

            }
        });
    }

    private void signup(final String name, final String email, final String password, final String phone,final String gender) {

        loginBtn.setEnabled(false);

        File file = new File(imageUri.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody  fullName = RequestBody .create(MediaType.parse("text/plain"), name);
        RequestBody  genderbody = RequestBody .create(MediaType.parse("text/plain"), gender);
        RequestBody  emailBody = RequestBody .create(MediaType.parse("text/plain"), email);
        RequestBody  phoneBody = RequestBody .create(MediaType.parse("text/plain"), phone);
        RequestBody  passBody = RequestBody .create(MediaType.parse("text/plain"), password);
        RequestBody  rem_tokenBody = RequestBody .create(MediaType.parse("text/plain"), "");
        RequestBody  tokenBody = RequestBody .create(MediaType.parse("text/plain"), "");
        RequestBody  referralBody = RequestBody .create(MediaType.parse("text/plain"), "");

        Call<List<Profile>> call = api.register(emailBody,body,fullName,passBody,phoneBody,genderbody,
                rem_tokenBody,tokenBody,100,referralBody);
       call.enqueue(new Callback<List<Profile>>() {
           @Override
           public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {

           }

           @Override
           public void onFailure(Call<List<Profile>> call, Throwable t) {

           }
       });

        Toast.makeText(SignUp.this, "Registration Complete", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUp.this,SignIn.class);
        intent.putExtra("phone",phone);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        loginBtn.setEnabled(true);
        progressbar.setVisibility(View.GONE);
        finish();
    }

    private void init() {
        userImage = findViewById(R.id.userImage);
        nameEt=findViewById(R.id.name_Et);
        emailEt=findViewById(R.id.email_Et);
        passEt = findViewById(R.id.password_Et);
        phoneEt = findViewById(R.id.phoneEt);
        genderGroup=findViewById(R.id.radio);
        loginBtn=findViewById(R.id.loginBtn);
        api = ApiUtils.getUserService();
        progressbar=findViewById(R.id.progrssbar);
        terms=findViewById(R.id.termsCheckBox);
        conditions=findViewById(R.id.conditions);

    }

    private void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                if (resultUri!=null) {
                    imageUri = resultUri;
                    userImage.setImageURI(imageUri);

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
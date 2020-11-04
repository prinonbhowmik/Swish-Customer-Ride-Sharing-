package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.hydertechno.swishcustomer.R;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    Animation logoanim;
    LottieAnimationView progressbar;
    private  static int splash_time_out=2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        logo = findViewById(R.id.logo);
        progressbar=findViewById(R.id.progrssbar);

        logoanim = AnimationUtils.loadAnimation(this,R.anim.blink);

        logo.setAnimation(logoanim);

        progressbar.setAnimation("car_moving.json");
        progressbar.playAnimation();

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
               overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
               finish();
           }
       },splash_time_out);
    }
}
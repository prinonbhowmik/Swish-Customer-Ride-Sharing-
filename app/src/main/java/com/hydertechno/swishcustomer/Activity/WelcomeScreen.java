package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.hydertechno.swishcustomer.Adapter.OnboardingAdapter;
import com.hydertechno.swishcustomer.Model.OnboardingItem;
import com.hydertechno.swishcustomer.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeScreen extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private ViewPager2 onboardingViewPager;
    private LinearLayout layoutOnboardingIndicators;
    private Button buttonOnboardingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);


        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        setupOnboardingItems();
        setupOnboardingIndicator();
        onboardingViewPager.setAdapter(onboardingAdapter);
        setCurrentOnboardingIndicator(0);

        if (restorePrefdata()) {
            startActivity( new Intent(WelcomeScreen.this, SignIn.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem()+1);
                }else{
                    startActivity( new Intent(WelcomeScreen.this, SignIn.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    savePrefData();
                    finish();
                }
            }
        });

    }//onCreate ends

    private boolean restorePrefdata() {
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpened=sharedPreferences.getBoolean("isIntroOpen",false);
        return isIntroActivityOpened;
    }

    private void savePrefData() {
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("isIntroOpen",true);
        editor.commit();
    }

    private void setupOnboardingItems(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();
        OnboardingItem itemPayOnline = new OnboardingItem();
        itemPayOnline.setImage(R.drawable.welcome_screen1);
        itemPayOnline.setTitle("Book Your Car");
        itemPayOnline.setDescription("Book your car when you need to travel.");

        OnboardingItem itemOnTheWay = new OnboardingItem();
        itemOnTheWay.setImage(R.drawable.welcome_screen2);
        itemOnTheWay.setTitle("Pick Up");
        itemOnTheWay.setDescription("Driver will pick you up from your place.");


        OnboardingItem itemEatTogether = new OnboardingItem();
        itemEatTogether.setImage(R.drawable.welcome_screen3);
        itemEatTogether.setTitle("Drop Off");
        itemEatTogether.setDescription("Driver will drop you off at your destination.");

        onboardingItems.add(itemPayOnline);
        onboardingItems.add(itemOnTheWay);
        onboardingItems.add(itemEatTogether);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);


    }

    private void setupOnboardingIndicator(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);

        for (int i=0; i<indicators.length;i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i=0; i<childCount; i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            }else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }

        if (index == onboardingAdapter.getItemCount()-1){
            buttonOnboardingAction.setText("Start");
        }else{
            buttonOnboardingAction.setText("Next");
        }
    }
}
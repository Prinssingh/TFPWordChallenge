package com.tfp.coding.wordchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tfp.coding.wordchallenge.Utils.SharedPrefs;

public class SettingsActivity extends AppCompatActivity {
    SharedPrefs sharedPrefs;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch soundEffect,darkMode;
    TextView beginnerScore,mediumScore,advanceScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPrefs = new SharedPrefs(this);
        initView();
        setupBannerAd();
    }
    private void initView(){
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        soundEffect= findViewById(R.id.soundEffect);
        soundEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.setSound(isChecked);
            }
        });
        soundEffect.setChecked(sharedPrefs.getSoundPref());

        darkMode= findViewById(R.id.darkMode);
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.setDarkMode(isChecked);
                darkModeActivate(isChecked);
            }
        });
        darkMode.setChecked(sharedPrefs.getDarkModePref());


        beginnerScore = findViewById(R.id.beginnerScore);
        beginnerScore.setText(String.valueOf(sharedPrefs.getBeginnerLevelScore()));

        mediumScore = findViewById(R.id.mediumScore);
        mediumScore.setText(String.valueOf(sharedPrefs.getMediumLevelScore()));

        advanceScore = findViewById(R.id.advanceScore);
        advanceScore.setText(String.valueOf(sharedPrefs.getAdvanceLevelScore()));

    }

    private void darkModeActivate(boolean active){
        if(active){
            //Switch to Dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            // switch to Light Mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void setupBannerAd(){
        AdView adview = findViewById(R.id.banner_ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
    }
}
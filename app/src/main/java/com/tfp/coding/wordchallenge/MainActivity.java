package com.tfp.coding.wordchallenge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.card.MaterialCardView;
import com.tfp.coding.wordchallenge.Utils.SharedPrefs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView settings;
    Button beginnerLevel, mediumLevel, advanceLevel;
    MaterialCardView adCoins;
    TextView coins;
    AdView banner_ad_view;
    SharedPrefs sharedPrefs;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs = new SharedPrefs(this);
        initView();

        MobileAds.initialize(this, initializationStatus -> {
            AdRequest adRequest = new AdRequest.Builder().build();
            banner_ad_view.loadAd(adRequest);

            createRewardedVideoAd();
        });


    }

    private void initView() {
        banner_ad_view = findViewById(R.id.banner_ad_view);
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(this);

        beginnerLevel = findViewById(R.id.beginnerLevel);
        beginnerLevel.setOnClickListener(this);

        mediumLevel = findViewById(R.id.mediumLevel);
        mediumLevel.setOnClickListener(this);

        advanceLevel = findViewById(R.id.advanceLevel);
        advanceLevel.setOnClickListener(this);

        coins = findViewById(R.id.coins);
        coins.setText(String.valueOf(sharedPrefs.getCoins()));
        adCoins = findViewById(R.id.adCoins);
        adCoins.setOnClickListener(this);
        adCoins.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (v == beginnerLevel) {

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("level", 0);
            startActivity(intent);
        } else if (v == mediumLevel) {

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("level", 1);
            startActivity(intent);
        } else if (v == advanceLevel) {

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("level", 2);
            startActivity(intent);
        } else if (v == adCoins) {
            showRewardedVideoAd();
            Toast.makeText(this, "Play Ads", Toast.LENGTH_SHORT).show();
        }
    }

    private void createRewardedVideoAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.admobe_rewarded_ad_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        adCoins.setVisibility(View.VISIBLE);
                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d("TAG", "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d("TAG", "Ad dismissed fullscreen content.");
                                rewardedAd = null;
                                createRewardedVideoAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e("TAG", "Ad failed to show fullscreen content.");
                                rewardedAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d("TAG", "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("TAG", "Ad showed fullscreen content.");
                            }
                        });
                    }
                });


    }

    private void showRewardedVideoAd() {
        if (rewardedAd != null) {
            Activity activityContext = MainActivity.this;
            adCoins.setVisibility(View.GONE);
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    sharedPrefs.addCoins(10);
                    coins.setText(String.valueOf(sharedPrefs.getCoins()));

                }
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            coins.setText(String.valueOf(sharedPrefs.getCoins()));
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
    }

}
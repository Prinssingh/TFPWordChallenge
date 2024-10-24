package com.tfp.coding.wordchallenge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.card.MaterialCardView;
import com.tfp.coding.wordchallenge.Utils.Constants;
import com.tfp.coding.wordchallenge.Utils.SharedPrefs;
import com.tfp.coding.wordchallenge.Utils.SoundUtils;

public class GameActivity extends AppCompatActivity {
    int level = 0;
    //View
    TextView timer, score, displayText;
    EditText editTextAnswer;
    Button check;
    MaterialCardView adLoadedView;
    ImageView hint;

    SharedPrefs sharedPrefs;
    SoundUtils soundUtils;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000 * 2;
    private boolean isTimerRunning = false;
    private String answer = "";
    private int currentScore = 0;


    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        level = getIntent().getIntExtra("level", 0);
        sharedPrefs = new SharedPrefs(this);
        soundUtils = new SoundUtils(this);
        initView();
        startGame();
        setupBannerAd();
        createRewardedVideoAd();
    }

    private void initView() {
        timer = findViewById(R.id.timer);
        displayText = findViewById(R.id.displayText);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        editTextAnswer.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                evaluateAnswer();
                return true;
            }
        });
        score = findViewById(R.id.score);
        score.setText("0");

        check = findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateAnswer();
            }
        });

        hint = findViewById(R.id.hint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (rewardedAd != null){
                    pauseTimer();
                    showRewardedVideoAd();}
                else {
                    if (sharedPrefs.getCoins() >= 10) {
                        pauseTimer();
                        sharedPrefs.useCoins(10);
                        showHintDialog();
                        Toast.makeText(GameActivity.this, "10 Coins used!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(GameActivity.this, "Not enough coins!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        adLoadedView = findViewById(R.id.adLoadedView);
        adLoadedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHintDialog();
            }
        });
        adLoadedView.setVisibility(View.GONE);
    }

    private void startGame() {
        setNewGame();
        startTimer();

    }


    private void setNewGame() {
        if (level == 0) {
            answer = Constants.getRandomItem(Constants.beginnerLevelWordList);

        } else if (level == 1) {
            answer = Constants.getRandomItem(Constants.mediumLevelWordList);

        } else if (level == 2) {
            answer = Constants.getRandomItem(Constants.advanceLevelWordList);
        }
        String shuffledWord = Constants.shuffleString(answer);
        displayText.setText(shuffledWord);

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                timer.setText("Time's Up!");
                timer.setTextColor(R.color.red);
                isTimerRunning = false;

                gameOverDialog();
            }
        }.start();
        isTimerRunning = true;
    }

    private void pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    private void resumeTimer() {
        if (!isTimerRunning) {
            startTimer(); // Restart the timer with the remaining time
        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        @SuppressLint("DefaultLocale")
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);
    }

    private void evaluateAnswer() {
        String enteredData = editTextAnswer.getText().toString().trim().toUpperCase();
        if (enteredData.equals(answer.toUpperCase())) {
            currentScore += 1;
            soundUtils.playCorrectSound();
            score.setText(String.valueOf(currentScore));
            editTextAnswer.setText("");
            setNewGame();

        } else {
            soundUtils.playWrongSound();

        }
    }

    private void updateSharedScore() {
        if (level == 0 && currentScore > sharedPrefs.getBeginnerLevelScore())
            sharedPrefs.setBeginnerLevelScore(currentScore);
        else if (level == 1 && currentScore > sharedPrefs.getMediumLevelScore())
            sharedPrefs.setMediumLevelScore(currentScore);
        else if (level == 2 && currentScore > sharedPrefs.getAdvanceLevelScore())
            sharedPrefs.setAdvanceLevelScore(currentScore);

    }

    private int getSharedScore() {
        int score = 0;
        if (level == 0)
            score = sharedPrefs.getBeginnerLevelScore();
        else if (level == 1)
            score = sharedPrefs.getMediumLevelScore();
        else if (level == 2)
            score = sharedPrefs.getAdvanceLevelScore();

        return score;

    }

    private void gameOverDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setTitle("Time's Up!!");
        dialog.setMessage("Game Over!! \nYour time is finished!!\n Your Score =" + currentScore);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (currentScore > getSharedScore()) {
                    updateSharedScore();
                    dialog.dismiss();
                    highScoreDialog();

                } else {
                    dialog.dismiss();
                    GameActivity.this.finish();
                }

            }
        });
        dialog.show();


    }

    private void highScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);

        // Inflate custom layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view_high_score, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                GameActivity.this.finish();
            }
        });

        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button closeButton = dialogView.findViewById(R.id.continueBtn);
        closeButton.setOnClickListener(v -> {
            dialog.dismiss();

        });

    }

    private void showHintDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog dialog = builder.create();
        dialog.setTitle(answer);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("is your correct answer!");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resumeTimer();

            }
        });
        dialog.show();

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();

        dialog.setTitle("Quit ??");
        dialog.setMessage("Are you sure want to quit??");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pauseTimer();
                GameActivity.super.onBackPressed();

            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void setupBannerAd() {
        AdView adview = findViewById(R.id.banner_ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview.loadAd(adRequest);
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
                        adLoadedView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
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
                                adLoadedView.setVisibility(View.GONE);
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
                        adLoadedView.setVisibility(View.VISIBLE);
                    }
                });


    }

    private void showRewardedVideoAd() {
        if (rewardedAd != null) {
            Activity activityContext = GameActivity.this;

            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    adLoadedView.setVisibility(View.GONE);
                    showHintDialog();



                }
            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }
    }
}
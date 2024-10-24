package com.tfp.coding.wordchallenge.Utils;

import android.media.SoundPool;

import android.content.Context;

import com.tfp.coding.wordchallenge.R;


public class SoundUtils {
    Context context;
    SoundPool soundPool;
    int soundHint,soundNext,soundWrong;
    SharedPrefs sharedPrefs;
    public  SoundUtils(Context context){
        this.context=context;

        sharedPrefs = new SharedPrefs(context);
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        soundHint = soundPool.load(this.context, R.raw.hint, 1);
        soundNext = soundPool.load(this.context, R.raw.next, 1);
        soundWrong = soundPool.load(this.context, R.raw.wrong, 1);
    }




    public void playCorrectSound(){
        if(sharedPrefs.getSoundPref())
            this.soundPool.play(soundNext, 1, 1, 0, 0, 1);  // Play the sound
    }
    public  void playWrongSound(){
        if(sharedPrefs.getSoundPref())
            this.soundPool.play(soundWrong, 1, 1, 0, 0, 1);
    }
    public  void playClickSound(){
        if(sharedPrefs.getSoundPref())
            this.soundPool.play(soundHint, 1, 1, 0, 0, 1);
    }
}

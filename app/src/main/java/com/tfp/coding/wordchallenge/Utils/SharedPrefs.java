package com.tfp.coding.wordchallenge.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    public static String sharedPrefName = "WordChallengePrefs";
    Context context;

    public SharedPrefs(Context context2) {
        this.context = context2;
    }

    public void clearSharedData() {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public int getCoins() {
        return this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getInt("coins", 10);
    }

    public void addCoins(int coins) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putInt("coins", getCoins() + coins);
        editor.apply();
    }

    public void useCoins(int coins) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putInt("coins", getCoins() - coins);
        editor.apply();
    }

    public int getBeginnerLevelScore() {
        return this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getInt("BeginnerLevelScore", 0);
    }

    public void setBeginnerLevelScore(int score) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putInt("BeginnerLevelScore", score);
        editor.apply();
    }


    public int getMediumLevelScore() {
        return this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getInt("MediumLevelScore", 0);
    }

    public void setMediumLevelScore(int score) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putInt("MediumLevelScore", score);
        editor.apply();
    }


    public int getAdvanceLevelScore() {
        return this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getInt("getAdvanceLevelScore", 0);
    }

    public void setAdvanceLevelScore(int score) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putInt("getAdvanceLevelScore", score);
        editor.apply();
    }


    public boolean getSoundPref() {
        return this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getBoolean("sound", true);
    }

    public void setSound(boolean pref) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putBoolean("sound", pref);
        editor.apply();
    }

    public boolean getDarkModePref() {
        return this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getBoolean("darkMode", false);
    }

    public void setDarkMode(boolean pref) {
        SharedPreferences.Editor editor = this.context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).edit();
        editor.putBoolean("darkMode", pref);
        editor.apply();
    }
}

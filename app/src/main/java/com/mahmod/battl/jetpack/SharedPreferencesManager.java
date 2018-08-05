package com.mahmod.battl.jetpack;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.crash.FirebaseCrash;

public class SharedPreferencesManager {

    private Context context;
    private SharedPreferences preferences;
    //SharedPreferences name
    public static final String PREF_NAME = "player_data";
    //SharedPreferences data key
    public static final String PREF_BEST_SCORE = "player_best_score";

    public SharedPreferencesManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //add score
    }

    public void add(String name, int value) {
        try  {
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.putInt(name, value);
            prefEditor.commit();
        }

        catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log("Caught an exception:sharedPrefernceManger add  ");
            FirebaseCrash.report(e);
        }
    }

    public void initializeScore() {
        try {
            if(get(SharedPreferencesManager.PREF_BEST_SCORE) == -1) {
                add(SharedPreferencesManager.PREF_BEST_SCORE, 0);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log("Caught an exception:sharedPrefernceManger init  ");
            FirebaseCrash.report(e);
        }
    }

    public void delete(String name) {
        try {
            SharedPreferences.Editor prefEditor = preferences.edit();
            prefEditor.remove(name);
            prefEditor.commit();
        }

        catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log("Caught an exception:sharedPrefernceManger delete  ");
            FirebaseCrash.report(e);
        }
    }

    public int get(String name) {
        return preferences.getInt(name, -1);
    }

}

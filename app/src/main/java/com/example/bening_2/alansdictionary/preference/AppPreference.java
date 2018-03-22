package com.example.bening_2.alansdictionary.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.example.bening_2.alansdictionary.R;

/**
 * Created by Bening_2 on 3/9/2018.
 */

public class AppPreference {

    SharedPreferences sharedPreferences;
    Context context;

    public AppPreference(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setFirstRun(Boolean input){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = context.getResources().getString(R.string.first_run);
        editor.putBoolean(key, input);
        editor.commit();

    }

    public Boolean getFirstRun() {
        String key = context.getResources().getString(R.string.first_run);
        return sharedPreferences.getBoolean(key, true);
    }

}

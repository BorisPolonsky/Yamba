package com.example.administrator.yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017/4/1.
 */


public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{
    SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        this.pref= PreferenceManager.getDefaultSharedPreferences(this);
        this.pref.registerOnSharedPreferenceChangeListener(this);
        this.pref.getString("username","");
        this.pref.getString("password","");
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
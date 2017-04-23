package com.example.administrator.yamba;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Administrator on 2017/4/1.
 */


public class YambaApplication extends Application implements
        SharedPreferences.OnSharedPreferenceChangeListener{
    SharedPreferences pref;
    private static final String TAG= YambaApplication.class.getSimpleName();
    private static String username;
    @Override
    public void onCreate() {
        super.onCreate();
        this.pref= PreferenceManager.getDefaultSharedPreferences(this);
        this.pref.registerOnSharedPreferenceChangeListener(this);
        this.pref.getString("username",null);
        this.pref.getString("password",null);//Perhaps I would use these strings later...
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG,"onTerminate");
    }
    @Override
    public synchronized void
    onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        this.username=sharedPreferences.getString("username","");//For later use
    }
}
package com.example.administrator.yamba;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String username=null;
    private String password=null;
    private boolean pullServiceStatus=false;
    public static final String LOCATION_PROVIDER_NONE="NONE";
    public static final String LOCATION_PROVIDER_GPS="gps";
    public static final String LOCATION_PROVIDER_NETWORK="network";
    public static final String LOCATION_PROVIDER_FUSED="fused";
    @Override
    public void onCreate() {
        super.onCreate();
        this.pref= PreferenceManager.getDefaultSharedPreferences(this);
        this.pref.registerOnSharedPreferenceChangeListener(this);
        this.username=this.pref.getString("username",null);
        this.password=this.pref.getString("password",null);//Perhaps I would use these strings later...
        this.pullServiceStatus=pref.getBoolean("pull",true);
        if(this.pullServiceStatus)
        {
            //As a workaround, stop service first in case that the service is already running. 
            stopService(new Intent(this,UpdaterService.class));
            startService(new Intent(this,UpdaterService.class));
        }
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
        if(key.equals("username")) {
            String username = sharedPreferences.getString("username", null);
            if(username!=null&&username.equals(""))
                Toast.makeText(this,"Username must be non-empty. ",Toast.LENGTH_SHORT);
            else
                this.username=username;
            return;
        }
        if(key.equals("password"))
        {
            this.password=sharedPreferences.getString("password",null);
            return;
        }
        if(key.equals("pull"))
        {
            if(sharedPreferences.getBoolean("pull",true))
                startService(new Intent(this,UpdaterService.class));
            else
                stopService(new Intent(this,UpdaterService.class));
        }
        if(key.equals("locationProvider"))
        {
            //No need to act yet.
        }
    }
    public String getUsername(){
        return this.username;
    }
    public void logOut(){
        this.username=null;
        this.password=null;
        SharedPreferences.Editor editor=pref.edit();
        editor.clear();
        editor.commit();
    }
    void setPullServiceStatus(boolean bool)
    {
        this.pullServiceStatus=bool;
    }
    boolean getPullServiceStatus()
    {return this.pullServiceStatus;}
    public String getProvider()
    {
        return this.pref.getString("locationProvider",LOCATION_PROVIDER_NONE);
    }
}
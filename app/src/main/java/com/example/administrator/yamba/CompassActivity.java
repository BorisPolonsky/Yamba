package com.example.administrator.yamba;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Polonsky on 4/26/2017.
 */

public class CompassActivity extends Activity {
    private static final String TAG=CompassActivity.class.getSimpleName();
    SensorManager sensorManager;
    Sensor sensor;
    Rose rose;
    CompassListener compassListener;
    class CompassListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            int heading =(int)event.values[0];
            rose.setDirection(heading);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.rose=new Rose(this);
        setContentView(rose);
        this.sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        this.sensor=this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        this.compassListener=new CompassListener();
        Log.i(TAG,"onCreate");
        }

    @Override
    protected void onResume() {
        super.onResume();
        this.sensorManager.registerListener(this.compassListener,this.sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this.compassListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

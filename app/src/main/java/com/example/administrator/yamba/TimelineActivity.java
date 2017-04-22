package com.example.administrator.yamba;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Polonsky on 4/21/2017.
 */

public class TimelineActivity extends Activity {
    private static final String TAG=TimelineActivity.class.getSimpleName();
    private StatusData statusData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_basic);
        this.statusData=new StatusData(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db=statusData.getDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusData.close();
        Log.i(TAG,"onDestroy");
    }
}

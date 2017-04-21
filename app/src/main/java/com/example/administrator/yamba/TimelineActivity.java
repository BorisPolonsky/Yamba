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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.timeline_basic);
        statusData=new StatusData(this);
        Log.i(TAG,"onCreate");
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

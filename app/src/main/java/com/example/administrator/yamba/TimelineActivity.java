package com.example.administrator.yamba;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Polonsky on 4/21/2017.
 */

public class TimelineActivity extends Activity {
    private static final String TAG=TimelineActivity.class.getSimpleName();
    private StatusData statusData;
    private SQLiteDatabase db;
    private TextView timelineView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_basic);
        this.statusData=new StatusData(this);
        this.db=statusData.getDatabase();
        this.timelineView=(TextView)findViewById(R.id.textTimeline);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor=db.query("timeline",null,null,null,null,null,"created_at DESC");
        String user,created_at,txt;
        startManagingCursor(cursor);
        while(cursor.moveToNext())//Will this miss the first row?
        {
            user=cursor.getString(cursor.getColumnIndex("user"));
            created_at=cursor.getString(cursor.getColumnIndex("created_at"));
            txt=cursor.getString(cursor.getColumnIndex("txt"));
            timelineView.append(String.format("%s:\n%s\n",user,txt));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        statusData.close();
        Log.i(TAG,"onDestroy");
    }
}

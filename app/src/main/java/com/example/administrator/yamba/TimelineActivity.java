package com.example.administrator.yamba;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Polonsky on 4/21/2017.
 */

public class TimelineActivity extends Activity {
    private static final String TAG=TimelineActivity.class.getSimpleName();

    private SQLiteDatabase db;
    private ListView listTimeline;
    private TimelineAdapter adapter;
    private StatusData statusData;
    static final String[] FROM={"created_at","user","txt"};
    static final int[] TO={R.id.textCreatedAt,R.id.textUser,R.id.textText};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_basic);
        this.listTimeline=(ListView)findViewById(R.id.listTimeline);
        this.statusData=new StatusData(this);
        this.db=statusData.getDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor=db.query("timeline",null,null,null,null,null,"created_at DESC");
        String user,created_at,txt;
        startManagingCursor(cursor);
        adapter=new TimelineAdapter(this,cursor);
        listTimeline.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.statusData.close();
        Log.i(TAG,"onDestroy");
    }
}

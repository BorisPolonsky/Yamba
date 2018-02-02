package com.example.administrator.yamba;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Polonsky on 4/21/2017.
 */

public class TimelineActivity extends Activity {
    private static final String TAG=TimelineActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listTimeline;
    private SimpleCursorAdapter adapter;
    private StatusData statusData;
    static final String[] FROM={"created_at","user","txt"};
    static final int[] TO={R.id.textCreatedAt,R.id.textUser,R.id.textText};
    private Button postButton;
    private Button menuButton;
    private YambaApplication yamba;
    private TimelineReceiver receiver;
    static final String SEND_TIMELINE_NOTIFICATIONS="com.example.administrator.yamba.SEND_TIMELINE_NOTIFICATION";
    class TimelineReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            TimelineActivity.this.cursor.requery();
            TimelineActivity.this.adapter.notifyDataSetChanged();
            Toast.makeText(TimelineActivity.this,"New message received\nNumber: "+
                    Integer.toString(intent.getIntExtra(UpdaterService.NEW_STATUS_EXTRA_COUNT,-1)),
                    Toast.LENGTH_SHORT).show();
            Log.i("TimelineReceiver","onReceive");
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_basic);
        this.listTimeline=(ListView)findViewById(R.id.listTimeline);
        this.statusData=new StatusData(this);
        this.db=statusData.getDatabase();
        menuButton=(Button)findViewById(R.id.buttonMenu);
        menuButton.setOnClickListener((View v) ->
                startActivity(new Intent(TimelineActivity.this,MiscActivity.class))
        );
        postButton=(Button)findViewById(R.id.buttonPost);
        postButton.setOnClickListener((View v) ->
                startActivity(new Intent(TimelineActivity.this,StatusActivity.class))
        );
        yamba=(YambaApplication)getApplication();
        receiver=new TimelineReceiver();
        Log.i(TAG,"onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(yamba.getUsername()==null) {
            startActivity(new Intent(this, PrefActivity.class));
            Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG).show();
            return;
        }
        this.cursor=db.query("timeline",null,null,null,null,null,"created_at DESC");
        startManagingCursor(cursor);
        this.adapter=new SimpleCursorAdapter(this,R.layout.row,cursor,FROM,TO);
        this.adapter.setViewBinder((View view, Cursor cursor, int columnIndex) -> {
                if (columnIndex==cursor.getColumnIndex(StatusData.C_CREATED_AT))
                {
                    TextView textCreatedAt=(TextView)view.findViewById(R.id.textCreatedAt);
                    long timeThen=cursor.getLong(columnIndex);
                    textCreatedAt.setText(TimeSpanHandler.TimeSpan(timeThen));
                    return true;
                }
                else
                    return false;
            }
        );
        listTimeline.setAdapter(adapter);
        registerReceiver(this.receiver,
                new IntentFilter(UpdaterService.NEW_STATUS_INTENT),SEND_TIMELINE_NOTIFICATIONS,null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.statusData.close();
        Log.i(TAG,"onDestroy");
    }
}

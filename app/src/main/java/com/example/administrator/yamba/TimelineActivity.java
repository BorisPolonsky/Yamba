package com.example.administrator.yamba;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Created by Polonsky on 4/21/2017.
 */

public class TimelineActivity extends Activity {
    private static final String TAG=TimelineActivity.class.getSimpleName();
    private SQLiteDatabase db;
    private ListView listTimeline;
    private SimpleCursorAdapter adapter;
    private StatusData statusData;
    static final String[] FROM={"created_at","user","txt"};
    static final int[] TO={R.id.textCreatedAt,R.id.textUser,R.id.textText};
    private Button postButton;
    private Button menuButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_basic);
        this.listTimeline=(ListView)findViewById(R.id.listTimeline);
        this.statusData=new StatusData(this);
        this.db=statusData.getDatabase();
        menuButton=(Button)findViewById(R.id.buttonMenu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TimelineActivity.this,MiscActivity.class));
            }
        });
        postButton=(Button)findViewById(R.id.buttonPost);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TimelineActivity.this,StatusActivity.class));
            }
        });
        YambaApplication yamba=(YambaApplication)getApplication();
        if(yamba.pref.getString("username",null)==null)
            startActivity(new Intent(this,PrefActivity.class));
        Log.i(TAG,"onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor=db.query("timeline",null,null,null,null,null,"created_at DESC");
        String user,created_at,txt;
        startManagingCursor(cursor);
        adapter=new SimpleCursorAdapter(this,R.layout.row,cursor,FROM,TO);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex==cursor.getColumnIndex(StatusData.C_CREATED_AT))
                {
                    TextView textCreatedAt=(TextView)view.findViewById(R.id.textCreatedAt);
                    textCreatedAt.setText
                            (DateUtils.getRelativeTimeSpanString(cursor.getLong(columnIndex)));
                    return true;
                }
                else
                    return false;
            }
        });
        listTimeline.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.statusData.close();
        Log.i(TAG,"onDestroy");
    }
}

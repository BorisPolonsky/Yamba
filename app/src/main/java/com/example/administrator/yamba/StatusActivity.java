package com.example.administrator.yamba;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends Activity
{
    class MicroBlogMonitor implements TextWatcher
    {    public void beforeTextChanged(CharSequence s, int start,
                                       int count, int after)
        {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }
        public void afterTextChanged(Editable s)
        {
            int count=140-s.length();
            characterCount.setText(""+Integer.toString(count));
            if(count<0)
                characterCount.setTextColor(Color.RED);
            else
                characterCount.setTextColor(Color.GREEN);
        }
    }
    private static final String TAG=StatusActivity.class.getSimpleName();
    private EditText microBlog;
    private Button shareButton;
    private Button clearButton;
    private Button menuButton;
    private TextView characterCount;
    private MicroBlogMonitor microBlogMonitor=new MicroBlogMonitor();
    SharedPreferences pref;
    private SQLiteOpenHelper dbHelper=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        microBlog=(EditText)findViewById(R.id.editTextMicroBlog);
        microBlog.addTextChangedListener(this.microBlogMonitor);
        shareButton=(Button)findViewById(R.id.buttonShare);
        clearButton=(Button)findViewById(R.id.buttonClear);
        menuButton=(Button)findViewById(R.id.buttonMenu);
        characterCount=(TextView)findViewById(R.id.textViewTextCount);
        characterCount.setTextColor(Color.GREEN);
        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MicroBlogPusher pusher=new MicroBlogPusher();
                String str=microBlog.getText().toString();
                if(str.length()>0 && str.length()<=140)
                    pusher.execute(microBlog.getText().toString());
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                char[] newText="".toCharArray();
                microBlog.setText(newText,0,newText.length);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatusActivity.this,MiscActivity.class));
            }
        });
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener
                (new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        });
        dbHelper=new SQLiteOpenHelper(this,"YambaDb.db",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table timeline ("+ BaseColumns._ID+" int primary key, int, " +
                        "user text, txt text)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("drop table timeline");
                this.onCreate(db);
            }
        };
        Log.i(TAG,"onCreate");
    }
    class MicroBlogPusher extends AsyncTask<String,String,String>
    {
        private final String TAG=MicroBlogPusher.class.getSimpleName();
        @Override
        protected String doInBackground(String... param)
        {
            SQLiteDatabase db=dbHelper.getReadableDatabase();
            try
            {
                db.execSQL("create table if not exists timeline ("+BaseColumns._ID+
                         " int primary key, created_at int, user text, txt text)");
                ContentValues values=new ContentValues();
                values.put("created_at",System.currentTimeMillis());
                values.put("user","user1");
                values.put("txt",param[0]);
                //db.insertOrThrow(this.TABLE,null,values) corresponds to
                //db.insertWithOnConflict(this.TABLE,null,values,CONFLICT_NONE)

                //References for ON CONFLICT clause in SQLite: http://sqlite.org/lang_conflict.html
                db.insertWithOnConflict("timeline",null,values,SQLiteDatabase.CONFLICT_IGNORE);
            }finally
            {
                db.close();
            }
            Log.i(TAG,"doInBackground");
            return("Done");
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            for (String str : progress)
                Log.i(MicroBlogPusher.class.getSimpleName(), str);
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG,"onPostExecute");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
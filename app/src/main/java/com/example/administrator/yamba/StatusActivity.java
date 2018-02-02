package com.example.administrator.yamba;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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
import android.widget.Toast;

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
    private TextView characterCount;
    private MicroBlogMonitor microBlogMonitor=new MicroBlogMonitor();
    private StatusData statusData;
    private YambaApplication yamba;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        microBlog=(EditText)findViewById(R.id.editTextMicroBlog);
        microBlog.addTextChangedListener(this.microBlogMonitor);
        shareButton=(Button)findViewById(R.id.buttonShare);
        clearButton=(Button)findViewById(R.id.buttonClear);
        characterCount=(TextView)findViewById(R.id.textViewTextCount);
        characterCount.setTextColor(Color.GREEN);
        shareButton.setOnClickListener((View v) ->
            {
                MicroBlogPusher pusher=new MicroBlogPusher();
                String str=microBlog.getText().toString();
                if(str.length()>0 && str.length()<=140) {
                    String text=microBlog.getText().toString();
                    YambaApplication yamba=(YambaApplication)getApplication();
                    String username=yamba.getUsername();
                    pusher.execute(username,text);
                }
            }
        );
        clearButton.setOnClickListener((View v) ->
            {
                char[] newText="".toCharArray();
                microBlog.setText(newText,0,newText.length);
            }
        );
        /*
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener
                (new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        });*/
        /*dbHelper=new SQLiteOpenHelper(this,"YambaDb.db",null,1) {
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
        };*/
        statusData=new StatusData(this);
        this.yamba=(YambaApplication) getApplication();
        Log.i(TAG,"onCreate");
    }
    class MicroBlogPusher extends AsyncTask<String,String,String>
    {
        private final String TAG=MicroBlogPusher.class.getSimpleName();
        @Override
        protected String doInBackground(String... param)
        {
            SQLiteDatabase db=statusData.getDatabase();
            try
            {
                db.execSQL("create table if not exists "+StatusData.TABLE+ "("+BaseColumns._ID+
                         " int primary key, "+StatusData.C_CREATED_AT+" int, "+StatusData.C_USER+
                        " text, "+StatusData.C_TEXT+" text)");
                ContentValues values=new ContentValues();
                values.put("created_at",System.currentTimeMillis());
                values.put("user",param[0]);
                values.put("txt",param[1]);
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
    protected void onResume() {
        super.onResume();
        String provider=yamba.getProvider();
        if (!provider.equals(YambaApplication.LOCATION_PROVIDER_NONE))
        {
            this.locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
            if(this.locationManager!=null) {
                this.locationListener=new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        StatusActivity.this.location=location;
                        Toast.makeText(StatusActivity.this,String.format("Latitude: %f\nLongtitude: %f",location.getLatitude(),location.getLongitude()),Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"Location updated");
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                try {
                    this.locationManager.requestLocationUpdates(provider, 30000, 10, this.locationListener);
                    this.locationListener.onLocationChanged(locationManager.getLastKnownLocation(provider));
                } catch (SecurityException e) {
                    Log.i(TAG,"Permission denied");
                    Toast.makeText(StatusActivity.this,"Unable to access location.\n Permission denied.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.locationManager!=null)
            locationManager.removeUpdates(this.locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusData.close();
    }
}
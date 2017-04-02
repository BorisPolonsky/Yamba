package com.example.administrator.yamba;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
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
                char[] newText = "Not implemented yet. ".toCharArray();
                microBlog.setText(newText, 0, newText.length);
                Log.i(TAG, "NotImplemented");
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
        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            }
        });
        Log.i(TAG,"onCreate");
    }
    class MicroBlogPusher extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... param)
        {
            return("Done");
        }
        @Override
        protected void onProgressUpdate(String... progress)
        {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
        }
    }
}
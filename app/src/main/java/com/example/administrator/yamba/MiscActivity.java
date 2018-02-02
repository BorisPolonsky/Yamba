package com.example.administrator.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2017/4/1.
 */

public class MiscActivity extends Activity
{
    private Button buttonSettings;
    private Button buttonLogOut;
    private Button buttonCompass;
    private Button buttonLocation;
    private static final String TAG=MiscActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
        this.setContentView(R.layout.misc);
        this.buttonSettings =(Button)findViewById(R.id.buttonSettings);
        this.buttonLogOut=(Button)findViewById(R.id.buttonLogOut);
        this.buttonCompass=(Button)findViewById(R.id.buttonCompass);
        this.buttonLocation=(Button)findViewById(R.id.buttonLocation);
        this.buttonSettings.setOnClickListener((View v)->startActivity(new Intent(MiscActivity.this,PrefActivity.class)));
        this.buttonLogOut.setOnClickListener((View v)->{
                YambaApplication yamba=(YambaApplication) getApplication();
                yamba.logOut();
            }
        );
        this.buttonCompass.setOnClickListener((View v) ->
                startActivity(new Intent(MiscActivity.this, CompassActivity.class)));
        this.buttonLocation.setOnClickListener((View v) ->
                startActivity(new Intent(MiscActivity.this,LocationActivity.class)));
    }
}

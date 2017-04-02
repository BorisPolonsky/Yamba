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
    private Button buttonStartService;
    private Button buttonStopService;
    private static final String TAG=MiscActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
        this.setContentView(R.layout.misc);
        this.buttonStartService=(Button)findViewById(R.id.buttonStartService);
        this.buttonStopService =(Button)findViewById(R.id.buttonStopService);
        this.buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MiscActivity.this,UpdaterService.class));
            }
        });
        this.buttonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MiscActivity.this,UpdaterService.class));
            }
        });
    }
}

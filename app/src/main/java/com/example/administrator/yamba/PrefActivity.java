package com.example.administrator.yamba;

/**
 * Created by Administrator on 2017/4/1.
 */

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.EditText;

public class PrefActivity extends PreferenceActivity {
    EditText microBlog;
    private static final String TAG=PrefActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        addPreferencesFromResource(R.xml.pref);
    }
}

package com.example.administrator.yamba;

/**
 * Created by Administrator on 2017/4/1.
 */

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.EditText;

public class PrefActivity extends PreferenceActivity {
    EditText microBlog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.pref);
    }
}

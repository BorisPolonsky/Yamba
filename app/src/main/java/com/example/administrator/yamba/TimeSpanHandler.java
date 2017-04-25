package com.example.administrator.yamba;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;

import static com.example.administrator.yamba.R.id.textCreatedAt;

/**
 * Created by Polonsky on 4/25/2017.
 */

public class TimeSpanHandler {
    public static String TimeSpan(long timeThen)
    {
        long timeSpan=System.currentTimeMillis()-timeThen;
        if(timeSpan<60000)//In a minute
            return "just now";
        else if(timeSpan<24*60*60*1000)//One day
            return (String)DateUtils.getRelativeTimeSpanString(timeThen);
        else
            return new SimpleDateFormat("MM-dd-yy HH:mm").format(timeThen);
    }
}

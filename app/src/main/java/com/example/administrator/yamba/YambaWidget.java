package com.example.administrator.yamba;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Polonsky on 4/25/2017.
 */

public class YambaWidget extends AppWidgetProvider {
    private static final String TAG=YambaWidget.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        ContentResolver contentResolver=context.getContentResolver();
        Cursor c=contentResolver.query(StatusProvider.CONTENT_URI,null,null,null,StatusData.GET_ALL_ORDER_BY);//Get the latest status
        try{
            if(c.moveToFirst()) {
                CharSequence user = c.getString(c.getColumnIndex(StatusData.C_USER));
                CharSequence createdAt= TimeSpanHandler.TimeSpan(
                        c.getLong(c.getColumnIndex(StatusData.C_CREATED_AT)));
                CharSequence text=c.getString(c.getColumnIndex(StatusData.C_TEXT));
                for(int appWidgetId:appWidgetIds) {//Loop over widgets
                    Log.i(TAG, "Updating widget " + appWidgetId);
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yamba_widget);
                    views.setTextViewText(R.id.textUser, user);
                    views.setTextViewText(R.id.textCreatedAt,createdAt);
                    views.setTextViewText(R.id.textText,text);
                    views.setOnClickPendingIntent(R.id.yamba_icon,
                            PendingIntent.getActivity(context,0,new Intent(context,TimelineActivity.class),0));
                    appWidgetManager.updateAppWidget(appWidgetId,views);
                }
            }else{
                Log.i(TAG,"No data to update");
            }
        }finally {
            c.close();
        }
        Log.i(TAG,"onUpdate");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(UpdaterService.NEW_STATUS_INTENT)){//No action filters for this?
            Log.i(TAG,"New status update detected");
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            this.onUpdate(context,appWidgetManager,appWidgetManager.getAppWidgetIds(new ComponentName(context,YambaWidget.class)));//??????
        }

    }
}

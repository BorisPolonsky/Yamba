package com.example.administrator.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Polonsky on 4/24/2017.
 */

public class BootReceiver extends BroadcastReceiver{
    private static final String TAG=BootReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,UpdaterService.class));
        Log.i(TAG,"onReceive");
    }
}

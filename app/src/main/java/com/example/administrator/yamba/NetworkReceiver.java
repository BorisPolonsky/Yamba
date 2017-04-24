package com.example.administrator.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Polonsky on 4/24/2017.
 */

public class NetworkReceiver extends BroadcastReceiver{
    private static final String TAG=NetworkReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNetworkDown= intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,true);
        if(isNetworkDown)
        {
            Toast.makeText(context,"No network connection",Toast.LENGTH_LONG).show();
            context.stopService(new Intent(context,UpdaterService.class));
        }
        else if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pull",false))
        {
            context.startService(new Intent(context,UpdaterService.class));
        }
        Log.i(TAG,"onReceive");
    }
}

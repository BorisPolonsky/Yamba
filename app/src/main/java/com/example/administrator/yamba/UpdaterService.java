package com.example.administrator.yamba;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/1.
 */

public class UpdaterService extends Service
{
    private static final String TAG=UpdaterService.class.getSimpleName();
    private class Updater extends Thread
    {
        private final String TAG=Updater.class.getSimpleName();
        private final int INTERVAL=10000;
        private UpdaterService updaterService=UpdaterService.this;
        @Override
        public void run()
        {
            while(true)
            {
                Log.i(TAG,"Updated!");
                try
                {
                    Thread.sleep(INTERVAL);
                }
                catch(InterruptedException e)
                {
                    Log.i(TAG,"Interrupted");
                    break;
                }
            }
        }
    }
    private Updater updater;
    private boolean runFlag=false;//Useless, maybe useful later.
    private SQLiteDatabase db;
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG,"onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.updater=new Updater();
        this.runFlag=false;
        Log.i(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG,"onStartCommand");
        this.runFlag=true;
        this.updater.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.updater.interrupt();
        this.updater=null;
        this.runFlag=false;
        Log.i(TAG,"onDestroy");
    }
}

package com.example.administrator.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/3.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG=DbHelper.class.getSimpleName();
    static final String DB_NAME="YambaDb.db";
    static final int DB_VERSION=1;
    static final String TABLE="Table1";
    //Need to study the following part.
    static final String C_ID= BaseColumns._ID;//???
    static final String C_CREATED_AT="created_at";
    static final String C_SOURCE="source";
    static final String C_TEXT="txt";
    static final String C_USER="user";
    private final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
    private Context context;
    DbHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+TABLE+" ("+C_ID+" int primary key, "+C_CREATED_AT+" int, "
                +C_USER+" text, "+C_TEXT+" text)";
        db.execSQL(sql);
        Log.i(TAG,"onCreate sql: "+sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE);
        Log.i(TAG,String.format("onUpdate: Dropped old database and create a new one. Version: %d -> %d",oldVersion,newVersion));
        onCreate(db);//We may have multiple instances of DbHelper, but we refer to the same database because DB_NAME is fixed for now.
    }
}
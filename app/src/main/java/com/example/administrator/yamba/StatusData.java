package com.example.administrator.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/3.
 */

public class StatusData {
    StatusData(Context context)
    {
        this.dbHelper=new DbHelper(context);
    }
    private static final String TAG=StatusData.class.getSimpleName();
    private final DbHelper dbHelper;
    static final String DB_NAME="YambaDb.db";
    static final int DB_VERSION=1;
    static final String TABLE="timeline";
    //Need to study the following part.
    static final String C_ID= BaseColumns._ID;//???
    static final String C_CREATED_AT="created_at";
    static final String C_SOURCE="source";
    static final String C_TEXT="txt";
    static final String C_USER="user";
    private static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
    private static final String[] MAX_CREATED_AT_COLUMNS={"MAX(", StatusData.C_CREATED_AT,")"};
    private static final String[] DB_TEXT_COLUMNS={C_TEXT};
    class DbHelper extends SQLiteOpenHelper
    {

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
    public SQLiteDatabase getDatabase()
    {
        return this.dbHelper.getReadableDatabase();
    }
    public void close()
    {
        this.dbHelper.close();
    }
    public void insertOrIgnore(ContentValues values)
    {
        SQLiteDatabase db=this.dbHelper.getWritableDatabase();
        try
        {
            //db.insertOrThrow(this.TABLE,null,values) corresponds to
            //db.insertWithOnConflict(this.TABLE,null,values,CONFLICT_NONE)

            //References for ON CONFLICT clause in SQLite: http://sqlite.org/lang_conflict.html
            db.insertWithOnConflict(this.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
        }
        finally
        {
            db.close();
        }
    }
    public Cursor getStatusUpdates()
    {
        SQLiteDatabase db=this.dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TABLE,null,null,null,null,null,null);//Query all columns
        //I'll study the last 5 parameters later.
        return cursor;
    }

    public Cursor getLatestStatusCreatedTime()
    {
        return null;//I'll implement this part later.
        //Need to learn more about the SQLite query statements.
    }
    public String getStatusTextById(long id)
    {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE, DB_TEXT_COLUMNS, C_ID + "=" + id, null, null, null, null);
            try {
                return cursor.moveToNext() ? cursor.getString(0) : null;//cursor.getString()?
            } finally {
                cursor.close();
            }
        }
        finally{
            db.close();
        }
    }
    public long getLatestStatusCreatedAtTime()
    {
        SQLiteDatabase db=this.dbHelper.getReadableDatabase();
        try {
            Cursor cursor=db.query(TABLE,MAX_CREATED_AT_COLUMNS,null,null,null,null,null);
            //How come it selects one column with an array with length 3?
            try{
                return cursor.moveToNext()?cursor.getLong(0):Long.MIN_VALUE;
            }
            finally
            {
                cursor.close();
            }
        }
        finally
        {
            db.close();
        }
    }
}
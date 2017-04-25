package com.example.administrator.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Polonsky on 4/25/2017.
 */

public class StatusProvider extends ContentProvider {
    private static final String TAG=StatusData.class.getSimpleName();
    public static final Uri CONTENT_URI=Uri.parse("content://com.example.administrator.yamba.statusprovider");
    public static final String SINGLE_RECORD_MIME_TYPE="vnd.android.cursor.item/vnd.example.administrator.yamba.status";
    public static final String MULTIPLE_RECORD_MIME_TYPE="vnd.android.cursor.dir/vnd.example.administrator.yamba.mstatus";
    StatusData statusData;
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return this.getId(uri)<0?MULTIPLE_RECORD_MIME_TYPE:SINGLE_RECORD_MIME_TYPE;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        long id=this.getId(uri);
        SQLiteDatabase db=statusData.getDbHelper().getWritableDatabase();
        try {
        if(id<0)
        {
            return db.update(StatusData.TABLE,values,selection,selectionArgs);
        }
        else{
            return db.update(StatusData.TABLE,values,StatusData.C_ID+"="+id,null);
        }
        }finally{
            db.close();
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //Warning: statusData not created
        SQLiteDatabase db=statusData.getDbHelper().getWritableDatabase();
        try{
            long id=db.insertOrThrow(StatusData.TABLE,null,values);
            if(id==-1){
                String err=String.format("%s: Failed to insert [%s] to [%s] for unknown reasons.",
                        TAG,values,uri);
                throw new RuntimeException(err);
            }
            return ContentUris.withAppendedId(uri,id);
        }finally{
            db.close();
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        long id=this.getId(uri);
        SQLiteDatabase db=statusData.getDbHelper().getWritableDatabase();
        try {
            if(id<0)
                return db.delete(StatusData.TABLE,selection,selectionArgs);
            else
                return db.delete(StatusData.TABLE,StatusData.C_ID+"="+id,null);
        }finally{
            db.close();
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        long id=this.getId(uri);
        SQLiteDatabase db=statusData.getDatabase();
        try{
            if(id<0)
                return db.query(StatusData.TABLE,projection,selection,selectionArgs,null,null,sortOrder);
            else
                return db.query(StatusData.TABLE,projection,StatusData.C_ID+"="+id,null,null,null,null);
        }finally{
            db.close();//Where there's no db.close() in the original book?
        }
    }

    @Override
    public boolean onCreate() {
        return false;
    }
    private long getId(Uri uri){
        String lastPathSegment=uri.getLastPathSegment();
        if(lastPathSegment!=null)
        {
            try{
                return Long.parseLong(lastPathSegment);
            }catch (NumberFormatException e)
            {
                //cannot convert lastPathSegment into long
            }
        }
        return -1;
    }
}

package com.example.administrator.yamba;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Polonsky on 4/23/2017.
 */
@Deprecated
public class TimelineAdapter extends SimpleCursorAdapter{
    static final String TAG=TimelineActivity.class.getSimpleName();
    static final String[] FROM={StatusData.C_CREATED_AT,StatusData.C_USER,StatusData.C_TEXT};
    static final int[] TO={R.id.textCreatedAt,R.id.textUser,R.id.textText};
    TimelineAdapter(Context context,Cursor c)
    {
        super(context,R.layout.row,c,FROM,TO);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        long timeStamp=cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
        TextView textCreatedAt=(TextView)view.findViewById(R.id.textCreatedAt);
        textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timeStamp));
    }
}

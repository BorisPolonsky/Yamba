package com.example.administrator.yamba;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

/**
 * Created by Polonsky on 4/26/2017.
 */

public class Rose extends ImageView {
    private float heading=0;
    Rose(Context context)
    {
        super(context);
        this.setImageResource(R.drawable.compass);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-heading,canvas.getWidth()/2,canvas.getHeight()/2);
        super.onDraw(canvas);
    }

    public void setDirection(int heading)
    {
        this.heading=heading;
        this.invalidate();
        //onDraw(android.graphics.Canvas)} will be called at some point in the future.
    }
}

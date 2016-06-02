package com.idroidms.railway.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ubuntu1 on 24/2/16.
 */
public class CustomTextLite extends TextView {
    public CustomTextLite(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), ApplicationConstants.TEXT_LIGHT_HELVETICA);
        this.setTypeface(face);
    }

    public CustomTextLite(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), ApplicationConstants.TEXT_LIGHT_HELVETICA);
        this.setTypeface(face);
    }

    public CustomTextLite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), ApplicationConstants.TEXT_LIGHT_HELVETICA);
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
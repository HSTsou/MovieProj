package com.example.handsome.thenewtest.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by handsome on 2015/10/13.
 *                           CAN NOT WORK!!!!!!!!!
 */



public class ChineseTextView extends TextView {

    public  ChineseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/wcl_06.ttf"));
    }
}



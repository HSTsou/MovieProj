package com.example.handsome.thenewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by hs on 2017/1/24.
 */

public class CustomGroup extends ViewGroup {


    public CustomGroup(Context context) {
        super(context);
    }

    public CustomGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}

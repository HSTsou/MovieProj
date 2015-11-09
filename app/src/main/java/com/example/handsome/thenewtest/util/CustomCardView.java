package com.example.handsome.thenewtest.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.example.handsome.thenewtest.R;

/**
 * Created by handsome on 2015/10/28.
 */
public class CustomCardView extends CardView {
    Context context;
    public CustomCardView(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void drawableStateChanged() {
        if (isPressed()) {
            this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_pressed));
        } else {
            this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
        }
    }
}
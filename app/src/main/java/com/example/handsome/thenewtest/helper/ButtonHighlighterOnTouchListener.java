package com.example.handsome.thenewtest.helper;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by handsome on 2015/10/20.
 */
public class ButtonHighlighterOnTouchListener implements View.OnTouchListener {

    private static final int TRANSPARENT_GREY = Color.argb(0, 185, 185, 185);
    private static final int FILTERED_GREY = Color.argb(155, 185, 185, 185);

    ImageView imageView;
    TextView textView;


    public ButtonHighlighterOnTouchListener(final ImageView imageView) {
        super();
        this.imageView = imageView;

    }

    public ButtonHighlighterOnTouchListener(final TextView textView) {
        super();
        this.textView = textView;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (imageView != null) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i("hs","ACTION_DOWN");
                imageView.setColorFilter(FILTERED_GREY);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                imageView.setColorFilter(TRANSPARENT_GREY); // or null
                Log.i("hs", "ACTION_UP");
            }
        } else {
            for (final Drawable compoundDrawable : textView.getCompoundDrawables()) {
                if (compoundDrawable != null) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // we use PorterDuff.Mode. SRC_ATOP as our filter color is already transparent
                        // we should have use PorterDuff.Mode.LIGHTEN with a non transparent color
                        compoundDrawable.setColorFilter(FILTERED_GREY, PorterDuff.Mode.SRC_ATOP);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        compoundDrawable.setColorFilter(TRANSPARENT_GREY, PorterDuff.Mode.SRC_ATOP); // or null
                    }
                }
            }
        }
        return false;
    }

}


package com.example.handsome.thenewtest.util;

import android.support.design.widget.AppBarLayout;

/**
 * For map not to be reactive with coordinator layout
 * Created by handsome on 2015/10/30.
 */
public class CustomBehavior extends AppBarLayout.Behavior.DragCallback {
    @Override
    public  boolean canDrag (AppBarLayout appBarLayout){
        return true;//false indicate not go with coordinator.
    }
}

package com.example.handsome.thenewtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hs on 2017/1/24.
 */

public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        a.getFloat(R.styleable.MyView_spacing, 1.0f);//attrs屬性, 預設值
        a.recycle();//要回收
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int r = getMeasuredWidth() / 2;
        int centerX = getLeft() + r;
        int centerY = getTop() + r;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        canvas.drawCircle(centerX, centerY, r, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = measureDimension(100, widthMeasureSpec);
        int height = measureDimension(200, heightMeasureSpec);

        setMeasuredDimension(width, height);//必須
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY) {//其子結點都要適應父的大小
            result = specSize;
        }else {
            result = defaultSize;
            if(specMode == MeasureSpec.AT_MOST){//不能超過父大小
                result = Math.min(result, specSize);
            }
        }

        return result;
    }
}

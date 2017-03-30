package com.example.handsome.thenewtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WrapLayout extends ViewGroup {

	private static final int DEFAULT_HORIZONTAL_SPACING = 0;
	private static final int DEFAULT_VERTICAL_SPACING = 0;
	private int horizontalSpacing;
	private int verticalSpacing;
	private ArrayList<Integer> indexOfAttachPreviousView = new ArrayList<>();
	private ArrayList<Integer> numberOfAttachView = new ArrayList<>();
	
	public WrapLayout(Context context) {
		super(context);
		horizontalSpacing = DEFAULT_HORIZONTAL_SPACING;
		verticalSpacing = DEFAULT_VERTICAL_SPACING;
	}
	public WrapLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WrapLayout);
		try{
			horizontalSpacing = a.getDimensionPixelSize(R.styleable.WrapLayout_horizontalSpacing, DEFAULT_HORIZONTAL_SPACING);
			verticalSpacing = a.getDimensionPixelSize(R.styleable.WrapLayout_verticalSpacing, DEFAULT_VERTICAL_SPACING);
		}finally{
			a.recycle();
		}
	}



	public int getHorizontalSpacing(){
		return horizontalSpacing;
	}
	
	public void setHorizontalSpacing(int horizontalSpacing){
		this.horizontalSpacing = horizontalSpacing;
	}
	
	public int getVerticalSpacing(){
		return verticalSpacing;
	}
	
	public void setVerticalSpacing(int verticalSpacing){
		this.verticalSpacing = verticalSpacing;
	}
	
	

	//measure the Wraplayout size
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		final int availableWidth = widthSize - getPaddingRight() - getPaddingLeft();
		final int availableHeight = heightSize - getPaddingTop() - getPaddingBottom();
		int maxWidth = 0;
		int rowHeight = 0;
		int x = 0;
		int y = 0;
		int count = getChildCount();
		
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			if(child.getVisibility() == View.GONE){
				continue;
			}
			LayoutParams lp = child.getLayoutParams();
			child.measure(createMeasureSpec(lp.width, availableWidth, widthMode), 
					createMeasureSpec(lp.height, availableHeight, heightMode));
			final int height = child.getMeasuredHeight();
			rowHeight = Math.max( rowHeight, height);
		}
		
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			if(child.getVisibility() == View.GONE){
				continue;
			}

			LayoutParams lp = child.getLayoutParams();
			child.measure(createMeasureSpec(lp.width, availableWidth, widthMode), 
					createMeasureSpec(lp.height, availableHeight, heightMode));
			final int width = child.getMeasuredWidth();
//			final int height = child.getMeasuredHeight();
			
			if(x + width > availableWidth){
				boolean doContinue = false;
				for(int j = 0; j < indexOfAttachPreviousView.size(); j++){
					if( i == indexOfAttachPreviousView.get(j) && i > 0 ){
						i = i - numberOfAttachView.get(j);
						doContinue = true;
						break;
					}
				}
				if(doContinue){
					maxWidth = Math.max( x, maxWidth);
					x = 0;
					y += rowHeight + verticalSpacing;
//					rowHeight = 0;
					continue;
				}
				maxWidth = Math.max( x, maxWidth);
				x = 0;
				y += rowHeight + verticalSpacing;
//				rowHeight = 0;
			}
			
//			rowHeight = Math.max( rowHeight, height);
			x += width + horizontalSpacing;
		}
		
		x += getPaddingRight() + getPaddingLeft();
		y += getPaddingTop() + getPaddingBottom();
		setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : Math.max(maxWidth, x),
				heightMode == MeasureSpec.EXACTLY ? heightSize : y + rowHeight);
		
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int availableWidth = r - l - getPaddingRight();
		int rowHeight = 0;
		int x = getPaddingLeft();
		int y = getPaddingTop();
		int count = getChildCount();
		
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			if(child.getVisibility() == View.GONE){
				continue;
			}
			
			final int height = child.getMeasuredHeight();
			rowHeight = Math.max(rowHeight, height);
		}
		
		for(int i = 0; i < count; i++){
			final View child = getChildAt(i);
			if(child.getVisibility() == View.GONE){
				continue;
			}
			
			final int width = child.getMeasuredWidth();
			final int height = child.getMeasuredHeight();
 
			if (x + width > availableWidth){
				boolean doContinue = false;
				for(int j = 0; j < indexOfAttachPreviousView.size(); j++){
					if( i == indexOfAttachPreviousView.get(j) && i > 0){
						i = i - numberOfAttachView.get(j);
						doContinue = true;
						break;
					}
				}
				if(doContinue){
					x = getPaddingLeft();
					y += rowHeight + verticalSpacing;
//					rowHeight = 0;
					continue;
				}
				x = getPaddingLeft();
				y += rowHeight + verticalSpacing;
//				rowHeight = 0;
			}
			
//			rowHeight = Math.max(rowHeight, height);
			child.layout(x, y + rowHeight - height, x + width, y + rowHeight);
			x += width + horizontalSpacing;
			
		}
		
		
	}
	
	public void addAttachPreviousView(int indexOfView , int numberOfView) {
		indexOfAttachPreviousView.add(indexOfView);
		numberOfAttachView.add(numberOfView + 1);
	}
	
	public void removeOneAttachPreviousView() {
		if(indexOfAttachPreviousView.size() > 0
				&& numberOfAttachView.size() > 0) {
			indexOfAttachPreviousView.remove(indexOfAttachPreviousView.size() - 1);
			numberOfAttachView.remove(numberOfAttachView.size() - 1);
		}
	}

	public void clearAttachPreviousView() {
		numberOfAttachView.clear();
		indexOfAttachPreviousView.clear();
	}
	
	private int createMeasureSpec (int size, int maxSize, int parentMode){ 
		switch (size){ 
			case LayoutParams.WRAP_CONTENT:
				return MeasureSpec.makeMeasureSpec(maxSize,
						parentMode == MeasureSpec.UNSPECIFIED ? MeasureSpec.UNSPECIFIED : MeasureSpec.AT_MOST);
 
 
			case LayoutParams.MATCH_PARENT:
				return MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.EXACTLY);
 
 
			default: 
				return MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
		} 
	}

	public int getMaxRowHeight(){
		int maxRowHeight = 0;
		for(int i = 0; i < getChildCount(); i++){
			View view = getChildAt(i);
			int viewHeight = view.getHeight();
			if(viewHeight > maxRowHeight){
				maxRowHeight = viewHeight;
			}
		}
		return maxRowHeight;
	}
}
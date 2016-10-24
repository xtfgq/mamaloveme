package com.netlab.loveofmum.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.view.MotionEvent;

public class ScrollViewForListView extends ScrollView{
	private float mLastY = -1; // save event y
	private float mLastX = -1; // save event x
    public ScrollViewForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
		    public boolean onInterceptTouchEvent(MotionEvent ev) {
		        boolean result = false;
		        
		        switch (ev.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		
		        	mLastX = ev.getX();
		        	mLastY = ev.getY();
		                break;
		        case MotionEvent.ACTION_MOVE:
		                int distanceX =(int) Math.abs( ev.getX() - mLastX);
		                int distanceY = (int) Math.abs(ev.getY()-mLastY);
		                
		                if(distanceX>distanceY&&distanceY>10){
		                        result = true;
		                }else{
		                        result = false;
		                }
		                break;
		
		        default:
		                break;
		        }
		        
		        return result;
		}
}

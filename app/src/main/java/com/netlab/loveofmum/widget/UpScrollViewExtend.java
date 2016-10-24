package com.netlab.loveofmum.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.jingchen.pulltorefresh.pullableview.Pullable;

/**
 * Created by Dell on 2016/3/15.
 */
public class UpScrollViewExtend extends ScrollView implements Pullable {
    public UpScrollViewExtend(Context context)
    {
        super(context);
    }

    public UpScrollViewExtend(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public UpScrollViewExtend(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if(xDistance > yDistance){
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean canPullDown()
    {
        if(xDistance > yDistance){
            return false;
        }  else{
            if (getScrollY() == 0)
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean canPullUp()
    {

            return false;
    }
}

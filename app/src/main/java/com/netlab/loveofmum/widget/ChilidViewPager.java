package com.netlab.loveofmum.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ChilidViewPager extends ViewPager {


    float mDownX,mDownY;
    private TextView tv;
    public ChilidViewPager(Context context) {
        super(context);
    }

    public ChilidViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }
    public ChilidViewPager(Context context,  TextView tv) {

        super(context);
        this.tv=tv;
    }

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent motionEvent ) {
//
//		return true;
//	}

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
                    String day=tv.getText().toString();
                    int weekNum=Integer.valueOf(day.substring(0,day.indexOf("周")));
                    int dayNum=Integer.valueOf(day.substring(day.indexOf("周"),day.length()-1));
                   float dis= mDownX - ev.getX();
                    Log.e("vvvvvvvvvvxxxx",dis+"");
                    if((mDownX - ev.getX())>0&&dayNum==1){

                        weekNum--;
                        dayNum=6;
                        tv.setText(weekNum+"周"+dayNum+"天");
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }else if((mDownX - ev.getX())<0&&dayNum==6){
                        weekNum++;
                        dayNum=1;
                        tv.setText(weekNum+"周"+dayNum+"天");
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }else if((mDownX - ev.getX())>0&&dayNum!=1){
                        dayNum--;
                        tv.setText(weekNum+"周"+dayNum+"天");
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if((mDownX - ev.getX())<0&&dayNum!=6){
                        dayNum++;
                        tv.setText(weekNum+"周"+dayNum+"天");
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

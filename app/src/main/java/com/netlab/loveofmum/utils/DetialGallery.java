package com.netlab.loveofmum.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created by Administrator on 2016/7/29.
 */
public class DetialGallery extends Gallery {
    public DetialGallery(Context context , AttributeSet attrSet) {
        super(context,attrSet);
        // TODO Auto-generated constructor stub
    }
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2)
    {
        return e2.getX() > e1.getX();
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
//         return super.onFling(e1, e2, 0, velocityY);

         return false;
//        //方法二：只去除翻页惯性 注：没有被注释掉的代码实现了开始说的2种效果。
//        int kEvent;
//        if(isScrollingLeft(e1, e2)){
//            //Check if scrolling left
//            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
//        } else{
//            //Otherwise scrolling right
//            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
//        }
//        onKeyDown(kEvent, null);
//        return true;
    }
}

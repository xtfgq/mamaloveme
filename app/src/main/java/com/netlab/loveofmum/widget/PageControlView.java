package com.netlab.loveofmum.widget;


import com.netlab.loveofmum.R;
import com.netlab.loveofmum.widget.ScrollLayout.OnScreenChangeListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PageControlView extends LinearLayout {
	private Context context;

	private int count;
	private float mScale;
	int currentNum;
	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}

	public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
		this.count=scrollViewGroup.getChildCount();
		System.out.println("count="+count);
//		generatePageControl(scrollViewGroup.getCurrentScreenIndex());
		
		scrollViewGroup.setOnScreenChangeListener(new OnScreenChangeListener() {
			
			public void onScreenChange(int currentIndex) {
				// TODO Auto-generated method stub
				generatePageControl(currentIndex);
			}
		});
	}

	public PageControlView(Context context) {
		super(context);
		this.init(context);
	}
	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
		mScale = context.getResources().getDisplayMetrics().density;
	}

	private void init(Context context) {
		this.context=context;
	}

	private void generatePageControl(int currentIndex) {
		this.removeAllViews();

		int pageNum = 3; // 显示多少个 
    	int pageNo = currentIndex+1; //第几页
		
		int pageSum = this.count; //总共多少页
		
		
		if(pageSum>1){
			
			int currentNum = (pageNo % pageNum == 0 ? (pageNo / pageNum) - 1  
	                 : (int) (pageNo / pageNum))   
	                 * pageNum; 
//			
//			 if (currentNum < 0)   
//	             currentNum = 0;   
			 
//			 if (pageNo > pageNum){
//				 ImageView imageView = new ImageView(context);
//				 imageView.setImageResource(R.drawable.zuo);
//				 this.addView(imageView);
//			 }
			 
			 
			 
			 for (int i = 0; i < pageNum; i++) {   
	             if ((currentNum + i + 1) > pageSum || pageSum < 2)   
	                 break;   
	             
	             ImageView imageView = new ImageView(context);
	         	int imageParams = (int) (mScale * 20+ 0.5f);
				int imagePadding = (int) (mScale * 5 + 0.5f);
	 			imageView.setLayoutParams(new LayoutParams(imageParams, imageParams));
	             if(currentNum + i + 1 == pageNo){
	            	 imageView.setImageResource(R.drawable.dot_yun_focus);
	             }else{
	            	 imageView.setImageResource(R.drawable.dot_yun_blur);
	             }
	             this.addView(imageView);
	         }   
			 
//			 if (pageSum > (currentNum + pageNum)) {
//				 ImageView imageView = new ImageView(context);
//				 imageView.setImageResource(R.drawable.you);
//				 this.addView(imageView);
//			 }
		}
	}

}

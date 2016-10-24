package com.netlab.loveofmum.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.netlab.loveofmum.R;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * @author xiaanming
 *
 */
public class RoundProgressBar extends View {
	

//	protected void dddd(Canvas canvas) {
//		super.onDraw(canvas);
//		
//		/**
//		 * 画最外层的大圆环
//		 */
//		int centre = getWidth()/2; //获取圆心的x坐标
//		int radius = (int) (centre - roundWidth/2); //圆环的半径
//		paint.setColor(roundColor); //设置圆环的颜色
//		paint.setStyle(Paint.Style.STROKE); //设置空心
//		paint.setStrokeWidth(roundWidth); //设置圆环的宽度
//		paint.setAntiAlias(true);  //消除锯齿 
//		canvas.drawCircle(centre, centre, radius, paint); //画出圆环
//		
//		Log.e("log", centre + "");
//		
//		/**
//		 * 画进度百分比
//		 */
//		paint.setStrokeWidth(0); 
//		paint.setColor(textColor);
//		paint.setTextSize(textSize);
//		paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
//		int percent = (int)(((float)progress / (float)max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
//		//float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
//		float textWidth2 = paint.measureText(textUp);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
//		
//		float textWidth = paint.measureText(textDown);   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
//		
//		if(textIsDisplayable && percent != 0 && style == STROKE){
////			canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //画出进度百分比
//			canvas.drawText(textDown, centre - textWidth / 2, (float)(centre/0.85) + textSize/2, paint); //画出进度百分比
//			canvas.drawText(textUp, centre - textWidth2 / 2 , (float)(centre/1.3) + textSize/2, paint); //画出进度百分比
//		}
//		
//		
//		/**
//		 * 画圆弧 ，画圆环的进度
//		 */
//		
//		//设置进度是实心还是空心
//		paint.setStrokeWidth(roundWidth); //设置圆环的宽度
//		paint.setColor(roundProgressColor);  //设置进度的颜色
//		RectF oval = new RectF(centre - radius, centre - radius, centre
//				+ radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
//		
//		switch (style) {
//		case STROKE:{
//			paint.setStyle(Paint.Style.STROKE);
//			canvas.drawArc(oval, -90, 360 * progress / max, false, paint);  //根据进度画圆弧
//			break;
//		}
//		case FILL:{
//			paint.setStyle(Paint.Style.FILL_AND_STROKE);
//			if(progress !=0)
//				canvas.drawArc(oval, -90, 360 * progress / max, true, paint);  //根据进度画圆弧
//			break;
//		}
//		}
//		circleStrokeWidth
//	}
	
	 private RectF mColorWheelRectangle = new RectF();
		private Paint mDefaultWheelPaint;
		private Paint mColorWheelPaint;
		private Paint textPaint1;
		private Paint textPaint2;
		
		private float mColorWheelRadius;
		private float circleStrokeWidth;
		private float pressExtraStrokeWidth;
		private String mText;
		
	

		private String mText2;
		
		
		private int mCount;
		private float mSweepAnglePer;
		private float mSweepAngle;
		private int mTextSize;
		BarAnimation anim;
		public RoundProgressBar(Context context) {
			super(context);
			init(null, 0);
		}

		public RoundProgressBar(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(attrs, 0);
		}

		public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init(attrs, defStyle);
		}

		
		private void init(AttributeSet attrs, int defStyle) {

			circleStrokeWidth = dip2px(getContext(), 5);
			pressExtraStrokeWidth = dip2px(getContext(), 2);
			mTextSize = dip2px(getContext(), 28);

			mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

//			mColorWheelPaint.setColor(0xffffffff);

			mColorWheelPaint.setColor(0xffffffff);
			mColorWheelPaint.setStyle(Style.STROKE);
			mColorWheelPaint.setStrokeWidth(pressExtraStrokeWidth);

			mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

//			mDefaultWheelPaint.setColor(0x22ffffff);
			mDefaultWheelPaint.setColor(0x00000000);
//			mDefaultWheelPaint.setColor(0xfff16892);
			mDefaultWheelPaint.setStyle(Style.STROKE);
			mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);

			textPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
			textPaint1.setColor(0x00000000);
			textPaint1.setStyle(Style.FILL_AND_STROKE);
			textPaint1.setTextAlign(Align.RIGHT);

			textPaint1.setTextSize(40);
			textPaint1.setTypeface(Typeface.DEFAULT_BOLD);

			textPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
			textPaint2.setColor(0x00000000);
			textPaint2.setStyle(Style.FILL_AND_STROKE);
			textPaint2.setTextAlign(Align.RIGHT);
			textPaint2.setTextSize(58);
			textPaint2.setTypeface(Typeface.DEFAULT_BOLD);
			//mText = "0";
			mSweepAngle = 0;

			anim = new BarAnimation();
			anim.setDuration(2000);
			
			
		}
		
		public Paint getTextPaint1() {
			return textPaint1;
		}

		public void setTextPaint1(Paint textPaint1) {
			this.textPaint1 = textPaint1;
		}

		public Paint getTextPaint2() {
			return textPaint2;
		}

		public void setTextPaint2(Paint textPaint2) {
			this.textPaint2 = textPaint2;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int centre = getWidth()/2; //获取圆心的x坐标
		
			canvas.drawArc(mColorWheelRectangle, -90, 360, false, mDefaultWheelPaint);
			canvas.drawArc(mColorWheelRectangle, -90, mSweepAnglePer, false, mColorWheelPaint);
//			Rect bounds = new Rect();
//			Rect bounds2 = new Rect();
////			String textstr=mCount+"";
//
//			textPaint1.getTextBounds(mText, 0, mText.length(), bounds);
//
//			textPaint2.getTextBounds(mText2, 0, mText2.length(), bounds2);
//			canvas.drawText(
//					mText,
//					(mColorWheelRectangle.centerX())
//							+ (textPaint1.measureText(mText) / 2),
//					mColorWheelRectangle.centerY() - (float)(bounds.height() / 1.5),
//					textPaint1);
//
//			canvas.drawText(
//					mText2,
//					(mColorWheelRectangle.centerX())
//							+(textPaint2.measureText(mText2) / 2),
//					mColorWheelRectangle.centerY() + (float)(bounds2.height() / 0.8),
//					textPaint2);
//			canvas.drawText(
//					mText,
//					(mColorWheelRectangle.centerX())
//							- (textPaint1.measureText(mText) / 2),
//					mColorWheelRectangle.centerY() - (float)(bounds.height() / 1.5),
//					textPaint1);
//			
//			canvas.drawText(
//					mText2,
//					(mColorWheelRectangle.centerX())
//							- (textPaint2.measureText(mText2) / 2),
//					mColorWheelRectangle.centerY() + (float)(bounds2.height() / 0.8),
//					textPaint2);
			
			//canvas.drawText(mText, centre - textWidth / 2, (float)(centre/0.85) + textSize/2, paint); //画出进度百分比
			//canvas.drawText(textUp, centre - textWidth2 / 2 , (float)(centre/1.3) + textSize/2, paint); //画出进度百分比
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int height = getDefaultSize(getSuggestedMinimumHeight(),
					heightMeasureSpec);
			int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
			int min = Math.min(width, height);
			setMeasuredDimension(min, min);
			mColorWheelRadius = min - circleStrokeWidth -pressExtraStrokeWidth ;

			mColorWheelRectangle.set(circleStrokeWidth+pressExtraStrokeWidth, circleStrokeWidth+pressExtraStrokeWidth,
					mColorWheelRadius, mColorWheelRadius);
		}
		
		
//		@Override
//	    public void setPressed(boolean pressed) {
//
//	        if (pressed) {
//	        	mColorWheelPaint.setColor(0xFF165da6);
//	    		textPaint.setColor(0xFF070707);
//	    		mColorWheelPaint.setStrokeWidth(circleStrokeWidth+pressExtraStrokeWidth);
//	    		mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth+pressExtraStrokeWidth);
//	    		textPaint.setTextSize(mTextSize-pressExtraStrokeWidth);
//	        } else {
//	        	mColorWheelPaint.setColor(0xFF29a6f6);
//	    		textPaint.setColor(0xFF333333);
//	    		mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
//	    		mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
//	    		textPaint.setTextSize(mTextSize);
//	        }
//	        super.setPressed(pressed);
//	        this.invalidate();
//	    }
		
		public void startCustomAnimation(){
			this.startAnimation(anim);
		}
	    
		public void setText(String text){
			mText = text;
			this.startAnimation(anim);
		}
		
		public void setText2(String text){
			mText2 = text;
			this.startAnimation(anim);
		}
		
		public void setSweepAngle(float sweepAngle){
			mSweepAngle = sweepAngle;
			
		}
		
	    
	    public class BarAnimation extends Animation {
	    	/**
	    	 * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
	    	 * @param view The view to animate
	    	 * @param type The type of animation: 0 will expand from gone and 0 size to visible and layout size defined in xml.
	    	 * 1 will collapse view and set to gone
	    	 */
	    	public BarAnimation() {

	    	}

	    	@Override
	    	protected void applyTransformation(float interpolatedTime, Transformation t) {
	    		super.applyTransformation(interpolatedTime, t);
	    		if (interpolatedTime < 1.0f) {
	    			mSweepAnglePer =  interpolatedTime * mSweepAngle;
	    			//mCount = (int)(interpolatedTime * Float.parseFloat(mText));
	    		} else {
	    			mSweepAnglePer = mSweepAngle;
	    			//mCount = Integer.parseInt(mText);
	    		}
	    		postInvalidate();  
	    	}
	    }
	    
	    public static int dip2px(Context context, float dipValue){
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int)(dipValue * scale + 0.5f);
	    }
}

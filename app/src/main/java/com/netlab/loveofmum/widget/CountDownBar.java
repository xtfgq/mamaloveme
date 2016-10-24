package com.netlab.loveofmum.widget;

import java.util.Date;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.utils.SystemUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CountDownBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 开始时间
	 */
	private Long start;
	/**
	 * 圆环的颜色
	 */
	private int roundColor;

	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;

	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	/**
	 * 最大进度
	 */
	private int max;
	private boolean first = true;
	/**
	 * 当前进度
	 */
	private int progress;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;

	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;
	/**
	 * 没有进行咨询时的时间
	 */
	Long m;
	private TextView tvnum;

	private Boolean isStart = false;
	private long current = 0l;
	private long deloyTime = 0l;
	private long endTime = 0l;
	private long distanceTime = 0l;
	private ImageView iv;

	public static final int STROKE = 0;
	public static final int FILL = 1;

	public CountDownBar(Context context) {
		this(context, null);
	}

	public CountDownBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CountDownBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		paint = new Paint();

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CountProgressBar);

		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(
				R.styleable.CountProgressBar_CountroundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(
				R.styleable.CountProgressBar_CountroundProgressColor,
				Color.GREEN);
		textColor = mTypedArray.getColor(
				R.styleable.CountProgressBar_CounttextColor, Color.GREEN);
		textSize = mTypedArray.getDimension(
				R.styleable.CountProgressBar_CounttextSize, 2);
		roundWidth = mTypedArray.getDimension(
				R.styleable.CountProgressBar_CountroundWidth, 5);
		max = mTypedArray
				.getInteger(R.styleable.CountProgressBar_Countmax, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.CountProgressBar_CounttextIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.CountProgressBar_Countstyle, 0);

		mTypedArray.recycle();
	}

	public void setEndTime(long endTime, long deloyTime, ImageView iv,
						   long curent, long starttime, TextView tvnum) {
		this.endTime = endTime;
		this.deloyTime = deloyTime;
		this.iv = iv;

		this.current = curent;
		this.tvnum=tvnum;
		progress = 0;
		this.start = starttime;
		if (current < starttime) {
			isStart = false;
		} else {
			isStart = true;
		}

		setProgress(progress);
		startTimer();
	}

	public void setNoStartTime() {

		setProgress(100);

		// startTimer();
	}

	private void startTimer() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (progress <= 100) {

					if (isStart) {
						distanceTime = endTime - System.currentTimeMillis();
						float dism = ((float) distanceTime / (float) deloyTime) * 100;
						if (dism > 0 && dism < 1) {
							progress = 99;
						} else {
							if (dism < 0) {
								progress = 100;
							} else {
								progress = 100 - (int) (((float) distanceTime / (float) deloyTime) * 100);
							}
						}

						setProgress(progress);
					} else {
//						current=System.currentTimeMillis();
						distanceTime = start - current;
						
						m = start - System.currentTimeMillis();
						long n = distanceTime;
						float dism = (float) m / (float) n * 100;
						if (dism > 0 && dism < 1) {
							progress = 99;
						} else {
							if (dism < 0) {
								progress = 100;
							} else {
								int p = (int) ((float) m / (float) n * 100);
								progress = 100 - p;
							}
						}
						setProgress(progress);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	// /**
	// * deal time string
	// *
	// * @param time
	// * @return
	// */
	public static String dealTime(long time) {
		StringBuffer returnString = new StringBuffer();
		long days = time / (24 * 60 * 60);
		long hours = days * 24 + (time % (24 * 60 * 60)) / (60 * 60);
		long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
		long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
		String hourStr = timeStrFormat(String.valueOf(hours));
		String minutesStr = timeStrFormat(String.valueOf(minutes));
		String secondStr = timeStrFormat(String.valueOf(second));
		returnString.append(hours).append("小时").append(minutesStr).append("分")
				.append(secondStr).append("秒");
		return returnString.toString();
	}

	/**
	 * format time
	 * 
	 * @param timeStr
	 * @return
	 */
	private static String timeStrFormat(String timeStr) {
		switch (timeStr.length()) {
		case 1:
			timeStr = "0" + timeStr;
			break;
		}
		return timeStr;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * 画最外层的大圆环
		 */
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int) (centre - roundWidth / 2); // 圆环的半径
		paint.setColor(roundColor); // 设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, centre, radius, paint); // 画出圆环

		Log.e("log", centre + "");

		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体

		int percent = (int) (((float) progress / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
		float textWidth = paint.measureText(dealTime(distanceTime / 1000)); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		// float textWidth = paint.measureText(percent+"%");
		if (textIsDisplayable && percent != 0 && style == STROKE) {

			if (percent == 100) {

				// canvas.drawText("", centre - textWidth / 2, centre +
				// textSize/2, paint); //画出进度百分比
				if (System.currentTimeMillis() >= start
						&& System.currentTimeMillis() <= endTime) {
					isStart = true;

					setProgress(0);
					startTimer();
					tvnum.setText("目前参与人数");
					// canvas.drawText("", centre - textWidth / 2, centre +
					// textSize/2, paint); //画出进度百分比
				} else if (isStart) {
					canvas.drawText("已结束", centre - textWidth / 4, centre
							+ textSize / 2, paint); // 画出进度百分比
					iv.setImageResource(R.drawable.peoplecyan);
					tvnum.setText("本次参与人数");
				}
			} else {

				if (System.currentTimeMillis() <= start) {
					canvas.drawText("倒计时", centre - textWidth / 4, centre
							- textSize, paint);
					String sub = dealTime(m / 1000);

					canvas.drawText(sub.substring(0, sub.indexOf("小时") + 2),
							centre + 1 - textWidth / 4, centre, paint);
					canvas.drawText(
							sub.substring(sub.indexOf("小时") +2, sub.length()),
							centre + 1 - textWidth / 3, centre + textSize,
							paint);
					tvnum.setText("目前参与人数");
				} else {
					canvas.drawText("进行中", centre + 1 - textWidth / 4, centre
							- textSize, paint);
					String sub = dealTime(distanceTime / 1000);

					canvas.drawText(sub.substring(0, sub.indexOf("小时") + 2),
							centre + 1 - textWidth / 4, centre, paint);
					canvas.drawText(
							sub.substring(sub.indexOf("小时")+2 , sub.length()),
							centre + 1 - textWidth / 3, centre + textSize,
							paint);
					tvnum.setText("目前参与人数");

				}
				// 画出进度百分比
			}

		}

		/**
		 * 画圆弧 ，画圆环的进度
		 */

		// 设置进度是实心还是空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(roundProgressColor); // 设置进度的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			if (isStart) {
				if (progress == max) {
					paint.setColor(Color.parseColor("#9AE0E0"));
					canvas.drawArc(oval, -90, 360, false, paint); // 根据进度画圆弧
				} else {
					canvas.drawArc(oval, -90, 360 * progress / max, false,
							paint); // 根据进度画圆弧
				}
			}
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0) {

				if (progress == max) {
					paint.setColor(Color.parseColor("#9AE0E0"));
					canvas.drawArc(oval, -90, 360, true, paint); // 根据进度画圆弧
				} else {
					canvas.drawArc(oval, -90, 360 * progress / max, true, paint); // 根据进度画圆弧
				}
			}
		}
			break;
		}
	}

	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max) {
			progress = max;

		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}

	}

	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}

}

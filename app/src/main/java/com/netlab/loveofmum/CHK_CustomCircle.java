package com.netlab.loveofmum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CHK_CustomCircle extends View {
	private float cirX = 500, cirY = 500;
	private Paint xPaint;
	private Paint sPaint;
	private int colors[] = { Color.BLUE, Color.GRAY, Color.GREEN, Color.YELLOW };
	private float pros[] = new float[] { 20, 30, 15, 35 };

	public CHK_CustomCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		/** 实体圆Paint初始化 */
		sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// sPaint.setStyle(Paint.Style.STROKE);
		// sPaint.setStrokeWidth(200);
		// sPaint.setColor(Color.YELLOW);

		/** 虚线圆Paint初始化 */
		xPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		xPaint.setColor(Color.WHITE);
		xPaint.setStyle(Paint.Style.STROKE);
		xPaint.setStrokeWidth(10);
		PathEffect effect = new DashPathEffect(new float[] { 40, 40, 40, 40 },
				1);
		xPaint.setPathEffect(effect);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		float curAngle = 0.0f;
		float proAngle = 0.0f;
		RectF arct = new RectF(0, 0, 1000, 1000);
		for (int i = 0; i < pros.length; i++) {
			proAngle = (pros[i] / 100) * 360;
			proAngle = (float) (Math.round(proAngle * 100)) / 100;

			sPaint.setColor(colors[i]);
			canvas.drawArc(arct, curAngle, proAngle, true, sPaint);
			curAngle += proAngle;
		}

		sPaint.setColor(Color.WHITE);
		canvas.drawCircle(500, 500, 300, sPaint);
		       
		canvas.drawCircle(cirX, cirY, 400, xPaint);
	}

}

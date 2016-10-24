package com.netlab.loveofmum;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CHK_instrument extends Activity {
	private ImageView needleView; // 指针图片
	private Timer timer; // 时间
	private EditText degreeText;
	private TextView showText;
	private Button degreeBtn;
	private float maxDegree = 0.0f;
	private float degree = 0.0f; // 记录指针旋转
	private RotateAnimation animation;
	private boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_pregnancy_weight_statistics);

		needleView = (ImageView) findViewById(R.id.needle);
		degreeText = (EditText) findViewById(R.id.degreeID2);
		degreeBtn = (Button) findViewById(R.id.degreeButton);
		showText=(TextView)findViewById(R.id.degreeID);
		degreeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				maxDegree = Float.parseFloat(degreeText.getText().toString()
						.trim());
				// 开始转动
				timer = new Timer();
				// 设置每100毫秒转动一下
				timer.schedule(new NeedleTask(), 0, 10);
				showText.setText(degreeText.getText().toString()
						.trim());
				flag = true;
			}
		});
	}

	private class NeedleTask extends TimerTask {
		@Override
		public void run() {
			if (degree <= maxDegree * (271 / 100.0f)) {
				handler.sendEmptyMessage(0);
			}
			if (degree > maxDegree * (271 / 100.0f) && flag == true) {
				handler2.sendEmptyMessage(0);
			}
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置仪表盘指针转动动画
			// 仪表盘最大是271度
			if (degree >= maxDegree * (271 / 100.0f)) {
				timer.cancel();
			}else{
			degree += 5.0f;
			animation = new RotateAnimation(degree, maxDegree * (271 / 100.0f),
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			}
			// 设置动画时间1秒
			animation.setDuration(10);
			animation.setFillAfter(true);
			needleView.startAnimation(animation);
			flag = false;
		}
	};

	private Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) { // 设置仪表盘指针转动动画
			// 仪表盘最大是172度，这个是自己测出来的
			if (degree <= maxDegree * (271 / 100.0f)) {
				timer.cancel();
			}else{
			degree += -5.0f;
			animation = new RotateAnimation(degree, maxDegree * (271 / 100.0f),
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			}
			// 设置动画时间5毫秒
			animation.setDuration(10);
			animation.setFillAfter(true);
			needleView.startAnimation(animation);
			flag = true;
		}
	};

	/**
	 * 关闭计时器对象
	 */
	@Override
	protected void onDestroy() {
		timer.cancel();
		timer = null;
		super.onDestroy();
	}
}

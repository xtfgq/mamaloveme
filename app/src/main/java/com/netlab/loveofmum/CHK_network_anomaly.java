package com.netlab.loveofmum;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.netlab.loveofmum.widget.DialogEnsureDialogCancelView;

public class CHK_network_anomaly extends Activity{
	public static int DIALOG_EXIT=1;
	private Button btn_showDialog;
	ImageView imageView;
	private TextView txtHead;
	private ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setTranslucentStatus();
		setContentView(R.layout.layout_network_anomaly);
		txtHead = (TextView) findViewById(R.id.txtHead);
		txtHead.setText("支付完成");
		imgBack = (ImageView) findViewById(R.id.img_left);
		imgBack.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(CHK_network_anomaly.this,
						MainTabActivity.class);
				i.putExtra("TabIndex", "B_TAB");

				startActivity(i);
				finish();
			}
		});
		
		btn_showDialog=(Button) findViewById(R.id.zixun);
//		
		btn_showDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogEnsureDialogCancelView dialogEnsureCancelView = new DialogEnsureDialogCancelView(
						CHK_network_anomaly.this).setDialogMsg("确定").setOnClickListenerEnsure(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0371-67762868"));
					        startActivity(intent);
							
							}
						});
				DialogUtils.showSelfDialog(CHK_network_anomaly.this, dialogEnsureCancelView);
			}
		});
	}
	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 监听主菜单返回事件  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	Intent i = new Intent(CHK_network_anomaly.this,
					MainTabActivity.class);
			i.putExtra("TabIndex", "B_TAB");

			startActivity(i);
			finish();
            return false;  
        }  
		return super.onKeyDown(keyCode, event);
	}


}



package com.netlab.loveofmum.widget;

import com.netlab.loveofmum.R;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogEnsureView extends MyDialogView{
	private TextView mTextTitle, mTextMsg, mTextEnsure;
	private LinearLayout mLayoutEnsure;
	private OnClickListener mClickListener;
	public DialogEnsureView(Context context) {
		super(context);
		initView();
	}

	public DialogEnsureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	private void initView() {
		removeAllViews();
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.dialog_default_ensure, this, false);
		mTextTitle = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
		mTextMsg = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		mTextEnsure = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
	 mTextEnsure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mClickListener != null) {
						mClickListener.onClick(v);
					}
					if (mExitDialog != null) {
						mExitDialog.exitDialog();
					}
				}
			});
	}
	/**
	 * 确定按钮点击事件
	 */
	public DialogEnsureView setOnClickListenerEnsure(OnClickListener clickListener) {
		mClickListener = clickListener;
	
		return this;
	}
	/**
	 * dialog信息
	 */
	public DialogEnsureView setDialogMsg(String title, String msg, String ensure) {
		if(!TextUtils.isEmpty(title)) {
			mTextTitle.setText(title);
		}
		if(!TextUtils.isEmpty(msg)) {
			mTextMsg.setText(msg);
		}
		if(!TextUtils.isEmpty(ensure)) {
			mTextEnsure.setText(ensure);
		}
	
		return this;
	}
	
	@Override
	public DialogInterface.OnClickListener getPositiveButtonListener() {
		// TODO Auto-generated method stub
		return new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		};
	}

	@Override
	public DialogInterface.OnClickListener getNegativeButtonListener() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  
      {
       if(keyCode==KeyEvent.KEYCODE_BACK)
       {
           return true;
       }
       return false;
      }

}

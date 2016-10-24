package com.netlab.loveofmum.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.netlab.loveofmum.R;

public class DialogCancle extends MyDialogView{
	private TextView mTextTitle, mTextMsg, mTextEnsure, mTextCancel;
	private OnClickListener mClickListenerEnsure, mClickListenerCancle;
	public DialogCancle(Context context) {
		super(context);
		initView();
	}

	public DialogCancle(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	public DialogCancle(Context context, AttributeSet attrs,Boolean isLong) {
		super(context, attrs);
	
		initView();
	}
	private void initView() {
		removeAllViews();
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.dialog_cancle_ensure_click, this, false);
		mTextTitle = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
		mTextMsg = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		mTextEnsure = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		mTextCancel = (TextView) layout.findViewById(R.id.dialog_default_click_cancel);
         mTextEnsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				ToastUtils.showShort(context, "功能待开放");
				if(mClickListenerEnsure != null) {
					mClickListenerEnsure.onClick(v);
				}
				if (mExitDialog != null) {
					mExitDialog.exitDialog();
				}
			}
		});
		
		mTextCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mClickListenerCancle != null) {
					mClickListenerCancle.onClick(v);
				}
				if(mExitDialog != null) {
					mExitDialog.exitDialog();
				}
			}
		});
	
		this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public DialogInterface.OnClickListener getPositiveButtonListener() {
		return null;
	}
	/**
	 * 确定按钮点击事件
	 */
	public DialogCancle setOnClickListenerEnsure(OnClickListener clickListener) {
		mClickListenerEnsure = clickListener;
		/*if(mClickListener != null) {
			mLayoutEnsure.setVisibility(View.VISIBLE);
		}
		else {
			mLayoutEnsure.setVisibility(View.GONE);
		}*/
		return this;
		
	}
	/**
	 * 取消按钮点击事件
	 */
	public DialogCancle setOnClickListenerCancle(OnClickListener clickListener) {
		mClickListenerCancle = clickListener;
		/*if(mClickListener != null) {
			mLayoutEnsure.setVisibility(View.VISIBLE);
		}
		else {
			mLayoutEnsure.setVisibility(View.GONE);
		}*/
		return this;
	}
	/**
	 * 确定按钮点击事件
	 */
	public DialogCancle setDialogMsg(String title, String msg, String ensure) {
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
	public DialogInterface.OnClickListener getNegativeButtonListener() {
		return new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
	
			

				dialog.dismiss();
			}
		};
	}
}

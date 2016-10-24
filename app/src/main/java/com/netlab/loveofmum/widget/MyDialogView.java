package com.netlab.loveofmum.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class MyDialogView extends LinearLayout{
	protected Context context;
	protected ExitDialog mExitDialog;
	
	public interface ExitDialog {
		public void exitDialog();
	}
	
	public void setExitDialog(ExitDialog exitDialog) {
		mExitDialog = exitDialog;
	}
	
	public MyDialogView(Context context) {
		super(context);
		this.context = context;
	}
	
	public MyDialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public boolean ensureInfo(){ return false; };
	public abstract DialogInterface.OnClickListener getPositiveButtonListener();
	public abstract DialogInterface.OnClickListener getNegativeButtonListener();
}

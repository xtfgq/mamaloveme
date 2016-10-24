package com.netlab.loveofmum.utils;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.widget.MyDialogView;
import com.netlab.loveofmum.widget.MyDialogView.ExitDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;

public class DialogUtils {
	/**
	 * 自定义弹框
	 * @param context
	 * @return
	 */
	public static Dialog showSelfDialog(Context context,MyDialogView dialogView){
		final Dialog dialog =  new Dialog(context, R.style.loadingDialog);
		dialogView.setExitDialog(new ExitDialog() {

			@Override
			public void exitDialog() {
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(dialogView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();
		return dialog;
	}
}

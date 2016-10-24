package com.netlab.loveofmum.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Configuration;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class UiUtils {
	/**
	 * 设置选中某一些 下拉框
	 * 
	 * @author 
	 * @param spinner
	 * @param selection
	 * @return void
	 */
	public static void setSelection(Spinner spinner, Object selection) {
		setSelection(spinner, selection.toString());
	}

	/**
	 * 设置选中某一些 下拉框
	 * 
	 * @author 
	 * @param spinner
	 * @param selection
	 * @return void
	 */
	public static void setSelection(Spinner spinner, String selection) {
		final int count = spinner.getCount();
		for (int i = 0; i < count; i++) {
			String item = spinner.getItemAtPosition(i).toString();
			if (item.equalsIgnoreCase(selection)) {
				spinner.setSelection(i);
			}
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @author 
	 * @param view
	 * @return void
	 */
	public static void hideSoftKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 显示软键盘
	 * 
	 * @author 
	 * @param view
	 * @return void
	 */
	public static void showSoftkeyboard(View view) {
		showSoftkeyboard(view, null);
	}

	/**
	 * 显示软键盘
	 * 
	 * @author guoqiangandroid@gmail.com 2013-10-12 下午3:47:19
	 * @param view
	 * @param resultReceiver
	 * @return void
	 */
	public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
		Configuration config = view.getContext().getResources().getConfiguration();
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			InputMethodManager imm = (InputMethodManager) view.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (resultReceiver != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT,
						resultReceiver);
			} else {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}
	public static void imageLLViewReset(View imageView, int bitmapW, int bitmapH, boolean isFull,Context pContext) {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
		HashMap<String, Integer> data = SysUtils.getDisplayMetrics(pContext);
		int width = data.get(SysUtils.systemWidth);// 540
		int height = data.get(SysUtils.systemHeight);//960
		if (isFull) {
			if (width > height) {
				layoutParams.width = (int) (bitmapW * 1.00f / bitmapH * height);
				layoutParams.height = height;
			} else {
				layoutParams.width = width;
				layoutParams.height = (int) (bitmapH * 1.00f / bitmapW * width);
			}
		} else {
			if (bitmapW > width) {
				layoutParams.width = width;
				layoutParams.height = (int) (width * 1.00f / bitmapW * bitmapH);
			} else {
				layoutParams.width = bitmapW;
				layoutParams.height = bitmapH;
			}
		}
		imageView.setLayoutParams(layoutParams);
	}
	public static void imageRLViewReset(View imageView, int bitmapW, int bitmapH, boolean isFull,Context pContext) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
		HashMap<String, Integer> data = SysUtils.getDisplayMetrics(pContext);
		int width = data.get(SysUtils.systemWidth);
		int height = data.get(SysUtils.systemHeight);
		if (isFull) {
			if (width > height) {
				layoutParams.width = (int) (bitmapW * 1.00f / bitmapH * height);
				layoutParams.height = height;
			} else {
				layoutParams.width = width;
				layoutParams.height = (int) (bitmapH * 1.00f / bitmapW * width);
			}
		} else {
			if (bitmapW > width) {
				layoutParams.width = width;
				layoutParams.height = (int) (width * 1.00f / bitmapW * bitmapH);
			} else {
				layoutParams.width = bitmapW;
				layoutParams.height = bitmapH;
			}
		}
		imageView.setLayoutParams(layoutParams);
	}
	public static void imageFLViewReset(View imageView, int bitmapW, int bitmapH, boolean isFull,Context pContext) {
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
		HashMap<String, Integer> data = SysUtils.getDisplayMetrics(pContext);
		int width = data.get(SysUtils.systemWidth);
		int height = data.get(SysUtils.systemHeight);
		if (isFull) {
			if (width > height) {
				layoutParams.width = (int) (bitmapW * 1.00f / bitmapH * height);
				layoutParams.height = height;
			} else {
				layoutParams.width = width;
				layoutParams.height = (int) (bitmapH * 1.00f / bitmapW * width);
			}
		} else {
			if (bitmapW > width) {
				layoutParams.width = width;
				layoutParams.height = (int) (width * 1.00f / bitmapW * bitmapH);
			} else {
				layoutParams.width = bitmapW;
				layoutParams.height = bitmapH;
			}
		}
		imageView.setLayoutParams(layoutParams);
	}
	public static void resetRL(Context pContext,View... view) {
		float rote = SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
			layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
			layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
			layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
			view2.setLayoutParams(layoutParams);
		}
	}
	/**
	 * 根据分辨率设置按钮的大小
	 * 
	 * @author 
	 * @param view
	 * @return void
	 */
	public static void resetRLBack(Context pContext,View... view) {
		float rote =SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.height = (int) (layoutParams.height * rote);
			layoutParams.width = (int) (layoutParams.width * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	public static void resetLL(Context pContext,View... view) {
		float rote =SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
			layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
			layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
			layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
			view2.setLayoutParams(layoutParams);
		}
	}
	/**
	 * 根据分辨率设置按钮的大小
	 * 
	 * @author 
	 * @param view
	 * @return void
	 */
	public static void resetLLBack(Context pContext,int height,View... view) {
		float rote =SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.height = height;
			layoutParams.width = (int) (layoutParams.width * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	/**
	 * 根据分辨率设置按钮的大小
	 * 
	 * @author 
	 * @param view
	 * @return void
	 */
	public static void resetLLBack(Context pContext,View... view) {
		float rote =SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.height = (int) (layoutParams.height * rote);
			layoutParams.width = (int) (layoutParams.width * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	public static void resetFL(Context pContext,View... view) {
		float rote =SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
			layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
			layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
			layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	/**
	 * 根据分辨率设置透明按钮的大小
	 * 
	 * @author
	 * @param view
	 * @return void
	 */
	public static void resetFLBack(Context pContext,View... view) {
		float rote =SysUtils.getWidthRoate(pContext);
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view2.getLayoutParams();
			layoutParams.height = (int) (layoutParams.height * rote);
			layoutParams.width = (int) (layoutParams.width * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

}

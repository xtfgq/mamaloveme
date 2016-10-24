package com.netlab.loveofmum.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class SystemUtils {
	private static Activity mActivity = null;

	public SystemUtils(Activity activity) {
		mActivity = activity;
	}
	/** 是否安装微信 */
	public boolean isInstallWx(String packageName) {
		try {

			PackageManager manager = mActivity.getPackageManager();

			PackageInfo info = manager.getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);

			if (info != null) {

				return true;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}


}

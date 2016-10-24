package com.netlab.loveofmum.utils;

import java.util.HashMap;

import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.MyApp;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

public class SysUtils {
	private static String mIMEI;
	private static String mSIM;
	private static String mMobileVersion;
	
	private static String mDeviceID;
	
	public static final String systemWidth = "width";
	public static final String systemHeight = "height";
	private static HashMap<String, Integer> map;

	/**
	 * 获得序列号码
	 * @param pContext
	 * @return
	 */
	
	public static String getSim(Context pContext){
		mSIM=((TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
		return mSIM;
		
	}
	private static long lastClickTime;
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if ( time - lastClickTime < 3000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	 /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
	 public static String getMetaData(Context context, String key) {
	       try {

	           ApplicationInfo  ai = context.getPackageManager().getApplicationInfo(
	                  context.getPackageName(), PackageManager.GET_META_DATA);
	           Object value = ai.metaData.get(key);

	           if (value != null) {

	              return value.toString();

	           }

	       } catch (Exception e) {

	           //

	       }

	       return null;

	    }
	/**
	 * 获得imei号码
	 * @param pContext
	 * @return
	 */
	public static String getImei(Context pContext){
		mIMEI=((TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(); 
		return mIMEI;
	}
	/**
	 * 获得设备号码
	 * @param pContext
	 * @return
	 */
	public static String getDeviceId(Context pContext){
		mDeviceID=((TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(); 
		return mDeviceID;
	}
	public static String getSystemVersion(Context pContext){
		mMobileVersion=((TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceSoftwareVersion(); 
		return mMobileVersion;
	}
	/**
	  * @author guoqiang
	  * @param context
	  * @判断当前网络类型
	  */
	 public static String getCurrentNetType(Context pContext) { 
		    String type = ""; 
		    ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE); 
		    NetworkInfo info = cm.getActiveNetworkInfo(); 
		    if (info == null) { 
		        type = "null"; 
		    } else if (info.getType() == ConnectivityManager.TYPE_WIFI) { 
		        type = "wifi"; 
		    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) { 
		        int subType = info.getSubtype(); 
		        if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS 
		                || subType == TelephonyManager.NETWORK_TYPE_EDGE) { 
		            type = "2g"; 
		        } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
		        		|| subType == TelephonyManager.NETWORK_TYPE_HSDPA 
		                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
		                || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 
		                || subType == TelephonyManager.NETWORK_TYPE_EVDO_B|| 
		                subType == TelephonyManager.NETWORK_TYPE_HSPA
		                ||subType == TelephonyManager.NETWORK_TYPE_HSUPA
		                ||subType == TelephonyManager.NETWORK_TYPE_EHRPD
		                ||subType == TelephonyManager.NETWORK_TYPE_HSPAP) { 
		            type = "3g"; 
		        } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
		            type = "4g"; 
		        } else{
		        	type=info.getType()+"";
		        }
		    } 
		    return type; 
		}
	  /**
		 * @author  guoqiangandroid@gmail.com
		 * @Title: getDisplayMetrics
		 * @Description: 获取屏幕的分辨率
		 * @param @param cx
		 * @param @return 设定文件
		 * @return HashMap<String,Integer> 返回类型
		 */
		public static HashMap<String, Integer> getDisplayMetrics(Context pContext) {
			if (map == null) {
				map = new HashMap<String, Integer>();
				 WindowManager manager = (WindowManager) pContext  
				            .getSystemService(Context.WINDOW_SERVICE);  
				 Display display = manager.getDefaultDisplay();  
				int screenWidth = display.getWidth();
				int screenHeight = display.getHeight();
				map.put(systemWidth, screenWidth);
				map.put(systemHeight, screenHeight);
			}
			return map;
		}
		/**
		 * 获取屏幕宽度缩放率
		 * @author guoqiangandroid@gmail.com
		 * @param width
		 * @return float
		 */
		public static float getWidthRoate(Context pContext) {
			if (map == null) {
				map = new HashMap<String, Integer>();
				 WindowManager manager = (WindowManager) pContext  
				            .getSystemService(Context.WINDOW_SERVICE);  
				 Display display = manager.getDefaultDisplay();  
				int screenWidth = display.getWidth();
				int screenHeight = display.getHeight();
				map.put(systemWidth, screenWidth);
				map.put(systemHeight, screenHeight);
			}
			return (map.get(systemWidth) * 1.00f) / MyApplication.getInstance().getMode_w();
		}
		public static float getRoate(Context pContext) {
			if (map == null) {
				map = new HashMap<String, Integer>();
				 WindowManager manager = (WindowManager) pContext  
				            .getSystemService(Context.WINDOW_SERVICE);  
				 Display display = manager.getDefaultDisplay(); 
				int screenWidth = display.getWidth();
				int screenHeight = display.getHeight();
				map.put(systemWidth, screenWidth);
				map.put(systemHeight, screenHeight);
			} 
			float w = (map.get(systemWidth) * 1.00f) / MyApplication.getInstance().getMode_w();
			float h = (map.get(systemHeight) * 1.00f) / MyApplication.getInstance().getMode_h();
			return w>h?w:h;
		}
		/**
		 * 获取屏幕高度缩放率
		 * @author 
		 * @param height
		 * @return float
		 */
		public static float getHeightRoate(Context pContext) {
			if (map == null) {
				map = new HashMap<String, Integer>();
				 WindowManager manager = (WindowManager) pContext  
				            .getSystemService(Context.WINDOW_SERVICE);  
				Display display = manager.getDefaultDisplay(); 
				int screenWidth = display.getWidth();
				int screenHeight = display.getHeight();
				map.put(systemWidth, screenWidth);
				map.put(systemHeight, screenHeight);
			} 
			return (map.get(systemHeight) * 1.00f) /  MyApplication.getInstance().getMode_h();
		}
		/**
		 * dp转px
		 * @author 
		 * @param dipValue
		 * @return int
		 */
		public static int dip2px(float dipValue,Context pContext) {
			final float scale = pContext.getApplicationContext().getResources().getDisplayMetrics().density;
			return (int) (dipValue * scale + 0.5f);
		}

		/**
		 * px转dip
		 * TODO(这里用一句话描述这个方法的作用)
		 * @author 
		 * @param pxValue
		 * @return
		 * @return int
		 */
		public static int px2dip(float pxValue,Context pContext) {
			final float scale =pContext.getApplicationContext().getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}
}

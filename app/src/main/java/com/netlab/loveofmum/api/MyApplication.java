package com.netlab.loveofmum.api;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;

import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

 
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Environment;

public class MyApplication extends Application {
    
	    private List<Activity> activitys = null;
	    private static MyApplication instance;
//	    public static Context appContext;
	    public String DoctorId,Hosid,DoctorUrl,HosName,Title,DoctorName,nowtime;

	  
	    private MyApplication() {
	        activitys = new LinkedList<Activity>();
	    }
	public String urls="";
	public int getMode_w() {
		return mode_w;
	}

	public void setMode_w(int mode_w) {
		this.mode_w = mode_w;
	}

	public int getMode_h() {
		return mode_h;
	}

	public void setMode_h(int mode_h) {
		this.mode_h = mode_h;
	}

	private int mode_w = 720;
	private int mode_h = 1280;
	    @Override
		public void onCreate()
		{
			// TODO Auto-generated method stub
			super.onCreate();
//			CrashHandler handler = CrashHandler.getInstance();
//	        handler.init(getApplicationContext());
//			EMChat.getInstance().init(appContext);
		}
	

		/**
	     * 单例模式中获取唯一的MyApplication实例
	     * 
	     * @return
	     */
	    public static MyApplication getInstance() {
	        if (null == instance) {
	            instance = new MyApplication();
	        }
	        return instance;
	 
	    }
	/**
	 * 结束指定的Activity
	 */
	public void killActivity(Activity activity) {
		if (activity != null) {
			activitys.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	 
	    // 添加Activity到容器中
	    public void addActivity(Activity activity) {
	        if (activitys != null && activitys.size() > 0) {
	            if(!activitys.contains(activity)){
	                activitys.add(activity);
	            }
	        }else{
	            activitys.add(activity);
	        }
	         
	    }
	 
	    // 遍历所有Activity并finish
	    public void exit() {
	        if (activitys != null && activitys.size() > 0) {
	            for (Activity activity : activitys) {
	                activity.finish();
	            }
	        }
	        System.exit(0);
	    }
 
}

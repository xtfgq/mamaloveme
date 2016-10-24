package com.netlab.loveofmum.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class CrashApplication extends Application
{
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "mmlovepic" + File.separator + "images" + File.separator;

  
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		 
//		CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        initImageLoader(getApplicationContext());
      
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//        .cacheInMemory(true)  //1.8.6包使用时候，括号里面传入参数true
//        .cacheOnDisc(true) //1.8.6包使用时候，括号里面传入参数true
//        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//        .bitmapConfig(Bitmap.Config.RGB_565)
//        .build();
//        
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//        .defaultDisplayImageOptions(defaultOptions)
//        .memoryCacheExtraOptions(480, 800)
//        .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据     
//        .threadPriority(Thread.NORM_PRIORITY - 2)
//        .denyCacheImageMultipleSizesInMemory()
//        .discCacheFileNameGenerator(new Md5FileNameGenerator())
//        .tasksProcessingOrder(QueueProcessingType.LIFO)
//        .build();
//        ImageLoader.getInstance().init(config);
	}
	   private void initImageLoader(Context context) {
	    	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.discCache(new UnlimitedDiskCache(new File(IMAGES_FOLDER)))
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.writeDebugLogs()
					// Remove for release app
					.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
					.memoryCacheSize(4 * 1024 * 1024).build();
			
			ImageLoader.getInstance().init(config);

		}

}

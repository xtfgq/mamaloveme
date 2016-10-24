package com.netlab.loveofmum.myadapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.SpaceImageDetailActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.widget.SquareCenterImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class MyCHK_OrderAdd_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;
	
	private List<Map<String, Object>> listData;
	private List<String> datas =new ArrayList<String>();

	private Context context;
	
	//private ImageLoader image;

	public MyCHK_OrderAdd_Adapter(Context context,
			List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context); 
		this.listData = listData;
		this.context = context;
		//this.image = imageloader;
	}

	public int getCount() {
		return listData.size();
	}

	public Object getItem(int position) {
		return listData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public boolean isEnabled(int position) {
		// return super.isEnabled(position);
		return super.isEnabled(position);
	}

	public View getView(final int position, View convertView,
			final ViewGroup parent) {
//		convertView = mInflater.inflate(R.layout.item_myorderadd, null);
//
//		ImageView img = (ImageView)convertView.findViewById(R.id.img001_item_myorderadd);
//		
//		FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
//		
//		fb.display(img,Constants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"));
		convertView = mInflater.inflate(R.layout.item_myorderadd, parent,false);
		final ImageView imageView = (ImageView)convertView.findViewById(R.id.img001_item_myorderadd);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		
//		 DisplayImageOptions options = new DisplayImageOptions.Builder()
//
//         .resetViewBeforeLoading(false) // default
//         .delayBeforeLoading(1000).cacheInMemory(false) // default
//         .considerExifParams(false) // default
//         .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
//         .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//         .displayer(new SimpleBitmapDisplayer()) // default
//         .handler(new Handler()) // default
//         .build();
		ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"), imageView);

		
		
//		FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
////		
//		fb.display(imageView,Constants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"));
		
		for(int i=0;i<listData.size();i++)
		{
			datas.add(MMloveConstants.JE_BASE_URL2 + listData.get(i).get("PicURL").toString().replace("~", "").replace("\\", "/"));
		}
		
		
		imageView.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SpaceImageDetailActivity.class);
				intent.putExtra("images", (ArrayList<String>) datas);
				intent.putExtra("position", position);
				int[] location = new int[2];
				imageView.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);
				intent.putExtra("locationY", location[1]);

				intent.putExtra("width", imageView.getWidth());
				intent.putExtra("height", imageView.getHeight());
				context.startActivity(intent);
				((Activity) context).overridePendingTransition(0, 0);
			}
		});
		return convertView;
	}
}

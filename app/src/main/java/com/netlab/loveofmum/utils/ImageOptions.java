package com.netlab.loveofmum.utils;

import com.netlab.loveofmum.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import android.graphics.Bitmap;
public class ImageOptions {
	public static DisplayImageOptions getStationOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar_default)
				.showImageForEmptyUri(R.drawable.avatar_default)
				.showImageOnFail(R.drawable.avatar_default).cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true)
				.build();
		return options;
	}
	public static DisplayImageOptions getQuanziOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.touxiang)
				.showImageForEmptyUri(R.drawable.touxiang)
				.showImageOnFail(R.drawable.touxiang).cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.build();
		return options;
	}
	public static DisplayImageOptions getTalkOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.touxiang)
				.showImageForEmptyUri(R.drawable.touxiang)
				.showImageOnFail(R.drawable.touxiang).cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true)
				.build();
		return options;
	}
	public static DisplayImageOptions getPinglunOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.touxiang)
				.showImageForEmptyUri(R.drawable.touxiang)
				.showImageOnFail(R.drawable.touxiang).cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true)
				.build();
		return options;
	}
	public static DisplayImageOptions getFaceOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.touxiang)
				.showImageForEmptyUri(R.drawable.touxiang)
				.showImageOnFail(R.drawable.touxiang).cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true)
				.build();
		return options;
	}
	public static DisplayImageOptions getDefaultOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
	public static DisplayImageOptions getHeaderOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_user_normal)
		.showImageForEmptyUri(R.drawable.icon_user_normal)
		.showImageOnFail(R.drawable.icon_user_normal).cacheInMemory(true)
		.cacheOnDisk(true)
		.build();
return options;
	}
	public static DisplayImageOptions getHeaderDoctorOptions(int round) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_user_normal)
		.showImageForEmptyUri(R.drawable.icon_user_normal)
		.showImageOnFail(R.drawable.icon_user_normal).cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(round)).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
		.build();
return options;

	}
	public static DisplayImageOptions getFaceOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.touxiang)
				.showImageForEmptyUri(R.drawable.touxiang)
				.showImageOnFail(R.drawable.touxiang).cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}
	public static DisplayImageOptions getFaceListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.touxiang)
				.showImageForEmptyUri(R.drawable.touxiang)
				.showImageOnFail(R.drawable.touxiang).cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}
	//产检时间表设置
	public static DisplayImageOptions getCHKListOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_user_normal)
				.showImageForEmptyUri(R.drawable.icon_user_normal)
				.showImageOnFail(R.drawable.icon_user_normal).cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}
}

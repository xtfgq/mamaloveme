package com.netlab.loveofmum.utils;

public class BulrManager {
	static
	{
		System.loadLibrary("blurjni");
	}
	public native static int[] StackBlur(int pix[], int w, int h, int radius) ;
	
}

package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Index001Adapter extends BaseAdapter
{
	private LayoutInflater				mInflater;
	
	private List<Map<String, Object>>	listData;
	
	private Context context;  
	
	
	public Index001Adapter(Context context, List<Map<String, Object>> listData)
	{
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
	}
	
	public int getCount()
	{
		return listData.size();
	}
	
	public Object getItem(int position)
	{
		return listData.get(position);
	}
	
	public long getItemId(int position)
	{
		return position;
	}
	
	public boolean isEnabled(int position)
	{
//		return super.isEnabled(position);
		return false;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		
		return convertView;
	}
}

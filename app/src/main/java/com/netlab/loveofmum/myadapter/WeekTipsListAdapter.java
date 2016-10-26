package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;


import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeekTipsListAdapter extends BaseAdapter
{
	private LayoutInflater				mInflater;
	
	private List<Map<String, Object>>	listData;

	public WeekTipsListAdapter(Context context, List<Map<String, Object>> listData)
	{
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
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
		return super.isEnabled(position);
		//return false;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if(position == 0)
		{
			convertView = mInflater.inflate(R.layout.img_bottomtext, parent, false);
			TextView txt = (TextView) convertView
					.findViewById(R.id.text);
			txt.setText(listData.get(position).get("Title").toString());
			ImageView img = (ImageView)convertView.findViewById(R.id.img);
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"), img);
		}
		else
		{
			convertView = mInflater.inflate(R.layout.recommedlist_item, parent, false);
			TextView title = (TextView) convertView
					.findViewById(R.id.title);
			ImageView img=(ImageView)convertView.findViewById(R.id.img);
			TextView content=(TextView)convertView.findViewById(R.id.content);
			content.setText(listData.get(position).get("SubContent").toString());
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"), img);
			title.setText(listData.get(position).get("Title").toString());
		}
		return convertView;
	}
}

package com.netlab.loveofmum.myadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.WeeKTip;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

public class WeekTipsListAdapter1 extends BaseAdapter
{
	private LayoutInflater				mInflater;

	private List<WeeKTip>	listData;

	public WeekTipsListAdapter1(Context context, List<WeeKTip> listData)
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
		WeeKTip tip = listData.get(position);
		if(position == 0)
		{
			convertView = mInflater.inflate(R.layout.img_bottomtext, parent, false);
			TextView txt = (TextView) convertView
					.findViewById(R.id.text);
			txt.setText(tip.Title);
			ImageView img = (ImageView)convertView.findViewById(R.id.img);
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + tip.PicURL.replace("~", "").replace("\\", "/"), img);
		}
		else
		{
			convertView = mInflater.inflate(R.layout.recommedlist_item, parent, false);
			TextView title = (TextView) convertView
					.findViewById(R.id.title);
			ImageView img=(ImageView)convertView.findViewById(R.id.img);
			TextView content=(TextView)convertView.findViewById(R.id.content);
			content.setText(tip.SubContent);
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 +tip.PicURL.replace("~", "").replace("\\", "/"), img);
			title.setText(tip.Title);
		}
		return convertView;
	}
}

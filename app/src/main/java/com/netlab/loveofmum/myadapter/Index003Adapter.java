package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;


import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.utils.LoadPictrue;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Index003Adapter extends BaseAdapter
{
	private LayoutInflater				mInflater;
	
	private List<Map<String, Object>>	listData;
	
	private Context context;  
	
	private String PicURL;
	
	
	public Index003Adapter(Context context, List<Map<String, Object>> listData)
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
		return super.isEnabled(position);
		//return false;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if(position == 0)
		{
			convertView = mInflater.inflate(R.layout.item_index_03, parent, false);

			TextView txt001 = (TextView) convertView
					.findViewById(R.id.txt001_newsdetail);
			txt001.setText(listData.get(position).get("Title").toString());
			
			//LinearLayout linear = (LinearLayout)convertView.findViewById(R.id.liner_newdetail);
			
			ImageView img001 = (ImageView)convertView.findViewById(R.id.img00001_index);
			PicURL = MMloveConstants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString();
			//FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"), img001);
			//fb.display(img001,Constants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"));
			
			//new LoadPictrue().getPicture(PicURL, linear);
		}
		else
		{
			convertView = mInflater.inflate(R.layout.item_index_01, parent, false);

			TextView txt001 = (TextView) convertView
					.findViewById(R.id.txtnewstitle);
			ImageView ivhead=(ImageView)convertView.findViewById(R.id.ivhead);
			TextView tvcontent=(TextView)convertView.findViewById(R.id.txtnewscontent);
			tvcontent.setText(listData.get(position).get("SubContent").toString());
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("PicURL").toString().replace("~", "").replace("\\", "/"), ivhead);
			txt001.setText(listData.get(position).get("Title").toString());
			
			
			
//			ImageView img001 = (ImageView)convertView.findViewById(R.id.chklist_item_img001);
//			if(position == 1)
//			{
//				img001.setImageResource(R.drawable.icon_food);
//			}
//			else if(position ==2)
//			{
//				img001.setImageResource(R.drawable.icon_food2);
//			}
		}
		
		return convertView;
	}
}

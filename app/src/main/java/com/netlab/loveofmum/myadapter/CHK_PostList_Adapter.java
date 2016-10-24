package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.widget.CircularImage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CHK_PostList_Adapter extends BaseAdapter
{
	private LayoutInflater				mInflater;
	
	private List<Map<String, Object>>	listData;
	
	private Context context;  
	
	//private ImageLoader image;
	
	
	public CHK_PostList_Adapter(Context context, List<Map<String, Object>> listData)
	{
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		//this.image = imageloader;
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
		convertView = mInflater.inflate(R.layout.item_postlist, null);
		TextView txt001 = (TextView)convertView.findViewById(R.id.item_postlist_txt001);
		
		TextView txt002 = (TextView)convertView.findViewById(R.id.item_postlist_txt002);
		TextView txt003 = (TextView)convertView.findViewById(R.id.item_postlist_txt003);
		TextView txt004 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt007);
		
		CircularImage img004 = (CircularImage)convertView.findViewById(R.id.img000_item_chkdetail_list);
		if(listData.get(position).get("PictureURL").toString().contains("vine.gif"))
		{
			img004.setImageResource(R.drawable.icon_user_normal);
		}
		else
		{
			ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + listData.get(position).get("PictureURL").toString().replace("~", "").replace("\\", "/"), img004);
		}

		
		txt001.setText(listData.get(position).get("NickName").toString());
		txt002.setText(listData.get(position).get("YuBirthTime").toString());
		txt003.setText(Html.fromHtml(listData.get(position).get("TopicTitle").toString()));
		txt004.setText(listData.get(position).get("CreatedDate").toString());

		return convertView;
	}
}

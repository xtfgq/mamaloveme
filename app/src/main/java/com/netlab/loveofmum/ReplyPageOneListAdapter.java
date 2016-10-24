package com.netlab.loveofmum;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netlab.loveofmum.CHK_TimeSelect;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.utils.ImageOptions;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ReplyPageOneListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;

	private int selectItem = -1;
	
	private int sign = -1;

	public ReplyPageOneListAdapter(Context context,
			List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
	}

	public int getCount() {
		return listData.size();
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
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

	public View getView( int position, View convertView,ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView){
			viewHolder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.item_reply_page_one, parent,false);

		viewHolder.txt001 = (TextView) convertView
				.findViewById(R.id.item_reply_pageone_txt01);
		viewHolder.txt002 = (TextView) convertView
				.findViewById(R.id.item_reply_pageone_txt02);
		viewHolder.txt003 = (TextView) convertView
				.findViewById(R.id.item_reply_pageone_txt03);
		viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.item_reply_pageone_img01);
		  convertView.setTag(viewHolder);
		}else{
	     viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txt001.setText(listData.get(position).get("picture").toString());
		viewHolder.txt002.setText(listData.get(position).get("content").toString());
		viewHolder.txt003.setText(listData.get(position).get("time").toString());
		
	
		
		//FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
		
		ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"), viewHolder.imgPic,ImageOptions.getFaceListOptions());
		//fb.display(imgPic,Constants.JE_BASE_URL2 + listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"),75,90);
		
		final String DoctorName = listData.get(position).get("item_reply_pageone_txt01").toString();
		final String DoctorTitle = listData.get(position).get("item_reply_pageone_txt02").toString();
		final String HospitalName = listData.get(position).get("item_reply_pageone_txt03").toString();
		final String ImageURL = listData.get(position).get("ImageURL").toString();
		return convertView;
	}
	 private static class ViewHolder
	    {
		 TextView txt001 ;
			TextView txt002 ;
			TextView txt003 ;
			ImageView imgPic;	
	    }
}

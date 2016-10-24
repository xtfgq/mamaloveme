package com.netlab.loveofmum.myadapter;

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

public class item_DoctorHome_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;

	private int selectItem = -1;
	
	private int sign = -1;

	public item_DoctorHome_Adapter(Context context,
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
		convertView = mInflater.inflate(R.layout.item_doctor_home, parent,false);

		viewHolder.txt001 = (TextView) convertView
				.findViewById(R.id.item_doctor_home_name);
		viewHolder.txt002 = (TextView) convertView
				.findViewById(R.id.item_doctor_home_time);
		viewHolder.txt003 = (TextView) convertView
				.findViewById(R.id.item_doctor_home_content);
		viewHolder.txt004 = (TextView) convertView
				.findViewById(R.id.item_doctor_home_zan);
		viewHolder.txt005 = (TextView) convertView
				.findViewById(R.id.item_doctor_home_pinglun);
		viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.item_doctor_home_img);
		  convertView.setTag(viewHolder);
		}else{
	     viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txt001.setText(listData.get(position).get("DoctorName").toString());
		viewHolder.txt002.setText(listData.get(position).get("Title").toString());
		viewHolder.txt003.setText(listData.get(position).get("HospitalName").toString());
		viewHolder.txt003.setText(listData.get(position).get("HospitalName").toString());
		//FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
		
		ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"), viewHolder.imgPic,ImageOptions.getFaceListOptions());
		//fb.display(imgPic,Constants.JE_BASE_URL2 + listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"),75,90);
		
		final String DoctorID = listData.get(position).get("DoctorID").toString();
		final String DoctorName = listData.get(position).get("DoctorName").toString();
		final String DoctorTitle = listData.get(position).get("Title").toString();
		final String HospitalName = listData.get(position).get("HospitalName").toString();
		final String Price = listData.get(position).get("Price").toString();
		final String ImageURL = listData.get(position).get("ImageURL").toString();
		final String DoctorNO = listData.get(position).get("DoctorNO").toString();
		final String DepartNO = listData.get(position).get("DepartNO").toString();
		final String Description= listData.get(position).get("Description").toString();

		

		return convertView;
	}
	 private static class ViewHolder
	    {
		 TextView txt001 ;
			TextView txt002 ;
			TextView txt003 ;
			TextView txt004 ;
			TextView txt005 ;
			ImageView imgPic;
	    }
}

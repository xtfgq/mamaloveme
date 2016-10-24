package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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

import com.netlab.loveofmum.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CHK_DoctorList_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;

	public void setListData(List<Map<String, Object>> listData) {
		this.listData = listData;
	}

	private List<Map<String, Object>> listData;

	private Context context;

	private int selectItem = -1;
	
	private int sign = -1;
	public CHK_DoctorList_Adapter(Context context) {
		this.mInflater = LayoutInflater.from(context);

		this.context = context;
	}

	public CHK_DoctorList_Adapter(Context context,
			List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
	}

	public int getCount() {
		return listData==null?0:listData.size();
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
		convertView = mInflater.inflate(R.layout.item_chk_doctorlist, parent,false);

		viewHolder.txt001 = (TextView) convertView
				.findViewById(R.id.doctor_name);
		viewHolder.txt002 = (TextView) convertView
				.findViewById(R.id.doctor_title);
		viewHolder.txt003 = (TextView) convertView
				.findViewById(R.id.hos_name);
		viewHolder.txt004 = (TextView) convertView
				.findViewById(R.id.doctor_jieshao);
		viewHolder.imgPic = (CircleImageView)convertView.findViewById(R.id.img001_item_chk_doctorlist);
		viewHolder.img = (ImageView)convertView.findViewById(R.id.img002_item_chk_doctorlist);
		
		viewHolder.btnKaidan = (Button)convertView.findViewById(R.id.btn_kaidan);
		  convertView.setTag(viewHolder);
		}else{
	     viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txt001.setText(listData.get(position).get("DoctorName").toString());
		viewHolder.txt002.setText(listData.get(position).get("Title").toString());
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
		final String HospitalID= listData.get(position).get("HospitalID").toString();
		final String DoctorNO = listData.get(position).get("DoctorNO").toString();
		final String DepartNO = listData.get(position).get("DepartNO").toString();
		final String Description= listData.get(position).get("Description").toString();
		viewHolder.btnKaidan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,
						CHK_TimeSelect.class);
				i.putExtra("DoctorID", DoctorID);
				i.putExtra("DoctorName", DoctorName);
				i.putExtra("HospitalName", HospitalName);
				i.putExtra("DoctorTitle", DoctorTitle);
				i.putExtra("Price", Price);
				i.putExtra("Description",Description);
				i.putExtra("ImageURL", ImageURL);
				i.putExtra("DoctorNO", DoctorNO);
				i.putExtra("DepartNO", DepartNO);
				i.putExtra("HospitalID", HospitalID);
			    context.startActivity(i);
			}
		});
		final ViewHolder vh=viewHolder;
		if (position == selectItem) {// 被选中的元素
			if (sign == selectItem) {// 再次选中的时候会隐藏，并初始化标记位置
				viewHolder.txt004.setText(listData.get(position).get("Description").toString().trim());
				viewHolder.txt004.post(new Runnable() {
					@Override
					public void run() {
						if(vh.txt004.getLineCount()>1){
							vh.txt004.setMaxLines(1);
						}
					}
				});
//				if(listData.get(position).get("Description").toString().length()>25)
//				{
//					viewHolder.txt004.setText(listData.get(position).get("Description").toString().substring(0, 25)+"...");
//				}
//				else
//				{
//					viewHolder.txt004.setText(listData.get(position).get("Description").toString());
//				}
				sign = -1;
				viewHolder.img.setImageResource(R.drawable.icon_arrow_down);
			} else {// 选中的时候会展示，并标记此位置
				viewHolder.txt004.setText(listData.get(position).get("Description").toString().trim());
				viewHolder.img.setImageResource(R.drawable.icon_arrow_up);
				viewHolder.txt004.post(new Runnable() {
					@Override
					public void run() {

						vh.txt004.setMaxLines(20);

					}
				});
				
				sign = selectItem;
			}
			

		} else {// 未被选中的元素
			viewHolder.txt004.setText(listData.get(position).get("Description").toString().trim());

			viewHolder.img.setImageResource(R.drawable.icon_arrow_down);
			if(TextUtils.isEmpty(listData.get(position).get("Description").toString().trim())){
				viewHolder.img.setVisibility(View.GONE);
			}else{
				viewHolder.img.setVisibility(View.VISIBLE);
			}
			viewHolder.txt004.post(new Runnable() {
				@Override
				public void run() {
					if(vh.txt004.getLineCount()>1){
						vh.txt004.setMaxLines(1);
					}
//					if(vh.txt004.getLineCount()==1){
//						vh.img.setVisibility(View.GONE);
//					}else{
//						vh.img.setVisibility(View.VISIBLE);
//					}
				}
			});
//			if(listData.get(position).get("Description").toString().length()>25)
//			{
//				viewHolder.txt004.setText(listData.get(position).get("Description").toString().substring(0, 25)+"...");
//			}
//			else
//			{
//				viewHolder.txt004.setText(listData.get(position).get("Description").toString());
//			}
		}

		return convertView;
	}
	 private static class ViewHolder
	    {
		 TextView txt001 ;
			TextView txt002 ;
			TextView txt003 ;
			TextView txt004 ;
			CircleImageView imgPic;
			ImageView img;
			Button btnKaidan;
	    }
}

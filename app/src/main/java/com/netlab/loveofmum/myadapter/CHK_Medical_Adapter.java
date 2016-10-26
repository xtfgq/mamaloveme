package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netlab.loveofmum.activity.DoctorHomeActivity;
import com.netlab.loveofmum.R;

import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


public class CHK_Medical_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;

	private int selectItem = -1;
	
	private int sign = -1;
	private User user;
	public CHK_Medical_Adapter(Context context) {
		this.mInflater = LayoutInflater.from(context);

		this.context = context;
		user=LocalAccessor.getInstance(context).getUser();
	}

	public List<Map<String, Object>> getListData() {
		return listData;
	}

	public void setListData(List<Map<String, Object>> listData) {
		this.listData = listData;
	}

	public CHK_Medical_Adapter(Context context,
							   List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		user=LocalAccessor.getInstance(context).getUser();
	}

	public int getCount() {
		return listData==null ? 0:listData.size() ;
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

	public View getView( final int position, View convertView,ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView){
			viewHolder = new ViewHolder();
			  LayoutInflater mInflater = LayoutInflater.from(context);
	            convertView = mInflater.inflate(R.layout.item_mead, null);
	            viewHolder.docdes=(LinearLayout) convertView.findViewById(R.id.doc_des);
	            viewHolder.imgIsLine=(ImageView) convertView.findViewById(R.id.iv_isonline);
	            viewHolder.txt001 = (TextView) convertView
				.findViewById(R.id.doctor_name);
	            viewHolder.txt002 = (TextView) convertView
				.findViewById(R.id.doctor_title);
	            viewHolder.txt003 = (TextView) convertView
				.findViewById(R.id.hos_name);
	            viewHolder.txt004 = (TextView) convertView
				.findViewById(R.id.doctor_jieshao);
	            viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.img001_item_chk_doctorlist);
	            viewHolder.img = (ImageView)convertView.findViewById(R.id.img002_item_chk_doctorlist);
		        convertView.setTag(viewHolder);

		}else{
			   viewHolder = (ViewHolder) convertView.getTag();
		}
		 viewHolder.txt001.setText(listData.get(position).get("DoctorName").toString());
		 viewHolder.txt002.setText(listData.get(position).get("Title").toString());
		 viewHolder.txt003.setText(listData.get(position).get("HospitalName").toString());
		
	
//		Button btnKaidan = (Button)convertView.findViewById(R.id.btn_kaidan);
		
		//FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
		
		ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2 + listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"), viewHolder.imgPic,ImageOptions.getFaceOptions());
		//fb.display(imgPic,Constants.JE_BASE_URL2 + listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"),75,90);
		
		final String DoctorID = listData.get(position).get("DoctorID").toString();
		final String DoctorName = listData.get(position).get("DoctorName").toString();
		final String DoctorTitle = listData.get(position).get("Title").toString();
		final String HospitalName = listData.get(position).get("HospitalName").toString();
		final String Price = listData.get(position).get("Price").toString();
		final String ImageURL = listData.get(position).get("ImageURL").toString();
		final String HospitalID = listData.get(position).get("HospitalID").toString();
		final String AskByOne=listData.get(position).get("AskPriceByOne").toString();
	
		final String DoctorNO = listData.get(position).get("DoctorNO").toString();
		final String IsFavored = listData.get(position).get("IsFavored").toString();
		final String PicUrl = listData.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/");
		if(Integer.valueOf(listData.get(position).get("IsLine").toString())!=1){
			 viewHolder.imgIsLine.setImageResource(R.drawable.icon_online);
		}else{
			 viewHolder.imgIsLine.setImageResource(R.drawable.icon_offline);
		}
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
			viewHolder.txt004.post(new Runnable() {
				@Override
				public void run() {
					if(vh.txt004.getLineCount()>1){
						vh.txt004.setMaxLines(1);
					}
				}
			});

		}

		 viewHolder.docdes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					user=LocalAccessor.getInstance(context).getUser();

					Intent i = new Intent(context,
							DoctorHomeActivity.class);
					i.putExtra("DoctorID", DoctorID);
					i.putExtra("HospitalID", HospitalID);
					i.putExtra("HospitalName", HospitalName);
					i.putExtra("DoctorName", DoctorName);
					
					i.putExtra("Price", Price);
					i.putExtra("ImageURL", ImageURL);
					i.putExtra("DoctorNO", DoctorNO);
					i.putExtra("IsFavored", IsFavored);
					i.putExtra("AskByOne", AskByOne);
					i.putExtra("AskCount", listData.get(position).get("AskCount").toString());
					i.putExtra("UserCount", listData.get(position).get("UserCount").toString());
					i.putExtra("PicUrl", PicUrl);
				    context.startActivity(i);

				}
			});
		viewHolder.imgPic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				user=LocalAccessor.getInstance(context).getUser();

				Intent i = new Intent(context,
						DoctorHomeActivity.class);
				i.putExtra("DoctorID", DoctorID);
				i.putExtra("HospitalID", HospitalID);
				i.putExtra("HospitalName", HospitalName);
				i.putExtra("DoctorName", DoctorName);

				i.putExtra("Price", Price);
				i.putExtra("ImageURL", ImageURL);
				i.putExtra("DoctorNO", DoctorNO);
				i.putExtra("IsFavored", IsFavored);
				i.putExtra("AskByOne", AskByOne);
				i.putExtra("AskCount", listData.get(position).get("AskCount").toString());
				i.putExtra("UserCount", listData.get(position).get("UserCount").toString());
				i.putExtra("PicUrl", PicUrl);
				context.startActivity(i);
			}
		});
		return convertView;
	}
	 private static class ViewHolder
	    {
		 TextView txt001 ;
			TextView txt002 ;
			TextView txt003 ;
			TextView txt004 ;
			ImageView imgPic ;
			ImageView img ;
			ImageView imgIsLine;
			LinearLayout docdes;
	    }

}

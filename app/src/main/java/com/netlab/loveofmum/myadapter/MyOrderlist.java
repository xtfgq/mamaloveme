package com.netlab.loveofmum.myadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.netlab.loveofmum.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyOrderlist extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Map<String, Object>> listData;
	private Context context;
	public MyOrderlist(Context context,
			List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
	}

	public int getCount() {
		return listData.size();
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

	


	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder = null;  
		 if(convertView==null){
		convertView = mInflater.inflate(R.layout.item_order_line, null);
		holder = new ViewHolder();  
		holder.tv_time = (TextView) convertView
				.findViewById(R.id.txt001_item_timeline);
	
		holder.tv_statusname= (TextView) convertView
				.findViewById(R.id.txt003_item_timeline);
		holder.tv_hos = (TextView) convertView
				.findViewById(R.id.txt004_item_timeline);
		holder.tv_Doctor = (TextView) convertView
				.findViewById(R.id.txt005_item_timeline);
		holder.tv_GoTime= (TextView) convertView.findViewById(R.id.txt004_item_gotime);
		holder.txt001_item_timeweek=(TextView) convertView.findViewById(R.id.txt001_item_timeweek);
		 convertView.setTag(holder);  
		 }else{
			 holder = (ViewHolder)convertView.getTag();
		 }
		String ordername= listData.get(position).get("OrderStatusName").toString().trim();
		holder.tv_statusname.setText(listData.get(position).get("OrderStatusName").toString().trim());
		holder.tv_hos.setText(listData.get(position).get("HospitalName").toString().trim());
		holder.tv_Doctor.setText(listData.get(position).get("DoctorName").toString().trim());
		 int isupload=Integer.valueOf(listData.get(position).get("IsUpload").toString());
		holder.txt001_item_timeweek.setText(listData.get(position).get("Week").toString()+"周");
	
		String time = listData.get(position).get("Gotime").toString().trim();
	    String CreateTime= listData.get(position).get("CreatedDate").toString().trim();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd");
		time = time.split(" ")[0].replace("/", "-");
		CreateTime=CreateTime.split(" ")[0].replace("/", "-");
		holder.tv_GoTime.setText("下单时间："+CreateTime.trim());
		
		try {
			time = formatter.format(formatter
					.parse(time));
			Date d = formatter.parse(time);
			
			Date dnow = formatter.parse(formatter.format(new Date()));
			
		
	
//		try
//		{
//			time = formatter.format(formatter
//					.parse(time));
//			Date d = formatter.parse(time);
//			
//			Date dnow = formatter.parse(formatter.format(new Date()));
			
//			if(d.compareTo(dnow)<0&&listData.get(position).get("OrderStatus").toString().equals("04"))
//			{
//				
//				holder.tv_statusname.setText("已就诊");
//				holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.drawable_zhuangtai2));
//				
//			}
//			else
//				if(d.compareTo(dnow)>0&&listData.get(position).get("OrderStatus").toString().equals("02"))
//				{
//					
//					holder.tv_statusname.setText("待确认");
//					holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.drawable_zhuangtai1));
//					
//				}
			Integer.valueOf(listData.get(position).get("IsUpload").toString());
			if(ordername.equals("用户取消")||ordername.equals("取消")){
				holder.tv_statusname.setText(ordername);
				holder.tv_statusname.setTextColor(context.getResources().getColor(R.color.white));
				holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.drawable_zhuangtai1));
			}else if(ordername.equals("待确认")||ordername.equals("待检查")){
				
				if(d.compareTo(dnow)<0){
					if(listData.get(position).get("MomSay").toString().equals("")&&isupload==0){
					holder.tv_statusname.setText("写心情");
					holder.tv_statusname.setTextColor(context.getResources().getColor(R.color.orderblue));
					holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.btn_shap));
					}else {
						holder.tv_statusname.setText("已完成");
						holder.tv_statusname.setTextColor(context.getResources().getColor(R.color.white));
						holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.btn_finish_shap));
					}
				}else{
					holder.tv_statusname.setText("待就诊");
					holder.tv_statusname.setTextColor(context.getResources().getColor(R.color.white));
				holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.drawable_zhuangtai3));
				}
			}
			else{
				holder.tv_statusname.setText(ordername);
				holder.tv_statusname.setTextColor(context.getResources().getColor(R.color.white));
				holder.tv_statusname.setBackground(context.getResources().getDrawable(R.drawable.drawable_zhuangtai1));
			}
			String[] data=time.split("\\-");
			holder.tv_time.setText(data[0]+"年"+data[1]+"月"+data[2]+"日");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
//		}
//		catch (ParseException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//		}
		
	
		
	
//	}
	
	return convertView;
	}
	 public static class ViewHolder { 
			
	        public TextView tv_time;
	        public TextView tv_statusname;
	        public TextView tv_hos;
	        public TextView tv_Doctor;
	        public TextView tv_GoTime;
	        public TextView txt001_item_timeweek;
	    }  
}

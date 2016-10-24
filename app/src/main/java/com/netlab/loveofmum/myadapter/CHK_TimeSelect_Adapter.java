package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.ListItemClickHelp;

public class CHK_TimeSelect_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;
	private ListItemClickHelp callback;
	
	private int selectItem = -1;
	
	private int sign = -1;

	public CHK_TimeSelect_Adapter(Context context,
			List<Map<String, Object>> listData,ListItemClickHelp callback) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		this.callback = callback;
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

	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_chktime_hour, null);

		TextView txt001 = (TextView) convertView
				.findViewById(R.id.txt001_item_chktime_hour);

//		txt001.setText(listData.get(position).get("ItemName").toString());
		String Week = listData.get(position).get("Week").toString();
		final String Status = listData.get(position).get("Status").toString();
		txt001.setText(Week);
		final Button btn001 = (Button)convertView.findViewById(R.id.btn001_item_chktime_hour);
		final ImageView ivdg=(ImageView) convertView.findViewById(R.id.iv_dg);
		
		final String isDefault = listData.get(position).get("IsDefault").toString();
//		if(Week.equals("日")||Week.equals("六"))
//		{
//			txt001.setTextColor(Color.parseColor("#fffd4425"));
//		}
//		else
//		{
//			txt001.setTextColor(Color.parseColor("#ff313131"));
//		}
		if(Status == "1")
		{	
			if(isDefault.equals("1"))
			{
				btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
				btn001.setTextColor(Color.parseColor("#ffffff"));
				selectItem = position;
				listData.get(position).put("IsDefault", "0");
			}
			else
			{
//				btn001.setBackgroundResource(R.drawable.bg_ciclce_red2);
				btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
				btn001.setTextColor(Color.parseColor("#ffffff"));
			}
		}
		else if(Status == "0")
		{
//			btn001.setBackgroundResource(R.drawable.bg_ciclce_gray);
			btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
			btn001.setTextColor(Color.parseColor("#ffffff"));
			btn001.setEnabled(false);
		}
		else if(Status == "2")
		{
			
		}
		
		btn001.setText(listData.get(position).get("Day").toString());
		final View view = convertView;
        final int p = position;
        final int one = btn001.getId();
		btn001.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callback.onClick(view, parent, p, one);
				
				
			}
		});
		
		if (position == selectItem) {// 被选中的元素
//			if (sign == selectItem) {// 再次选中的时候会隐藏，并初始化标记位置
//				btn001.setBackgroundResource(R.drawable.bg_ciclce_red2);
//				sign = -1;
//				
//			} else {// 选中的时候会展示，并标记此位置
//
//				btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
//				sign = selectItem;
//			}
			btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
			btn001.setTextColor(Color.parseColor("#ffffff"));
			ivdg.setVisibility(View.VISIBLE);
			sign = selectItem;

		} else {// 未被选中的元素
			if(Status.equals("1"))
			{
				if(isDefault.equals("1"))
				{
					btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
					btn001.setTextColor(Color.parseColor("#ffffff"));
				}
				else
				{
//					btn001.setBackgroundResource(R.drawable.bg_ciclce_red2);
					btn001.setBackgroundResource(R.drawable.bg_ciclce_red);
					btn001.setTextColor(Color.parseColor("#ffffff"));
				}
			}
			else
			{
				btn001.setBackgroundResource(R.color.white);
				btn001.setTextColor(Color.parseColor("#313131"));
				btn001.setEnabled(false);
			}
			ivdg.setVisibility(View.INVISIBLE);
//			btn001.setTextColor(Color.parseColor("#313131"));
		}
		
		return convertView;
	}
}

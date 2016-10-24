package com.netlab.loveofmum.myadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netlab.loveofmum.MyCHK_OrderAdd;
import com.netlab.loveofmum.MyCHK_OrderDetail;
import com.netlab.loveofmum.MyCHK_Timeline;
import com.netlab.loveofmum.R;

public class MyCHK_Timeline_Adapter2 extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;


	public MyCHK_Timeline_Adapter2(Context context,List<Map<String, Object>> listData) {
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

	 public View getView(final int position, View convertView,
		    	final ViewGroup parent) {
		
		convertView = mInflater.inflate(R.layout.my_check, null);
		
		Button btn_my_check = (Button)convertView.findViewById(R.id.btn_my_check);
		
		btn_my_check.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(context, MyCHK_OrderAdd.class);
				i.putExtra("Add_food", "Add");
				context.startActivity(i);
			}
		});
		return convertView;
	}
}

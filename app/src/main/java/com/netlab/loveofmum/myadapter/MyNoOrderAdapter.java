package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.CHK_TimeLineTwo;
import com.netlab.loveofmum.MainTabActivity;
import com.netlab.loveofmum.R;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MyNoOrderAdapter extends BaseAdapter{
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;


	public MyNoOrderAdapter(Context context,
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

	 public View getView(final int position, View convertView,
		    	final ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.activity_noorder, null);
		
		
		return convertView;
	}

	

}

package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter{
     List<Map<String,Object>>list;
	
	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	LayoutInflater inflater;
	
	FragmentActivity activity;
	
	public MyListViewAdapter(FragmentActivity fragmentActivity) {
		// TODO Auto-generated constructor stub
		
		this.activity = fragmentActivity;
		inflater = (LayoutInflater) fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list==null ? 0:list.size() ;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null; 
		if (view == null) {
			   holder=new ViewHolder();
			view = inflater.inflate(R.layout.item_topical_layout, null);
			 holder.tv_count=(TextView)view.findViewById(R.id.tv_topic);
			 view.setTag(holder);  
		} else{
			holder = (ViewHolder)view.getTag(); 
		}
		holder.tv_count.setText(list.get(position).get("Title").toString());
		
		return view;
	}
	
	class ViewHolder {
		        public TextView tv_count;
		    
	}

}

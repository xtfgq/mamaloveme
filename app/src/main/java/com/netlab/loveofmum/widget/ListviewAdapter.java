package com.netlab.loveofmum.widget;

import java.util.ArrayList;

import com.netlab.loveofmum.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListviewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> list;
	private int pos;
	public ListviewAdapter(Context context,ArrayList<String> list,int pos){
		this.context = context;
		this.list = list;
		this.pos = pos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(arg1 == null&&list.size() != 0){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			arg1 = inflater.inflate(R.layout.item, null);
			viewHolder.textView = (TextView)arg1.findViewById(R.id.itemText);
			
			viewHolder.imgView = (ImageView)arg1.findViewById(R.id.imglist);
			
			if(pos == arg0)
			{
				viewHolder.imgView.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.imgView.setVisibility(View.GONE);
			}
			
			arg1.setTag(viewHolder);
		}else
			viewHolder = (ViewHolder) arg1.getTag();
		viewHolder.textView.setText(list.get(arg0));
		return arg1;
	}
}

package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;

import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedAadapter extends BaseAdapter{
   private LayoutInflater mInflater;
	
	private List<Map<String, Object>>	listData;
	private ImageLoader imageLoader;
	
	private Context context;  
	public FeedAadapter(Context context, List<Map<String, Object>> listData)
	{
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		imageLoader=ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public boolean isEnabled(int position)
	{
		return super.isEnabled(position);
		//return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null; 
		if(convertView==null){
			   holder=new ViewHolder();
			   convertView = mInflater.inflate(R.layout.item_sq, null);
			   holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			   holder.tv_count=(TextView)convertView.findViewById(R.id.tv_count);
			   holder.iv_sq=(ImageView)convertView.findViewById(R.id.icon);
			   convertView.setTag(holder);  
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		imageLoader.displayImage(listData.get(position).get("Icon").toString(), holder.iv_sq,ImageOptions.getQuanziOptions(120));
		holder.tv_name.setText(listData.get(position).get("Name").toString());
		holder.tv_count.setText("话题数:"+listData.get(position).get("Count").toString());
		final Object topic=listData.get(position).get("Topic");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return convertView;
	}

	 public static class ViewHolder { 
			
	        public TextView tv_count;
	        public TextView tv_name;
	        public ImageView iv_sq;
	      
	    }  
}

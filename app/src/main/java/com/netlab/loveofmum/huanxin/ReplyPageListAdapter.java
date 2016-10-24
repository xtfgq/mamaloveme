package com.netlab.loveofmum.huanxin;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.netlab.loveofmum.Activity_ReplyPage;
import com.netlab.loveofmum.CHK_TimeSelect;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.FeedAadapter.ViewHolder;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReplyPageListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	public List<Map<String, Object>> getListData() {
		return listData;
	}



	public void setListData(List<Map<String, Object>> listData) {
		this.listData = listData;
	}
	private Context context;

	

	public ReplyPageListAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	
		this.context = context;
	}



	public int getCount() {
		return listData==null?0:listData.size();
	}

	
	public Object getItem(int position) {
		return listData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	

	public View getView( int position, View convertView,ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView){
			viewHolder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.item_reply_page, parent,false);
		viewHolder.txt004 = (TextView) convertView
				.findViewById(R.id.item_reply_page_tv01);
		
		  convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag(); 
		}		
		viewHolder.txt004.setText(listData.get(position).get("Content").toString());
		return convertView;
	}
	 class ViewHolder
	    {
			TextView txt004 ;
				
	    }
}

package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.myadapter.FeedAadapter.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChktimelistAdapter extends BaseAdapter {
	
    private LayoutInflater mInflater;
	private Context mContext;
	private List<Map<String, Object>>	listData;
	private ImageLoader imageLoader;
	public ChktimelistAdapter(Context context, List<Map<String, Object>> listData)
	{
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.mContext = context;
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
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			   convertView = mInflater.inflate(R.layout.item_chklist, null);
			   TextView tv_chkname=(TextView) convertView.findViewById(R.id.chktime_name);
			   ImageView iv_tupian=(ImageView)convertView.findViewById(R.id.chklist_item_img001);
		TextView tv_status=(TextView)convertView.findViewById(R.id.chklist_item_img002);
		 tv_chkname.setText(listData.get(position).get("CHKType").toString());

		 switch (position) {
		case 0:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum1));

			break;
		case 1:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum2));
			break;
		case 2:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum3));
			break;
		case 3:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum4));
			break;
		case 4:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum5));
			break;
		case 5:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum6));
			break;
		case 6:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum7));
			break;
		case 7:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum8));
			break;
		case 8:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum9));
			break;
		case 9:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum10));
			break;
		case 10:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum11));
			break;
		case 11:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum12));
			break;
		case 12:
			iv_tupian.setBackground(mContext.getResources().getDrawable(R.drawable.iconnum13));
			break;
		default:
			break;
		}
//		if(status==1){
		if(TextUtils.isEmpty(listData.get(position).get("Type").toString())){
			tv_status.setVisibility(View.INVISIBLE);
		}else {
			tv_status.setVisibility(View.VISIBLE);
			tv_status.setText(listData.get(position).get("Type").toString());
		}
		return convertView;
	}
	 
}

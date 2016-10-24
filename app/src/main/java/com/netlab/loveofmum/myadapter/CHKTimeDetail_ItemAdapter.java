package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netlab.loveofmum.CHKItem_Base;
import com.netlab.loveofmum.R;

public class CHKTimeDetail_ItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;

	public CHKTimeDetail_ItemAdapter(Context context,
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
		convertView = mInflater.inflate(R.layout.item_chkdetail, null);

		TextView txt001 = (TextView) convertView
				.findViewById(R.id.txt_item_chkdetail);
		
		txt001.setText(listData.get(position).get("ItemName").toString());
//		txt001.setBackgroundResource(R.drawable.pic_btn_jc);
		
//		if(listData.get(position).get("ItemStatus").toString().equals("不可预约"))
//		{
//			txt001.setBackgroundResource(R.drawable.pic_btn_jc);
//		}
//		else
//		{
//			txt001.setBackgroundResource(R.drawable.pic_btn_jc1);

//		}
		TextView txtcontnet=(TextView) convertView
				.findViewById(R.id.txt_item_chkcontent);
		txtcontnet.setText(listData.get(position).get("ItemContent").toString());
//		txt001.setOnClickListener(new View.OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				Intent i = new Intent(context,
//						CHKItem_Base.class);
//				i.putExtra("ItemName",listData.get(position).get("ItemName").toString());
//				i.putExtra("ItemContent",listData.get(position).get("ItemContent").toString());
//				context.startActivity(i);
//			}
//		});
		return convertView;
	}
}


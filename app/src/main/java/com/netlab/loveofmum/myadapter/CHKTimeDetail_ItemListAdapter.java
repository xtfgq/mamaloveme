package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netlab.loveofmum.CHKItem_Base;
import com.netlab.loveofmum.R;

public class CHKTimeDetail_ItemListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;

	public CHKTimeDetail_ItemListAdapter(Context context,
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
		convertView = mInflater.inflate(R.layout.item_list_chkdetail, null);

		TextView txt001 = (TextView) convertView
				.findViewById(R.id.txt_item_chkdetail);
		TextView txt_item_chkcontent=(TextView) 
		convertView.findViewById(R.id.txt_item_chkcontent);
		ImageView ivkyy=(ImageView) convertView.findViewById(R.id.ivkyy);
		
		txt001.setText(listData.get(position).get("ItemName").toString());
		txt_item_chkcontent.setText(listData.get(position).get("ItemContent").toString());
		if(listData.get(position).get("ItemStatus").toString().equals("不可预约"))
		{

			ivkyy.setVisibility(View.GONE);

		}
		else
		{
			ivkyy.setVisibility(View.VISIBLE);

		}

		convertView.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent i = new Intent(context,
						CHKItem_Base.class);
				i.putExtra("ItemName",listData.get(position).get("ItemName").toString());
				i.putExtra("ItemContent",listData.get(position).get("ItemContent").toString());
				context.startActivity(i); 
			}
		});
		return convertView;
	}

}

package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netlab.loveofmum.R;

public class News_Type_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<Map<String, Object>> listData;

	private Context context;

	private int selectItem = -1;
	
	private int sign = -1;

	public News_Type_Adapter(Context context,
			List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
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
		convertView = mInflater.inflate(R.layout.item_newstype, null);

		TextView txt001 = (TextView) convertView
				.findViewById(R.id.txt001_item_newstype);

		txt001.setText(listData.get(position).get("TypeName").toString());

		ImageView img = (ImageView)convertView.findViewById(R.id.img001_item_newstype);
		if (position == selectItem) {// 被选中的元素
			if (sign == selectItem) {// 再次选中的时候会隐藏，并初始化标记位置
				
				sign = -1;
				
			} else {// 选中的时候会展示，并标记此位置

				sign = selectItem;			
			}

		} else {// 未被选中的元素
		}
		return convertView;
	}
}

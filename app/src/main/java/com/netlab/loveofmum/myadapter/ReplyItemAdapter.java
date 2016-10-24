package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.FeedAadapter.ViewHolder;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.widget.CircularImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplyItemAdapter extends BaseAdapter {

private LayoutInflater				mInflater;
	
	private List<Map<String, Object>>	listData;
	
	private Context context;  
	
	//private ImageLoader image;
	
	
	public ReplyItemAdapter(Context context, List<Map<String, Object>> listData)
	{
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		//this.image = imageloader;
	}
	
	public int getCount()
	{
		return listData.size();
	}
	
	public Object getItem(int position)
	{
		return listData.get(position);
	}
	
	public long getItemId(int position)
	{
		return position;
	}
	
	public boolean isEnabled(int position)
	{
//		return super.isEnabled(position);
		return false;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null; 
		if(convertView==null){
		convertView = mInflater.inflate(R.layout.item_reply, null);
		holder=new ViewHolder();
	
		holder.txt001 = (TextView)convertView.findViewById(R.id.item_postlist_txt001);
		
		holder.txt002= (TextView)convertView.findViewById(R.id.item_postlist_txt003);
		
		holder.txt004 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt007);
		
		holder.img004 = (CircularImage)convertView.findViewById(R.id.img000_item_chkdetail_list);
		convertView.setTag(holder);  
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		int Zantype = Integer.valueOf(listData.get(position)
				.get("AuthorType").toString());
		int typeInner = Integer.valueOf(listData.get(position)
				.get("ParentID").toString());
		if (typeInner == 0) {
			holder.txt001.setText(listData.get(position).get("Nick")
					.toString());

		} else {
			String span = listData.get(position).get("Nick")
					.toString()
					+ "回复"
					+ listData.get(position).get("ReplyNick").toString();
			SpannableStringBuilder style = new SpannableStringBuilder(span);
			int bstart = span.indexOf("回复");
			int bend = bstart + ("回复").length();

			style.setSpan(
					new ForegroundColorSpan(context.getResources().getColor(
							R.color.pink)), bstart, bend,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.txt001.setText(style);
		}
		if (Zantype == 0) {
			if (listData.get(position).get("PicUrl").toString()
					.contains("vine.gif")) {
				holder.img004.setImageResource(R.drawable.icon_user_normal);
			} else {
				ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3
						+ listData.get(position).get("PicUrl").toString()
								.replace("~", "").replace("\\", "/"),
								holder.img004, ImageOptions.getHeaderDoctorOptions(120));
			}
		} else {
			if (listData.get(position).get("PicUrl").toString()
					.contains("vine.gif")) {
				holder.img004.setImageResource(R.drawable.touxiang);
			} else {
				ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL2
						+ listData.get(position).get("PicUrl").toString()
								.replace("~", "").replace("\\", "/"),
								holder.img004,
						ImageOptions.getPinglunOptions(120));
			}
		}
	
		
	
		
		holder.txt002.setText(listData.get(position).get("Content").toString());
		holder.txt004.setText(listData.get(position).get("CreatedDate").toString());

		return convertView;
	}
	 public static class ViewHolder { 
			
	        public TextView txt001;
	        public TextView txt002;
	        public TextView txt004;
	        public ImageView img004;
	      
	    }  
}

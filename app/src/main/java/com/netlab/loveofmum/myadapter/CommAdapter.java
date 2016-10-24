package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;

import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String,Object>> listdat;
	private ImageLoader mImageLoader;
	public List<Map<String, Object>> getListdat() {
		return listdat;
	}

	public void setListdat(List<Map<String, Object>> listdat) {
		this.listdat = listdat;
	}
	private LayoutInflater mInflater;
	
	public CommAdapter(Context context){
		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);
		mImageLoader=ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return listdat==null ? 0:listdat.size() ;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listdat.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null; 
		if(convertView==null){
			   holder=new ViewHolder();
			   convertView = mInflater.inflate(R.layout.item_reply_page_one, null);
			  holder.tv_name=(TextView) convertView.findViewById(R.id.item_reply_pageone_name);
			  holder.tvcontent=(TextView) convertView.findViewById(R.id.item_reply_pageone_content);
			  holder.tvtime=(TextView) convertView.findViewById(R.id.item_reply_pageone_time);
			  holder.iv_head=(ImageView) convertView.findViewById(R.id.item_reply_pageone_img01);
			   convertView.setTag(holder);  
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		 mImageLoader.displayImage(listdat.get(position).get("PicUrl").toString(), holder.iv_head,ImageOptions.getTalkOptions(120));
		 holder.tv_name.setText(listdat.get(position).get("Name").toString());
		 holder.tvcontent.setText(listdat.get(position).get("Content").toString());
		 holder.tvtime.setText(listdat.get(position).get("Time").toString());
		return convertView;
	}
	 public static class ViewHolder { 
	        public TextView tv_name;
	        public TextView tvcontent;
	        public TextView tvtime;
	        public ImageView iv_head;
	      
	    }  

}

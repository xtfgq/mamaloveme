package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.FeedAadapter.ViewHolder;
import com.netlab.loveofmum.myadapter.ZanAdapter.ViewHolder1;
import com.netlab.loveofmum.myadapter.ZanAdapter.ViewHolder2;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorAadpter extends BaseAdapter {
	private Context mContext;
	private List<Map<String,Object>> listdat;
	private ImageLoader mImageLoader;
	final int TYPE_1=0;
	final int TYPE_2=1;
	  public List<Map<String, Object>> getListdat() {
		return listdat;
	}

	public void setListdat(List<Map<String, Object>> listdat) {
		this.listdat = listdat;
	}
	private LayoutInflater mInflater;
	
	public DoctorAadpter(Context context){
		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);
		mImageLoader=ImageLoader.getInstance();
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
     @Override
    public int getItemViewType(int position) {
    	// TODO Auto-generated method stub
    	 	int type=Integer.valueOf(listdat.get(position).get("Type").toString());
        	if(type==0){
        		return TYPE_1;
        		
        	}else{
        		return TYPE_2;
        	}
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		ViewHolder1 holder1 = null; 
		ViewHolder2 holder2=null;
		int type=getItemViewType(position);
		if(convertView==null){
			switch (type) {
			case TYPE_1:
				 convertView = mInflater.inflate(R.layout.item_replay, parent,false);
				 holder1=new ViewHolder1();
				 holder1.iv1_head=(ImageView) convertView.findViewById(R.id.iv1_head);
				 holder1.tv1_name=(TextView) convertView.findViewById(R.id.tv1_name);
				 holder1.tv1_time=(TextView) convertView.findViewById(R.id.tv1_time);
				 holder1.tv1_count=(TextView) convertView.findViewById(R.id.tv1_count);
				 holder1.tv2_name=(TextView) convertView.findViewById(R.id.tv2_name);
				 holder1.tv2_time=(TextView) convertView.findViewById(R.id.tv2_time);
				 holder1.tv2_count=(TextView) convertView.findViewById(R.id.tv2_count);
				 holder1.iv2_head=(ImageView) convertView.findViewById(R.id.iv2_head);
				   convertView.setTag(holder1);  
				  
				break;
			case TYPE_2:
				 convertView = mInflater.inflate(R.layout.item_doctor_post, parent,false);
				   holder2=new ViewHolder2();
				   holder2.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
				   holder2.iv_head=(ImageView)convertView.findViewById(R.id.iv_head);
				   holder2.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
				   holder2.tv_count=(TextView) convertView.findViewById(R.id.tv_count);
				   convertView.setTag(holder2);  
				
				break;

			default:
				break;
			}
			 
		
			  
		}else{
			switch (type) {
			case TYPE_1:
				holder1 = (ViewHolder1)convertView.getTag(); 
				
				break;
			case TYPE_2:
				holder2 = (ViewHolder2)convertView.getTag(); 
				break;

			default:
				break;
			}
		}
		switch (type) {
		case TYPE_1:
//			if(listdat.get(position).get("PicUrl").toString().contains("vine.gif"))
			{
				holder1.iv1_head.setImageResource(R.drawable.icon_user_normal);
			}
//			else
//			{
//				 mImageLoader.displayImage(MMloveConstants.JE_BASE_URL3 +listdat.get(position).get("PicUrl").toString().replace("~", "").replace("\\", "/"), holder1.iv1_head,ImageOptions.getTalkOptions(120));
//			}
			
			String nickname=listdat.get(position).get("Nick").toString();
		if(nickname.length()>0){
			 if(nickname.length()>1){
					nickname=nickname.substring(0, 2)+"***";
					}
			 else{
						nickname=nickname.substring(0, 1)+"***";
					}
		}else{
			nickname="**";
		}
			 holder1.tv1_name.setText(nickname);
			 holder1.tv1_count.setText(listdat.get(position).get("Content").toString());
              holder1.tv1_time.setText(listdat.get(position).get("CreatedDate").toString());
          	if(listdat.get(position).get("DoctorPic").toString().contains("vine.gif"))
			{
				holder1.iv2_head.setImageResource(R.drawable.qitu);
			}
			else
			{
			
				 mImageLoader.displayImage(MMloveConstants.JE_BASE_URL2 +listdat.get(position).get("DoctorPic").toString().replace("~", "").replace("\\", "/"), holder1.iv2_head,ImageOptions.getTalkOptions(120));
			}
			 holder1.tv2_name.setText(listdat.get(position).get("Doctor").toString());
			String nick= listdat.get(position).get("Nick").toString();
			if(nick.length()>0){
			 if(nick.length()>1){
				 nick=nick.substring(0, 2)+"**";
			 }else{
				 nick=nick.substring(0, 1)+"**";
			 }
			}else{
				nick="**";
			}
				String span = "回复"+"@"+nick+listdat.get(position).get("ReplyContent").toString();
				SpannableStringBuilder style = new SpannableStringBuilder(span);
				int bstart = span.indexOf("@"+nick);
				int bend = bstart + ("@"+nick).length();

				style.setSpan(
						new ForegroundColorSpan(mContext.getResources().getColor(
								R.color.pink)), bstart, bend,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				 holder1.tv2_count.setText(style);
			
              holder1.tv2_time.setText(listdat.get(position).get("ReplyCreatedDate").toString());
			
			break;
		case TYPE_2:
//			if(listdat.get(position).get("PicUrl").toString().contains("vine.gif"))
//			{
				holder2.iv_head.setImageResource(R.drawable.icon_user_normal);
//			}
//			else
//			{
//				 mImageLoader.displayImage(MMloveConstants.JE_BASE_URL3 +listdat.get(position).get("PicUrl").toString().replace("~", "").replace("\\", "/"), holder2.iv_head,ImageOptions.getTalkOptions(120));
//			}
			String nickname1=listdat.get(position).get("Nick").toString();
			if(nickname1.length()>0){
			if(nickname1.length()>1){
			nickname1=nickname1.substring(0, 2)+"***";
			}else{
				nickname1=nickname1.substring(0, 1)+"***";
			}
			}else{
				nickname1="***";
			}
			 holder2.tv_name.setText(nickname1);
			 holder2.tv_count.setText(listdat.get(position).get("Content").toString());
              holder2.tv_time.setText(listdat.get(position).get("CreatedDate").toString());
		
			break;

		default:
			break;
		}
		

	return convertView;
	}
	 public static class ViewHolder1 { 
			public TextView tv1_time;
	        public TextView tv1_count;
	        public TextView tv1_name;
	        public ImageView iv1_head;
	    	public TextView tv2_time;
	        public TextView tv2_count;
	        public TextView tv2_name;
	        public ImageView iv2_head;
	      
	    }  
	 public static class ViewHolder2 { 
			
	        public TextView tv_count;
	        public TextView tv_name;
	        public TextView tv_time;
	        public ImageView iv_head;
	      
	    }  
}

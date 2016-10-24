package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.DoctorDes;
import com.netlab.loveofmum.HistoryList;
import com.netlab.loveofmum.PrivateDoctor;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.FeedAadapter.ViewHolder;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivateDortorAdapter extends BaseAdapter {
	  private LayoutInflater mInflater;
		
		private List<Map<String, Object>>	listData;
		private ImageLoader imageLoader;
		private Context context;  
		public PrivateDortorAdapter(Context context, List<Map<String, Object>> listData)
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null; 
		if(convertView==null){
			   holder=new ViewHolder();
			   convertView = mInflater.inflate(R.layout.item_prlist, null);
			   holder.iv_head=(ImageView) convertView.findViewById(R.id.iv_itemhead);
			   holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			   holder.tv_count=(TextView)convertView.findViewById(R.id.tv_count);
			   holder.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
			   convertView.setTag(holder);  
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		int status=-1;
		if("".equals(listData.get(position).get("OrderStatus").toString())){
			status=0;
		}else{
		 status=Integer.valueOf(listData.get(position).get("OrderStatus").toString());
		}
		String content=listData.get(position).get("ConsultationContent").toString();
		if("ξφψ?妈妈爱我?ψφξ".equals(content)){
			 holder.tv_count.setText("图片");
		}else{
		  holder.tv_count.setText(content);
		}
final	String DocPicURL= listData.get(position).get("DocPicURL").toString();
final	String DoctorId=listData.get(position).get("DoctorID").toString();
final	String HospitalID=listData.get(position).get("HospitalID").toString();
final	String orderstatus=listData.get(position).get("OrderStatus").toString();
final	String AskOrderID=listData.get(position).get("AskOrderID").toString();

if(status==4){
	
	holder.tv_count.setTextColor(context.getResources().getColor(R.color.hometext));
}else{
	holder.tv_count.setTextColor(context.getResources().getColor(R.color.red));
}
		
		imageLoader.displayImage(listData.get(position).get("DocPicURL").toString(), holder.iv_head,ImageOptions.getTalkOptions(120));
		 holder.tv_name.setText(listData.get(position).get("DoctorName").toString());
		 
		   holder.tv_time.setText(listData.get(position).get("UpdatedDate").toString());
		   convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context,HistoryList.class);
				i.putExtra("DoctorID", DoctorId);
				i.putExtra("HospitalID", HospitalID);
				i.putExtra("DocPicURL", DocPicURL);
				i.putExtra("DoctorName", listData.get(position).get("DoctorName").toString());
				i.putExtra("AskOrderID", AskOrderID);
				i.putExtra("OrderStatus", orderstatus);
				context.startActivity(i);
			}
		});
		return convertView;
	}
	 public static class ViewHolder { 
			public ImageView iv_head;
	        public TextView tv_name;
	        public TextView tv_time;
	        public TextView tv_count;
	      
	    }  

}

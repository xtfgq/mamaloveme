package com.netlab.loveofmum.myadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.MyCHK_OrderAdd;
import com.netlab.loveofmum.MyCHK_OrderDetail;
import com.netlab.loveofmum.MyCHK_Timeline;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.ReducePointsActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCHK_Timeline_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;
	
	private List<Map<String, Object>> listData;
	private ImageLoader mImageLoader;

	private Context context;
	private User user;

	private int selectItem = -1;
	
	private int sign = -1;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.OrderDelete;
	private final String SOAP_METHODNAME = MMloveConstants.OrderDelete;
	private final String SOAP_URL = MMloveConstants.URL002;

	public MyCHK_Timeline_Adapter(Context context,
			List<Map<String, Object>> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		mImageLoader=ImageLoader.getInstance();
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type=-1;
		int status=Integer.valueOf(listData.get(position).get("OrderStatus").toString());
		if(status==4){

			type=1;
		}else{
			type=0;
		}
		return type;
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
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return VIEW_TYPE;
	}

	@SuppressLint("NewApi")
	public View getView(int position, View convertView,
			ViewGroup parent) {

		user= LocalAccessor.getInstance(context).getUser();
		int typeItem = getItemViewType(position);
		ViewHolder0 holder0=null;
		ViewHolder1 holder1=null;

			 if(convertView==null){
				 switch (typeItem) {
					 case TYPE_1:
						 convertView =  LayoutInflater.from(context).inflate(R.layout.itme_type_3, parent, false);
						 holder0=new ViewHolder0();
						 holder0.top=convertView
								 .findViewById(R.id.top);
						 holder0.lltop=(LinearLayout)convertView
								 .findViewById(R.id.lineer001_item_timeline);
						 holder0.tv_title=(TextView)convertView
								 .findViewById(R.id.tv_title);
						 holder0.ivdel=(ImageView) convertView.findViewById(R.id.iv_delete);
						 holder0.tv_titleYear=(TextView)convertView
								 .findViewById(R.id.tv_year);
						 holder0.llgo=(LinearLayout)convertView.findViewById(R.id.ll_go);
						 holder0.tv_hos=(TextView)convertView
								 .findViewById(R.id.hos_name);
						 holder0.iv_head=(ImageView) convertView.findViewById(R.id.time_dot);
						 holder0.tv_Doctor=(TextView)convertView.findViewById(R.id.tv_docname);
						 holder0.tv_time=(TextView)convertView.findViewById(R.id.tv_time);
						 convertView.setTag(holder0);
						 break;
					 case TYPE_2:
						 convertView =  LayoutInflater.from(context).inflate(R.layout.itme_type_4, parent, false);
						 holder1=new ViewHolder1();
						 holder1.top=convertView
								 .findViewById(R.id.top);
						 holder1.lltop=(LinearLayout)convertView
								 .findViewById(R.id.lineer001_item_timeline);
						 holder1.tv_title=(TextView)convertView
								 .findViewById(R.id.txt002_item_timeline);
						 holder1.tv_titleYear=(TextView)convertView
								 .findViewById(R.id.tv_year);
						 holder1.tv_say=(TextView)convertView
								 .findViewById(R.id.tv_say);
						 holder1.ivhead=(ImageView) convertView
								 .findViewById(R.id.time_dot);
						 holder1.llhoder=convertView.findViewById(R.id.tv_say);
						 holder1.ivdel=(ImageView) convertView.findViewById(R.id.iv_del);
						 convertView.setTag(holder1);
						 break;
					 default:
						 break;
				 }

			 }else{
				 switch (typeItem) {
					 case TYPE_1:
						 holder0 = (ViewHolder0)convertView.getTag();
						 break;
					 case TYPE_2:
						 holder1 = (ViewHolder1)convertView.getTag();
						 break;
				 }


			 }
		switch (typeItem) {
			case TYPE_1:
				holder0.tv_title.setText(listData.get(position).get("TitleName").toString());
				String timeYear=listData.get(position).get("Gotime").toString().split(" ")[0];
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy/MM/dd");
				try {
					timeYear = formatter.format(formatter
                            .parse(timeYear));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				holder0.tv_titleYear.setText(timeYear.replace("/","."));

				if(position==0){
					holder0.lltop.setVisibility(View.VISIBLE);
					holder0.top.setVisibility(View.GONE);
				}
				if(position>0){
					if(listData.get(position).get("TitleName").equals(listData.get(position-1).get("TitleName"))){
						holder0.lltop.setVisibility(View.GONE);
						holder0.top.setVisibility(View.VISIBLE);
					}else{
						holder0.lltop.setVisibility(View.VISIBLE);
						holder0.top.setVisibility(View.GONE);
					}
				}
				holder0.tv_hos.setText(listData.get(position).get("HospitalName").toString());
				holder0.tv_Doctor.setText(listData.get(position).get("DoctorName").toString());
				holder0.tv_time.setText(timeYear+"  "+listData.get(position).get("BeginTime").toString()+"-"+listData.get(position).get("EndTime").toString());
				if(!TextUtils.isEmpty(user.PicURL)) {

					mImageLoader.displayImage(user.PicURL, holder0.iv_head);
				}
				final int p=position;
				holder0.llgo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String time = listData.get(p).get("Gotime").toString();

						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd");
						time = time.split(" ")[0].replace("/", "-");
						try {
							time = formatter.format(formatter
									.parse(time));
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						time=time.replace("-",".");
						Intent i = new Intent(context, MyCHK_OrderAdd.class);
						String ID = listData.get(p).get("ID").toString();
						String MomSay=listData.get(p).get("MomSay").toString();
						i.putExtra("page", "User_Foot");
						i.putExtra("HospitalName",  listData.get(p).get("HospitalName").toString());
						i.putExtra("DoctorName", listData.get(p).get("DoctorName").toString());
						i.putExtra("Gotime", time+" "+listData.get(p)
								.get("BeginTime").toString()+"-"+listData.get(p)
								.get("EndTime").toString());
						i.putExtra("HospitalID", listData.get(p).get("HospitalID").toString());
						i.putExtra("MomSay", MomSay);
						i.putExtra("ID", ID);
						i.putExtra("TitleName",listData.get(p).get("TitleName").toString());
						i.putExtra("Yeartime",listData.get(p).get("Gotime").toString());
						i.putExtra("IsUpload",listData.get(p).get("IsUpload").toString());
						context.startActivity(i);

					}
				});
				holder0.ivdel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						OrderDelete(listData.get(p).get("OrderID").toString(),p);
					}
				});

				break;

			case TYPE_2:
				holder1.tv_title.setText(listData.get(position).get("TitleName").toString());
				String timeYear1=listData.get(position).get("Gotime").toString().split(" ")[0];
				SimpleDateFormat formatter1 = new SimpleDateFormat(
						"yyyy/MM/dd");
				try {
					timeYear1 = formatter1.format(formatter1
							.parse(timeYear1));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				holder1.tv_titleYear.setText(timeYear1.replace("/","."));


				if(position==0){
					holder1.lltop.setVisibility(View.VISIBLE);
					holder1.top.setVisibility(View.GONE);
				}
				if(position>0){
					if(listData.get(position).get("TitleName").equals(listData.get(position-1).get("TitleName"))){
						holder1.lltop.setVisibility(View.GONE);
						holder1.top.setVisibility(View.VISIBLE);
					}else{
						holder1.lltop.setVisibility(View.VISIBLE);
						holder1.top.setVisibility(View.GONE);
					}
				}
				if(listData.get(position).get("MomSay")==null||listData.get(position).get("MomSay").toString().equals("")){
					holder1.tv_say.setText(listData.get(position).get("Reason").toString());
				}else {
					holder1.tv_say.setText(listData.get(position).get("MomSay").toString());
				}
				if(!TextUtils.isEmpty(user.PicURL)) {

					mImageLoader.displayImage(user.PicURL, holder1.ivhead);
				}
				final int p1=position;
				holder1.llhoder.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String reason=listData.get(p1).get("Reason").toString();
						if(listData.get(p1).get("MomSay")==null||listData.get(p1).get("MomSay").toString().equals("")){
							reason=listData.get(p1).get("Reason").toString();
						}else {
							reason=listData.get(p1).get("MomSay").toString();
						}
						String gotime =listData.get(p1).get("Gotime").toString();

						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd");
						gotime = gotime.split(" ")[0].replace("/", "-");

						Intent i = new Intent(context,MyCHK_OrderAdd.class);
						i.putExtra("Reason", reason);
						i.putExtra("Gotime",gotime);
						i.putExtra("Orderid",listData.get(p1).get("OrderID").toString());
						i.putExtra("IsUpload",listData.get(p1).get("IsUpload").toString());
						i.putExtra("TitleName",listData.get(p1).get("TitleName").toString());
						context.startActivity(i);
					}
				});
				holder1.ivdel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						OrderDelete(listData.get(p1).get("OrderID").toString(),p1);
					}
				});
				break;
		}

		return convertView;

	}
	private ImageView findView(int img02) {
		// TODO Auto-generated method stub
		return null;
	}
	private void OrderDelete(String OrderID,final int p){
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				try {
					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
                            .getJSONArray("OrderDelete");
					if("0".equals(array.getJSONObject(0).getString("MessageCode"))){

						listData.remove(p);
						if(listData.size()==0){
							Intent intent = new Intent();
							intent.setAction("action.foot");
							context.sendBroadcast(intent);
						}
						notifyDataSetChanged();
						Toast.makeText(context,array.getJSONObject(0).getString("MessageContent").toString(),1).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				context, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><OrderID>%s</OrderID></Request>";
		str = String.format(str, new Object[]
				{OrderID});
		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
				SOAP_URL, paramMap);
	}

	
	 public  class ViewHolder0 {
		
		        public TextView tv_time;
		 public LinearLayout lltop;
		 View top;
		        public TextView tv_title;
		 public TextView tv_titleYear;
		        public TextView tv_hos;
		        public TextView tv_Doctor;
		 public ImageView iv_head;
		 public LinearLayout llgo;
		 public ImageView ivdel;


//		        public ImageView ivdot;
		    }
	public  class ViewHolder1 {
		public LinearLayout lltop;
		View top;
		public TextView tv_time;
		public TextView tv_title;
		public TextView tv_titleYear;
		public TextView tv_say;
		public  ImageView ivhead;
		public View llhoder;
		public ImageView ivdel;


//		        public ImageView ivdot;
	}

}

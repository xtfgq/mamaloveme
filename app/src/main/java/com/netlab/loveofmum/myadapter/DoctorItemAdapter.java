package com.netlab.loveofmum.myadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.netlab.loveofmum.activity.DoctorDetailsActivity;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.widget.CircularImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorItemAdapter extends BaseAdapter{
	private List<Map<String,Object>> mList;
	private Context mContext;
	private ImageLoader mImageLoader;
	public static final int DOCTOR_PAGE_SIZE = 3;
	
	public DoctorItemAdapter(Context context, List<Map<String,Object>> list, int page) {
		mContext = context;
	
		mImageLoader = ImageLoader.getInstance();
		mList = new ArrayList<Map<String,Object>>();
		int i = page * DOCTOR_PAGE_SIZE;
		int iEnd = i+DOCTOR_PAGE_SIZE;
		while ((i<list.size()) && (i<iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Map docInfo = mList.get(position);
		DocItem docItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.doctor_item, null);
			docItem = new DocItem();
			docItem.mDocIcon=(CircularImage) v.findViewById(R.id.fragment_mine_avatar);
			docItem.mDocName=(TextView) v.findViewById(R.id.tv_docname);
			docItem.mHosName=(TextView) v.findViewById(R.id.tv_hosname);
			docItem.mIsLine=(ImageView) v.findViewById(R.id.iv_isonline);
			v.setTag(docItem);
			convertView = v;
		} else {
			docItem = (DocItem)convertView.getTag();
		}
		mImageLoader.displayImage(MMloveConstants.JE_BASE_URL2 + mList.get(position).get("ImageURL").toString().replace("~", "").replace("\\", "/"), docItem.mDocIcon,ImageOptions.getTalkOptions(120));
		if(Integer.valueOf(mList.get(position).get("IsLine").toString())!=1){
			docItem.mIsLine.setImageResource(R.drawable.icon_online);
		}else{
			docItem.mIsLine.setImageResource(R.drawable.icon_offline);
		}

		// set the app name
		docItem.mDocName.setText(mList.get(position).get("DoctorName").toString());
		docItem.mHosName.setText(mList.get(position).get("HospitalName").toString());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!hasInternetConnected()){
					Toast.makeText(mContext, "数据获取失败，请检查网络！", 1).show();
					return;
				}
				if(!"".equals(mList.get(position).get("HospitalID").toString())){
				Intent i = new Intent(mContext,
						DoctorDetailsActivity.class);
				i.putExtra("DoctorID", mList.get(position).get("DoctorID").toString());
				i.putExtra("HospitalID", mList.get(position).get("HospitalID").toString());
				i.putExtra("AskCount", mList.get(position).get("AskCount").toString());
				i.putExtra("UserCount", mList.get(position).get("UserCount").toString());
				mContext.startActivity(i);
				}else{
//					Intent i = new Intent(mContext,
//							CHKItem_Base.class);
//					i.putExtra("ItemName","                     提示                     ");
//					i.putExtra("ItemContent","                 该医生不存在!                 ");
//					mContext.startActivity(i); 
//		
				}
			}
		});
		
		
		return convertView;
	}
	/**
	 * 判断是否有网络连接,没有返回false
	 */
	
	public boolean hasInternetConnected() {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}

	class DocItem {
		CircularImage mDocIcon;
		ImageView mIsLine;
		TextView mHosName;
		TextView mDocName;
		
	}

}

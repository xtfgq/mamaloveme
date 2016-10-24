package com.netlab.loveofmum.myadapter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.BBSWebView;
import com.netlab.loveofmum.MainTabActivity;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.TopicWebView;
import com.netlab.loveofmum.User_InfoChange;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.FeedAadapter.ViewHolder;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.utils.IOUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedLastAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private User user;
	private List<Map<String, Object>> listData;
	private MyApplication myApplication;

	private Context context;
	private String clientid;

	public FeedLastAdapter(Context context, List<Map<String, Object>> listData,
			String clientid) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.context = context;
		this.clientid = clientid;
		myApplication=MyApplication.getInstance();
		user = LocalAccessor.getInstance(context).getUser();
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_sq_last, null);
			holder.tvquanziname = (TextView) convertView.findViewById(R.id.tvquanziname);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tvnick=(TextView)convertView.findViewById(R.id.tv_nick_name);
			holder.tvtitle=(TextView)convertView.findViewById(R.id.tv_tiltle);
			holder.head=(ImageView)convertView.findViewById(R.id.iv_head);
			holder.view_count=(TextView)convertView.findViewById(R.id.tv_pingview);
			holder.pinglun_count=(TextView)convertView.findViewById(R.id.tv_pinglun);
			holder.tvtime=(TextView)convertView.findViewById(R.id.tvtime);
			holder.tvyu=(TextView)convertView.findViewById(R.id.tv_yunchan);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(listData.get(position).get("avatar")
				.toString().contains("vine.gif"))
		{
			holder.head.setImageResource(R.drawable.icon_user_normal);
		}
		else
		{
			String str=listData.get(position).get("avatar")
					.toString();
			ImageLoader.getInstance().displayImage(listData.get(position).get("avatar")
					.toString(), holder.head);

		}
		holder.tvquanziname.setText(listData.get(position).get("Name").toString());
		holder.tv_count.setText(Html.fromHtml(listData.get(position).get("content")
				.toString()));
		holder.tvtitle.setText(listData.get(position).get("title").toString());
		holder.tvnick.setText(listData.get(position).get("username").toString());
		holder.view_count.setText(listData.get(position).get("viewCount").toString());
		holder.pinglun_count.setText(listData.get(position).get("replyCount").toString());
		holder.tvtime.setText(IOUtils.getMistimingTimes(listData.get(position).get("createTime").toString()));
		holder.tvyu.setText("孕"+listData.get(position).get("YuBirthTime").toString());
		// final Object item=listData.get(position).get("Item");
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, TopicWebView.class);
				i.putExtra("url", listData.get(position).get("url").toString()
						+ "?returnflag=appindex" + "&UserID=" + user.UserID
						+ "&YuBithdayTime=" + user.YuBirthDate + "&ClientID="
						+ clientid);
				context.startActivity(i);

//				Intent i = new Intent(context, MainTabActivity.class);
//
//				i.putExtra("TabIndex", "B_TAB");
//				myApplication.urls=listData.get(position).get("url").toString()
//						+ "?returnflag=appindex" + "&UserID=" + user.UserID
//						+ "&YuBithdayTime=" + user.YuBirthDate + "&ClientID="
//						+ clientid;
//				context.startActivity(i);

			
			}
		});

		return convertView;

	}

	public static class ViewHolder {

		public TextView tv_count;
		public TextView tvnick;
		public TextView tvquanziname;
		public TextView tvtitle;
		public ImageView head;
		public TextView view_count;
		public TextView pinglun_count;
		public TextView tvtime;
		public TextView tvyu;

	}

}

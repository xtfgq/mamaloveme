package com.netlab.loveofmum.myadapter;

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

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.TopicWebView;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.BBSTalk;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.IOUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

public class FeedLastAdapter1 extends BaseAdapter {
	private LayoutInflater mInflater;
	private User user;
	private List<BBSTalk> listData;
	private MyApplication myApplication;

	private Context context;
	private String clientid;

	public FeedLastAdapter1(Context context, List<BBSTalk> listData,
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
		final BBSTalk talk = listData.get(position);
		if(talk.avatar.contains("vine.gif"))
		{
			holder.head.setImageResource(R.drawable.icon_user_normal);
		}
		else
		{
			ImageLoader.getInstance().displayImage(talk.avatar, holder.head);

		}
		holder.tvquanziname.setText(talk.bbsForumName);
		holder.tv_count.setText(talk.content);
		holder.tvtitle.setText(talk.title);
		holder.tvnick.setText(talk.username);
		holder.view_count.setText(talk.viewCount);
		holder.pinglun_count.setText(talk.replyCount);
		holder.tvtime.setText(IOUtils.getMistimingTimes(talk.createTime));
		holder.tvyu.setText("孕"+talk.YuBirthTime);
		holder.url =talk.getUrl();
		// final Object item=listData.get(position).get("Item");
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(context, TopicWebView.class);
//				i.putExtra("url", talk.getUrl()
//						+ "?returnflag=appindex" + "&UserID=" + user.UserID
//						+ "&YuBithdayTime=" + user.YuBirthDate + "&ClientID="
//						+ clientid);
//				context.startActivity(i);
//
////				Intent i = new Intent(context, MainTabActivity.class);
////
////				i.putExtra("TabIndex", "B_TAB");
////				myApplication.urls=listData.get(position).get("url").toString()
////						+ "?returnflag=appindex" + "&UserID=" + user.UserID
////						+ "&YuBithdayTime=" + user.YuBirthDate + "&ClientID="
////						+ clientid;
////				context.startActivity(i);
//
//
//			}
//		});

		return convertView;

	}

	public static class ViewHolder {
		public String url;
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

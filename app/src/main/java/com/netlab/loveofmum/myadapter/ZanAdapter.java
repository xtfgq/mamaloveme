package com.netlab.loveofmum.myadapter;

import java.util.List;
import java.util.Map;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.myadapter.MyListViewAdapter.ViewHolder;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZanAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> listdat;
	private ImageLoader mImageLoader;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public List<Map<String, Object>> getListdat() {
		return listdat;
	}

	public void setListdat(List<Map<String, Object>> listdat) {
		this.listdat = listdat;
	}

	private LayoutInflater mInflater;

	public ZanAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdat == null ? 0 : listdat.size();
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type = Integer
				.valueOf(listdat.get(position).get("Type").toString());
		if (type == 0) {
			return TYPE_1;

		} else {
			return TYPE_2;
		}

	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_1:
				convertView = mInflater.inflate(R.layout.item_reply_page_one,
						parent, false);
				holder2 = new ViewHolder2();
				holder2.tv_name = (TextView) convertView
						.findViewById(R.id.item_reply_pageone_name);
				holder2.tvcontent = (TextView) convertView
						.findViewById(R.id.item_reply_pageone_content);
				holder2.tvtime = (TextView) convertView
						.findViewById(R.id.item_reply_pageone_time);
				holder2.iv_head = (ImageView) convertView
						.findViewById(R.id.item_reply_pageone_img01);
				convertView.setTag(holder2);

				break;
			case TYPE_2:
				convertView = mInflater.inflate(R.layout.layout_replypage_two,
						parent, false);
				holder1 = new ViewHolder1();
				holder1.iv_head = (ImageView) convertView
						.findViewById(R.id.headreplay);
				holder1.tv_name = (TextView) convertView
						.findViewById(R.id.tvname);
				convertView.setTag(holder1);

				break;

			default:
				break;
			}

		} else {
			switch (type) {
			case TYPE_1:
				holder2 = (ViewHolder2) convertView.getTag();

				break;
			case TYPE_2:
				holder1 = (ViewHolder1) convertView.getTag();
				break;

			default:
				break;
			}

		}
		switch (type) {

		case TYPE_1:
			int Zantype = Integer.valueOf(listdat.get(position)
					.get("AuthorType").toString());
			int typeInner = Integer.valueOf(listdat.get(position)
					.get("ParentID").toString());
			if (typeInner == 0) {
				if("".equals(listdat.get(position).get("Name")
						.toString())){
					holder2.tv_name.setText("宝妈");

				}else {
					holder2.tv_name.setText(listdat.get(position).get("Name")
							.toString());
				}
			} else {
				String span;
				if("".equals(listdat.get(position).get("ReplyNick")
						.toString())){
					span = listdat.get(position).get("Name")
							.toString()
							+ "回复"
							+ "宝妈";

				}else {
					span = listdat.get(position).get("Name")
							.toString()
							+ "回复"
							+ listdat.get(position).get("ReplyNick").toString();
				}
				SpannableStringBuilder style = new SpannableStringBuilder(span);
				int bstart = span.indexOf("回复");
				int bend = bstart + ("回复").length();

				style.setSpan(
						new ForegroundColorSpan(mContext.getResources().getColor(
								R.color.pink)), bstart, bend,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				
				holder2.tv_name.setText(style);
			}
			if (Zantype == 0) {
				if (listdat.get(position).get("PicUrl").toString()
						.contains("vine.gif")) {
					holder2.iv_head
							.setImageResource(R.drawable.icon_user_normal);
				} else {
					mImageLoader.displayImage(MMloveConstants.JE_BASE_URL3
							+ listdat.get(position).get("PicUrl").toString()
									.replace("~", "").replace("\\", "/"),
							holder2.iv_head, ImageOptions.getHeaderDoctorOptions(120));
				}
			} else {
				if (listdat.get(position).get("PicUrl").toString()
						.contains("vine.gif")) {
					holder2.iv_head.setImageResource(R.drawable.touxiang);
				} else {
					mImageLoader.displayImage(MMloveConstants.JE_BASE_URL2
							+ listdat.get(position).get("PicUrl").toString()
									.replace("~", "").replace("\\", "/"),
							holder2.iv_head,
							ImageOptions.getPinglunOptions(120));
				}
			}

			holder2.tvcontent.setText(listdat.get(position).get("Content")
					.toString());
			holder2.tvtime
					.setText(listdat.get(position).get("Time").toString());

			break;
		case TYPE_2:

			if (listdat.get(position).get("PictureUrl").toString()
					.contains("vine.gif")) {
				holder1.iv_head.setImageResource(R.drawable.icon_user_normal);
			} else {
				mImageLoader.displayImage(MMloveConstants.JE_BASE_URL3
						+ listdat.get(position).get("PictureUrl").toString()
								.replace("~", "").replace("\\", "/"),
						holder1.iv_head, ImageOptions.getHeaderDoctorOptions(120));
			}

			if("".equals(listdat.get(position).get("Name")
					.toString())){
				holder1.tv_name.setText("宝妈");
			}else {
				holder1.tv_name.setText(listdat.get(position).get("Name")
						.toString());
			}
			break;

		default:
			break;
		}

		return convertView;
	}

	public static class ViewHolder1 {

		public TextView tv_name;
		public ImageView iv_head;

	}

	public static class ViewHolder2 {
		public TextView tv_name;
		public TextView tvcontent;
		public TextView tvtime;
		public ImageView iv_head;

	}
}

package com.netlab.loveofmum.myadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.netlab.loveofmum.Activity_ReplyPage;
import com.netlab.loveofmum.DialogEnsure;
import com.netlab.loveofmum.ImagePagesAct;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.ReplyAct;

import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.Image;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.ScreenUtils;
import com.netlab.loveofmum.widget.CustomImageView;
import com.netlab.loveofmum.widget.DialogEnsureEdit;
import com.netlab.loveofmum.widget.NineGridlayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TalkAbount extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> datalist;
	private User user;
	private String doctorid;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ZANACTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleLikesInsert;
	private final String SOAP_ZANMETHODNAMEA = MMloveConstants.ArticleLikesInsert;
	private LinearLayout header;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_PINGLUNCTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleTopicInsert;
	private final String SOAP_PINGLUNMETHODNAMEA = MMloveConstants.ArticleTopicInsert;

	public TalkAbount(Context context, String doctorid) {
		this.context = context;
		this.datalist = datalist;
		this.doctorid = doctorid;
		user = LocalAccessor.getInstance(context).getUser();
	}

	public List<Map<String, Object>> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<Map<String, Object>> datalist) {
		this.datalist = datalist;
	}

	@Override
	public int getCount() {
		return datalist == null ? 0 : datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub

		return Integer.valueOf(datalist.get(position).get("Type").toString());
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return VIEW_TYPE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewArticleHolder articleHolder = null;
		ViewHolder viewHolder = null;
		List<Image> itemList = (List<Image>) datalist.get(position).get(
				"itemList");
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			// 0 长文章
			case TYPE_1:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_doctor_home, parent, false);
				articleHolder = new ViewArticleHolder();
				articleHolder.tvdocname = (TextView) convertView
						.findViewById(R.id.item_doctor_home_name);
				articleHolder.tvtime = (TextView) convertView
						.findViewById(R.id.item_doctor_home_time);
				articleHolder.Content = (TextView) convertView
						.findViewById(R.id.item_doctor_home_content);
				articleHolder.tvArtcile = (TextView) convertView
						.findViewById(R.id.tvlink);
				articleHolder.pinglunnum = (TextView) convertView
						.findViewById(R.id.item_chkdetail_list_txt005);
				articleHolder.zan = (TextView) convertView
						.findViewById(R.id.item_doctor_home_zan);
				articleHolder.pinglun = (TextView) convertView
						.findViewById(R.id.item_doctor_home_pinglun);
				convertView.setTag(articleHolder);
				break;

			case TYPE_2:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_ninegridlayout, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.ivMore = (NineGridlayout) convertView
						.findViewById(R.id.iv_ngrid_layout);
				viewHolder.ivOne = (CustomImageView) convertView
						.findViewById(R.id.iv_oneimage);
				viewHolder.tvdocname = (TextView) convertView
						.findViewById(R.id.tvname);
				viewHolder.tvtime = (TextView) convertView
						.findViewById(R.id.tvtime);
				viewHolder.Content = (TextView) convertView
						.findViewById(R.id.tvcontent);
				viewHolder.zan = (TextView) convertView
						.findViewById(R.id.item_doctor_home_zan);
				viewHolder.pinglunnum = (TextView) convertView
						.findViewById(R.id.item_chkdetail_list_txt005);
				viewHolder.pinglun = (TextView) convertView
						.findViewById(R.id.item_doctor_home_pinglun);
				convertView.setTag(viewHolder);
				break;

			default:
				break;
			}

		} else {
			switch (type) {
			case TYPE_1:
				articleHolder = (ViewArticleHolder) convertView.getTag();
				break;
			case TYPE_2:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			}

		}
		switch (type) {
		case TYPE_1:
//			articleHolder = (ViewArticleHolder) convertView.getTag();
			setViewArticleHolder(position, articleHolder);
			break;

		case TYPE_2:
			if (itemList.isEmpty() || itemList.isEmpty()) {
				viewHolder.ivMore.setVisibility(View.GONE);
				viewHolder.ivOne.setVisibility(View.GONE);
			} else if (itemList.size() == 1) {
				viewHolder.ivMore.setVisibility(View.GONE);
				viewHolder.ivOne.setVisibility(View.VISIBLE);

				handlerOneImage(viewHolder, itemList.get(0));
			} else {
				viewHolder.ivMore.setVisibility(View.VISIBLE);
				viewHolder.ivOne.setVisibility(View.GONE);
				viewHolder.ivMore.setImagesData(itemList);
			}
			setViewHolder(position, viewHolder);
			break;
		}

		return convertView;
	}

	private void setViewArticleHolder(int position, ViewArticleHolder viewHolder) {
		// TODO Auto-generated method stub
		viewHolder.tvdocname.setText(datalist.get(position).get("DoctorName")
				.toString());
		viewHolder.tvtime.setText(datalist.get(position).get("CreatedDate")
				.toString());
		viewHolder.Content.setText(datalist.get(position).get("Title")
				.toString());
		viewHolder.zan.setText(datalist.get(position).get("likecount")
				.toString());
		viewHolder.pinglunnum.setText(datalist.get(position).get("ReplysCount")
				.toString());
		final int pinglunNum=Integer.valueOf(datalist.get(position).get("ReplysCount")
				.toString());
		final int p = position;
		viewHolder.tvArtcile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent im = new Intent(context, Activity_ReplyPage.class);
				im.putExtra("ID", datalist.get(p).get("ID").toString());
				context.startActivity(im);

			}
		});
		viewHolder.pinglunnum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pinglunNum>0) {
					Intent im = new Intent(context, ReplyAct.class);
					im.putExtra("ArticleID", datalist.get(p).get("ID").toString());
					context.startActivity(im);
				}else{
					Toast.makeText(context,R.string.titl_pinglunnume,1).show();
				}
			}
		});
		viewHolder.zan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user=LocalAccessor.getInstance(context).getUser();
				if(user.UserID!=0) {
					postZan(p);
				}else{
					context.startActivity(new Intent(context, LoginActivity.class));
				}
			}
		});
		viewHolder.pinglun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!hasInternetConnected()) {
					Toast.makeText(context, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
					return;
				}
				user=LocalAccessor.getInstance(context).getUser();
				if(user.UserID!=0){
					DialogEnsureEdit dialogEdit = new DialogEnsureEdit(context);
					final EditText edit = dialogEdit.getmEditText();

					dialogEdit.setDialogMsg("请填写评论内容", "确定")
							.setOnClickListenerEnsure(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (TextUtils.isEmpty(edit.toString().trim())
											|| edit.toString().trim().length() == 0) {
										Toast.makeText(context, R.string.topic_msg,
												Toast.LENGTH_SHORT).show();
										return;
									} else {

										String inputName = Html.toHtml(edit
												.getText());

										if (inputName.equals("")
												|| edit.getText().toString().trim()
												.length() == 0) {
											Toast.makeText(context,
													R.string.topic_msg,
													Toast.LENGTH_SHORT).show();
										} else {
											if (inputName.contains("\"ltr\"")) {

											} else {
												inputName = inputName.replace(
														"ltr", "\"ltr\"");
											}
											edit.clearFocus();

											postPinglun(inputName, p, edit);

										}
									}
								}
							});
					DialogUtils.showSelfDialog(context, dialogEdit);

				}else{
					context.startActivity(new Intent(context,LoginActivity.class));
				}

			}
		});
	}

	public void setViewHolder(int position, ViewHolder viewHolder) {
		viewHolder.tvdocname.setText(datalist.get(position).get("DoctorName")
				.toString());
		viewHolder.tvtime.setText(datalist.get(position).get("CreatedDate")
				.toString());
		viewHolder.Content.setText(datalist.get(position).get("Content")
				.toString());
		viewHolder.zan.setText(datalist.get(position).get("likecount")
				.toString());
		viewHolder.pinglunnum.setText(datalist.get(position).get("ReplysCount")
				.toString());
		final int p = position;
		final int num=Integer.valueOf(datalist.get(position).get("ReplysCount")
				.toString());
		viewHolder.pinglunnum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(num>0){
				Intent im = new Intent(context, ReplyAct.class);
				im.putExtra("ArticleID", datalist.get(p).get("ID").toString());
				context.startActivity(im);
			}else{
					Toast.makeText(context,R.string.titl_pinglunnume,1).show();
				}
			}
		});
		viewHolder.zan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user=LocalAccessor.getInstance(context).getUser();
				if(user.UserID!=0) {
					postZan(p);
				}else{
					context.startActivity(new Intent(context,LoginActivity.class));
				}
			}
		});
		viewHolder.pinglun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!hasInternetConnected()) {
					Toast.makeText(context, R.string.msgUninternet,
							Toast.LENGTH_SHORT).show();
					return;
				}
				user=LocalAccessor.getInstance(context).getUser();
				if(user.UserID!=0){
					DialogEnsureEdit dialogEdit = new DialogEnsureEdit(context);
					final EditText edit = dialogEdit.getmEditText();

					dialogEdit.setDialogMsg("请填写评论内容", "确定")
							.setOnClickListenerEnsure(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (TextUtils.isEmpty(edit.toString().trim())
											|| edit.toString().trim().length() == 0) {
										Toast.makeText(context, R.string.topic_msg,
												Toast.LENGTH_SHORT).show();
										return;
									} else {

										String inputName = Html.toHtml(edit
												.getText());

										if (inputName.equals("")
												|| edit.getText().toString().trim()
												.length() == 0) {
											Toast.makeText(context,
													R.string.topic_msg,
													Toast.LENGTH_SHORT).show();
										} else {
											if (inputName.contains("\"ltr\"")) {

											} else {
												inputName = inputName.replace(
														"ltr", "\"ltr\"");
											}
											edit.clearFocus();

											postPinglun(inputName, p, edit);

										}
									}
								}
							});
					DialogUtils.showSelfDialog(context, dialogEdit);

				}else{
					context.startActivity(new Intent(context,LoginActivity.class));
				}

			}

		});
	}

	protected void postPinglun(String input, int position, final EditText edit) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String value = result.toString();
				// {"ArticleLikesInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
				JSONObject mySO = (JSONObject) result;
				JSONArray array;
				try {
					array = mySO.getJSONArray("ArticleTopicInsert");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {

						if(array.getJSONObject(0).has("PointAddCode")) {
							if(array.getJSONObject(0).has("PointAddCode")) {
								if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

									Intent i = new Intent(context, DialogEnsure.class);
									i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
									context.startActivity(i);
								}
							}
						}
						Intent intent = new Intent();
						intent.setAction("action.doctor");
						context.sendBroadcast(intent);
					} else {
						Toast.makeText(
								context,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(context, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 0 用户 1医生
		String str = "<Request><ArticleID>%s</ArticleID><ParentID>%s</ParentID><AuthorID>%s</AuthorID><AuthorType>%s</AuthorType><ReplyID>%s</ReplyID><ReplyType>%s</ReplyType>"
				+ "<Content>%s</Content></Request>";
		str = String.format(
				str,
				new Object[] {
						datalist.get(position).get("ID").toString(),
						"0",
						user.UserID + "",
						"0",
						doctorid,
						"1",
						input.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll(
								"<[^>]*>", "\n\t") });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_PINGLUNCTION,
				SOAP_PINGLUNMETHODNAMEA, SOAP_URL, paramMap);
	}

	protected void postZan(int position) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String value = result.toString();
				// {"ArticleLikesInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
				JSONObject mySO = (JSONObject) result;
				JSONArray array;
				try {
					array = mySO.getJSONArray("ArticleLikesInsert");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						Intent intent = new Intent();
						intent.setAction("action.doctor");
						context.sendBroadcast(intent);
					} else {
						Toast.makeText(
								context,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		if (hasInternetConnected()) {
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(context, true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><ArticleID>%s</ArticleID></Request>";
			str = String.format(str, new Object[] { user.UserID + "",
					datalist.get(position).get("ID").toString() });

			paramMap.put("str", str);
			task.execute(SOAP_NAMESPACE, SOAP_ZANACTION, SOAP_ZANMETHODNAMEA,
					SOAP_URL, paramMap);
		} else {
			Toast.makeText(context, R.string.msgUninternet, Toast.LENGTH_SHORT)
					.show();
		}
		// ArticleLikesInsert
	}

	public boolean hasInternetConnected() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}

	private void handlerOneImage(ViewHolder viewHolder, final Image image) {
		int totalWidth;
		int imageWidth;
		int imageHeight;
		// ScreenTools screentools = ScreenTools.instance(context);
		totalWidth = ScreenUtils.getScreenWidth(context)
				- ScreenUtils.dip2px(context, 80);
		imageWidth = ScreenUtils.dip2px(context, image.getWidth());
		imageHeight = ScreenUtils.dip2px(context, image.getHeight());
		if (image.getWidth() <= image.getHeight()) {
			if (imageHeight > totalWidth) {
				imageHeight = totalWidth;
				imageWidth = (imageHeight * image.getWidth())
						/ image.getHeight();
			}
		} else {
			if (imageWidth > totalWidth) {
				imageWidth = totalWidth;
				imageHeight = (imageWidth * image.getHeight())
						/ image.getWidth();
			}
		}

		ViewGroup.LayoutParams layoutparams = viewHolder.ivOne
				.getLayoutParams();
		layoutparams.height = imageHeight;
		layoutparams.width = imageWidth;
		viewHolder.ivOne.setLayoutParams(layoutparams);
		viewHolder.ivOne.setClickable(true);

		viewHolder.ivOne
				.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
		viewHolder.ivOne.setImageUrl(image.getUrl());

		viewHolder.ivOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ImagePagesAct.class);
				ArrayList<String> urls = new ArrayList<String>();
				urls.add(image.getUrl());
				intent.putStringArrayListExtra("imgs", urls);
				intent.putExtra("position", 0);
				context.startActivity(intent);
			}
		});

	}

	class ViewArticleHolder {

		public TextView tvdocname;
		public TextView Content;
		public TextView tvtime;
		public TextView tvArtcile;
		public TextView zan;
		public TextView pinglun;
		public TextView pinglunnum;
	}

	class ViewHolder {
		public NineGridlayout ivMore;
		public CustomImageView ivOne;
		public TextView tvdocname;
		public TextView Content;
		public TextView tvtime;
		public TextView zan;
		public TextView pinglun;
		public TextView pinglunnum;
	}

}

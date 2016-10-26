package com.netlab.loveofmum;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.huanxin.ReplyPageListAdapter;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.ReplyItemAdapter;
import com.netlab.loveofmum.myadapter.ZanAdapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.FoundWebView;
import com.netlab.loveofmum.widget.FoundWebView.ScrollInterface;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.PullableScrollView;
import com.netlab.loveofmum.widget.ScrollViewExtend;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 说说文章详情页
 * 
 * @author guoqiang
 * 
 */
public class Activity_ReplyPage extends BaseActivity implements
		OnClickListener, OnRefreshListener {
	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
	private String returnvalue001;
	private ListViewForScrollView listview;
	private ListViewForScrollView listviewreply;
	private FoundWebView mWebView = null;
	private TextView txtHead;
	private ImageView imgBack;
	private Intent mIntent;
	private ZanAdapter zanAdapter;
	private LinearLayout replypagelinear, zanlineaout, pinglunlayout;
	private TextView[] mTabViews;
	private int searchLayoutTop, webviewHight;
	// LinearLayout rlayout;
	RelativeLayout rlsame, rltips;
	PullToRefreshLayout refreshview;
	private PullableScrollView myScrollView;
	private ImageView ivhead;
	private TextView tvname, tvcreate;
	Boolean isFinsh = false;
	String ID, likecount, ReplysCount, DoctorName;
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_URL = MMloveConstants.URL002;
	// MicroTopicDeitalInquiry
	// 查询文章详情
	private final String SOAP_ACTIONMICROTOPICDEITALINQUIRY = MMloveConstants.URL001
			+ MMloveConstants.MicroTopicDeitalInquiry;
	private final String SOAP_METHODNAMEMICROTOPICDEITALINQUIRY = MMloveConstants.MicroTopicDeitalInquiry;
	// 查询评论列表
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleTopicInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.ArticleTopicInquiry;
	// 查询评论列表
	private final String SOAP_LIKEACTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleLikesInquiry;
	private final String SOAP_LIKEMETHODNAME = MMloveConstants.ArticleLikesInquiry;

	private final String SOAP_ZANACTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleLikesInsert;
	private final String SOAP_ZANMETHODNAMEA = MMloveConstants.ArticleLikesInsert;
	private final String SOAP_PINGLUNCTION = MMloveConstants.URL001
			+ MMloveConstants.ArticleTopicInsert;
	private final String SOAP_PINGLUNMETHODNAMEA = MMloveConstants.ArticleTopicInsert;

	protected int page;
	public static final int FRIST_GET_DATE = 111;
	public static final int REFRESH_GET_DATE = 112;
	public static final int LOADMORE_GET_DATE = 113;
	private int postion;
	private User user;
	String doctorid;
	private EditText edit;
	private ImageLoader mImageLoader;
	private TextView tvhits;
	private RelativeLayout loadmore;
	private TextView loadstate_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.layout_reply_page);
		mWebView = (FoundWebView) findViewById(R.id.reply_page_web);
		WebSettings webSettings = mWebView.getSettings();
		// mWebView.setWebViewClient(new MyWebViewClient());
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		mIntent = this.getIntent();
		// String
		// url="http://baike.baidu.com/link?url=kfoey0k1sJGt1V9viGbjU4b79QtekLHwMTPgXdO4mhb_Wkg8j-pYRfIJMjbWVU654TWYwXs6asUMgC66EXqOBIZCe7uT6MKdUBy3jrz036nyaYWcCWeczgkCU6xN-z8QDlRnTrSGATwdDQd4rLXcT_gA57-HDq1uUjRG7DQeZoK";
		page = 1;
		mIntent = this.getIntent();
		mImageLoader = ImageLoader.getInstance();
		user = LocalAccessor.getInstance(Activity_ReplyPage.this).getUser();
		if (mIntent != null) {
			ID = mIntent.getStringExtra("ID");

		}
		String url = MMloveConstants.URLARTICLE + ID;
		mWebView.loadUrl(url);

		// mWebView.setOnCustomScroolChangeListener(this);
		iniView();
		// getArtcicledDes(ID);
		zanAdapter = new ZanAdapter(Activity_ReplyPage.this);
		listviewreply.setAdapter(zanAdapter);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回键退回
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// getArtcile();
		setText(0);
		myScrollView.smoothScrollTo(0, 0);
		refreshview.setOnRefreshListener(this);
		// listviewreply.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "44444", 1).show();
		// if (firstVisibleItem >= 0) {
		// Toast.makeText(getApplicationContext(), "5555555", 1).show();
		// Toast.makeText(getApplicationContext(), "yyyyy"+visibleItemCount,
		// 1).show();
		// rltips.setVisibility(View.VISIBLE);
		// } else {
		// Toast.makeText(getApplicationContext(), "xxxxxxx"+visibleItemCount,
		// 1).show();
		// Toast.makeText(getApplicationContext(), "6666665", 1).show();
		// rltips.setVisibility(View.GONE
		// );
		// }
		//
		// }

		// });
	}

	protected void postZan() {
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

					} else {
						Toast.makeText(
								Activity_ReplyPage.this,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}
					if (postion == 1) {
						reZanList(arrayList2.size());
					}
					getArtcicledDes(ID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		if (hasInternetConnected()) {
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					Activity_ReplyPage.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><ArticleID>%s</ArticleID></Request>";
			str = String.format(str, new Object[] { user.UserID + "", ID });

			paramMap.put("str", str);
			task.execute(SOAP_NAMESPACE, SOAP_ZANACTION, SOAP_ZANMETHODNAMEA,
					SOAP_URL, paramMap);
		} else {
			Toast.makeText(Activity_ReplyPage.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
		// ArticleLikesInsert
	}

	// private void geneItems(final int ACTION, int postition) {
	// if (ACTION == FRIST_GET_DATE) {// 第一次加载 0 文章 1 说说
	// if (arrayList2.size() > 0) {
	// arrayList2.clear();
	// }
	// // getDate(page);
	// if (postition == 0) {
	// searchPostList();
	// } else if (postition == 1) {
	// searchZanList();
	//
	// }
	//
	// } else if (ACTION == LOADMORE_GET_DATE) {
	// page++;
	// if (postition == 0) {
	// searchPostList();
	// } else if (postition == 1) {
	// searchZanList();
	// }
	// // getDate(page);
	// }
	// }

	private void getArtcicledDes(String ID) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
				// TODO Auto-generated method stub

				String value = result.toString();

				JSONObject mySO = (JSONObject) result;

				try {
					JSONArray array = mySO
							.getJSONArray("MicroTopicDeitalInquiry");
					ReplysCount = array.getJSONObject(0)
							.getString("ReplysCount").toString();
					likecount = array.getJSONObject(0).getString("likecount")
							.toString();
					doctorid = array.getJSONObject(0).getString("DoctorID")
							.toString();
					DoctorName = array.getJSONObject(0).getString("DoctorName")
							.toString();
					String urlimg = array.getJSONObject(0)
							.getString("ImageURL1").toString();
					String createdate = array.getJSONObject(0)
							.getString("CreatedDate").toString();
					createdate = createdate.split(" ")[0].replace("/", "-");

					if (urlimg.contains("vine.gif")) {
						ivhead.setImageResource(R.drawable.icon_user_normal);
					} else {
						mImageLoader.displayImage(MMloveConstants.JE_BASE_URL2
								+ urlimg.replace("~", "").replace("\\", "/"),
								ivhead, ImageOptions.getTalkOptions(120));
					}
					tvcreate.setText(createdate);
					tvname.setText(DoctorName);
					txtHead.setText(DoctorName);
					tvhits.setText("阅读 "
							+ array.getJSONObject(0).getString("Hits")
									.toString());
					mTabViews[0].setText("评论:" + ReplysCount);
					mTabViews[1].setText("赞:" + likecount);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><ID>%s</ID></Request>";
		str = String.format(str, new Object[] { ID, });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONMICROTOPICDEITALINQUIRY,
				SOAP_METHODNAMEMICROTOPICDEITALINQUIRY, SOAP_URL, paramMap);
	}

	private void searchPostList() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					String returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("ArticleTopicInquiry");

							if (array.getJSONObject(0).has("MessageCode")) {
								if (page == 1) {
									loadmore.setVisibility(View.GONE);
								}

								else {

									loadmore.setVisibility(View.VISIBLE);
									loadstate_tv.setText("没有更多数据");
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件加载完毕了哦！
											refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);
									listviewreply.setSelection(listviewreply.getBottom());

								}
							} else {
								for (int i = 0; i < array.length(); i++) {
									String time = array.getJSONObject(i)
											.get("CreatedDate").toString()
											.replace("/", "-");
									time = IOUtils.stringToDate2(time,
											"yyyy-MM-dd HH:mm:ss");
									String times = IOUtils.getMistimingTimes(
											time).toString();
									map = new HashMap<String, Object>();
									map.put("AuthorType",
											array.getJSONObject(i)
													.getString("AuthorType")
													.toString());
									map.put("Time", times);
									map.put("Type", 0 + "");
									map.put("CreatedDate",
											array.getJSONObject(i).getString(
													"CreatedDate"));
									String str = array.getJSONObject(i)
											.getString("AuthorNickPic");
									if ("".equals(str)) {
										map.put("Name", "");
										map.put("PicUrl", "");

									} else {
										String[] strs = str.split("\\,");
										map.put("Name", strs[0]);
										map.put("PicUrl", strs[1]);
										

									}
									
									String str1 = array.getJSONObject(i)
											.getString("ReplyNickPic");
									if ("".equals(str1)) {
										map.put("ReplyNick", "");

									} else {
										String[] strs1 = str1.split("\\,");
										map.put("ReplyNick", strs1[0]);

									}
									map.put("ParentID", array.getJSONObject(i)
											.getString("ParentID"));
									
									map.put("Content", array.getJSONObject(i)
											.getString("Content"));
									arrayList2.add(map);
								}
							}

							zanAdapter.setListdat(arrayList2);
							zanAdapter.notifyDataSetChanged();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}
						} catch (Exception ex) {
							ex.printStackTrace();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}

						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					Activity_ReplyPage.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ArticleID>%s</ArticleID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

			str = String.format(str, new Object[] { ID, page + "", 10 + "" });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void rePostList(int size) {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					String returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							if (arrayList2.size() > 0) {
								arrayList2.clear();
							}
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("ArticleTopicInquiry");
							if (array.getJSONObject(0).has("MessageCode")) {
								if (page == 1) {
									loadmore.setVisibility(View.GONE);
								}

								else {

									loadmore.setVisibility(View.VISIBLE);
									loadstate_tv.setText("没有更多数据");
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件加载完毕了哦！
											refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);
									listviewreply.setSelection(listviewreply.getBottom());

								}
							} else{

							for (int i = 0; i < array.length(); i++) {
								String time = array.getJSONObject(i)
										.get("CreatedDate").toString()
										.replace("/", "-");
								time = IOUtils.stringToDate2(time,
										"yyyy-MM-dd HH:mm:ss");
								String times = IOUtils.getMistimingTimes(time)
										.toString();
								map = new HashMap<String, Object>();
								map.put("Time", times);
								map.put("Type", 0 + "");
								map.put("CreatedDate", array.getJSONObject(i)
										.getString("CreatedDate"));
								map.put("AuthorType",
										array.getJSONObject(i)
												.getString("AuthorType")
												.toString());
								map.put("ParentID", array.getJSONObject(i)
										.getString("ParentID"));
								String str = array.getJSONObject(i).getString(
										"AuthorNickPic");
								String[] strs = str.split("\\,");
								map.put("Name", strs[0]);

								map.put("PicUrl", strs[1]);
								map.put("Content", array.getJSONObject(i)
										.getString("Content"));
								arrayList2.add(map);
							}
								}

							zanAdapter.setListdat(arrayList2);
							zanAdapter.notifyDataSetChanged();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}
						} catch (Exception ex) {
							ex.printStackTrace();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}

						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					Activity_ReplyPage.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ArticleID>%s</ArticleID><PageIndex>%s</PageIndex><PageSize>%s</PageSize><ParentID>%s</ParentID></Request>";

			str = String.format(str, new Object[] { ID, 1 + "", size + "",
					0 + "" });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
	}

	public void goZan(View v) {
		user=LocalAccessor.getInstance(Activity_ReplyPage.this).getUser();
		if(user.UserID!=0) {
			postZan();
		}else{
			startActivity(new Intent(Activity_ReplyPage.this,LoginActivity.class));
		}
	}

	public void goSend(View v) {
		user=LocalAccessor.getInstance(Activity_ReplyPage.this).getUser();
       if(user.UserID!=0) {
		   String inputName = Html.toHtml(edit.getText());

		   if (inputName.equals("")
				   || edit.getText().toString().trim().length() == 0) {
			   Toast.makeText(Activity_ReplyPage.this, R.string.topic_msg,
					   Toast.LENGTH_SHORT).show();
		   } else {
			   edit.setText("");
			   if (inputName.contains("\"ltr\"")) {

			   } else {
				   inputName = inputName.replace("ltr", "\"ltr\"");
			   }
			   edit.clearFocus();

			   postPinglun(inputName, edit);

		   }
	   }else{
		   startActivity(new Intent(Activity_ReplyPage.this,LoginActivity.class));
	   }

	}

	protected void postPinglun(String input, final EditText edit) {
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
//						startActivity(new Intent(Activity_ReplyPage.this,
//								PingLunAct.class));
						if(array.getJSONObject(0).has("PointAddCode")) {
							if(array.getJSONObject(0).getString("PointAddCode").equals("0")) {

								Intent i = new Intent(Activity_ReplyPage.this,DialogEnsure.class);
								i.putExtra("content", array.getJSONObject(0).getString("PointAddContent") + "+" + array.getJSONObject(0).getString("PointValue"));
								startActivity(i);
							}
						}

					} else {
						Toast.makeText(
								Activity_ReplyPage.this,
								array.getJSONObject(0).getString(
										"MessageContent"), 1).show();
					}
					
					if (postion == 0) {
						setText(0);
					}
					getArtcicledDes(ID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				Activity_ReplyPage.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 0 用户 1医生
		String str = "<Request><ArticleID>%s</ArticleID><ParentID>%s</ParentID><AuthorID>%s</AuthorID><AuthorType>%s</AuthorType><ReplyID>%s</ReplyID><ReplyType>%s</ReplyType>"
				+ "<Content>%s</Content></Request>";
		str = String.format(
				str,
				new Object[] {
						ID,
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

	// class MyWebViewClient extends WebViewClient {
	// // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
	// @Override
	// public boolean shouldOverrideUrlLoading(WebView view, String url) {
	// if (url.startsWith("tel:")) {
	// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	// } else {
	// view.loadUrl(url);
	// }
	//
	// // 如果不需要其他对点击链接事件的处理返回true，否则返回false
	// return true;
	// }
	//
	// @Override
	// public void onPageFinished(WebView view, String url) {
	// // TODO Auto-generated method stub
	// super.onPageFinished(view, url);
	//
	// }
	//
	// @Override
	// public void onPageStarted(WebView view, String url, Bitmap favicon) {
	// // TODO Auto-generated method stub
	// super.onPageStarted(view, url, favicon);
	// }
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.onPause(); // 暂停网页中正在播放的视频
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mWebView.onResume();

	}

	// private void showView(int postion) {
	// // TODO Auto-generated method stub
	// switch (postion){
	// case 0:
	// replypagelinear.removeAllViews();
	// View v1 = getLocalActivityManager().startActivity(
	// "one", new Intent(Activity_ReplyPage.this,
	// ReplyPageOneList.class)).getDecorView();
	//
	// replypagelinear.addView(v1);
	// break;
	// case 1:
	// replypagelinear.removeAllViews();
	// View v2 = getLocalActivityManager().startActivity("two",
	// new Intent(Activity_ReplyPage.this,
	// ReplyPageTwoListAct.class)).getDecorView();
	// replypagelinear.addView(v2);
	// break;
	// }
	// }
	private void setText(int postion) {

		for (int m = 0; m < mTabViews.length; m++) {
			mTabViews[m].setTextColor(getResources()
					.getColor(R.color.tabscolor));
			mTabViews[m].setBackgroundColor(this.getResources().getColor(
					R.color.white));
		}
		mTabViews[postion].setTextColor(getResources().getColor(R.color.pink));
		mTabViews[postion].setBackgroundResource(R.drawable.linepink);
		this.postion = postion;
		getArtcicledDes(ID);
		getDasta(postion);
		// showView(postion);
	}

	private void getDasta(int i) {
		// TODO Auto-generated method stub
		if (i == 0) {
			if (arrayList2.size() > 0) {
				arrayList2.clear();
			}
			page = 1;

			searchPostList();

		} else if (i == 1) {
			if (arrayList2.size() > 0) {
				arrayList2.clear();
			}
			page = 1;

			searchZanList();

		}
	}

	private void searchZanList() {
		// TODO Auto-generated method stub
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					String returnvalue001 = result.toString();
					if (returnvalue001 == null) {
					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("ArticleLikesInquiry");
							if (arrayList2.size() > 0 && page==1) {
								arrayList2.clear();
							}

							if (array.getJSONObject(0).has("MessageCode")) {
								if (page == 1) {
									loadmore.setVisibility(View.GONE);
								}

								else {

									loadmore.setVisibility(View.VISIBLE);
									loadstate_tv.setText("没有更多数据");
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件加载完毕了哦！
											refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);
									listviewreply.setSelection(listviewreply.getBottom());

								}
							}  else {

								for (int i = 0; i < array.length(); i++) {
									String time = array.getJSONObject(i)
											.get("CreatedDate").toString()
											.replace("/", "-");
									time = IOUtils.stringToDate2(time,
											"yyyy-MM-dd HH:mm:ss");
									String times = IOUtils.getMistimingTimes(
											time).toString();
									map = new HashMap<String, Object>();
									map.put("Time", times);
									map.put("Type", 1 + "");
									map.put("Name", array.getJSONObject(i)
											.getString("NickName"));
									map.put("CreatedDate",
											array.getJSONObject(i).getString(
													"CreatedDate"));
									map.put("PictureUrl",
											array.getJSONObject(i).getString(
													"PictureUrl"));
									arrayList2.add(map);
								}
							}

							zanAdapter.setListdat(arrayList2);
							zanAdapter.notifyDataSetChanged();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}

						} catch (Exception ex) {
							ex.printStackTrace();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}
						}
					}
				}
			};
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					Activity_ReplyPage.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ArticleID>%s</ArticleID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

			str = String.format(str, new Object[] { ID, page + "", 10 + "" });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_LIKEACTION, SOAP_LIKEMETHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void reZanList(int size) {
		// TODO Auto-generated method stub
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					String returnvalue001 = result.toString();
					if (returnvalue001 == null) {

					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("ArticleLikesInquiry");
							if (arrayList2.size() > 0) {
								arrayList2.clear();
							}
							if (array.getJSONObject(0).has("MessageCode")) {
								if (page == 1) {
									loadmore.setVisibility(View.GONE);
								}

								else {

									loadmore.setVisibility(View.VISIBLE);
									loadstate_tv.setText("没有更多数据");
									new Handler() {
										@Override
										public void handleMessage(Message msg) {
											// 千万别忘了告诉控件加载完毕了哦！
											refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}.sendEmptyMessageDelayed(0, 500);
									listviewreply.setSelection(listviewreply.getBottom());

								}
							} else{
							for (int i = 0; i < array.length(); i++) {
								String time = array.getJSONObject(i)
										.get("CreatedDate").toString()
										.replace("/", "-");
								time = IOUtils.stringToDate2(time,
										"yyyy-MM-dd HH:mm:ss");
								String times = IOUtils.getMistimingTimes(time)
										.toString();
								map = new HashMap<String, Object>();

								map.put("Time", times);
								map.put("Type", 1 + "");
								map.put("Name", array.getJSONObject(i)
										.getString("NickName"));
								map.put("CreatedDate", array.getJSONObject(i)
										.getString("CreatedDate"));
								map.put("PictureUrl", array.getJSONObject(i)
										.getString("PictureUrl"));

								arrayList2.add(map);
							}
								}
							zanAdapter.setListdat(arrayList2);
							zanAdapter.notifyDataSetChanged();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.SUCCEED);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}
						} catch (Exception ex) {
							ex.printStackTrace();
							if (page > 1) {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件加载完毕了哦！
										refreshview.loadmoreFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);
								listviewreply.setSelection(listviewreply.getBottom());
							} else {
								new Handler() {
									@Override
									public void handleMessage(Message msg) {
										// 千万别忘了告诉控件刷新完毕了哦！
										refreshview.refreshFinish(PullToRefreshLayout.FAIL);
									}
								}.sendEmptyMessageDelayed(0, 500);

							}
						}
					}
				}
			};
			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					Activity_ReplyPage.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ArticleID>%s</ArticleID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

			str = String.format(str, new Object[] { ID, 1 + "", size + "" });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_LIKEACTION, SOAP_LIKEMETHODNAME,
					SOAP_URL, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			// 返回键退回
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	protected void iniView() {
		// listview = (ListViewForScrollView)
		// findViewById(R.id.reply_page_list);
		txtHead = (TextView) findViewById(R.id.txtHead);
		replypagelinear = (LinearLayout) findViewById(R.id.reply_page_linear02);
		imgBack = (ImageView) findViewById(R.id.imv01_title_bar22);
		mTabViews = new TextView[2];
		mTabViews[0] = (TextView) findViewById(R.id.doc_pinlun);
		mTabViews[1] = (TextView) findViewById(R.id.doc_zan);
		zanlineaout = (LinearLayout) findViewById(R.id.reply_zan_page_linear);
		pinglunlayout = (LinearLayout) findViewById(R.id.reply_pinglun_page);
		myScrollView = (PullableScrollView) findViewById(R.id.sv);
		listviewreply = (ListViewForScrollView) findViewById(R.id.listviewcontent);
		zanlineaout.setOnClickListener(this);
		pinglunlayout.setOnClickListener(this);
		refreshview = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		ivhead = (ImageView) findViewById(R.id.ivhead);
		edit = (EditText) findViewById(R.id.et_sendmessage);
		tvname = (TextView) findViewById(R.id.tvname);
		tvcreate = (TextView) findViewById(R.id.tvcreatedate);
		tvhits = (TextView) findViewById(R.id.tvhits);
		loadmore = (RelativeLayout) findViewById(R.id.loadmore);
		loadstate_tv = (TextView) loadmore.findViewById(R.id.loadstate_tv);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reply_zan_page_linear:
			setText(1);
			break;
		case R.id.reply_pinglun_page:
			setText(0);
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		// 下拉刷新操作
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 千万别忘了告诉控件刷新完毕了哦！
				page = 1;
				if (hasInternetConnected()){
					if (postion == 0) {
					rePostList(arrayList2.size());
					} else if (postion == 1) {
						reZanList(arrayList2.size());
					}

				}else{
					Toast.makeText(Activity_ReplyPage.this, "请检查网络连接", 1)
						.show();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);

				}
//				if (hasInternetConnected()) {
//
//					pullToRefreshLayout
//							.refreshFinish(PullToRefreshLayout.SUCCEED);
//					if (postion == 0) {
//						rePostList(arrayList2.size());
//					} else if (postion == 1) {
//						reZanList(arrayList2.size());
//					}
//
//				} else {
//					Toast.makeText(Activity_ReplyPage.this, "请检查网络连接", 1)
//							.show();
//					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
//				}

			}
		}.sendEmptyMessageDelayed(0, 500);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		// 加载操作


				page++;

				if (postion == 0) {
					searchPostList();
				} else if (postion == 1) {
					searchZanList();
				}

	}

}

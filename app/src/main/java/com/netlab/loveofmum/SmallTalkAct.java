package com.netlab.loveofmum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.myadapter.DoctorAadpter;
import com.netlab.loveofmum.myadapter.FeedLastAdapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CountDownBar;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.MyScrollView;
import com.netlab.loveofmum.widget.MyScrollView.OnScrollListener;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SmallTalkAct extends BaseActivity implements OnScrollListener,
        OnRefreshListener, OnClickListener {
    private CountDownBar mRoundProgressBar;
    private ImageView ivpeople;
    private ListViewForScrollView llconsult;
    private LinearLayout search01, search02, search_edit, lltalk;
    private ImageView iv_bottom_line, bottom_line;
    private int first = 0;
    private int second = 0;
    private DoctorAadpter mAdapter;
    PullToRefreshLayout pullToRefreshLayout;
    private int currPosition = 0;
    private TextView[] mTabViews;
    private TextView txtHead;
    private ImageView imgBack;
    private MyScrollView sv;
    private int searchLayoutTop;
    LinearLayout llayout;
    private Intent mIntent;
    private Date nowdate, enddate;
    private String doctorid, ReplyUserCount;
    private final String SOAP_NAMESPACE = MMloveConstants.URL001;
    private final String SOAP_URL = MMloveConstants.URL002;
    String start, end, ID, DoctorName, StartTime, EndTime;
    private User user;
    AlertDialog dialog;
    private Boolean isSend = false;
    private int postion, page;
    private TextView tvname;
    private PercentRelativeLayout bottomchk;
    private ImageView ivhead;
    private AlertDialog.Builder builder;
    private RelativeLayout loadmore;
    private TextView loadstate_tv;
    String huifu;
    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
    // 得到系统时间
    private final String SOAP_SERVERTIMEMETHODNAME = MMloveConstants.GetServerTime;
    private final String SOAP_SERVERTIMEACTION = MMloveConstants.URL001
            + MMloveConstants.GetServerTime;

    private final String SOAP_PINGLUNCTION = MMloveConstants.URL001
            + MMloveConstants.ArticleTopicInsert;
    private final String SOAP_PINGLUNMETHODNAMEA = MMloveConstants.ArticleTopicInsert;

    private final String SOAP_ACTION = MMloveConstants.URL001
            + MMloveConstants.ArticleTopicInquiry;
    private final String SOAP_METHODNAME = MMloveConstants.ArticleTopicInquiry;
    private EditText sendmessage;
    // 查询文章详情
    private final String SOAP_ACTIONMICROTOPICDEITALINQUIRY = MMloveConstants.URL001
            + MMloveConstants.MicroTopicDeitalInquiry;
    private final String SOAP_METHODNAMEMICROTOPICDEITALINQUIRY = MMloveConstants.MicroTopicDeitalInquiry;
    private final String SOAP_EXPERTACTION = MMloveConstants.URL001
            + MMloveConstants.ExpertReplyInquiry;
    private final String SOAP_EXPERTMETHODNAMEA = MMloveConstants.ExpertReplyInquiry;
    private final String SOAP_ADDACTION = MMloveConstants.URL001
            + MMloveConstants.UserPointAdd;
    private final String SOAP_METHODNAMEADD = MMloveConstants.UserPointAdd;
    private TextView tvusrcount;
    private TextView tvtitle, tvnum;
    private ImageLoader mImageLoader;
    private ImageView ivisonline;
    private MyApplication myApp;
    private TextView tvok;
    private ImageView ivsend;
    private Boolean isFirst=false;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setListViewHeightBasedOnChildren(llconsult);
                    sv.smoothScrollTo(0, 0);

                    break;

                default:
                    break;
            }
        }

    };
//	private BroadcastReceiver mPushReceiver=new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if(intent.getAction().equals("action.push")){
////				myApp.killActivity(SmallTalkAct.this);
//				setText(0);
//			}
//
//		}
//	};

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            // start=mIntent.getStringExtra("StartTime");
            // end=mIntent.getStringExtra("EndTime");
            // doctorid=mIntent.getStringExtra("DoctorID");

            if (intent.getStringExtra("huifu") != null) {
                huifu = mIntent.getStringExtra("huifu");
                if (hasInternetConnected()) {
                    getArtcicledDes(ID);
                }
                setText(0);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_small_talk);
        iniView();
        mTabViews = new TextView[2];
        mTabViews[0] = (TextView) findViewById(R.id.doc_feed);
        mTabViews[1] = (TextView) findViewById(R.id.doc_comm);
        mTabViews[0].setOnClickListener(new MyClickListener(0));
        mTabViews[1].setOnClickListener(new MyClickListener(1));
        mIntent = this.getIntent();
        user = LocalAccessor.getInstance(this).getUser();
        mImageLoader = ImageLoader.getInstance();
        page = 1;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mAdapter = new DoctorAadpter(this);
        llconsult.setAdapter(mAdapter);
        txtHead.setText("微专题");
        if (mIntent != null) {
            // start=mIntent.getStringExtra("StartTime");
            // end=mIntent.getStringExtra("EndTime");
            // doctorid=mIntent.getStringExtra("DoctorID");
            ID = mIntent.getStringExtra("ID");
            if (mIntent.getStringExtra("huifu") != null) {
                huifu = mIntent.getStringExtra("huifu");
                setText(0);
            } else {
                setText(1);
            }
            if (hasInternetConnected()) {
                getArtcicledDes(ID);

            } else {
                Toast.makeText(SmallTalkAct.this, "请查看网络连接", 1).show();
            }
        }

        sv.setOnScrollListener(this);
        lltalk.setVisibility(View.GONE);
        setListner();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("action.push");
//		registerReceiver(mPushReceiver, intentFilter);
//		myApp=MyApplication.getInstance();
//		myApp.addActivity(this);
    }

    public void goSend(View v) {
        user = LocalAccessor.getInstance(SmallTalkAct.this).getUser();
        if (user.UserID != 0) {
            if (sendmessage.getText().toString().trim().length() > 200) {
                Toast.makeText(SmallTalkAct.this, "输入内容长度过长", 1).show();
                return;
            }

            if (!isSend) {
                getServerTime();
            }
        } else {
            startActivity(new Intent(SmallTalkAct.this, LoginActivity.class));
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
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

                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("ArticleTopicInquiry");
                            if (arrayList.size() > 0) {
                                arrayList.clear();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                map = new HashMap<String, Object>();
                                String time = array.getJSONObject(i)
                                        .get("CreatedDate").toString()
                                        .replace("/", "-");
                                time = IOUtils.stringToDate2(time,
                                        "yyyy-MM-dd HH:mm:ss");
                                String times = IOUtils.getMistimingTimes(time)
                                        .toString();
                                map.put("CreatedDate", times);
                                map.put("Type", 1 + "");

                                String str = array.getJSONObject(i).getString(
                                        "AuthorNickPic");
                                if ("".equals(str)) {
                                    map.put("Nick", "");
                                    map.put("PicUrl", "");
                                } else {
                                    String[] strs = str.split("\\,");
                                    map.put("Nick", strs[0]);
                                    map.put("PicUrl", strs[1]);
                                }
                                map.put("Content", array.getJSONObject(i)
                                        .getString("Content"));
                                arrayList.add(map);
                            }
                            mAdapter.setListdat(arrayList);
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(SmallTalkAct.this,
                    true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><ArticleID>%s</ArticleID><ParentID>%s</ParentID><PageIndex>%s</PageIndex><PageSize>%s</PageSize><Flag>%s</Flag></Request>";

            str = String.format(str, new Object[]{ID, "0", 1 + "", size + "",
                    "0"});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
                    SOAP_URL, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void searchPostList() {
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    String returnvalue001 = result.toString();
                    Toast.makeText(SmallTalkAct.this, returnvalue001, 1).toString();
                    if (returnvalue001 == null) {

                    } else {
                        Map<String, Object> map;
                        // 下边是具体的处理
                        try {

                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("ArticleTopicInquiry");
                            if (page == 1 && arrayList.size() > 0) {
                                arrayList.clear();

                            }
                            if (array.getJSONObject(0).has("MessageCode")) {
                                if (page == 1) {
                                    loadmore.setVisibility(View.GONE);
                                } else {
                                    if(isFirst) {
                                        loadmore.setVisibility(View.VISIBLE);
                                        loadstate_tv.setText("没有更多数据");
                                        new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                // 千万别忘了告诉控件加载完毕了哦！
                                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                            }
                                        }.sendEmptyMessageDelayed(0, 500);
                                        llconsult.setSelection(llconsult.getBottom());
                                    }

                                }
                                isFirst=true;

                            } else {


                                for (int i = 0; i < array.length(); i++) {
                                    map = new HashMap<String, Object>();
                                    String time = array.getJSONObject(i)
                                            .get("CreatedDate").toString()
                                            .replace("/", "-");
                                    time = IOUtils.stringToDate2(time,
                                            "yyyy-MM-dd HH:mm:ss");
                                    String times = IOUtils.getMistimingTimes(time)
                                            .toString();
                                    map.put("CreatedDate", times);
                                    map.put("Type", 1 + "");

                                    String str = array.getJSONObject(i).getString(
                                            "AuthorNickPic");

                                    if ("".equals(str)) {
                                        map.put("Nick", "");
                                        map.put("PicUrl", "");
                                    } else {
                                        String[] strs = str.split("\\,");
                                        map.put("Nick", strs[0]);
                                        if (strs.length > 1) {
                                            map.put("PicUrl", strs[1]);
                                        } else {
                                            map.put("PicUrl", "");
                                        }
                                    }

                                    map.put("Content", array.getJSONObject(i)
                                            .getString("Content"));
                                    arrayList.add(map);
                                }
                                mAdapter.setListdat(arrayList);
                                mAdapter.notifyDataSetChanged();
                                if (page > 1) {
                                    new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            // 千万别忘了告诉控件加载完毕了哦！
                                            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                        }
                                    }.sendEmptyMessageDelayed(0, 500);
                                    llconsult.setSelection(llconsult.getBottom());
                                } else {
                                    if(isFirst) {
                                        new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                // 千万别忘了告诉控件刷新完毕了哦！
                                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                            }
                                        }.sendEmptyMessageDelayed(0, 500);
                                        Message message = Message.obtain();
                                        message.what = 0;
                                        mHandler.sendMessage(message);
                                    }

                                }
                                isFirst=true;

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (page > 1) {
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        // 千万别忘了告诉控件加载完毕了哦！
                                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                                    }
                                }.sendEmptyMessageDelayed(0, 500);
                                llconsult.setSelection(llconsult.getBottom());
                            } else {
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        // 千万别忘了告诉控件刷新完毕了哦！
                                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                    }
                                }.sendEmptyMessageDelayed(0, 500);


                            }
                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(SmallTalkAct.this,
                    true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><ArticleID>%s</ArticleID><ParentID>%s</ParentID><PageIndex>%s</PageIndex><PageSize>%s</PageSize><Flag>%s</Flag></Request>";

            str = String.format(str, new Object[]{ID, "0", page + "",
                    10 + "", "0"});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
                    SOAP_URL, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void supportPostponeEnterTransition() {
        super.supportPostponeEnterTransition();
    }

    private void reExpertReplyInquiryList(int size) {
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    String returnvalue001 = result.toString();

                    if (returnvalue001 == null) {

                    } else {
                        Map<String, Object> map;
                        // 下边是具体的处理
                        try {
                            if (arrayList.size() > 0) {
                                arrayList.clear();
                            }

                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("ExpertReplyInquiry");
                            if (array.getJSONObject(0).has("MessageCode")) {

                                Toast.makeText(SmallTalkAct.this, "暂时没有回复信息", 1)
                                        .show();
                            } else {
                                for (int i = 0; i < array.length(); i++) {
                                    map = new HashMap<String, Object>();
                                    String time = array.getJSONObject(i)
                                            .get("CreatedDate").toString()
                                            .replace("/", "-");

                                    time = IOUtils.stringToDate2(time,
                                            "yyyy-MM-dd HH:mm:ss");
                                    String times = IOUtils.getMistimingTimes(
                                            time).toString();
                                    map.put("CreatedDate", times);
                                    map.put("Type", 0 + "");
                                    String replytime = array.getJSONObject(i)
                                            .get("ReplyCreatedDate").toString()
                                            .replace("/", "-");

                                    replytime = IOUtils.stringToDate2(
                                            replytime, "yyyy-MM-dd HH:mm:ss");
                                    String replytimes = IOUtils
                                            .getMistimingTimes(replytime)
                                            .toString();
                                    map.put("ReplyCreatedDate", replytimes);
                                    String str = array.getJSONObject(i)
                                            .getString("AuthorNickPic");
                                    if ("".equals(str)) {
                                        map.put("Nick", "");
                                        map.put("PicUrl", "");
                                    } else {
                                        String[] strs = str.split("\\,");
                                        map.put("Nick", strs[0]);
                                        map.put("PicUrl", strs[1]);
                                    }
                                    String strDoctor = array.getJSONObject(i)
                                            .getString("ReplyNickPic");

                                    if (strDoctor.indexOf(",") >= 0) {
                                        String[] strs2 = strDoctor.split("\\,");
                                        map.put("Doctor", strs2[0]);
                                        map.put("DoctorPic", strs2[1]);
                                    } else {
                                        map.put("Doctor", "");
                                        map.put("DoctorPic", "");
                                    }
                                    map.put("ReplyContent",
                                            array.getJSONObject(i).getString(
                                                    "ReplyContent"));
                                    map.put("Content", array.getJSONObject(i)
                                            .getString("Content"));
                                    arrayList.add(map);
                                }
                                mAdapter.setListdat(arrayList);
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(SmallTalkAct.this,
                    true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><ArticleID>%s</ArticleID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

            str = String.format(str, new Object[]{ID, 1 + "", size + ""});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_EXPERTACTION,
                    SOAP_EXPERTMETHODNAMEA, SOAP_URL, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void searchExpertReplyInquiryList() {
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    String returnvalue001 = result.toString();
                    // {"ExpertReplyInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}
                    if (returnvalue001 == null) {

                    } else {
                        Map<String, Object> map;
                        // 下边是具体的处理
                        try {

                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("ExpertReplyInquiry");
                            if (page == 1 && arrayList.size() > 0) {
                                arrayList.clear();

                            }
                            if (array.getJSONObject(0).has("MessageCode")) {

                                Toast.makeText(SmallTalkAct.this, "暂时没有回复信息", 1)
                                        .show();


                                if (page == 1) {
                                    loadmore.setVisibility(View.GONE);
                                } else {
                                    loadmore.setVisibility(View.VISIBLE);
                                    loadstate_tv.setText("没有更多数据");

                                    new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            // 千万别忘了告诉控件加载完毕了哦！
                                            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                        }
                                    }.sendEmptyMessageDelayed(0, 500);
                                    llconsult.setSelection(llconsult.getBottom());

                                }


                            } else {
                                for (int i = 0; i < array.length(); i++) {
                                    map = new HashMap<String, Object>();
                                    String time = array.getJSONObject(i)
                                            .get("CreatedDate").toString()
                                            .replace("/", "-");

                                    time = IOUtils.stringToDate2(time,
                                            "yyyy-MM-dd HH:mm:ss");
                                    String times = IOUtils.getMistimingTimes(
                                            time).toString();
                                    map.put("CreatedDate", times);
                                    map.put("Type", 0 + "");
                                    String replytime = array.getJSONObject(i)
                                            .get("ReplyCreatedDate").toString()
                                            .replace("/", "-");

                                    replytime = IOUtils.stringToDate2(
                                            replytime, "yyyy-MM-dd HH:mm:ss");
                                    String replytimes = IOUtils
                                            .getMistimingTimes(replytime)
                                            .toString();
                                    map.put("ReplyCreatedDate", replytimes);
                                    String str = array.getJSONObject(i)
                                            .getString("AuthorNickPic");
                                    if ("".equals(str)) {
                                        map.put("Nick", "");
                                        map.put("PicUrl", "");
                                    } else {
                                        String[] strs = str.split("\\,");
                                        map.put("Nick", strs[0]);
                                        if (strs.length > 1) {
                                            map.put("PicUrl", strs[1]);
                                        } else {
                                            map.put("PicUrl", "");
                                        }
                                    }
                                    String strDoctor = array.getJSONObject(i)
                                            .getString("ReplyNickPic");

                                    if (strDoctor.indexOf(",") >= 0) {
                                        String[] strs2 = strDoctor.split("\\,");
                                        map.put("Doctor", strs2[0]);
                                        map.put("DoctorPic", strs2[1]);
                                    } else {
                                        map.put("Doctor", "");
                                        map.put("DoctorPic", "");
                                    }
                                    map.put("ReplyContent",
                                            array.getJSONObject(i).getString(
                                                    "ReplyContent"));
                                    map.put("Content", array.getJSONObject(i)
                                            .getString("Content"));
                                    arrayList.add(map);
                                }
                                mAdapter.setListdat(arrayList);
                                mAdapter.notifyDataSetChanged();
                                if (page > 1) {
                                    new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            // 千万别忘了告诉控件加载完毕了哦！
                                            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                        }
                                    }.sendEmptyMessageDelayed(0, 500);
                                    llconsult.setSelection(llconsult.getBottom());
                                } else {
                                    if(isFirst) {
                                        new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                // 千万别忘了告诉控件刷新完毕了哦！
                                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                            }
                                        }.sendEmptyMessageDelayed(0, 500);
                                        Message message = Message.obtain();
                                        message.what = 0;
                                        mHandler.sendMessage(message);
                                    }

                                }
                                isFirst=true;

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (page > 1) {
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        // 千万别忘了告诉控件加载完毕了哦！
                                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                                    }
                                }.sendEmptyMessageDelayed(0, 500);
                                llconsult.setSelection(llconsult.getBottom());
                            } else {
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        // 千万别忘了告诉控件刷新完毕了哦！
                                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                    }
                                }.sendEmptyMessageDelayed(0, 500);


                            }
                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(SmallTalkAct.this,
                    true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><ArticleID>%s</ArticleID><PageIndex>%s</PageIndex><PageSize>%s</PageSize></Request>";

            str = String.format(str, new Object[]{ID, page + "", 5 + ""});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_EXPERTACTION,
                    SOAP_EXPERTMETHODNAMEA, SOAP_URL, paramMap);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getServerTime() {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
                // TODO Auto-generated method stub

                String value = result.toString();
                // {"UserInquiry":[{"UserSex":"01","UserAge":"33","PictureURL":"~\/UploadFile\/image\/2016030109454450694.jpg","NickName":"宝爸20153028230623hhhh","UserNO":"410102198201251510","UserID":"50694","YuBirthTime":"2016\/10\/25
                // 0:00:00","RealName":"郭芮","Point":"60","ClientID":"0c1726ae905edaa84b2b26e794910ab1","City":"郑州"}]}
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("GetServerTime");
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    nowdate = IOUtils.string2Date(array.getJSONObject(0)
                            .getString("ServerTime"), "yyyy-MM-dd HH:mm:ss");

                    if (nowdate.getTime() >= enddate.getTime()) {
                        isSend = false;
                        bottomchk.setVisibility(View.GONE);
                        Toast.makeText(SmallTalkAct.this, "活动已经结束", 1).show();
                    } else {
                        isSend = false;
                        String inputName = Html.toHtml(sendmessage.getText());
                        if (inputName.equals("")
                                || sendmessage.getText().toString().trim()
                                .length() == 0) {
                            Toast.makeText(SmallTalkAct.this,
                                    R.string.topic_msg, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            sendmessage.setText("");
                            if (inputName.contains("\"ltr\"")) {

                            } else {
                                inputName = inputName.replace("ltr", "\"ltr\"");
                            }

                            sendmessage.clearFocus();
//							startActivity(new Intent(SmallTalkAct.this,
//									SignBase.class));
                            // DialogAddEnsure dialogEnsureView = new
                            // DialogAddEnsure(
                            // SmallTalkAct.this).setDialogMsg("",
                            // "问题已提交，等待医生回复", "关闭")
                            // .setOnClickListenerEnsure(new OnClickListener() {
                            //
                            // @Override
                            // public void onClick(View v) {
                            // finish();
                            // }
                            // });
                            //
                            // DialogUtils.showSelfDialog(SmallTalkAct.this,
                            // dialogEnsureView);

//							setUserPoint(1);
                            postPinglun(inputName, sendmessage);

                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    isSend = false;
                }

            }
        };
        isSend = true;
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]{"",});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_SERVERTIMEACTION,
                SOAP_SERVERTIMEMETHODNAME, SOAP_URL, paramMap);
    }

    public void AddDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_add_jifen, null);

        builder = new AlertDialog.Builder(SmallTalkAct.this);
        builder.setView(layout);
        builder.setCancelable(false);
        dialog = builder.show();
        tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);

        tvok.setOnClickListener(this);
    }

    private void setUserPoint(final int point) {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
                // TODO Auto-generated method stub
                String value = result.toString();
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("UserPointAdd");
                    if ("0".equals(array.getJSONObject(0).getString(
                            "MessageCode"))) {
                        // Toast.makeText(
                        // SignAct.this,
                        // array.getJSONObject(0).getString(
                        // "MessageContent"), 1).show();

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(SmallTalkAct.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><PointValue>%s</PointValue><Remark>%s</Remark></Request>";
        str = String.format(str, new Object[]{user.UserID + "", point + "",
                "用户提问送积分"});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_ADDACTION, SOAP_METHODNAMEADD,
                SOAP_URL, paramMap);
    }

    private void setListner() {
        // TODO Auto-generated method stub
        pullToRefreshLayout.setOnRefreshListener(this);
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        sendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sendmessage.getText().toString().length() > 0) {
                    ivsend.setBackgroundResource(R.drawable.send_post);

                } else {
                    ivsend.setBackgroundResource(R.drawable.send_post_un);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void initWidth() {
        int lineWidth = iv_bottom_line.getLayoutParams().width;
        Log.d("lineWidth ", lineWidth + "");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        first = width / 2;
        second = first * 2;
    }

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

                    doctorid = array.getJSONObject(0).getString("DoctorID")
                            .toString();
                    DoctorName = array.getJSONObject(0).getString("DoctorName")
                            .toString();
                    StartTime = array.getJSONObject(0).getString("StartTime")
                            .toString().replace("/", "-");
                    EndTime = array.getJSONObject(0).getString("EndTime")
                            .toString().replace("/", "-");
                    ReplyUserCount = array.getJSONObject(0)
                            .getString("ReplyUserCount").toString();
                    String urlimg = array.getJSONObject(0)
                            .getString("ImageURL1").toString();

                    if (urlimg.contains("vine.gif")) {
                        ivhead.setImageResource(R.drawable.icon_user_normal);
                    } else {
                        mImageLoader.displayImage(MMloveConstants.JE_BASE_URL2
                                        + urlimg.replace("~", "").replace("\\", "/"),
                                ivhead, ImageOptions.getTalkOptions(120));
                    }
                    if (Integer.valueOf(array.getJSONObject(0)
                            .getString("IsLine").toString()) != 1) {
                        ivisonline.setImageResource(R.drawable.icon_online);
                    } else {
                        ivisonline.setImageResource(R.drawable.icon_offline);
                    }
                    tvname.setText(DoctorName);
                    String title = array.getJSONObject(0).getString("Title")
                            .toString();
                    tvtitle.setText(title);
                    tvusrcount.setText(ReplyUserCount);
                    getTime();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><ID>%s</ID></Request>";
        str = String.format(str, new Object[]{ID,});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_ACTIONMICROTOPICDEITALINQUIRY,
                SOAP_METHODNAMEMICROTOPICDEITALINQUIRY, SOAP_URL, paramMap);
    }

    private void getTime() {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
                // TODO Auto-generated method stub

                String value = result.toString();
                // {"UserInquiry":[{"UserSex":"01","UserAge":"33","PictureURL":"~\/UploadFile\/image\/2016030109454450694.jpg","NickName":"宝爸20153028230623hhhh","UserNO":"410102198201251510","UserID":"50694","YuBirthTime":"2016\/10\/25
                // 0:00:00","RealName":"郭芮","Point":"60","ClientID":"0c1726ae905edaa84b2b26e794910ab1","City":"郑州"}]}
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("GetServerTime");
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    nowdate = IOUtils.string2Date(array.getJSONObject(0)
                            .getString("ServerTime"), "yyyy-MM-dd HH:mm:ss");

                    Date curDate = new Date();
                    enddate = IOUtils.string2Date(EndTime,
                            "yyyy-MM-dd HH:mm:ss");
                    long time = (curDate.getTime() - nowdate.getTime()) / 1000;
                    long minutes = time / 60;
                    int interval = (int) Math.abs(minutes);
                    if (interval >= 5) {
                        Toast.makeText(SmallTalkAct.this, "请调整成标准时间", 1).show();
                    } else {
                        countDown(StartTime, EndTime);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request></Request>";
        str = String.format(str, new Object[]{"",});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_SERVERTIMEACTION,
                SOAP_SERVERTIMEMETHODNAME, SOAP_URL, paramMap);
    }

    public void countDown(String start, String end) {
        try {

            Date dstart = IOUtils.string2Date(start, "yyyy-MM-dd HH:mm:ss");
            Date dend = IOUtils.string2Date(end, "yyyy-MM-dd HH:mm:ss");
            if (nowdate.getTime() < dstart.getTime()) {
                bottomchk.setVisibility(View.VISIBLE);
                mRoundProgressBar.setEndTime(dend.getTime(), dend.getTime()
                                - dstart.getTime(), ivpeople, nowdate.getTime(),
                        dstart.getTime(), tvnum);
            } else if (nowdate.getTime() > dstart.getTime()
                    && nowdate.getTime() < dend.getTime()) {
                bottomchk.setVisibility(View.VISIBLE);
                mRoundProgressBar.setEndTime(dend.getTime(), dend.getTime()
                                - dstart.getTime(), ivpeople, nowdate.getTime(),
                        dstart.getTime(), tvnum);
            } else {
                bottomchk.setVisibility(View.GONE);
                setText(0);
                mRoundProgressBar.setEndTime(dend.getTime(), dend.getTime()
                                - dstart.getTime(), ivpeople, nowdate.getTime(),
                        dstart.getTime(), tvnum);
            }
        } catch (ParseException e) {
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

    protected void postPinglun(String input, final EditText edit) {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub
                String value = result.toString();
                // {"ArticleLikesInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
                JSONObject mySO = (JSONObject) result;
//				Toast.makeText(SmallTalkAct.this,value,1).show();
                JSONArray array;
                try {
                    array = mySO.getJSONArray("ArticleTopicInsert");
                    if ("0".equals(array.getJSONObject(0).getString(
                            "MessageCode"))) {

//						search01.removeView(search_edit);
//						search02.addView(search_edit);
//						lltalk.setVisibility(View.GONE);
//
                        sv.smoothScrollTo(0, 0);
                        if (search_edit.getParent() != search02) {

                            search01.removeView(search_edit);
                            search02.addView(search_edit);
                            lltalk.setVisibility(View.GONE);
                        }
                        if (postion == 0) {
                            setText(0);
                            getArtcicledDes(ID);
                        }
                        if (postion == 1) {
                            setText(1);
                            getArtcicledDes(ID);
                        }
                    } else {

                        Toast.makeText(
                                SmallTalkAct.this,
                                array.getJSONObject(0).getString(
                                        "MessageContent"), 1).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(SmallTalkAct.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 0 用户 1医生
        String str = "<Request><ArticleID>%s</ArticleID><ParentID>%s</ParentID><AuthorID>%s</AuthorID><AuthorType>%s</AuthorType><ReplyID>%s</ReplyID><ReplyType>%s</ReplyType>"
                + "<Content>%s</Content></Request>";
        str = String.format(
                str,
                new Object[]{
                        ID,
                        "0",
                        user.UserID + "",
                        "0",
                        doctorid,
                        "1",
                        input.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll(
                                "<[^>]*>", "\n\t")});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_PINGLUNCTION,
                SOAP_PINGLUNMETHODNAMEA, SOAP_URL, paramMap);
    }

    protected void iniView() {
        // TODO Auto-generated method stub
        txtHead = (TextView) findViewById(R.id.txtHead);
        imgBack = (ImageView) findViewById(R.id.img_left);
        mRoundProgressBar = (CountDownBar) findViewById(R.id.pbcountDownBar);
        ivpeople = (ImageView) findViewById(R.id.iv_people);
        llconsult = (ListViewForScrollView) findViewById(R.id.llconsult);
        iv_bottom_line = (ImageView) findViewById(R.id.iv_bottom_line);
        llayout = (LinearLayout) findViewById(R.id.llayout);
        sv = (MyScrollView) findViewById(R.id.small_sv);
        search02 = (LinearLayout) findViewById(R.id.lltabs);
        search_edit = (LinearLayout) findViewById(R.id.ll_tbs);
        search01 = (LinearLayout) findViewById(R.id.search01);
        lltalk = (LinearLayout) findViewById(R.id.lltalk);
        sendmessage = (EditText) findViewById(R.id.et_sendmessage);
        tvusrcount = (TextView) findViewById(R.id.tvusrcount);
        tvname = (TextView) findViewById(R.id.tvname);
        tvtitle = (TextView) findViewById(R.id.tvmocro_titlex);
        bottomchk = (PercentRelativeLayout) findViewById(R.id.bottom_chk);
        ivhead = (ImageView) findViewById(R.id.iv_head);
        ivisonline = (ImageView) findViewById(R.id.iv_isonline);
        loadmore = (RelativeLayout) findViewById(R.id.loadmore);
        loadstate_tv = (TextView) loadmore.findViewById(R.id.loadstate_tv);
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        tvnum = (TextView) findViewById(R.id.tvnum);
        ivsend = (ImageView) findViewById(R.id.iv_send);
    }

    private void initWidth2() {
        int lineWidth = bottom_line.getLayoutParams().width;
        Log.d("lineWidth ", lineWidth + "");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        first = width / 2;
        second = first * 2;

    }

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

        if (postion == 0) {
            page = 1;
            arrayList.clear();
            mAdapter.notifyDataSetChanged();
            searchExpertReplyInquiryList();

        } else if (postion == 1) {
            arrayList.clear();
            mAdapter.notifyDataSetChanged();
            page = 1;

            searchPostList();
        }
        setListViewHeightBasedOnChildren(llconsult);
        sv.smoothScrollTo(0, 0);
        // showView(postion);

    }


    class MyClickListener implements OnClickListener {

        private int index = 0;

        public MyClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // mViewPager.setCurrentItem(index);

            TranslateAnimation ta = null;
            switch (index) {
                case 0:

                    // if (currPosition == 1) {
                    // ta = new TranslateAnimation(first, 0, 0, 0);
                    //
                    // currPosition=index;
                    // }
                    //
                    setText(0);

                    break;

                case 1:

                    // if (currPosition == 0) {
                    // ta = new TranslateAnimation(0, first, 0, 0);
                    // // selectpage(index, ta);
                    // // select2page(index, ta);
                    // currPosition=index;
                    // }

                    setText(1);

                    break;

            }

        }

        private void selectpage(int arg0, TranslateAnimation ta) {
            currPosition = arg0;
            ta.setDuration(300);
            ta.setFillAfter(true);
            iv_bottom_line.startAnimation(ta);
        }

        private void select2page(int arg0, TranslateAnimation ta) {
            currPosition = arg0;
            ta.setDuration(300);
            ta.setFillAfter(true);
            bottom_line.startAnimation(ta);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            searchLayoutTop = llayout.getBottom();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

//		myApp.killActivity(SmallTalkAct.this);try {
//			unregisterReceiver(mPushReceiver);
//
//			mPushReceiver = null;
//		} catch (Exception e) {
//		}
    }

    @Override
    public void onScroll(int scrollY) {
        // TODO Auto-generated method stub
        if (scrollY >= searchLayoutTop) {
            if (search_edit.getParent() != search01) {
                search02.removeView(search_edit);
                search01.addView(search_edit);
                lltalk.setVisibility(View.VISIBLE);
            }
        } else {
            if (search_edit.getParent() != search02) {

                search01.removeView(search_edit);
                search02.addView(search_edit);
                lltalk.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        // TODO Auto-generated method stub
        // 下拉刷新操作

        // 千万别忘了告诉控件刷新完毕了哦！
        page = 1;
        if (postion == 0) {
            searchExpertReplyInquiryList();
        } else if (postion == 1) {

            searchPostList();
        }

    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        // TODO Auto-generated method stub
        // 加载操作

        // 千万别忘了告诉控件加载完毕了哦！


        page++;
        if (postion == 0) {
            searchExpertReplyInquiryList();
        } else if (postion == 1) {

            searchPostList();
        }


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.dialog_default_click_ensure:
                if (dialog != null) {
                    dialog.dismiss();
                }

                break;
        }
    }


}
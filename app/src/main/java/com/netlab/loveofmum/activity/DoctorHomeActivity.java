package com.netlab.loveofmum.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.Activity_ReplyPage;
import com.netlab.loveofmum.CHK_DoctorList;
import com.netlab.loveofmum.CHK_TimeSelect;
import com.netlab.loveofmum.MainTabActivity;
import com.netlab.loveofmum.MyCHK_Timeline;
import com.netlab.loveofmum.News_Detail;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.ShopWebView;
import com.netlab.loveofmum.SmallTalkAct;
import com.netlab.loveofmum.StaticWebView;
import com.netlab.loveofmum.TopicAllAct;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.Image;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.TalkAbount;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.timepicker.Util;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.netlab.loveofmum.widget.ImageCycleView;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.MyScrollView;
import com.netlab.loveofmum.widget.ImageCycleView.ImageCycleViewListener;
import com.netlab.loveofmum.widget.MyScrollView.OnScrollListener;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout;
import com.netlab.loveofmum.widget.XScrollView.PullToRefreshLayout.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;


public class DoctorHomeActivity extends BaseActivity implements OnScrollListener,
        OnRefreshListener {
    private ImageView  imgBack, imgback1;
    private ImageView head;
    private final String mPageName = "AnalyticsDoctorHome";
    private ImageLoader mImageLoader;
    private int searchLayoutTop;
    private TextView txtMore, txtName, txtFans, tvpingtai, tvhead1, img_rv1;
    RelativeLayout rlayout, rl_zixun;
    private RelativeLayout loadmore;
    private LinearLayout  search01, llhome, llone, lltwo, llmore;
    private MyScrollView myScrollView;
    private ListViewForScrollView mListView;
    private TalkAbount mAdapter;
    private User user;
    private Intent mIntnet;
    String DoctorID, HospitalID, AskCount, UserCount, AskByOne, PicUrl,
            DoctorName, HosName, ID,DepartNO,DoctorNO,DoctorTitle,ImageUrl;
    private ImageCycleView docorView;
    private Boolean isFav = false;
    private int isLine = 2;
    ArrayList<Map<String, Object>> mObject = new ArrayList<Map<String, Object>>();
    PullToRefreshLayout pullToRefreshLayout;
    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayDocList = new ArrayList<Map<String, Object>>();
    ArrayList<String> mImageUrl = new ArrayList<String>();
    private final String SOAP_ACTION2 = MMloveConstants.URL001
            + MMloveConstants.FavorDoctorInsert;
    private final String SOAP_METHODNAME2 = MMloveConstants.FavorDoctorInsert;
    private TextView tvhead;
    public static final int FRIST_GET_DATE = 111;
    public static final int LOADMORE_GET_DATE = 113;
    public static final int BROADCAST = 114;

    private TextView loadstate_tv;
    // 定义Web Service相关的字符串
    private final String SOAP_NAMESPACE = MMloveConstants.URL001;
    private final String SOAP_ACTION = MMloveConstants.URL001
            + MMloveConstants.ArticleInquiry;
    private final String SOAP_METHODNAMEA = MMloveConstants.ArticleInquiry;

    // 请求广告接口
    private final String SOAP_ACTIONADs = MMloveConstants.URL001
            + MMloveConstants.Ads;
    private final String SOAP_METHODNAMEADs = MMloveConstants.Ads;
    // 请求医生详情
    private final String SOAP_DESACTION = MMloveConstants.URL001
            + MMloveConstants.DoctorInquiry;
    private final String SOAP_DESMETHODNAME = MMloveConstants.DoctorInquiry;

    private final String SOAP_SUBJECTACTION = MMloveConstants.URL001
            + MMloveConstants.MicroTopicInquiryIndex;
    private final String SOAP_SUBJECTMETHODNAME = MMloveConstants.MicroTopicInquiryIndex;


    private final String SOAP_ACTION3 = MMloveConstants.URL001
            + MMloveConstants.FavorDoctorDelete;
    private final String SOAP_METHODNAME3 = MMloveConstants.FavorDoctorDelete;

    private final String SOAP_MICROACTION = MMloveConstants.URL001
            + MMloveConstants.MicroTopicInquiry;
    private final String SOAP_MICROMETHODNAMEA = MMloveConstants.MicroTopicInquiry;
    private final String SOAP_URL = MMloveConstants.URL002;
    private int page = 1;
    Yuchan yu;
    int scrollX=0;
    int scrollY=0;
    // 咨询订单编号
    private String ID1, ID2;
    private String price;

    // 得到系统时间
    private final String SOAP_SERVERTIMEMETHODNAME = MMloveConstants.GetServerTime;
    private final String SOAP_SERVERTIMEACTION = MMloveConstants.URL001
            + MMloveConstants.GetServerTime;
    private TextView microTopic, specialone, specialtwo;
    String starttime, endtime;
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("action.doctor")) {
                geneItems(BROADCAST);
            }
        }

    };

    private TextView imgfous;
    private Boolean isFisrt=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_doctor_home);
        iniView();
        loadmore.setVisibility(View.INVISIBLE);
        user = LocalAccessor.getInstance(DoctorHomeActivity.this).getUser();
        mImageLoader = ImageLoader.getInstance();
        mIntnet = this.getIntent();
        if (mIntnet != null) {
            DoctorID = mIntnet.getStringExtra("DoctorID");
        }
        mAdapter = new TalkAbount(DoctorHomeActivity.this, DoctorID);
        mListView.setAdapter(mAdapter);
        myScrollView.setOnScrollListener(this);
        search01.setVisibility(View.GONE);
        setListener();
        rl_zixun.setVisibility(View.GONE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.doctor");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        if (hasInternetConnected()) {
            searchDocDes();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            searchLayoutTop = rlayout.getBottom();// 获取searchLayout的顶部位置
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        yu = new Yuchan();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = fmt.parse(user.YuBirthDate);
            yu = IOUtils.WeekInfo(date);

        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        searchAds(MyApplication.getInstance().nowtime, "01");
        myScrollView.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                myScrollView.scrollTo(scrollX, scrollY);
            }
        });
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    public void goChat(View v) {
        if (!hasInternetConnected()) {
            Toast.makeText(DoctorHomeActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
            return;
        }
        user=LocalAccessor.getInstance(DoctorHomeActivity.this).getUser();

        if (user.UserID != 0) {
//			AskOrderInquiry();
            if (isLine == 0) {
                Intent i = new Intent(DoctorHomeActivity.this,
                        DoctorDetailsActivity.class);
                i.putExtra("DoctorID", DoctorID);
                i.putExtra("HospitalID", HospitalID);
                i.putExtra("AskByOne", AskByOne);
                i.putExtra("UserCount", UserCount);
                i.putExtra("AskCount", AskCount);
                startActivity(i);
            } else {
                Toast.makeText(DoctorHomeActivity.this, "该医生不在线，不能进行在线咨询", 1).show();
            }

        } else {
            startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
        }

    }
    public void goYuyue(View v) {
        if (!hasInternetConnected()) {
            Toast.makeText(DoctorHomeActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
            return;
        }
        user=LocalAccessor.getInstance(DoctorHomeActivity.this).getUser();

        if (user.UserID != 0) {
//			AskOrderInquiry();
            if (isLine == 0) {
                Intent i = new Intent(DoctorHomeActivity.this,
                        CHK_TimeSelect.class);
                i.putExtra("DoctorID", DoctorID);
                i.putExtra("HospitalID", HospitalID);
                i.putExtra("DoctorNO", DoctorNO);
                i.putExtra("DoctorTitle", DoctorTitle);
                i.putExtra("DoctorName", DoctorName);
                i.putExtra("HospitalName", HosName);
                i.putExtra("DepartNO", DepartNO);
                i.putExtra("ImageURL",ImageUrl);
                i.putExtra("Price",price);
                i.putExtra("yuyueDoc","yuyue");
                i.putExtra("DoctorTitle",DoctorTitle);
                startActivity(i);
            } else {
                Toast.makeText(DoctorHomeActivity.this, "该医生不在线，不能进行在线咨询", 1).show();
            }

        } else {
            startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
        }

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
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

    @Override
    protected void iniView() {
        // globleLayout=(RelativeLayout) findViewById(R.id.globleLayout);
        rlayout = (RelativeLayout) findViewById(R.id.lrtop);
        search01 = (LinearLayout) findViewById(R.id.search01);
        txtMore = (TextView) findViewById(R.id.hometv);

        tvhead = (TextView) findViewById(R.id.tvhead);
        imgBack = (ImageView) findViewById(R.id.imgback);
        myScrollView = (MyScrollView) findViewById(R.id.sv);
        head = (ImageView) findViewById(R.id.iv_head);
        rl_zixun = (RelativeLayout) findViewById(R.id.bottom_gochat);
        mListView = (ListViewForScrollView) findViewById(R.id.listview);
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        txtName = (TextView) findViewById(R.id.nameone);
        txtFans = (TextView) findViewById(R.id.fan_quantity);
        tvpingtai = (TextView) findViewById(R.id.tvpingtai);
        microTopic = (TextView) findViewById(R.id.micro_thematic_content);
        specialone = (TextView) findViewById(R.id.special_period_one);
        specialtwo = (TextView) findViewById(R.id.special_period_two);
        llhome = (LinearLayout) findViewById(R.id.llhome);
        llmore = (LinearLayout) findViewById(R.id.llmore);
        llone = (LinearLayout) findViewById(R.id.llone);
        lltwo = (LinearLayout) findViewById(R.id.lltwo);
        loadmore = (RelativeLayout) findViewById(R.id.loadmore);
        loadstate_tv = (TextView) loadmore.findViewById(R.id.loadstate_tv);
        docorView = (ImageCycleView) findViewById(R.id.doctor_view);
        imgfous = (TextView) findViewById(R.id.img_rv);
        imgback1 = (ImageView) findViewById(R.id.imgback1);
        tvhead1 = (TextView) findViewById(R.id.tvhead1);
        img_rv1 = (TextView) findViewById(R.id.img_rv1);
    }

    private void searchAds(String date, String showposition) {
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                String value = result.toString();

                if (value != null) {
                    JSONObject mySO = (JSONObject) result;
                    Map<String, Object> map;
                    JSONArray array;
                    try {
                        array = mySO.getJSONArray("ADInquiry");
                        if (array.getJSONObject(0).has("MessageCode")) {

                            docorView.setVisibility(View.GONE);
                        } else {
                            if (mImageUrl.size() > 0) {
                                mImageUrl.clear();
                            }
                            for (int i = 0; i < array.length(); i++) {

                                map = new HashMap<String, Object>();
                                map.put("ID",
                                        array.getJSONObject(i).getString(
                                                "LinkID"));
                                map.put("ImageUr",
                                        (MMloveConstants.JE_BASE_URL + array
                                                .getJSONObject(i).getString(
                                                        "ImageUrl")).toString()
                                                .replace("~", "")
                                                .replace("\\", "/"));
                                map.put("Position", array.getJSONObject(i)
                                        .getString("Position"));
                                mImageUrl
                                        .add((MMloveConstants.JE_BASE_URL + array
                                                .getJSONObject(i).getString(
                                                        "ImageUrl")).toString()
                                                .replace("~", "")
                                                .replace("\\", "/"));
                                mObject.add(map);

                            }
                            showADs();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                // P0005 小贴士
                // P0006 热点
                // P0007 个人中心
                // P0008 妈妈足迹
                // P0009 微专题
                // P0010 医生首页
                // P0011 医生文章
                // P0012 孕健康
                // P0013 话题
                // P0014 商城
                // P0015 商品购买
                // P0016 选择医生


            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
        str = String.format(str, new Object[]{date, "02"});

        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_ACTIONADs, SOAP_METHODNAMEADs,
                SOAP_URL, paramMap);
    }

    private void showADs() {

        docorView.setVisibility(View.VISIBLE);
        docorView.setImageResources(mImageUrl, mObject,
                new ImageCycleViewListener() {

                    @Override
                    public void displayImage(String imageURL,
                                             ImageView imageView) {
                        try {
                            ImageLoader.getInstance().displayImage(imageURL,
                                    imageView);
                            LayoutParams para;
                            para = imageView.getLayoutParams();
                            para.width = Util.getScreenWidth(DoctorHomeActivity.this);
                            para.height = para.width * 2 / 5;
                            imageView.setLayoutParams(para);
                        } catch (OutOfMemoryError e) {

                        }

                    }

                    @Override
                    public void onImageClick(int position, View imageView,
                                             ArrayList<Map<String, Object>> mObject) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(getApplicationContext(),
                        // "type"+mType.get(position), 1).show();
                        // P0005 小贴士
                        // P0006 热点
                        // P0007 个人中心
                        // P0008 妈妈足迹
                        // P0009 微专题
                        // P0010 医生首页
                        // P0011 医生文章
                        // P0012 孕健康
                        // P0013 话题
                        // P0014 商城
                        // P0015 商品购买
                        // P0016 选择医生

                        String p = mObject.get(position).get("Position")
                                .toString();
                        if (p.equals("P0005")) {
                            Intent intent = new Intent(DoctorHomeActivity.this,
                                    TestWebView.class);
                            intent.putExtra("YunWeek", yu.Week + 1 + "");
                            startActivity(intent);
//							String ID = mObject.get(position).get("ID")
//									.toString();
//							Intent i = new Intent(DoctorHomeAct.this,
//									News_Detail.class);
//							i.putExtra("ID", ID);
//							startActivity(i);
                        } else if (p.equals("P0003") || p.equals("P0006") || p.equals("P0004")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();

                            int linkid=Integer.valueOf(ID);
                            if(linkid!=0) {
                                Intent i = new Intent(DoctorHomeActivity.this,
                                        News_Detail.class);
                                i.putExtra("ID", ID);
                                startActivity(i);
                            }
                        } else if (p.equals("P0007")) {

                            Intent i = new Intent(DoctorHomeActivity.this,
                                    MainTabActivity.class);
                            i.putExtra("TabIndex", "D_TAB");

                            startActivity(i);
                        } else if (p.equals("P0008")) {
                            user=LocalAccessor.getInstance(DoctorHomeActivity.this).getUser();
                            if(user.UserID==0){

                                startActivity(new Intent(DoctorHomeActivity.this,LoginActivity.class));
                            }else{
                                Intent i = new Intent(DoctorHomeActivity.this,
                                        MyCHK_Timeline.class);
                                i.putExtra("Page", "User_Foot");
                                startActivity(i);
                            }
                        } else if (p.equals("P0009")) {


                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid=Integer.valueOf(ID);
                            if(linkid!=0) {
                                Intent i = new Intent(DoctorHomeActivity.this, SmallTalkAct.class);
                                i.putExtra("ID", ID);
                                startActivity(i);
                            }
                        } else if (p.equals("P0010")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid=Integer.valueOf(ID);
                            if(linkid!=0) {
                                if (ID.equals(DoctorID)) {
                                    Toast.makeText(DoctorHomeActivity.this,
                                            "您已经在当前医生首页", 1).show();
                                } else {
                                    Intent i = new Intent(DoctorHomeActivity.this,
                                            DoctorHomeActivity.class);
                                    i.putExtra("DoctorID", ID);
                                    startActivity(i);

                                }
                            }
                        } else if (p.equals("P0016")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid=Integer.valueOf(ID);
                            if(linkid!=0) {
                                Intent i = new Intent(DoctorHomeActivity.this,
                                        CHK_DoctorList.class);
                                i.putExtra("HostiptalID", ID);
                                i.putExtra("selectdoc","select");
                                startActivity(i);
                            }
                        } else if (p.equals("P0011")) {

                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid=Integer.valueOf(ID);
                            if(linkid!=0) {
                                Intent i = new Intent(DoctorHomeActivity.this,
                                        Activity_ReplyPage.class);
                                i.putExtra("ID", ID);

                                startActivity(i);
                            }
                        } else if (p.equals("P0017")) {
//							https://lovestaging.topmd.cn/html/1.html
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid=Integer.valueOf(ID);
                            if(linkid!=0) {
                                Intent i = new Intent(DoctorHomeActivity.this,
                                        StaticWebView.class);
                                i.putExtra("ID", ID);

                                startActivity(i);
                            }

                        } else if (p.equals("P0014")) {
                            if (user.UserID == 0) {
                                Intent i = new Intent(DoctorHomeActivity.this, LoginActivity.class);
                                i.putExtra("Page", "User_Set");
                                startActivityForResult(i, 1);
                            } else {
                                startActivity(new Intent(DoctorHomeActivity.this, ShopWebView.class));
                            }

                        }

                    }


                });
        docorView.startImageCycle();

    }

    private void getDate(final int page) {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub

                String value = result.toString();

                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("ArticleInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
//                        if (page == 1) {
//
//                        }

                        if(isFisrt) {
                            new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    // 千万别忘了告诉控件刷新完毕了哦！
                                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                }
                            }.sendEmptyMessageDelayed(0, 500);
                        }
                        if(!isFisrt) {
                            loadmore.setVisibility(View.GONE);
                        }else{
                            if(page>1) {
                                loadmore.setVisibility(View.VISIBLE);
                                loadstate_tv.setText("没有更多数据");
                            }
                        }

                        isFisrt= true;

//                        else {
//                            loadmore.setVisibility(View.VISIBLE);
//                            loadstate_tv.setText("没有更多数据");
//                            new Handler() {
//                                @Override
//                                public void handleMessage(Message msg) {
//                                    // 千万别忘了告诉控件加载完毕了哦！
//                                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                                }
//                            }.sendEmptyMessageDelayed(0, 500);
//                            mListView.setSelection(mListView.getBottom());
//                        }

                    } else {
                        loadmore.setVisibility(View.VISIBLE);
                        Map<String, Object> map;
                        for (int i = 0; i < array.length(); i++) {
                            map = new HashMap<String, Object>();
                            map.put("DoctorName", DoctorName);
                            map.put("Title", array.getJSONObject(i)
                                    .getString("Title").toString());
                            map.put("Content", array.getJSONObject(i)
                                    .getString("Content").toString());
                            map.put("ID", array.getJSONObject(i)
                                    .getString("ID").toString());
                            map.put("DoctorID", array.getJSONObject(i)
                                    .getString("DoctorID").toString());
                            map.put("likecount", array.getJSONObject(i)
                                    .getString("likecount"));
                            map.put("Type",
                                    array.getJSONObject(i).getString("Type"));
                            String time = array.getJSONObject(i)
                                    .get("CreatedDate").toString()
                                    .replace("/", "-");
                            time = stringToDate2(time, "yyyy-MM-dd HH:mm:ss");
                            String times = IOUtils.getMistimingTimes(time)
                                    .toString();
                            map.put("CreatedDate", times);

                            map.put("ReplysCount", array.getJSONObject(i)
                                    .getString("ReplysCount").toString());

                            String imgUrl = array.getJSONObject(i).getString(
                                    "ImageURL");
                            ArrayList<Image> itemList = new ArrayList<Image>();
                            if (!"".equals(imgUrl)) {
                                itemList.clear();
                                if (imgUrl.indexOf(",") >= 0) {
                                    String[] strs = imgUrl.split("\\,");
                                    Image img;
                                    Bitmap bitmap = null;
                                    for (int m = 0; m < strs.length; m++) {
                                        if (bitmap == null) {
                                            bitmap = mImageLoader
                                                    .loadImageSync(strs[m]);
                                        } else {
                                            bitmap.recycle();
                                            bitmap = mImageLoader
                                                    .loadImageSync(strs[m]);
                                        }
                                        int width = bitmap.getWidth();
                                        int height = bitmap.getHeight();
                                        img = new Image(strs[m], width, height,
                                                m);
                                        itemList.add(img);
                                    }
                                    map.put("itemList", itemList);

                                } else {

                                    Bitmap bitmap = mImageLoader
                                            .loadImageSync(imgUrl);
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    Image img = new Image(imgUrl, width,
                                            height, 0);
                                    itemList.add(img);
                                    map.put("itemList", itemList);

                                }

                            } else {
                                map.put("itemList", itemList);
                            }
                            arrayList.add(map);

                        }
                        mAdapter.setDatalist(arrayList);
                        mAdapter.notifyDataSetChanged();

                            if (page > 1) {
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        // 千万别忘了告诉控件加载完毕了哦！
                                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                    }
                                }.sendEmptyMessageDelayed(0, 500);
                                mListView.setSelection(mListView.getBottom());
                            } else {
                                if(isFisrt) {
                                    new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            // 千万别忘了告诉控件刷新完毕了哦！
                                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                        }
                                    }.sendEmptyMessageDelayed(0, 500);
                                }

                            }
                        isFisrt=true;


                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();


                        if (page > 1) {
                            new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    // 千万别忘了告诉控件加载完毕了哦！

                                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                                }
                            }.sendEmptyMessageDelayed(0, 500);
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
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID><Type>%s</Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize><Status>%s</Status></Request>";
        str = String.format(str, new Object[]{ "", DoctorID, "",
                page + "", 10 + "", 1 + ""});
        // ArticleInquiry
        //
        // 刘志赫 2016/1/20 16:24:20
        // DoctorID Type PageIndex PageSize
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAMEA, SOAP_URL,
                paramMap);
    }

    // 返回string类型时间
    public static String stringToDate2(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formatter.format(date);
    }

    private void geneItems(final int ACTION) {
        if (ACTION == FRIST_GET_DATE) {// 第一次加载 0 文章 1 说说
            if (arrayList.size() > 0) {
                arrayList.clear();
            }
            getTime();
            page = 1;
            getDate(page);

        } else if (ACTION == LOADMORE_GET_DATE) {
            page++;
            getDate(page);
        } else if (ACTION == BROADCAST) {

            getmB(arrayList.size());

        }

    }

    private void getSubject() {
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub

                String value = result.toString();

                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("MicroTopicInquiryIndex");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        llhome.setVisibility(View.GONE);
                    } else {
                        Map<String, Object> map;
                        {
                            endtime = array.getJSONObject(0).getString(
                                    "EndTime").toString().replace("/", "-");

                            String time = endtime.replace("/", "-");

                            Date endtimed = IOUtils.string2Date(time,
                                    "yyyy-MM-dd HH:mm:ss");
//							if (endtimed.getTime() > date.getTime()) {
                            llhome.setVisibility(View.VISIBLE);
                            starttime = array.getJSONObject(0).getString(
                                    "StartTime").toString().replace("/", "-");
                            String endtime1 = array.getJSONObject(0)
                                    .getString("EndTime");
                            endtime1 = endtime1.replace("/", "-");
                            String[] endtime1s = (endtime1.split(" ")[1])
                                    .split(":");
                            String starttime1 = array.getJSONObject(0)
                                    .getString("StartTime");
                            starttime1 = starttime1.replace("/", "-");
                            String starttimes[] = (starttime1.split(" ")[0])
                                    .split("-");
                            String starttimem[] = (starttime1.split(" ")[1])
                                    .split(":");
                            String tiltle = array.getJSONObject(0)
                                    .getString("Title")
                                    + "#"
                                    + starttimes[1]
                                    + "月"
                                    + starttimes[2]
                                    + "日"
                                    + starttimem[0]
                                    + ":"
                                    + starttimem[1]
                                    + "-"
                                    + endtime1s[0]
                                    + ":"
                                    + endtime1s[1]
                                    + ","

                                    + "与您相约";
                            ID = array.getJSONObject(0)
                                    .getString("ID").toString();
                            microTopic.setText(tiltle);

                        }

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        // TODO Auto-generated method stub
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><DoctorID>%s</DoctorID><Type>%s</Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize><strWhere>%s</strWhere><Flag>%s</Flag><Index>%s</Index><Top>%s</Top></Request>";
        str = String.format(str, new Object[]{DoctorID, "", "0", "",
                "1", "", "", "1"});
        // ArticleInquiry
        //
        // 刘志赫 2016/1/20 16:24:20
        // DoctorID Type PageIndex PageSize
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_SUBJECTACTION,
                SOAP_SUBJECTMETHODNAME, SOAP_URL, paramMap);
    }

    private void getTime() {
        // TODO Auto-generated method stub
//        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
//
//            @Override
//            public void processJsonObject(Object result) {
//                // {"UserPointAdd":[{"MessageContent":"成功！","MessageCode":"0"}]}
//                // TODO Auto-generated method stub
//
//                String value = result.toString();
//                // {"UserInquiry":[{"UserSex":"01","UserAge":"33","PictureURL":"~\/UploadFile\/image\/2016030109454450694.jpg","NickName":"宝爸20153028230623hhhh","UserNO":"410102198201251510","UserID":"50694","YuBirthTime":"2016\/10\/25
//                // 0:00:00","RealName":"郭芮","Point":"60","ClientID":"0c1726ae905edaa84b2b26e794910ab1","City":"郑州"}]}
//                JSONObject mySO = (JSONObject) result;
//                try {
//                    JSONArray array = mySO.getJSONArray("GetServerTime");
//                    SimpleDateFormat sdf = new SimpleDateFormat(
//                            "yyyy-MM-dd HH:mm:ss");
//                    date = IOUtils.string2Date(array.getJSONObject(0)
//                            .getString("ServerTime"), "yyyy-MM-dd HH:mm:ss");
                    getSubject();
                    getMicroTopicInquiry(DoctorID);

//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        String str = "<Request></Request>";
//        str = String.format(str, new Object[]{"",});
//
//        paramMap.put("str", str);
//        task.execute(SOAP_NAMESPACE, SOAP_SERVERTIMEACTION,
//                SOAP_SERVERTIMEMETHODNAME, SOAP_URL, paramMap);
    }

    protected void getMicroTopicInquiry(String doctorID2) {
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
            @Override
            public void processJsonObject(Object result) {
                String returnvalue = result.toString();
                Map<String, Object> map;
                try {
                    JSONObject mySO = (JSONObject) result;
                    JSONArray array = mySO
                            .getJSONArray("MicroTopicInquiry");

                    if (array.getJSONObject(0).has("MessageCode")) {
                        llmore.setVisibility(View.GONE);
                    } else {
                        llmore.setVisibility(View.VISIBLE);
                        for (int i = 0; i < array.length(); i++) {
                            String endtime2 = array.getJSONObject(i)
                                    .getString("EndTime");
                            endtime2 = endtime2.replace("/", "-");
                            String[] endtime2s = (endtime2.split(" ")[1])
                                    .split(":");
                            String starttime2 = array.getJSONObject(i)
                                    .getString("StartTime");
                            starttime2 = starttime2.replace("/", "-");
                            String starttimes2[] = (starttime2.split(" ")[0])
                                    .split("-");
                            String starttimem2[] = (starttime2.split(" ")[1])
                                    .split(":");
                            String content2 = array.getJSONObject(i)
                                    .getString("Title")
                                    + "#"
                                    + starttimes2[1]
                                    + "月"
                                    + starttimes2[2]
                                    + "日"
                                    + starttimem2[0]
                                    + ":"
                                    + starttimem2[1]
                                    + "-"
                                    + endtime2s[0]
                                    + ":"
                                    + endtime2s[1]
                                    + ","
                                    + "与您相约";

                            if (i == 0) {
                                ID1 = array.getJSONObject(i)
                                        .getString("ID").toString();
                                specialone.setText(content2);
                                llone.setVisibility(View.VISIBLE);
                                lltwo.setVisibility(View.GONE);
                            } else if (i == 1) {
                                ID2 = array.getJSONObject(i)
                                        .getString("ID").toString();
                                specialtwo.setText(content2);
                                llone.setVisibility(View.VISIBLE);
                                lltwo.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(
                mContext, true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String str = "<Request><DoctorID>%s</DoctorID><PageIndex>%s</PageIndex><PageSize>%s</PageSize><Type>%s</Type><Flag>%s</Flag></Request>";

        str = String.format(str, new Object[]
                {DoctorID, 1 + "", 2 + "", "3", "0"});

        paramMap.put("str", str);


        task.execute(SOAP_NAMESPACE, SOAP_MICROACTION, SOAP_MICROMETHODNAMEA,
                SOAP_URL, paramMap);
    }


    private void searchDocDes() {
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {

                String returnvalue = result.toString();
                if (arrayDocList.size() > 0) {
                    arrayDocList.clear();
                }
                if (returnvalue != null) {
                    Map<String, Object> map;
                    try {
                        JSONObject mySO = (JSONObject) result;
                        JSONArray array = mySO.getJSONArray("DoctorInquiry");
                        map = new HashMap<String, Object>();
                        map.put("Description", array.getJSONObject(0)
                                .getString("Description"));
                        map.put("AskCount",
                                array.getJSONObject(0).getString("AskCount"));
                        map.put("UserCount",
                                array.getJSONObject(0).getString("UserCount"));
                        map.put("IsLine",
                                array.getJSONObject(0).getString("IsLine"));
                        map.put("DoctorType",
                                array.getJSONObject(0).getString("DoctorType"));
                        map.put("DoctorName",
                                array.getJSONObject(0).getString("DoctorName"));
                        map.put("HospitalName", array.getJSONObject(0)
                                .getString("HospitalName"));
                        map.put("DepartNO",
                                array.getJSONObject(0).getString("DepartNO"));
                        map.put("AskPrice",
                                array.getJSONObject(0).getString("AskPrice"));
                        map.put("IsFavored",
                                array.getJSONObject(0).getString("IsFavored"));
                        map.put("HospitalID",
                                array.getJSONObject(0).getString("HospitalID"));
                        map.put("Price",
                                array.getJSONObject(0).getString("Price"));
                        map.put("AskPriceByOne", array.getJSONObject(0)
                                .getString("AskPriceByOne"));
                        map.put("DoctorID",
                                array.getJSONObject(0).getString("DoctorID"));
                        map.put("DoctorNO",
                                array.getJSONObject(0).getString("DoctorNO"));
                        map.put("Title",
                                array.getJSONObject(0).getString("Title"));
                        map.put("DoctorFavors", array.getJSONObject(0)
                                .getString("DoctorFavors"));
                        map.put("ImageURL", MMloveConstants.JE_BASE_URL2
                                + array.getJSONObject(0).getString("ImageURL")
                                .replace("~", "").replace("\\", "/"));
                        AskByOne = array.getJSONObject(0).getString(
                                "AskPriceByOne");
                        ImageUrl=array.getJSONObject(0).getString("ImageURL");
                        DoctorName = array.getJSONObject(0).getString(
                                "DoctorName");
                        AskCount = array.getJSONObject(0).getString("AskCount");
                        UserCount = array.getJSONObject(0).getString(
                                "UserCount");
                        HospitalID = array.getJSONObject(0).getString(
                                "HospitalID");
                        DoctorNO= array.getJSONObject(0).getString(
                                "DoctorNO");
                        DepartNO=array.getJSONObject(0).getString(
                                "DepartNO");
                        PicUrl = array.getJSONObject(0).getString("ImageURL");
                        isLine = Integer.valueOf(array.getJSONObject(0).getString("IsLine"));
                        tvhead1.setText(DoctorName);
                        DoctorTitle= array.getJSONObject(0).getString("Title");
                        price=array.getJSONObject(0).getString("Price");
                        if (PicUrl != null && !"".equals(PicUrl)) {
                            mImageLoader.displayImage(
                                    MMloveConstants.JE_BASE_URL2
                                            + PicUrl.replace("~", "").replace(
                                            "\\", "/"), head);
                        }
                        arrayDocList.add(map);
                        if (Integer.valueOf(arrayDocList.get(0)
                                .get("IsFavored").toString()) > 0) {
                            imgfous.setText("已关注");
                            img_rv1.setText("已关注");
                        } else {
                            imgfous.setText("关注");
                            img_rv1.setText("关注");
                        }

                        HosName = arrayDocList.get(0).get("HospitalName")
                                .toString();
                        tvhead.setText(DoctorName);
                        txtName.setText(DoctorName);
                        txtFans.setText("粉丝量:" + arrayDocList.get(0).get("DoctorFavors")
                                .toString());
                        tvpingtai.setText(
                                array.getJSONObject(0).getString(
                                        "HospitalName"));
                        if (page == 1) {

                            geneItems(FRIST_GET_DATE);
                        } else {
                            geneItems(BROADCAST);
                        }
                        if(Integer.valueOf(array.getJSONObject(0).getString("Enabled"))==0){
                            findViewById(R.id.tvyuyue).setVisibility(View.VISIBLE);
                            findViewById(R.id.tvzixuncontainer).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.tvyuyue).setVisibility(View.GONE);
                            findViewById(R.id.tvzixuncontainer).setVisibility(View.VISIBLE);
                        }
                        getSubject();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        };
        // TODO Auto-generated method stub
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();

        String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID><HospitalID>%s</HospitalID><IsPMD>%s</IsPMD></Request>";
        if(user.UserID!=0){
            str = String.format(str, new Object[]{DoctorID, user.UserID + "", "",
                    "1"});
        }else {

            str = String.format(str, new Object[]{DoctorID,"", "",
                    "1"});
        }
        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_DESACTION, SOAP_DESMETHODNAME,
                SOAP_URL, paramMap);

    }

    private void getmB(int size) {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub

                String value = result.toString();

                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("ArticleInquiry");
                    if (array.getJSONObject(0).has("MessageCode")) {
                        if (page == 1) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                            loadstate_tv.setText("没有更多数据");
                        }

                    } else {
                        Map<String, Object> map;
                        if (arrayList.size() > 0) {
                            arrayList.clear();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            map = new HashMap<String, Object>();
                            map.put("DoctorName", DoctorName);
                            map.put("Title", array.getJSONObject(i)
                                    .getString("Title").toString());
                            map.put("Content", array.getJSONObject(i)
                                    .getString("Content").toString());
                            map.put("ID", array.getJSONObject(i)
                                    .getString("ID").toString());
                            map.put("DoctorID", array.getJSONObject(i)
                                    .getString("DoctorID").toString());
                            map.put("likecount", array.getJSONObject(i)
                                    .getString("likecount"));
                            map.put("Type",
                                    array.getJSONObject(i).getString("Type"));
                            String time = array.getJSONObject(i)
                                    .get("CreatedDate").toString()
                                    .replace("/", "-");
                            time = stringToDate2(time, "yyyy-MM-dd HH:mm:ss");
                            String times = IOUtils.getMistimingTimes(time)
                                    .toString();
                            map.put("CreatedDate", times);

                            map.put("ReplysCount", array.getJSONObject(i)
                                    .getString("ReplysCount").toString());

                            String imgUrl = array.getJSONObject(i).getString(
                                    "ImageURL");
                            ArrayList<Image> itemList = new ArrayList<Image>();
                            if (!"".equals(imgUrl)) {
                                itemList.clear();
                                if (imgUrl.indexOf(",") >= 0) {
                                    String[] strs = imgUrl.split("\\,");
                                    Image img;
                                    Bitmap bitmap = null;
                                    for (int m = 0; m < strs.length; m++) {
                                        if (bitmap == null) {
                                            bitmap = mImageLoader
                                                    .loadImageSync(strs[m]);
                                        } else {
                                            bitmap.recycle();
                                            bitmap = mImageLoader
                                                    .loadImageSync(strs[m]);
                                        }
                                        int width = bitmap.getWidth();
                                        int height = bitmap.getHeight();
                                        img = new Image(strs[m], width, height,
                                                m);
                                        itemList.add(img);
                                    }
                                    map.put("itemList", itemList);

                                } else {

                                    Bitmap bitmap = mImageLoader
                                            .loadImageSync(imgUrl);
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    Image img = new Image(imgUrl, width,
                                            height, 0);
                                    itemList.add(img);
                                    map.put("itemList", itemList);

                                }

                            } else {
                                map.put("itemList", itemList);
                            }
                            arrayList.add(map);

                        }
                        mAdapter.setDatalist(arrayList);
                        mAdapter.notifyDataSetChanged();
//						myScrollView.smoothScrollTo(0, 0);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID><Type>%s</Type><PageIndex>%s</PageIndex><PageSize>%s</PageSize><Status>%s</Status></Request>";
        str = String.format(str, new Object[]{user.UserID + "", DoctorID, "",
                1 + "", size + "", 1 + ""});
        // ArticleInquiry
        //
        // 刘志赫 2016/1/20 16:24:20
        // DoctorID Type PageIndex PageSize
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAMEA, SOAP_URL,
                paramMap);
    }

    private void setListener() {
        // TODO Auto-generated method stub
        pullToRefreshLayout.setOnRefreshListener(this);
        imgBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        imgback1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        imgfous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                user=LocalAccessor.getInstance(DoctorHomeActivity.this).getUser();
                // TODO Auto-generated method stub
                if (user.UserID != 0) {
                    if (imgfous.getText().toString().trim().equals("已关注")) {
                        if (!hasInternetConnected()) {
                            Toast.makeText(DoctorHomeActivity.this, "网络数据获取失败，请检查网络设置",
                                    1).show();
                            return;
                        }
                        if (!isFav) {
                            DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
                                    DoctorHomeActivity.this).setDialogMsg("友情提示",
                                    "确定要取消关注吗", "确定").setOnClickListenerEnsure(
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            FavorDel();

                                        }
                                    });
                            DialogUtils.showSelfDialog(DoctorHomeActivity.this, dialogEnsureCancelView);


                        } else {
                            Toast.makeText(DoctorHomeActivity.this, "请不要重复关注", 1).show();
                        }
                    } else if (imgfous.getText().toString().trim().equals("关注")) {
                        if (!hasInternetConnected()) {
                            Toast.makeText(DoctorHomeActivity.this, "网络数据获取失败，请检查网络设置",
                                    1).show();
                            return;
                        }
                        if (!isFav) {
                            FavorDoctor();
                        } else {
                            Toast.makeText(DoctorHomeActivity.this, "请不要重复关注", 1).show();
                        }
                    }
                } else {
                    startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
                }


            }
        } );
        img_rv1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                user=LocalAccessor.getInstance(DoctorHomeActivity.this).getUser();
                if (user.UserID != 0) {
                    if (imgfous.getText().toString().trim().equals("已关注")) {
                        if (!hasInternetConnected()) {
                            Toast.makeText(DoctorHomeActivity.this, "网络数据获取失败，请检查网络设置",
                                    1).show();
                            return;
                        }
                        if (!isFav) {
                            DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
                                    DoctorHomeActivity.this).setDialogMsg("友情提示",
                                    "确定要取消关注吗", "确定").setOnClickListenerEnsure(
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            FavorDel();

                                        }
                                    });
                            DialogUtils.showSelfDialog(DoctorHomeActivity.this, dialogEnsureCancelView);


                        } else {
                            Toast.makeText(DoctorHomeActivity.this, "请不要重复关注", 1).show();
                        }
                    } else if (imgfous.getText().toString().trim().equals("关注")) {
                        if (!hasInternetConnected()) {
                            Toast.makeText(DoctorHomeActivity.this, "网络数据获取失败，请检查网络设置",
                                    1).show();
                            return;
                        }
                        if (!isFav) {
                            FavorDoctor();
                        } else {
                            Toast.makeText(DoctorHomeActivity.this, "请不要重复关注", 1).show();
                        }
                    }
                } else {
                    startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
                }
            }
        });
        txtMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DoctorHomeActivity.this, TopicAllAct.class);

                intent.putExtra("DoctorID", DoctorID);

                startActivity(intent);

            }
        });
        llhome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DoctorHomeActivity.this, SmallTalkAct.class);

                intent.putExtra("ID", ID);

                startActivity(intent);
            }
        });
        llone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DoctorHomeActivity.this, SmallTalkAct.class);
                intent.putExtra("ID", ID1);
                startActivity(intent);
            }
        });
        lltwo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(DoctorHomeActivity.this, SmallTalkAct.class);
                intent.putExtra("ID", ID2);
                startActivity(intent);
            }
        });
        myScrollView.setOnTouchListener(new OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;

                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了，此处你的操作业务
                            scrollX = myScrollView.getScrollX();
                            scrollY = myScrollView.getScrollY();
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
                            lastY = scroller.getScrollY();
                        }
                    }
                }
            };


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int eventAction = event.getAction();
                int y = (int) event.getRawY();
                switch (eventAction) {
                    case MotionEvent.ACTION_UP:

                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);

                        break;
                    default:
                        break;
                }
                return false;
            }


        });

    }

    private void FavorDel() {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub
                String returnvalue = result.toString();
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("FavorDoctorDelete");
                    if ("0".equals(array.getJSONObject(0).getString(
                            "MessageCode"))) {

//                        Toast.makeText(DoctorHomeAct.this, "取消关注成功！", 1).show();

                        imgfous.setText("关注");
                    } else if ("1".equals(array.getJSONObject(0).getString(
                            "MessageCode"))) {
                        Toast.makeText(
                                DoctorHomeActivity.this,
                                array.getJSONObject(0).getString(
                                        "MessageContent"), 1).show();
                    }
                    isFav = false;
                    searchDocDes();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    isFav = false;
                }
                // {"FavorDoctorInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}

            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        isFav = true;
        String userId = user.UserID + "";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID></Request>";

        str = String.format(str, new Object[]{userId, DoctorID});

        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3, SOAP_URL,
                paramMap);
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

//	@SuppressWarnings("deprecation")
//	private void setBackground(Bitmap bmp) {
//
//		final Bitmap blurBmp = BlurUtil.fastblur(DoctorHomeAct.this, bmp, 20);
//		final Drawable newBitmapDrawable = new BitmapDrawable(blurBmp);
//		rlmaosha.post(new Runnable() {
//			@Override
//			public void run() {
//				rlmaosha.setBackgroundDrawable(newBitmapDrawable);// 设置背景
//				rlmaosha.setScaleType(ScaleType.CENTER);
//			}
//		});
//	}

    @Override
    public void onScroll(int scrollY) {

        // TODO Auto-generated method stub
        if (scrollY >= searchLayoutTop) {
            search01.setVisibility(View.VISIBLE);
            rl_zixun.setVisibility(View.VISIBLE);
        } else {
            search01.setVisibility(View.GONE);
            rl_zixun.setVisibility(View.GONE);
        }

    }

    private void FavorDoctor() {
        // TODO Auto-generated method stub
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub
                String returnvalue = result.toString();
                JSONObject mySO = (JSONObject) result;
                try {
                    JSONArray array = mySO.getJSONArray("FavorDoctorInsert");
                    if ("0".equals(array.getJSONObject(0).getString(
                            "MessageCode"))) {
                        Toast.makeText(DoctorHomeActivity.this, "关注成功！", 1).show();
                        imgfous.setText("已关注");
                    } else if ("1".equals(array.getJSONObject(0).getString(
                            "MessageCode"))) {
                        Toast.makeText(
                                DoctorHomeActivity.this,
                                array.getJSONObject(0).getString(
                                        "MessageContent"), 1).show();
                    }
                    searchDocDes();
                    isFav = false;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    isFav = false;
                }
                // {"FavorDoctorInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}

            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(DoctorHomeActivity.this,
                true, doProcess);
        String userId = user.UserID + "";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><UserID>%s</UserID><DoctorID>%s</DoctorID></Request>";
        isFav = true;
        str = String.format(str, new Object[]{userId, DoctorID});

        paramMap.put("str", str);

        // 必须是这5个参数，而且得按照顺序
        task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2, SOAP_URL,
                paramMap);
    }

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

        page = 1;
        Log.e("vvvv0000===",page+"");
        searchDocDes();



    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            unregisterReceiver(mRefreshBroadcastReceiver);

            mRefreshBroadcastReceiver = null;
        } catch (Exception e) {
        }
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        // TODO Auto-generated method stub
        // 加载操作


        page++;
        Log.e("vvvv11111===",page+"");
        geneItems(LOADMORE_GET_DATE);

    }
}

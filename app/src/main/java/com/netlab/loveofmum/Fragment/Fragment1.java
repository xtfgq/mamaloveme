package com.netlab.loveofmum.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.Activity_ReplyPage;
import com.netlab.loveofmum.CHKTimeDetailActivity;
import com.netlab.loveofmum.CHKTimeList;
import com.netlab.loveofmum.CHK_DoctorList;
import com.netlab.loveofmum.MainActivity;
import com.netlab.loveofmum.MainTabActivity;
import com.netlab.loveofmum.MyCHK_OrderDetail;
import com.netlab.loveofmum.MyCHK_Timeline;
import com.netlab.loveofmum.News_Detail;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.SearchWebView;
import com.netlab.loveofmum.SmallTalkAct;
import com.netlab.loveofmum.StaticWebView;
import com.netlab.loveofmum.TopicWebView;
import com.netlab.loveofmum.activity.DoctorHomeActivity;
import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.activity.SignInActivity;
import com.netlab.loveofmum.api.CustomHttpClient;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.BBSTalk;
import com.netlab.loveofmum.model.Hospital;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.WeeKTip;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.model.YunWeekAll;
import com.netlab.loveofmum.myadapter.FeedLastAdapter1;
import com.netlab.loveofmum.myadapter.WeekTipsListAdapter1;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.utils.AppUtils;
import com.netlab.loveofmum.utils.ComCallBack;
import com.netlab.loveofmum.utils.DetialGallery;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ScreenUtils;
import com.netlab.loveofmum.widget.ImageCycleView;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.MyViewPageAdapter;
import com.netlab.loveofmum.widget.MyViewPager;
import com.netlab.loveofmum.widget.RoundProgressBar;
import com.netlab.loveofmum.widget.XScrollView.RefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends BaseFragment implements RefreshLayout.OnRefreshListener,AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener {
    RefreshLayout refreshLayout;
    int displaywidth;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    FeedLastAdapter1 adapter = new FeedLastAdapter1(getActivity(),
                            communityTalkData, ClientID);
                    community_listView.setAdapter(adapter);
                    break;
                case 1010:
                    if (isPost) {
                        TipsInquiry();
                    }
                    break;

                default:
                    break;
            }
        }

    };

    //宝宝模块引用
    View updatecontainer,nextdatecontainer,gotaday;
    TextView update,currentdate,nextdate;
    DetialGallery babyGallery;
    TextView babyHeight,babyBirth_leftday,babyWeight,babay_tips;
    ImageCycleView baby_ad;
    ArrayList<String> baby_adimgurls = new ArrayList<String>();
    ArrayList<Map<String, Object>> babyad_data = new ArrayList<Map<String, Object>>();
    private Animation gotadayAnimation = null;

    //产检引用
    View check_infos,checkitem,check_quickReservationContainer;
    TextView check_quickReservation,reservationlabel,check_titletips,check_more;
    View check_Adscontainer;
    MyViewPager checkads_hospital;
    private List<Hospital> hosList = new ArrayList<Hospital>();
    View checkads_hospital_left,checkads_hospital_right;

    //搜索引用
    TextView searchkey;

    //社区引用
    ListViewForScrollView community_listView;
    List<BBSTalk>   communityTalkData;
    ImageCycleView community_ads;
    // 孕社区的广告数据
    ArrayList<String> community_adimgurls = new ArrayList<String>();
    ArrayList<Map<String, Object>> communityad_data= new ArrayList<Map<String, Object>>();



    //周提醒引用
    TextView weektips_titleLabel;
    ListViewForScrollView weektips_listView;
    List<WeeKTip> weeKTipsData;
    private User user;


    private int[] weekIcons = new int[]{R.drawable.week1, R.drawable.week2, R.drawable.week3, R.drawable.week4, R.drawable.week5, R.drawable.week6, R.drawable.week7, R.drawable.week8,
            R.drawable.week9, R.drawable.week10, R.drawable.week11, R.drawable.week12, R.drawable.week13, R.drawable.week14, R.drawable.week15, R.drawable.week16, R.drawable.week17, R.drawable.week18,
            R.drawable.week19, R.drawable.week20, R.drawable.week21, R.drawable.week22, R.drawable.week23, R.drawable.week24, R.drawable.week25, R.drawable.week26, R.drawable.week27, R.drawable.week28,
            R.drawable.week29, R.drawable.week30, R.drawable.week31, R.drawable.week32, R.drawable.week33, R.drawable.week34, R.drawable.week35, R.drawable.week36, R.drawable.week37, R.drawable.week38, R.drawable.week39, R.drawable.week40};
    private GalleryAdapter adapterGallery;
    private int per;
    private String ClientID;
    private int selectdayindex;
    private int todayGalleryindex;
    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_1, container, false);

        refreshLayout = (RefreshLayout)v;
        refreshLayout.setOnRefreshListener(this);

        displaywidth = ScreenUtils.getScreenWidth(getActivity());
        //头部宝宝周期模块控件初始化
        updatecontainer = v.findViewById(R.id.update_container);
        updatecontainer.setOnClickListener(this);

        gotaday = v.findViewById(R.id.gotaday);
        nextdatecontainer = v.findViewById(R.id.nextdate_container);
        nextdatecontainer.setOnClickListener(this);
        update = (TextView)v.findViewById(R.id.update);
        currentdate = (TextView)v.findViewById(R.id.currentdate);
        nextdate = (TextView)v.findViewById(R.id.nextdate);
        babyGallery = (DetialGallery)v.findViewById(R.id.babygallery);
        babyHeight = (TextView)v.findViewById(R.id.baby_height);
        babyBirth_leftday = (TextView)v.findViewById(R.id.baby_birth_leftday);
        babyWeight = (TextView)v.findViewById(R.id.baby_weight);
        babay_tips = (TextView)v.findViewById(R.id.babay_tips);
        baby_ad = (ImageCycleView)v.findViewById(R.id.baby_ad);
        v.findViewById(R.id.tosingn).setOnClickListener(this);

        gotadayAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim3);

        //产检模块
        check_infos = v.findViewById(R.id.check_infos);
        checkitem = v.findViewById(R.id.checkitem);
        check_quickReservation = (TextView)v.findViewById(R.id.check_quickReservation);
        reservationlabel = (TextView)v.findViewById(R.id.reservationlabel);
        check_quickReservationContainer = v.findViewById(R.id.check_quickReservationContainer);
        check_titletips = (TextView)v.findViewById(R.id.check_titletips);
        check_Adscontainer= v.findViewById(R.id.check_Adscontainer);
        checkads_hospital_left= v.findViewById(R.id.checkads_hospital_left);
        checkads_hospital_right= v.findViewById(R.id.checkads_hospital_right);
        checkads_hospital = (MyViewPager) v.findViewById(R.id.checkads_hospital);
        check_more = (TextView)v.findViewById(R.id.check_more);
        //搜索模块初始化
        searchkey = (TextView)v.findViewById(R.id.searchkey);
        v.findViewById(R.id.search).setOnClickListener(this);

        //社区模块初始化
        v.findViewById(R.id.community_title).setOnClickListener(this);
        community_ads = (ImageCycleView)v.findViewById(R.id.community_ads);
        community_listView = (ListViewForScrollView)v.findViewById(R.id.community_listview);
        community_listView.setEnabled(false);
        community_listView.setOnItemClickListener(this);
         //周提醒模块初始化
        v.findViewById(R.id.weektips_title).setOnClickListener(this);
        weektips_titleLabel = (TextView)v.findViewById(R.id.weektips_titlelabel);
        weektips_listView = (ListViewForScrollView)v.findViewById(R.id.weektips_listview);
        weektips_listView.setEnabled(false);
        ClientID = PushManager.getInstance().getClientid(getActivity());

        return v;
    }

    @Override
    public void initData() {
        user = LocalAccessor.getInstance(getActivity()).getUser();
        initBabyGalleryData();                                          //初始化BabyGallery
        onRefresh(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        initBirthData();        //   初始化一下当前孕妈的数据，  根据当前用户数据决定是否显示医院数据（getHospital）,并且获取产检信息  ，因为有用户状态切换所以写到onresume方法里
        getweektips();                                                 //因为要根据孕妈当前周期获取数据 所以放到后边。为什么不让initBirthData调用因为灵活
    }
    @Override
    public void onRefresh(RefreshLayout pullToRefreshLayout) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());// new Date()为获取当前系统时间
        getBabyAdsData(date);
        getCommunityAdsData(date);
        getCommunityTalkData();

    }
    public int selectNum = 0;//全局变量，保存被选中的item
    private Date  selectday;     //表示当前Gallery显示的日期，初始日期的当前日期
    public Boolean isPost = false;
    Yuchan yuChan;          //从怀孕到当前时间的天数,如果孩子已经出生继续计时

//    private int week;
    int leftdays, childday;//leftdays(表示到孩子出生剩余的天数，childday表示孩子多少天数)
    private Boolean isAlready=false;
    String[] lengths;                       //存储宝宝某个天数的相应身高
    String[] weights;                       //存储宝宝某个天数的相应体重

    String currentBirthDay="";             //孕妈上次的预产日期
    int userID=0;                           //孕妈上次的用户ID
    private void initBabyGalleryData(){
        String text = null;
        try {
            InputStream is = getActivity().getAssets().open("eat.txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            text = new String(buffer, "UTF-8");
            String[] keys = text.split("\\,");
            Random ra = new Random();
            int key = ra.nextInt(keys.length) + 0;
            searchkey.setText("能不能吃:" + keys[key]);
            InputStream isH = getActivity().getAssets().open("height.txt");
            int sizeH = isH.available();
            // Read the entire asset into a local byte buffer.
            byte[] bufferH = new byte[sizeH];
            isH.read(bufferH);
            isH.close();
            // Convert the buffer into a string.
            text = new String(bufferH, "UTF-8");
            lengths = text.split("\\,");
            InputStream isW = getActivity().getAssets().open("weight.txt");
            int sizeW = isW.available();
            // Read the entire asset into a local byte buffer.
            byte[] bufferW = new byte[sizeW];
            isW.read(bufferW);
            isW.close();
            // Convert the buffer into a string.
            text = new String(bufferW, "UTF-8");
            weights = text.split("\\,");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化首页顶部Gallery的Adapter
        final List<YunWeekAll> yunlist = new ArrayList<YunWeekAll>();
        for (int i = 1; i <= 281; i++) {
            YunWeekAll map = new YunWeekAll();
            if (i / 7 == 0) {
                map.setDay(i % 7 + "天");
                map.setImage(getResources().getDrawable(weekIcons[0]));
            } else if (i % 7 == 0) {
                map.setDay(i / 7 + "周");
                map.setImage(getResources().getDrawable(weekIcons[i / 7 - 1]));
            } else if (i == 281) {
                map.setDay("宝宝已经出生了");
                map.setImage(getResources().getDrawable(R.drawable.img_bb));
            } else {
                map.setDay(i / 7 + "周+" + i % 7 + "天");
                map.setImage(getResources().getDrawable(weekIcons[i / 7 - 1]));
            }
            yunlist.add(map);
        }
        adapterGallery = new GalleryAdapter(getActivity(), yunlist);


        babyGallery.setAdapter(adapterGallery);
        babyGallery.setOnItemSelectedListener(this);
    }
    //获取宝宝下边的广告
    private void getBabyAdsData(String date) {

        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {

                if (result != null) {
                    JSONObject mySO = (JSONObject) result;
                    Map<String, Object> map;
                    try {
                        JSONArray array = mySO.getJSONArray("ADInquiry");
                        if (array.getJSONObject(0).has("MessageCode")) {
                            // Toast.makeText(getActivity(), "数据为空", 1).show();
                            baby_ad.setVisibility(View.GONE);


                        } else {
                            if (baby_adimgurls != null && baby_adimgurls.size() > 0) {
                                baby_adimgurls.clear();
                            }


                            if (babyad_data != null && babyad_data.size() > 0) {
                                babyad_data.clear();
                            }

                            for (int i = 0; i < array.length(); i++) {

                                map = new HashMap<String, Object>();
                                map.put("ID",
                                        array.getJSONObject(i).getString("LinkID"));
                                map.put("Position",
                                        array.getJSONObject(i).getString("Position"));
                                map.put("ImageUr",
                                        (MMloveConstants.JE_BASE_URL + array
                                                .getJSONObject(i).getString(
                                                        "ImageUrl")).toString()
                                                .replace("~", "")
                                                .replace("\\", "/"));


                                baby_adimgurls
                                        .add((MMloveConstants.JE_BASE_URL + array
                                                .getJSONObject(i).getString(
                                                        "ImageUrl")).toString()
                                                .replace("~", "")
                                                .replace("\\", "/"));

                                babyad_data.add(map);
                            }
                            showBabyAds();
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        // TODO Auto-generated method stub
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
                doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
        str = String.format(str, new Object[]{date, "01"});

        paramMap.put("str", str);
        task.execute(MMloveConstants.URL001, MMloveConstants.URL001+ MMloveConstants.Ads, MMloveConstants.Ads,
                MMloveConstants.URL002, paramMap);
    }
    private void showBabyAds() {
        baby_ad.setVisibility(View.VISIBLE);
        baby_ad.setImageResources(baby_adimgurls, babyad_data,
                new ImageCycleView.ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL,
                                             ImageView imageView) {
                        try {
                            ImageLoader.getInstance().displayImage(imageURL,
                                    imageView);
                            ViewGroup.LayoutParams para;
                            para = imageView.getLayoutParams();
                            para.width = displaywidth;
                            para.height = para.width * 2 / 5;
                            imageView.setLayoutParams(para);
                        } catch (OutOfMemoryError e) {
                            // TODO Auto-generated catch block
                        }
                    }
                    @Override
                    public void onImageClick(int position, View imageView,
                                             ArrayList<Map<String, Object>> mObject) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(getApplicationContext(),
                        // "type"+mType.get(position), 1).show();
                        String p = mObject.get(position).get("Position")
                                .toString();
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
                        // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                        if (p.equals("P0005")) {
                            Intent intent = new Intent(getActivity(),
                                    TestWebView.class);
                            intent.putExtra("YunWeek", yuChan.Week + 1 + "");
                            startActivity(intent);


                        } else if (p.equals("P0003") || p.equals("P0006") || p.equals("P0004")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        News_Detail.class);
                                i.putExtra("ID", ID);
                                startActivity(i);
                            }
                        } else if (p.equals("P0007")) {

                            Intent i = new Intent(getActivity(), MainTabActivity.class);
                            i.putExtra("TabIndex", "D_TAB");

                            startActivity(i);

                        } else if (p.equals("P0008")) {
                            if (user.UserID == 0) {

                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            } else {
                                Intent i = new Intent(getActivity(),
                                        MyCHK_Timeline.class);
                                i.putExtra("Page", "User_Foot");
                                startActivity(i);
                            }
                        } else if (p.equals("P0009")) {


                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(), SmallTalkAct.class);
                                i.putExtra("ID", ID);
                                startActivity(i);
                            }


                        } else if (p.equals("P0010")) {


                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        DoctorHomeActivity.class);
                                i.putExtra("DoctorID", ID);
                                startActivity(i);
                            }


                        } else if (p.equals("P0016")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        CHK_DoctorList.class);
                                i.putExtra("HostiptalID", ID);
                                i.putExtra("selectdoc", "select");
                                startActivity(i);
                            }

                        } else if (p.equals("P0011")) {

                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        Activity_ReplyPage.class);
                                i.putExtra("ID", ID);

                                startActivity(i);
                            }


                        } else if (p.equals("P0017")) {
//							https://lovestaging.topmd.cn/html/1.html
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            Intent i = new Intent(getActivity(),
                                    StaticWebView.class);
                            i.putExtra("ID", ID);

                            startActivity(i);


                        }

                    }

                });
        baby_ad.startImageCycle();

    }
    //初始化宝宝的数据

    private void initBirthData(){
        if(user==null){
            user = LocalAccessor.getInstance(getActivity()).getUser();
        }
        if(currentBirthDay==user.YuBirthDate){
            if (yuChan.Week < 40&&user.UserID != 0) {
                searchOrder();
            } else{
                check_infos.setVisibility(View.GONE);
                check_Adscontainer.setVisibility(View.VISIBLE);
                check_titletips.setText("");
                getHospital();
            }
            gotaday.setVisibility(View.INVISIBLE);
            return;
        }else{
            currentBirthDay = user.YuBirthDate;
        }
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = fmt.parse(user.YuBirthDate);
                selectday = fmt.parse(fmt.format(new Date()));
                leftdays = IOUtils.DaysCount(date);
                yuChan = IOUtils.WeekInfo(date);
                weektips_titleLabel.setText(yuChan.Week+"周提醒");
                per = (int) ((280 - leftdays) / 2.8);
                if (100 < per) {
                    per = 100;
                }
                if (per <= 0) {
                    per = 1;
                }
                selectdayindex= 0;
                if(leftdays>280){
                    selectdayindex = 280;
                }else if(leftdays<0){
                    selectdayindex=0;
                }else{
                    selectdayindex =280-leftdays;
                }
                babyGallery.setSelection(selectdayindex);
                todayGalleryindex = selectdayindex;

                if (yuChan.Week < 40&&user.UserID != 0) {
                    searchOrder();
                } else{
                    check_infos.setVisibility(View.GONE);
                    check_Adscontainer.setVisibility(View.VISIBLE);
                    check_titletips.setText("");
                    getHospital();
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    /*
	 * 查询订单信息 是为了获取产检预约模块的显示
	 */
    private void searchOrder() {
        if(user.UserID==0){                                                        //如果未登录
        userID=user.UserID;
        check_infos.setVisibility(View.GONE);
        check_Adscontainer.setVisibility(View.VISIBLE);
        check_titletips.setText("");
            getHospital();
            return;
        }
//        if(userID==user.UserID){                                                 //如果登录了，并且原来的id和现在的id一样，直接return。
//            return;
//        }
        check_infos.setVisibility(View.VISIBLE);
        check_Adscontainer.setVisibility(View.GONE);
        check_quickReservationContainer.setClickable(false);
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    if (result == null) {
                        check_infos.setVisibility(View.GONE);
                        check_Adscontainer.setVisibility(View.VISIBLE);
                        check_titletips.setText("");
                    } else {
                        Map<String, Object> map;
                        // 下边是具体的处理
                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("OrderInquiryForIndex");
                            if (array.getJSONObject(0).has("MessageCode")) {
                                // weektipsListView.setAdapter(null);
                                if (yuChan.Week < 11) {
                                    check_quickReservation.setBackgroundResource(R.color.yuyuebtncolor2);
                                    check_quickReservation.setText("待检查");
                                    check_quickReservationContainer.setClickable(false);
                                    reservationlabel
                                            .setText("妈妈正处于孕早期，如果没有特别的身体不适，此阶段不需要做频繁的孕期检查！");
                                } else {
                                    userID=user.UserID;
                                    check_infos.setVisibility(View.GONE);
                                    check_Adscontainer.setVisibility(View.VISIBLE);
                                    check_titletips.setText("");
                                }
                            } else {
                                // 如果已经做过检查，订单ID非空
                                if (array.getJSONObject(0).has("OrderID")) {
                                    for (int i = 0; i < array.length(); i++) {
                                        if (i == 0) {
                                            String time = array
                                                    .getJSONObject(i)
                                                    .getString("Gotime");
                                            int days = IOUtils.DaysCount2(time);
                                            if (0 <= days) {
                                                check_titletips
                                                        .setText(("还有"
                                                                + days
                                                                + "天就要进行"
                                                                + array.getJSONObject(
                                                                i)
                                                                .getString(
                                                                        "CHKType") + "了"));
                                                String label = array.getJSONObject(i).getString("HospitalName")+"		"+array.getJSONObject(i).getString("DoctorName")+
                                                        "\n		"+IOUtils.formatStrDate(array.getJSONObject(i).getString("Gotime"))+"	"+array.getJSONObject(i).getString("BeginTime")+"-"+array.getJSONObject(i).getString("EndTime");
                                                check_quickReservation.setText("待产检");
                                                check_quickReservation.setBackgroundResource(R.color.yuyuebtncolor1);
                                                reservationlabel
                                                        .setText(label);
                                                check_quickReservationContainer.setClickable(true);
                                                final String ID = array
                                                        .getJSONObject(i)
                                                        .getString("ID");
                                                check_quickReservationContainer.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // TODO Auto-generated
                                                        // method stub
                                                        Intent i = new Intent(
                                                                getActivity(),
                                                                MyCHK_OrderDetail.class);
                                                        i.putExtra("ID", ID);
                                                        startActivity(i);

                                                    }
                                                });
                                            }
                                        }
                                        break;
                                    }
                                }
                                // 如果没做过预约，ORderID为空
                                else {
                                    for (int i = 0; i < array.length(); i++) {

                                        check_quickReservation.setBackgroundResource(R.color.yuyuebtncolor3);
                                        check_quickReservation.setText("快速预约");
                                        final String CHKTypeName;
                                        if (array
                                                .getJSONObject(i)
                                                .getString("WeekStart")
                                                .equals(array.getJSONObject(i)
                                                        .getString("WeekEnd"))) {

                                            CHKTypeName = "第"
                                                    + array.getJSONObject(i)
                                                    .getString(
                                                            "WeekStart")
                                                    + "周产检";
                                        } else {
                                            CHKTypeName = "第("
                                                    + array.getJSONObject(i)
                                                    .getString(
                                                            "WeekStart")
                                                    + "-"
                                                    + array.getJSONObject(i)
                                                    .getString(
                                                            "WeekEnd")
                                                    + "周)产检";
                                        }
                                        String str001 = "您马上就该进行"
                                                + CHKTypeName
                                                + "了，点这里快速预约。";
                                        int bstart = str001
                                                .indexOf(CHKTypeName);
                                        int bend = bstart
                                                + CHKTypeName.length();
                                        SpannableString style = new SpannableString(
                                                str001);
                                        style.setSpan(
                                                new TextAppearanceSpan(getActivity(), R.style.stylecolor),
                                                bstart,
                                                bend,
                                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                        reservationlabel.setText(style);
                                        check_quickReservationContainer.setClickable(true);
                                        final String ID = array
                                                .getJSONObject(i)
                                                .getString("ID");
                                        check_quickReservationContainer.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated
                                                // method stub
                                                Intent i = new Intent(
                                                       getActivity(),
                                                        CHKTimeDetailActivity.class);
                                                i.putExtra("ID", ID);
                                                i.putExtra("CHKType",
                                                        CHKTypeName);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }
                            }
                            userID=user.UserID;                                                      //获取产检数据，确保产检信息显示
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            userID=user.UserID;
                            check_infos.setVisibility(View.GONE);
                            check_Adscontainer.setVisibility(View.VISIBLE);
                            check_titletips.setText("");

                        }
                    }
                    getHospital();
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
                    doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><UserID>%s</UserID><Week>%s</Week><HospitalID>%s</HospitalID></Request>";

            str = String.format(
                    str,
                    new Object[]{String.valueOf(user.UserID),
                            yuChan.Week+"", "1"});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(MMloveConstants.URL001, MMloveConstants.URL001+ MMloveConstants.OrderInquiryForIndex, MMloveConstants.OrderInquiryForIndex,
                    MMloveConstants.URL002, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 获取医院列表
     */
    private void getHospital() {
        if(hosList.size()==0){                                                         //如果hosList.size已经有数据了表示医院的AD绘制过了，直接显示就行了
            AppUtils.getHospital(getActivity(), new ComCallBack() {
                @Override
                public void onCallBack(Object obj) {
                    hosList = (List<Hospital>) obj;
                    for(Hospital hospital:hosList){
                        if(hospital.HospitalID==(user.HospitalID+"")&&hospital.SoundFlag.equals("1")){
                            rootView.findViewById(R.id.checkitem).setVisibility(View.VISIBLE);
                            check_more.setText("进入");
                            break;
                        }else{
                            rootView.findViewById(R.id.checkitem).setVisibility(View.GONE);
                            check_more.setText("查询");
                        }
                    }
                    showHospital();

                }
            });
        }else{
            for(Hospital hospital:hosList){
                if(hospital.HospitalID==(user.HospitalID+"")&&hospital.SoundFlag.equals("1")){
                    rootView.findViewById(R.id.checkitem).setVisibility(View.VISIBLE);
                    check_more.setText("进入");
                    break;
                }else{
                    rootView.findViewById(R.id.checkitem).setVisibility(View.GONE);
                    check_more.setText("查询");
                }
            }
            showHospital();
        }
    }
    private void showHospital() {
        if(check_Adscontainer.getVisibility()==View.GONE){
            return;
        }else if(checkads_hospital.getAdapter()!=null){                                       //如果已经绘制，直接return
            return;
        }
        List<View> views=new ArrayList<>();
        for (int i = 0; i < hosList.size(); i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.hospital_viewpager_layout,
                    null);
            ImageView iv_pic1 = (ImageView) view.findViewById(R.id.iv_pic1);
            ImageLoader.getInstance().displayImage(
                    hosList.get(i).getImageURL()+ "", iv_pic1);
            TextView tv_hosname1 = (TextView) view
                    .findViewById(R.id.tv_hosname1);
            final String hosName1 = hosList.get(i).HospitalName + "";
            tv_hosname1.setText(hosName1);
            final String hosID1 = hosList.get(i).HospitalID;
            iv_pic1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), CHKTimeList.class);
                    intent.putExtra("HospitalID", hosID1);
                    intent.putExtra("name", hosName1);
                    startActivity(intent);
                }
            });
            i++;
            ImageView iv_pic2 = (ImageView) view.findViewById(R.id.iv_pic2);
            TextView tv_hosname2 = (TextView) view
                    .findViewById(R.id.tv_hosname2);
            if (i < hosList.size()) {
                ImageLoader.getInstance().displayImage(
                        hosList.get(i).getImageURL() + "", iv_pic2);
                final String hosName2 = hosList.get(i).HospitalName + "";
                tv_hosname2.setText(hosName2);
                final String hosID2 = hosList.get(i).HospitalID;
                iv_pic2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                CHKTimeList.class);
                        intent.putExtra("HospitalID", hosID2);
                        intent.putExtra("name", hosName2);
                        startActivity(intent);
                    }
                });
            } else {
                iv_pic2.setVisibility(View.INVISIBLE);
                tv_hosname2.setVisibility(View.INVISIBLE);

            }
            views.add(view);
        }
        MyViewPageAdapter adapter = new MyViewPageAdapter(views);
        checkads_hospital.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.community_title:
            MainActivity activity = (MainActivity) getActivity();
            activity.gotab(1);
            break;
        case R.id.tosingn:
            if (!hasInternetConnected()) {
                Toast.makeText(getActivity(), "网络数据获取失败，请检查网络设置", 1).show();
                return;
            }
            // SignDialog(this);
            if (user.UserID != 0) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            break;
        case R.id.search:
            startActivity(new Intent(getActivity(), SearchWebView.class));
            break;
        case R.id.update_container:
            update();
            break;
        case R.id.nextdate_container:
            nextdate();
            break;
    }
    }

    private void TipsInquiry() {
        try {

            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    String resultObj = result.toString();
                    // {"TipsInquiry":[{"IsPic":"0","Sort":"99","Flag":"03","YunWeek":"4","Type":"","DeleteFlag":"0","LinkID":"","IsTop":"0","LinkTitle":"","Zhaiyao":"你好么","Title":"胎儿发育情况","Hits":"0","EndDate":"","StartDate":"","UpdatedDate":"2016\/1\/6
                    // 15:39:36","PicURL":"","CreatedDate":"2016\/1\/6
                    // 15:39:36","CreatedBy":"1","ID":"287","UpdatedBy":"1","Content":""}]}
                    if (resultObj == null) {
                        babay_tips.setText("");
                    } else {

                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO.getJSONArray("DevelopmentInquiry");

                            if (array.getJSONObject(0).has("MessageCode")) {
                                babay_tips.setText("");
                            } else {
                                String content = array.getJSONObject(0).getString(
                                        "Content");
                                babay_tips.setText(content);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
                    doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><Day>%s</Day></Request>";
            if(isAlready){
                str = String.format(str, new Object[]{"281"
                });
            }else {
                str = String.format(str, new Object[]{String.valueOf(childday)
                });
            }
            paramMap.put("str", str);
            // 必须是这5个参数，而且得按照顺序
            task.execute(MMloveConstants.URL001, MMloveConstants.URL001+ MMloveConstants.TipsInquiry,  MMloveConstants.TipsInquiry,
                    MMloveConstants.URL002, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取社区广告数据
    private void getCommunityAdsData(String date) {
        if(true) {
            return;
        }
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                if (result != null) {
                    JSONObject mySO = (JSONObject) result;
                    Map<String, Object> map;
                    try {
                        JSONArray array = mySO.getJSONArray("ADInquiry");
                        if (array.getJSONObject(0).has("MessageCode")) {
                            // Toast.makeText(getActivity(), "数据为空", 1).show();

                            //模拟怀孕社区
                            community_ads.setVisibility(View.GONE);
                        } else {

                            if (community_adimgurls != null && community_adimgurls.size() > 0) {
                                community_adimgurls.clear();
                            }

                            if (communityad_data != null && communityad_data.size() > 0) {
                                communityad_data.clear();
                            }

                            for (int i = 0; i < array.length(); i++) {

                                map = new HashMap<String, Object>();
                                map.put("ID",
                                        array.getJSONObject(i).getString("LinkID"));
                                map.put("Position",
                                        array.getJSONObject(i).getString("Position"));
                                map.put("ImageUr",
                                        (MMloveConstants.JE_BASE_URL + array
                                                .getJSONObject(i).getString(
                                                        "ImageUrl")).toString()
                                                .replace("~", "")
                                                .replace("\\", "/"));


                                community_adimgurls.add((MMloveConstants.JE_BASE_URL + array
                                        .getJSONObject(i).getString(
                                                "ImageUrl")).toString()
                                        .replace("~", "")
                                        .replace("\\", "/"));
                                communityad_data.add(map);

                            }

                            showCommunityAds();
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        // TODO Auto-generated method stub
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
                doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String str = "<Request><Date>%s</Date><ShowPosition>%s</ShowPosition></Request>";
        str = String.format(str, new Object[]{date, "03"});

        paramMap.put("str", str);
        task.execute(MMloveConstants.URL001, MMloveConstants.URL001+ MMloveConstants.Ads, MMloveConstants.Ads,
                MMloveConstants.URL002, paramMap);

    }
    //显示孕社区的广告
    private void showCommunityAds() {
        community_ads.setVisibility(View.VISIBLE);
        community_ads.setImageResources(community_adimgurls, communityad_data,
                new ImageCycleView.ImageCycleViewListener() {

                    @Override
                    public void displayImage(String imageURL,
                                             ImageView imageView) {
                        try {
                            ImageLoader.getInstance().displayImage(imageURL,
                                    imageView);
                            ViewGroup.LayoutParams para;
                            para = imageView.getLayoutParams();
                            para.width = displaywidth;
                            para.height = para.width * 2 / 5;
                            imageView.setLayoutParams(para);
                        } catch (OutOfMemoryError e) {
                            // TODO Auto-generated catch block

                        }

                    }

                    @Override
                    public void onImageClick(int position, View imageView,
                                             ArrayList<Map<String, Object>> mObject) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(getApplicationContext(),
                        // "type"+mType.get(position), 1).show();
                        String p = mObject.get(position).get("Position")
                                .toString();
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
                        // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                        if (p.equals("P0005")) {
                            Intent intent = new Intent(getActivity(),
                                    TestWebView.class);
                            intent.putExtra("YunWeek", yuChan.Week + 1 + "");
                            startActivity(intent);


                        } else if (p.equals("P0003") || p.equals("P0006") || p.equals("P0004")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        News_Detail.class);
                                i.putExtra("ID", ID);
                                startActivity(i);
                            }
                        } else if (p.equals("P0007")) {

                            Intent i = new Intent(getActivity(), MainTabActivity.class);
                            i.putExtra("TabIndex", "D_TAB");

                            startActivity(i);

                        } else if (p.equals("P0008")) {
                            if (user.UserID == 0) {

                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            } else {
                                Intent i = new Intent(getActivity(),
                                        MyCHK_Timeline.class);
                                i.putExtra("Page", "User_Foot");
                                startActivity(i);
                            }
                        } else if (p.equals("P0009")) {


                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(), SmallTalkAct.class);
                                i.putExtra("ID", ID);
                                startActivity(i);
                            }


                        } else if (p.equals("P0010")) {


                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        DoctorHomeActivity.class);
                                i.putExtra("DoctorID", ID);
                                startActivity(i);
                            }


                        } else if (p.equals("P0016")) {
                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        CHK_DoctorList.class);
                                i.putExtra("HostiptalID", ID);
                                i.putExtra("selectdoc", "select");
                                startActivity(i);
                            }

                        } else if (p.equals("P0011")) {

                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            int linkid = Integer.valueOf(ID);
                            if (linkid != 0) {
                                Intent i = new Intent(getActivity(),
                                        Activity_ReplyPage.class);
                                i.putExtra("ID", ID);

                                startActivity(i);
                            }


                        } else if (p.equals("P0017")) {

                            String ID = mObject.get(position).get("ID")
                                    .toString();
                            Intent i = new Intent(getActivity(),
                                    StaticWebView.class);
                            i.putExtra("ID", ID);

                            startActivity(i);


                        }

                    }

                });
        community_ads.startImageCycle();

    }
    private void getCommunityTalkData(){
        // TODO Auto-generated method stub
        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        String result = "";
                        try {
                            result = CustomHttpClient.getFromWebByHttpClient(getActivity(), MMloveConstants.URLWEB + "/molms/webservice/getTopic.jspx",
                                    new BasicNameValuePair("topiccount", "3"));
                            Map<String, Object> map;
                            com.alibaba.fastjson.JSONObject obj = JSON.parseObject(result);
                            com.alibaba.fastjson.JSONArray jsonArray = obj.getJSONArray("topicData");
                            communityTalkData = JSON.parseArray(jsonArray.toJSONString(),BBSTalk.class);

//                            for (int i = 0; i < jsonArray.size(); i++) {
//                                map = new HashMap<String, Object>();
//                                com.alibaba.fastjson.JSONObject jo = jsonArray.getJSONObject(i);
//                                map.put("title", jo.getString("title"));
//                                map.put("Name", jo.getString("bbsForumName"));
//                                map.put("username", jo.getString("username"));
//                                map.put("content", jo.getString("content"));
//                                map.put("avatar", jo.getString("avatar"));
//                                map.put("replyCount", jo.getString("replyCount"));
//                                map.put("viewCount", jo.getString("viewCount"));
//                                map.put("createTime", jo.getString("createTime"));
//                                map.put("YuBirthTime", jo.getString("YuBirthTime"));
//                                map.put("url", MMloveConstants.URLWEB + jo.getString("url"));
//                                communitydata.add(map);
//                            }
                            Message message = Message.obtain();
                            message.what = 1;
                            mHandler.sendMessage(message);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }
    /**
	 * 获取周提醒模块
	 */
	private void getweektips() {
		try {
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
				public void processJsonObject(Object result) {
					if (result == null) {
					} else {
						Map<String, Object> map;
						// 下边是具体的处理
						try {
							JSONObject mySO = (JSONObject) result;

							JSONArray array = mySO
									.getJSONArray("NewsInquiryIndex");


							if (array.getJSONObject(0).has("MessageCode")) {
								weektips_listView.setVisibility(View.GONE);
							} else {
//								for (int i = 0; i < array.length(); i++) {
//									map = new HashMap<String, Object>();
//									map.put("ID", array.getJSONObject(i)
//											.getString("ID"));
//									map.put("Title", array.getJSONObject(i)
//											.getString("Title"));
//									map.put("PicURL", array.getJSONObject(i)
//											.getString("PicURL"));
//									map.put("SubContent", array.getJSONObject(i)
//											.getString("SubContent"));
//									arrayList.add(map);
//								}
//								arrayList.size();
                                weeKTipsData = JSON.parseArray(array.toString(),WeeKTip.class);
								WeekTipsListAdapter1 adapter = new WeekTipsListAdapter1(
										getActivity(), weeKTipsData);
								weektips_listView.setAdapter(adapter);
//								setListViewHeightBasedOnChildren(weektipsListView);
							}

							// 添加listView点击事件
							weektips_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								public void onItemClick(AdapterView<?> arg0,
														View arg1, int arg2, long arg3) {
									Map<String, Object> mapDetail = new HashMap<String, Object>();
                                    WeeKTip tip = weeKTipsData.get(arg2);

									Intent i = new Intent(getActivity(),
											News_Detail.class);
									i.putExtra("ID", tip.ID);
									startActivity(i);
								}
							});

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
					doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><Week>%s</Week></Request>";

			str = String
					.format(str, new Object[]{yuChan.Week+""});

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(MMloveConstants.URL001, MMloveConstants.URL001+ MMloveConstants.NewsIndexInquiry, MMloveConstants.NewsIndexInquiry,
                    MMloveConstants.URL002, paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    //上一天
    private void update() {
        int temp =babyGallery.getSelectedItemPosition();
            if(temp>0){
               selectdayindex = temp-1;
            }
        babyGallery.setSelection(selectdayindex);
        adapterGallery.notifyDataSetChanged();
    }
    //下一天
    private void nextdate() {
        int temp =babyGallery.getSelectedItemPosition();
        if(temp<280){
            selectdayindex = temp+1;
        }
        babyGallery.setSelection(selectdayindex);
        adapterGallery.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectdayindex=position;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date enddate;
        try {
            enddate = fmt.parse(user.YuBirthDate);
            Date date = new Date();
            date.setTime(enddate.getTime()-(280-position)*24*60*60*1000L);
            setTitle(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(position==280){
            babyHeight.setText(lengths[279] + "mm");
            babyWeight.setText(weights[279] + "g");
            babyBirth_leftday.setText("0天");
        }else{
            babyHeight.setText(lengths[position] + "mm");
            babyWeight.setText(weights[position] + "g");
            babyBirth_leftday.setText((280 - position) + "天");
        }

        TipsInquiry(position+1);
        adapterGallery.notifyDataSetChanged();
        if(todayGalleryindex!=position){
            gotaday.setVisibility(View.VISIBLE);
            gotaday.startAnimation(gotadayAnimation);
        }else{
            gotaday.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /**
     * 获取宝宝当前的提示内容
     */
    private void TipsInquiry(int dayindex) {
        try {

            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    String resultObj = result.toString();
                    // {"TipsInquiry":[{"IsPic":"0","Sort":"99","Flag":"03","YunWeek":"4","Type":"","DeleteFlag":"0","LinkID":"","IsTop":"0","LinkTitle":"","Zhaiyao":"你好么","Title":"胎儿发育情况","Hits":"0","EndDate":"","StartDate":"","UpdatedDate":"2016\/1\/6
                    // 15:39:36","PicURL":"","CreatedDate":"2016\/1\/6
                    // 15:39:36","CreatedBy":"1","ID":"287","UpdatedBy":"1","Content":""}]}
                    if (resultObj == null) {
                        babay_tips.setText("");
                    } else {

                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO.getJSONArray("DevelopmentInquiry");

                            if (array.getJSONObject(0).has("MessageCode")) {
                                babay_tips.setText("");
                            } else {
                                String content = array.getJSONObject(0).getString(
                                        "Content");
                                babay_tips.setText(content);

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
                    doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><Day>%s</Day></Request>";
//            if(isAlready){
//                str = String.format(str, new Object[]{"281"
//                });
//            }else {
//                str = String.format(str, new Object[]{String.valueOf(dayindex)
//                } );
//            }
            str = String.format(str, new Object[]{String.valueOf(dayindex)
            } );
            paramMap.put("str", str);
            // 必须是这5个参数，而且得按照顺序
            task.execute(MMloveConstants.URL001, MMloveConstants.URL001
                            + MMloveConstants.TipsInquiry, MMloveConstants.TipsInquiry,
                    MMloveConstants.URL002, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (view.getId()){
                case R.id.feedlastitem:
                    FeedLastAdapter1.ViewHolder viewHolder = (FeedLastAdapter1.ViewHolder)view.getTag();
                    Intent i = new Intent(getActivity(), TopicWebView.class);
                    i.putExtra("url", viewHolder.url
                            + "?returnflag=appindex" + "&UserID=" + user.UserID
                            + "&YuBithdayTime=" + user.YuBirthDate + "&ClientID="
                            + ClientID);
                    getActivity().startActivity(i);
                    break;
            }
    }

    public class GalleryAdapter extends BaseAdapter {
        public List<YunWeekAll> list = null;
        public Context ctx = null;

        public GalleryAdapter(Context ctx, List<YunWeekAll> list) {
            this.list = list;
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ctx, R.layout.image_item, null);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.rlimg = (RelativeLayout) convertView.findViewById(R.id.rlimg);
                holder.tvcontent = (TextView) convertView.findViewById(R.id.tvcontent);
                holder.mRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBarright);
                holder.mRoundProgressBar.setText("");
                holder.mRoundProgressBar.setText2("");
                int height = ScreenUtils.getScreenHeight(getActivity());
                holder.mRoundProgressBar.getTextPaint1().setTextSize(28 * height / 720);
                holder.mRoundProgressBar.getTextPaint2().setTextSize(58 * height/ 720);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.image.setImageDrawable(list.get(position).getImage());
            holder.tvcontent.setText(list.get(position).getDay());

            if (selectdayindex == position) {
                int tempwidth =displaywidth / 3;
                int marginWidth = 0;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tempwidth, tempwidth);
                params.leftMargin=marginWidth;
                params.rightMargin=marginWidth;
                holder.rlimg.setLayoutParams(params);

//                if (position + 1 == 281) {
//                    childday = 280;
//                    isAlready=true;
//                } else {
//                    childday = position + 1;
//                    isAlready=false;
//                }
                holder.tvcontent.setTextSize(18);
                setText(holder.tvcontent, list.get(position).getDay(), 2);
                holder.image.setAlpha(250);
                holder.mRoundProgressBar.setAlpha(1.0f);
//                try {
//                    if(isAlready&&position + 1 == 281){
//                        Date date1 = fmt.parse(user.YuBirthDate);
//                        if((new Date().getTime())>(date1.getTime()+24*60*60*1000)){
//                            selectday = new Date();
//                        }else{
//                            selectday=fmt.parse(fmt.format(date1.getTime()+24*60*60*1000));
//                        }
//                        setTitle(selectday);
//                    }else{
//                        Date date1 = fmt.parse(user.YuBirthDate);
//                        String curr = fmt.format(IOUtils.getDayBefore(date1, "yyyy-MM-dd", (280 - childday)));
//                        setTitle(fmt.parse(curr));
//                        selectday = fmt.parse(curr);
//                    }
//                    babyHeight.setText(lengths[childday - 1] + "mm");
//                    babyWeight.setText(weights[childday - 1] + "g");
//                    babyBirth_leftday.setText((280 - childday) + "天");
//                    if (childday - 1 == 0) {
//                        updatecontainer.setVisibility(View.INVISIBLE);
//                        nextdatecontainer.setVisibility(View.VISIBLE);
//                    } else if (position + 1 == 281) {
//                        updatecontainer.setVisibility(View.VISIBLE);
//                        nextdatecontainer.setVisibility(View.INVISIBLE);
//                    } else {
//                        updatecontainer.setVisibility(View.VISIBLE);
//                        nextdatecontainer.setVisibility(View.VISIBLE);
//                    }
//                    if (position != 280) {
//                        if (childday == (280 - leftdays)) {
//                            gotaday.setVisibility(View.INVISIBLE);
//                        } else {
//                            gotaday.setVisibility(View.VISIBLE);
//                            gotaday.startAnimation(gotadayAnimation);
//                        }
//                    } else {
//                        gotaday.setVisibility(View.INVISIBLE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                isPost = true;
//                mHandler.sendEmptyMessageDelayed(1010, 50);
            } else {
//
                int tempwidth =displaywidth / 5;
                int marginWidth = displaywidth/12;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tempwidth, tempwidth);
                params.leftMargin=marginWidth;
                params.rightMargin=marginWidth;
                holder.rlimg.setLayoutParams(params);//否则正常
                holder.tvcontent.setTextSize(12);
                setText(holder.tvcontent, list.get(position).getDay(), 1);

                holder.image.setAlpha(100);
//                if(position==279){
//                    if(isAlready&&selectNum==280){
//                        holder.rlimg.setVisibility(View.INVISIBLE);
//                        holder.tvcontent.setVisibility(View.INVISIBLE);
//                    }else{
//                        holder.rlimg.setVisibility(View.VISIBLE);
//                        holder.tvcontent.setVisibility(View.VISIBLE);
//                    }
//                }
                holder.mRoundProgressBar.setAlpha(0.5f);
            }
            int per = (int) (childday / 2.8);
            if (100 < per) {
                per = 100;
            }
            if (per <= 0) {
                per = 1;
            }
            holder.mRoundProgressBar.setSweepAngle((int) (per * 3.6));
            holder.mRoundProgressBar.startCustomAnimation();
            return convertView;
        }

        private void setText(TextView tv, String content, int type) {
            if (content.indexOf("+") >= 0) {
                if (type == 1) {
                    String spantxtyun = content;
                    SpannableString styleyun = new SpannableString(
                            spantxtyun);
                    styleyun.setSpan(new TextAppearanceSpan(getActivity(), R.style.styleIndexSmallColor), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(styleyun);
                } else {
                    String spantxtyun = content;
                    SpannableString styleyun = new SpannableString(
                            spantxtyun);
                    styleyun.setSpan(new TextAppearanceSpan(getActivity(), R.style.styleBigColor), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(styleyun);
                }
            }
        }
    }

    class ViewHolder {
        ImageView image;
        RelativeLayout rlimg;
        RoundProgressBar mRoundProgressBar;
        TextView tvcontent;
    }
    private void setTitle(Date d) {
        DateFormat fmt2 = new SimpleDateFormat("M月d日");
        update.setText(fmt2.format(d.getTime() - 24 * 60 * 60 * 1000));
        currentdate.setText(fmt2.format(d.getTime()));
        nextdate.setText(fmt2.format(d.getTime() + 24 * 60 * 60 * 1000));
    }
}

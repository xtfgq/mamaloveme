package com.netlab.loveofmum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.ListItemClickHelp;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.community.CHK_medical_community;
import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.myadapter.CHK_DoctorList_Adapter;
import com.netlab.loveofmum.myadapter.CHK_TimeSelect_Adapter;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.wheel.ArrayWheelAdapter;
import com.netlab.loveofmum.wheel.OnWheelScrollListener;
import com.netlab.loveofmum.wheel.WheelView;
import com.netlab.loveofmum.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class CHK_TimeSelect extends BaseActivity implements ListItemClickHelp {

    private TextView txtHead, tv_chktiem;
    private ImageView imgBack;

    private TextView txtDocName;
    private TextView txtHosName;
    private TextView txtTitle;

    private TextView txt_timeselect_001;

    private Button btn_timeselect;

    private String DoctorID;
    private String DoctorName;
    private String HospitalName;
    private String HospitalID = "1";
    private String Price;
    private String DoctorTitle;

    private String DoctorNO;
    private String DepartNO;
    private String SchemaID;

    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

    private List<Map<String, Object>> arrayList2;
    private String returnvalue001;
    private GridView gridview;
    private WheelView catalogWheel;

    // 初始化时汉字会挤压，在汉字左右两边添加空格即可
    private String[] names;
    private CHK_TimeSelect_Adapter adapter;

    private Map<String, Object> map;

    private CircleImageView img001;

    private String ImageURL;

    private Boolean isHasDefault = false;

    private ProgressBar bar;
    private FrameLayout layout_progress;
    private RelativeLayout rlchek;

    // 定义Web Service相关的字符串
    private final String SOAP_NAMESPACE = MMloveConstants.URL001;
    private final String SOAP_ACTION = MMloveConstants.URL001
            + MMloveConstants.DoctorTimeInquiry;
    private final String SOAP_METHODNAME = MMloveConstants.DoctorTimeInquiry;
    private final String SOAP_URL = MMloveConstants.URL002;
    private final String SOAP_ACTION4 = MMloveConstants.URL001
            + MMloveConstants.DoctorTimeInquiry3;
    private final String SOAP_METHODNAME4 = MMloveConstants.DoctorTimeInquiry3;
    private Boolean isStop = false;
    private String yuyue = "";
    // 根据传过来的参数ID
    private String CHKTypeID;
    private String CHKTypeName2;

    private String CHKItemValue;
    private Double CHKFEE = 0.0;
    private User user;
    private Order order;
    LocalAccessor local;


    // 得到孕周
    private final String SOAP_YUNWEEKMETHOD = MMloveConstants.CHKInquiryForWeek;
    private final String SOAP_YUNWEEKACTION = MMloveConstants.URL001
            + MMloveConstants.CHKInquiryForWeek;
    Yuchan yu;
    private final String SOAP_ACTION3 = MMloveConstants.URL001
            + MMloveConstants.CHKTypeItemsInquiry;
    private final String SOAP_METHODNAME3 = MMloveConstants.CHKTypeItemsInquiry;
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int current = catalogWheel.getCurrentItem();
//            if(Integer.valueOf(HospitalID)==1){
                if(false){
                tv_chktiem.setText(arrayList2.get(current).get("TimeStart").toString() );
            }else {
                tv_chktiem.setText(arrayList2.get(current).get("TimeStart").toString() + "-" + arrayList2.get(current).get("TimeEnd").toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.layout_topi);
        MyApplication.getInstance().addActivity(this);
        user = LocalAccessor.getInstance(CHK_TimeSelect.this).getUser();
        iniView();

        setListeners();
        if (hasInternetConnected()) {
            searchCHK_TimeSelect();
        } else {
            Toast.makeText(CHK_TimeSelect.this, R.string.msgUninternet,
                    Toast.LENGTH_SHORT).show();
        }
        // InitData();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
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
        // TODO Auto-generated method stub
        txtHead = (TextView) findViewById(R.id.txtHead);
        imgBack = (ImageView) findViewById(R.id.img_left);

        txtHead.setText("选择时间");

        layout_progress = (FrameLayout) findViewById(R.id.layout_progress);
        bar = (ProgressBar) findViewById(R.id.progressBar1);

        txtDocName = (TextView) findViewById(R.id.doctor_name);
        txtTitle = (TextView) findViewById(R.id.doctor_title);
        txtHosName = (TextView) findViewById(R.id.hos_name);
        DoctorID = this.getIntent().getExtras().getString("DoctorID")
                .toString();
        DoctorName = this.getIntent().getExtras().getString("DoctorName")
                .toString();
        txtDocName.setText(DoctorName);
        txtTitle.setText(this.getIntent().getExtras().getString("DoctorTitle")
                .toString());
        HospitalName = this.getIntent().getExtras().getString("HospitalName")
                .toString();
        txtHosName.setText(HospitalName);

        DoctorNO = this.getIntent().getExtras().getString("DoctorNO")
                .toString();

        DepartNO = this.getIntent().getExtras().getString("DepartNO")
                .toString();
        HospitalID = this.getIntent().getExtras().getString("HospitalID")
                .toString();
        Price = this.getIntent().getExtras().getString("Price").toString();

        DoctorTitle = this.getIntent().getExtras().getString("DoctorTitle")
                .toString();
        if (this.getIntent().getExtras().getString("yuyueDoc") != null) {
            yuyue = this.getIntent().getExtras().getString("yuyueDoc");
        }


        gridview = (GridView) findViewById(R.id.grid_timeselect);

        catalogWheel = (WheelView) findViewById(R.id.wheel);

        catalogWheel.setVisibility(View.GONE);

        btn_timeselect = (Button) findViewById(R.id.btn_timeselect);

        btn_timeselect.setVisibility(View.GONE);

        txt_timeselect_001 = (TextView) findViewById(R.id.txt_timeselect_001);

        img001 = (CircleImageView) findViewById(R.id.img001_item_chk_doctorlist);

        ImageURL = this.getIntent().getExtras().getString("ImageURL")
                .toString();
        rlchek = (RelativeLayout) findViewById(R.id.rlchek);
        tv_chktiem = (TextView) findViewById(R.id.tv_chktiem);

        // FinalBitmap fb =
        // FinalBitmap.create(CHK_TimeSelect.this);//初始化FinalBitmap模块

        ImageLoader.getInstance().displayImage(
                MMloveConstants.JE_BASE_URL2
                        + ImageURL.replace("~", "").replace("\\", "/"), img001,
                ImageOptions.getFaceListOptions());
        // fb.display(img001,Constants.JE_BASE_URL2 + ImageURL.replace("~",
        // "").replace("\\", "/"),75,90);

//        User user = LocalAccessor.getInstance(CHK_TimeSelect.this).getUser();
        catalogWheel.addScrollingListener(scrollListener);
        rlchek.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(yuyue)) {
            local = new LocalAccessor(CHK_TimeSelect.this,
                    MMloveConstants.ORDERINFO);
            order = local.getOrder();
            user.HospitalID = Integer.valueOf(HospitalID);
            user.HospitalName = HospitalName;

            try {
                LocalAccessor.getInstance(CHK_TimeSelect.this).updateUser(user);
                getWeek();


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//		HospitalID = String.valueOf(user.HospitalID);
    }

    private void setListeners() {
        imgBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        btn_timeselect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (Integer.valueOf(HospitalID) == 1) {
                if (false) {
                    int current = catalogWheel.getCurrentItem();
                    LocalAccessor local = new LocalAccessor(
                            CHK_TimeSelect.this, MMloveConstants.ORDERINFO);
                    Intent i = new Intent(CHK_TimeSelect.this,
                            CHK_OrderSure.class);
                    Order order = local.getOrder();

                    order.DoctorID = DoctorID;
                    order.DoctorName = DoctorName;
                    order.HospitalID = HospitalID;
                    order.HospitalName = HospitalName;
                    order.DepartNO = DepartNO;
                    order.DoctorNO = DoctorNO;
                    order.DoctorTime = arrayList2.get(current)
                            .get("ScheduleTime").toString();
                    order.TimeStart = arrayList2.get(current)
                            .get("TimeStart").toString();
                    order.TimeEnd ="";


                    order.DeptCode = arrayList2.get(current)
                            .get("DeptCode").toString();

                    order.DeptName = arrayList2.get(current)
                            .get("DeptName").toString();

                    order.SchemaID = arrayList2.get(current)
                            .get("ScheduleID").toString();

                    order.GuaFee = Price;
                    order.DoctorTitle = DoctorTitle;
                    order.Price = Price;
                    order.ImageURL = ImageURL;
                    try {
                        local.updateOrder(order);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    startActivity(i);
                } else {
                    if (!isStop) {
                        // TODO Auto-generated method stub
                        int current = catalogWheel.getCurrentItem();
                        int leftcount = 0;
                        if (!arrayList2.get(current).get("LeftNumberCount")
                                .toString().equals("")) {
                            leftcount = Integer.valueOf(arrayList2.get(current)
                                    .get("LeftNumberCount").toString());
                        }
                        if (leftcount > 0) {
                            Intent i = new Intent(CHK_TimeSelect.this,
                                    CHK_OrderSure.class);

                            LocalAccessor local = new LocalAccessor(
                                    CHK_TimeSelect.this, MMloveConstants.ORDERINFO);

                            Order order = local.getOrder();

                            order.DoctorID = DoctorID;
                            order.DoctorName = DoctorName;
                            order.HospitalID = HospitalID;
                            order.HospitalName = HospitalName;
                            order.DepartNO = DepartNO;
                            order.DoctorNO = DoctorNO;
                            order.DoctorTime = arrayList2.get(current)
                                    .get("ScheduleTime").toString();
                            order.TimeStart = arrayList2.get(current)
                                    .get("TimeStart").toString();
                            order.TimeEnd = arrayList2.get(current).get("TimeEnd")
                                    .toString();

                            order.DeptCode = arrayList2.get(current)
                                    .get("DeptCode").toString();

                            order.DeptName = arrayList2.get(current)
                                    .get("DeptName").toString();

                            order.SchemaID = arrayList2.get(current)
                                    .get("ScheduleID").toString();

                            order.GuaFee = Price;
                            order.DoctorTitle = DoctorTitle;
                            order.Price = Price;
                            order.ImageURL = ImageURL;

                            try {
                                local.updateOrder(order);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            startActivity(i);
                        } else {
                            Toast.makeText(CHK_TimeSelect.this,
                                    "此时间段号源已满，请更换其它时间段", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CHK_TimeSelect.this,
                                "此时间段已停诊，请更换其它时间段", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /*
     * 查询gridview
     */
    private void searchCHK_TimeSelect() {
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    returnvalue001 = result.toString();
                    Boolean isNull = false;
                    // [{"MessageContent":"该医生暂无坐诊时间！","MessageCode":"2"}]}
                    // {"DoctorTimeInquiry":[{"DeptCode":"8430","Flag":"0","NumberCount":"40","TimeStart":"8:00","LeftNumberCount":"38","ScheduleTimeContent":"","ScheduleID":"1056215","DeptName":"产科一门诊","ScheduleStatus":"","DoctorID":"54","IsByTime":"0","DoctorNO":"050266","TimeEnd":"12:00","ScheduleTime":"2016\/1\/8
                    // 00:00:00"},{"DeptCode":"8430","Flag":"1","NumberCount":"40","TimeStart":"8:00","LeftNumberCount":"40","ScheduleTimeContent":"","ScheduleID":"1066893","DeptName":"产科一门诊","ScheduleStatus":"","DoctorID":"54","IsByTime":"0","DoctorNO":"050266","TimeEnd":"12:00","ScheduleTime":"2016\/1\/11
                    // 00:00:00"},{"DeptCode":"8430","Flag":"0","NumberCount":"40","TimeStart":"8:00","LeftNumberCount":"39","ScheduleTimeContent":"","ScheduleID":"1057494","DeptName":"产科一门诊","ScheduleStatus":"","DoctorID":"54","IsByTime":"0","DoctorNO":"050266","TimeEnd":"12:00","ScheduleTime":"2016\/1\/12
                    // 00:00:00"},{"DeptCode":"8430","Flag":"0","NumberCount":"40","TimeStart":"8:00","LeftNumberCount":"40","ScheduleTimeContent":"","ScheduleID":"1059204","DeptName":"产科一门诊","ScheduleStatus":"","DoctorID":"54","IsByTime":"0","DoctorNO":"050266","TimeEnd":"12:00","ScheduleTime":"2016\/1\/15
                    // 00:00:00"}]}
//                    layout_progress.setVisibility(View.GONE);
                    if (returnvalue001 == null) {

                    } else {
                        // 下边是具体的处理
                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("DoctorTimeInquiry");

                            if (array.getJSONObject(0).has("MessageCode")) {
                                if (array.getJSONObject(0)
                                        .getString("MessageCode").equals("1")) {
                                    Toast.makeText(CHK_TimeSelect.this,array.getJSONObject(0)
                                            .getString("MessageContent"),Toast.LENGTH_SHORT).show();
                                    isNull = true;
                                    return;
                                }
                                if (array.getJSONObject(0)
                                        .getString("MessageCode").equals("2")) {
                                    Toast.makeText(CHK_TimeSelect.this,array.getJSONObject(0)
                                            .getString("MessageContent"),Toast.LENGTH_SHORT).show();
                                    isNull = true;
                                    return;
                                }
                                if (array.getJSONObject(0)
                                        .getString("MessageCode").equals("3")) {
                                    Toast.makeText(CHK_TimeSelect.this,array.getJSONObject(0)
                                            .getString("MessageContent"),Toast.LENGTH_SHORT).show();
                                    isNull = true;
                                    return;
                                }
                            }

                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "yyyy-MM-dd");

                            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                            Date start;
                            Date end;
                            //判断下午2点以后不能预约后天的
//                            if(Integer.valueOf(HospitalID)==1){
                            if(false){
                                start = curDate;
                            }else {
                                if (IOUtils.isOutoftwo(curDate)) {
                                    start = IOUtils.getNextDay(curDate, 2);
                                } else {
                                    start = IOUtils.getNextDay(curDate, 1);
                                }
                            }
                            end = IOUtils.getNextDay(start, 7);
                            String str = formatter.format(curDate);

                            DateFormat df = DateFormat.getDateTimeInstance(
                                    DateFormat.FULL, DateFormat.FULL,
                                    Locale.CHINA);
                            Boolean isDefault = false;
                            // if(array.getJSONObject(0).getString("MessageCode")
                            // .equals("2")){
                            for (int i = 0; i < 7; i++) {
                                Date dnow = IOUtils.getNextDay(start, i);
                                String strNow = formatter.format(dnow);
                                map = new HashMap<String, Object>();
                                map.put("Week", IOUtils.getWeekOfDate(dnow));
                                map.put("Day", IOUtils.getDayOfDate(dnow));
                                map.put("Status", "0");
                                map.put("IsDefault", "0");
                                map.put("Date", strNow);
                                if (isNull) {
                                    String content = array.getJSONObject(0)
                                            .getString("MessageContent");

                                    Toast.makeText(CHK_TimeSelect.this,
                                            content, 1).show();
                                } else {
                                    for (int k = 0; k < array.length(); k++) {
                                        String time = array.getJSONObject(k)
                                                .getString("ScheduleTime");
                                        // int leftcount =
                                        // Integer.valueOf(array.getJSONObject(k)
                                        // .getString("LeftNumberCount"));

                                        time = time.split(" ")[0].replace("/",
                                                "-");
                                        time = formatter.format(formatter
                                                .parse(time));
                                        if (time.equals(strNow)) {
                                            map.put("Status", "1");
                                            if (!isDefault) {
                                                map.put("IsDefault", "1");
                                                isDefault = true;
                                                isHasDefault = true;
                                            }
                                            break;

                                            // if(leftcount>0)
                                            // {
                                            //
                                            // }
                                            // else if(leftcount==0)
                                            // {
                                            // map.put("Status", "2");
                                            // }
                                        }
                                    }
                                }

                                arrayList.add(map);
                            }
                            // }

                            // SimpleAdapter adapter = new SimpleAdapter(
                            // CHKTimeInfo.this, arrayList,
                            // R.layout.item_chktimeinfo,
                            // new Object[] { "ItemName","ItemContent" },
                            // new int[] {
                            // R.id.chktime_name,R.id.chktime_description });
                            // listview.setAdapter(adapter);
                            // listview.setEnabled(true);
                            //
                            adapter = new CHK_TimeSelect_Adapter(
                                    CHK_TimeSelect.this, arrayList,
                                    CHK_TimeSelect.this);
                            gridview.setAdapter(adapter);

                            if (isNull) {
                                SimpleDateFormat formatter2 = new SimpleDateFormat(
                                        "yyyy年MM月");
                                txt_timeselect_001.setText(formatter2
                                        .format(start));
                                btn_timeselect.setVisibility(View.GONE);

                            } else {
                                InitData();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();

                            Toast.makeText(CHK_TimeSelect.this,
                                    "因该医院预约系统升级，暂无法提供预约服务，敬请谅解！", 1).show();
                        }
                    }
                }
            };
            layout_progress.setVisibility(View.VISIBLE);
            JsonAsyncTask_Info task = new JsonAsyncTask_Info(
                    CHK_TimeSelect.this, true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><ScheduleTime></ScheduleTime><DoctorNO>%s</DoctorNO><DepartNO>%s</DepartNO></Request>";

            str = String.format(str, new Object[]{DoctorID, HospitalID,
                    DoctorNO, DepartNO});
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
    public void onClick(View item, View widget, int position, int which) {
        // TODO Auto-generated method stub
        switch (which) {
            case R.id.btn001_item_chktime_hour:
                // inputTitleDialog();
                catalogWheel.setVisibility(View.VISIBLE);
                rlchek.setVisibility(View.VISIBLE);
                Map<String, Object> map2;
                JSONObject mySO;
                final int p = position;
//                if (Integer.valueOf(HospitalID) == 1) {
                if (false) {
                    try {
                        mySO = new JSONObject(returnvalue001);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy年MM月");
                        String strNow = arrayList.get(position).get("Date").toString();
                        txt_timeselect_001.setText(formatter2.format(formatter
                                .parse(strNow)));
                       final  String ScheduleTime=strNow;
                        arrayList2 = new ArrayList<Map<String, Object>>();
                        try {
                            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                                public void processJsonObject(Object result) {
                                    try {
                                        JSONObject mySO2 = (JSONObject) result;
                                        JSONArray array2 = mySO2
                                                .getJSONArray("DoctorTimeInquiry");
                                        Map<String, Object> map3;

                                        for (int k = 0; k < array2.length(); k++) {
                                            map3 = new HashMap<String, Object>();


                                            map3.put("ScheduleID", array2.getJSONObject(k)
                                                    .getString("ScheduleID"));
                                            map3.put("LeftNumberCount", array2.getJSONObject(k)
                                                    .getString("LeftNumberCount"));
                                            map3.put("TimeStart",
                                                    array2.getJSONObject(k).getString("TimeStart"));
                                            map3.put("TimeEnd",
                                                    array2.getJSONObject(k).getString("TimeEnd"));
                                            map3.put("ScheduleTime",ScheduleTime);

                                            map3.put("DeptCode",
                                                    array2.getJSONObject(k).getString("DeptCode"));
                                            map3.put("DeptName",
                                                    array2.getJSONObject(k).getString("DeptName"));

                                            arrayList2.add(map3);

                                        }
                                        names = new String[arrayList2.size()];
                                        for (int i = 0; i < arrayList2.size(); i++) {
                                            names[i] = arrayList2.get(i).get("TimeStart").toString();
                                        }

                                        catalogWheel.setVisibleItems(5);
                                        // catalogWheel.setCyclic(true);

                                        ArrayWheelAdapter<String> array3 = new ArrayWheelAdapter<String>(
                                                CHK_TimeSelect.this, names);
                                        catalogWheel.setViewAdapter(array3);
                                        catalogWheel.setMinimumHeight(600);
                                        catalogWheel.setCurrentItem(0);
                                        tv_chktiem.setText(arrayList2.get(0).get("TimeStart").toString());
                                        adapter.setSelectItem(p);
                                        // 刷新listView
                                        adapter.notifyDataSetChanged();
                                        layout_progress.setVisibility(View.GONE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            layout_progress.setVisibility(View.VISIBLE);
                            JsonAsyncTask_Info task = new JsonAsyncTask_Info(
                                    CHK_TimeSelect.this, true, doProcess);
                            Map<String, Object> paramMap = new HashMap<String, Object>();
                            String str = "<Request><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><ScheduleTime>%s</ScheduleTime><DoctorNO>%s</DoctorNO><DepartNO>%s</DepartNO></Request>";

                            str = String.format(str, new Object[]{DoctorID, HospitalID, strNow,
                                    DoctorNO, DepartNO});
                            paramMap.put("str", str);

                            // 必须是这5个参数，而且得按照顺序
                            task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
                                    SOAP_URL, paramMap);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        mySO = new JSONObject(returnvalue001);
                        JSONArray array = mySO.getJSONArray("DoctorTimeInquiry");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy年MM月");
                        String strNow = arrayList.get(position).get("Date").toString();
                        // String strNow = formatter.format(dnow);
                        txt_timeselect_001.setText(formatter2.format(formatter
                                .parse(strNow)));
                        arrayList2 = new ArrayList<Map<String, Object>>();
                        for (int k = 0; k < array.length(); k++) {
                            map2 = new HashMap();
                            String time = array.getJSONObject(k).getString(
                                    "ScheduleTime");
                            int leftcount = 0;
                            if (!array.getJSONObject(k).getString("LeftNumberCount")
                                    .equals("")) {
                                leftcount = Integer.valueOf(array.getJSONObject(k)
                                        .getString("LeftNumberCount"));
                            } else {

                            }
                            time = time.split(" ")[0].replace("/", "-");
                            time = formatter.format(formatter.parse(time));

                            if (time.equals(strNow)) {
                                if (Integer.valueOf(array.getJSONObject(k)
                                        .getString("Flag").toString()) > 0) {
                                    map2.put(
                                            "Timespan",
                                            array.getJSONObject(k).getString(
                                                    "TimeStart")
                                                    + "-"
                                                    + array.getJSONObject(k).getString(
                                                    "TimeEnd") + "（停诊）");

                                    isStop = true;


                                } else {
                                    if (leftcount > 0) {
                                        map2.put(
                                                "Timespan",
                                                array.getJSONObject(k).getString(
                                                        "TimeStart")
                                                        + "-"
                                                        + array.getJSONObject(k)
                                                        .getString("TimeEnd") + "         号源:" + array.getJSONObject(k).getString(
                                                        "NumberCount") + "人" + "      剩余:" + leftcount + "人");
                                    } else {
                                        map2.put(
                                                "Timespan",
                                                array.getJSONObject(k).getString(
                                                        "TimeStart")
                                                        + "-"
                                                        + array.getJSONObject(k)
                                                        .getString("TimeEnd")
                                                        + "（已满）");
                                    }
                                    isStop = false;
                                }
                                map2.put("ScheduleTime", time);
                                map2.put("ScheduleID", array.getJSONObject(k)
                                        .getString("ScheduleID"));
                                map2.put("LeftNumberCount", array.getJSONObject(k)
                                        .getString("LeftNumberCount"));
                                map2.put("TimeStart",
                                        array.getJSONObject(k).getString("TimeStart"));
                                map2.put("TimeEnd",
                                        array.getJSONObject(k).getString("TimeEnd"));
                                map2.put("Flag",
                                        array.getJSONObject(k).getString("Flag"));

                                map2.put("DeptCode",
                                        array.getJSONObject(k).getString("DeptCode"));
                                map2.put("DeptName",
                                        array.getJSONObject(k).getString("DeptName"));

                                arrayList2.add(map2);
                            }
                        }

                        names = new String[arrayList2.size()];
                        for (int i = 0; i < arrayList2.size(); i++) {
                            names[i] = arrayList2.get(i).get("Timespan").toString();
                        }

                        catalogWheel.setVisibleItems(5);
                        // catalogWheel.setCyclic(true);

                        ArrayWheelAdapter<String> array2 = new ArrayWheelAdapter<String>(
                                CHK_TimeSelect.this, names);
                        catalogWheel.setViewAdapter(array2);
                        catalogWheel.setMinimumHeight(600);
                        catalogWheel.setCurrentItem(0);
                        tv_chktiem.setText(arrayList2.get(0).get("TimeStart").toString() + "-" + arrayList2.get(0).get("TimeEnd").toString());
                        adapter.setSelectItem(position);
                        // 刷新listView
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }
                }
                break;
            case R.id.img002_item_chkdetail_list:

                break;
            case R.id.img003_item_chkdetail_list:

                break;
            default:
                break;
        }
    }


    private void InitData() {
        if (!isHasDefault) {
            catalogWheel.setVisibility(View.GONE);
            rlchek.setVisibility(View.GONE);
            return;
        }
        catalogWheel.setVisibility(View.GONE);
        rlchek.setVisibility(View.VISIBLE);
        arrayList2 = new ArrayList<Map<String, Object>>();
//        if (Integer.valueOf(HospitalID) == 1) {
        if (false) {
            JSONObject mySO;
            int pos = -1;
            String isDefault;
            for (int n = 0; n < arrayList.size(); n++) {
                isDefault = arrayList.get(n).get("IsDefault").toString();
                if (isDefault.equals("1")) {
                    pos = n;
                    break;
                }
            }
            try {
                mySO = new JSONObject(returnvalue001);
                JSONArray array = mySO.getJSONArray("DoctorTimeInquiry");
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "yyyy-MM-dd");
                SimpleDateFormat formatter2 = new SimpleDateFormat(
                        "yyyy年MM月");
                String strNow = arrayList.get(pos).get("Date").toString();

                txt_timeselect_001.setText(formatter2.format(formatter
                        .parse(strNow)));
                final int p = pos;


                InitSanfuyuan(strNow);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } else {
            for (int i = 0; i < arrayList.size(); i++) {
                String isDefault = arrayList.get(i).get("IsDefault").toString();

                if (isDefault.equals("1")) {
                    Map<String, Object> map2;
                    JSONObject mySO;
                    try {
                        mySO = new JSONObject(returnvalue001);
                        JSONArray array = mySO.getJSONArray("DoctorTimeInquiry");
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        SimpleDateFormat formatter2 = new SimpleDateFormat(
                                "yyyy年MM月");
                        String strNow = arrayList.get(i).get("Date").toString();

                        txt_timeselect_001.setText(formatter2.format(formatter
                                .parse(strNow)));
                        // String strNow = formatter.format(dnow);
                        for (int k = 0; k < array.length(); k++) {
                            map2 = new HashMap();
                            String time = array.getJSONObject(k).getString(
                                    "ScheduleTime");
                            int leftcount = 0;
                            if (!array.getJSONObject(k)
                                    .getString("LeftNumberCount").equals("")) {
                                leftcount = Integer.valueOf(array.getJSONObject(k)
                                        .getString("LeftNumberCount"));
                            } else {

                            }
                            time = time.split(" ")[0].replace("/", "-");
                            time = formatter.format(formatter.parse(time));

                            if (time.equals(strNow)) {
                                if (Integer.valueOf(array.getJSONObject(k)
                                        .getString("Flag").toString()) > 0) {
                                    map2.put(
                                            "Timespan",
                                            array.getJSONObject(k).getString(
                                                    "TimeStart")
                                                    + "-"
                                                    + array.getJSONObject(k).getString(
                                                    "TimeEnd") + "（停诊）");

                                } else {
                                    if (leftcount > 0) {
                                        map2.put(
                                                "Timespan",
                                                array.getJSONObject(k).getString(
                                                        "TimeStart")
                                                        + "-"
                                                        + array.getJSONObject(k)
                                                        .getString("TimeEnd") + "         号源:" + array.getJSONObject(k).getString(
                                                        "NumberCount") + "人" + "      剩余:" + leftcount + "人");
                                    } else {
                                        map2.put(
                                                "Timespan",
                                                array.getJSONObject(k).getString(
                                                        "TimeStart")
                                                        + "-"
                                                        + array.getJSONObject(k)
                                                        .getString("TimeEnd")
                                                        + "（已满）");
                                    }

                                }

                                map2.put("ScheduleTime", time);
                                map2.put("ScheduleID", array.getJSONObject(k)
                                        .getString("ScheduleID"));
                                map2.put("LeftNumberCount", array.getJSONObject(k)
                                        .getString("LeftNumberCount"));
                                map2.put("TimeStart", array.getJSONObject(k)
                                        .getString("TimeStart"));
                                map2.put("TimeEnd", array.getJSONObject(k)
                                        .getString("TimeEnd"));

                                map2.put("DeptCode", array.getJSONObject(k)
                                        .getString("DeptCode"));
                                map2.put("DeptName", array.getJSONObject(k)
                                        .getString("DeptName"));

                                arrayList2.add(map2);
                            }
                        }


                            names = new String[arrayList2.size()];
                            for (int j = 0; j < arrayList2.size(); j++) {
                                names[j] = arrayList2.get(j).get("Timespan").toString();
                            }
                            if (Integer.valueOf(array.getJSONObject(0)
                                    .getString("Flag").toString()) > 0) {
                                isStop = true;
                            } else {
                                isStop = false;
                            }
                        catalogWheel.setVisibility(View.VISIBLE);
                            catalogWheel.setVisibleItems(5);
                            // catalogWheel.setCyclic(true);
                            // catalogWheel
                            // .setAdapter(new ArrayWheelAdapter<String>(names));
                            ArrayWheelAdapter<String> array2 = new ArrayWheelAdapter<String>(
                                    CHK_TimeSelect.this, names);
                            catalogWheel.setViewAdapter(array2);
                            catalogWheel.setMinimumHeight(600);

                            tv_chktiem.setText(arrayList2.get(0).get("TimeStart").toString() + "-" + arrayList2.get(0).get("TimeEnd").toString());
                            btn_timeselect.setVisibility(View.VISIBLE);
                        layout_progress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }
                }
           }
        }
    }

    private void getWeek() {
        JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

            @Override
            public void processJsonObject(Object result) {
                // TODO Auto-generated method stub
                String rs = result.toString();


                Order order = new Order();

                JSONObject mySO = (JSONObject) result;
                JSONArray array;
                try {
                    array = mySO
                            .getJSONArray("CHKInquiryForWeek");
                    CHKTypeID = array.getJSONObject(0)
                            .getString("ID");

                    int sweek = Integer.parseInt(array
                            .getJSONObject(0)
                            .getString("WeekStart"));
                    int eweek = Integer.parseInt(array
                            .getJSONObject(0).getString("WeekEnd"));

                    if (eweek > sweek) {
                        CHKTypeName2 =
                                "第("
                                        + array.getJSONObject(0)
                                        .getString(
                                                "WeekStart")
                                        + "-"
                                        + array.getJSONObject(0)
                                        .getString(
                                                "WeekEnd")
                                        + "周)产检";

                    } else if (eweek == sweek) {

                        CHKTypeName2 =
                                "第"
                                        + array.getJSONObject(0)
                                        .getString(
                                                "WeekStart")
                                        + "周产检";
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                searchCHKTimeItems();

            }
        };
        JsonAsyncTask_Info task = new JsonAsyncTask_Info(this, true, doProcess);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        yu = new Yuchan();
        Date date;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String yubirth = user.YuBirthDate;
            date = fmt.parse(user.YuBirthDate);
            yu = IOUtils.WeekInfo(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String str = "<Request><Week>%s</Week></Request>";
        if (yu.Week > 39) {
            str = String.format(str, new Object[]{"40",});
        } else {
            str = String.format(str, new Object[]{yu.Week + 1 + "",});
        }
        paramMap.put("str", str);
        task.execute(SOAP_NAMESPACE, SOAP_YUNWEEKACTION,
                SOAP_YUNWEEKMETHOD, SOAP_URL, paramMap);
    }

    private void searchCHKTimeItems() {
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {

                    CHKItemValue = result.toString();

                    if (returnvalue001 == null) {

                    } else {
                        Map<String, Object> map;
                        // 下边是具体的处理
                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("CHKTypeItemsInquiry");

                            if (array.getJSONObject(0).has("MessageCode")) {
                                if (array.getJSONObject(0).getString("MessageCode")
                                        .equals("0")) {

                                }
                            } else {
                                for (int i = 0; i < array.length(); i++) {


                                    if ("可预约".equals(array.getJSONObject(i)
                                            .getString("Value"))) {
                                        if (!array.getJSONObject(i).getString("ItemPrice").equals("")) {
                                            CHKFEE += Double.valueOf(array
                                                    .getJSONObject(i).getString(
                                                            "ItemPrice"));
                                        }
                                    }
                                }
//							LocalAccessor local = new LocalAccessor(CHK_DoctorList.this,
//									MMloveConstants.ORDERINFO);
                                order.CHKTypeID = Integer.valueOf(CHKTypeID);
                                order.CHKItemValue = CHKItemValue;
                                order.CHKFee = String.valueOf(CHKFEE);
                                order.CHKTypeName = CHKTypeName2;
                                try {
                                    local.updateOrder(order);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(
                    CHK_TimeSelect.this, true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><CHKTypeID>%s</CHKTypeID><HospitalID>%s</HospitalID></Request>";

            str = String.format(str, new Object[]
                    {CHKTypeID, String.valueOf(HospitalID)});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
                    SOAP_URL, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void InitSanfuyuan(String strNow) {
        try {
            final String ScheduleTime=strNow;
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    try {
                        JSONObject mySO2 = (JSONObject) result;
                        JSONArray array2 = mySO2
                                .getJSONArray("DoctorTimeInquiry");
                        Map<String, Object> map3;

                        for (int k = 0; k < array2.length(); k++) {
                            map3 = new HashMap<String, Object>();


                            map3.put("ScheduleID", array2.getJSONObject(k)
                                    .getString("ScheduleID"));
                            map3.put("LeftNumberCount", array2.getJSONObject(k)
                                    .getString("LeftNumberCount"));
                            map3.put("TimeStart",
                                    array2.getJSONObject(k).getString("TimeStart"));
                            map3.put("TimeEnd",
                                    array2.getJSONObject(k).getString("TimeEnd"));
                            map3.put("ScheduleTime",ScheduleTime);

                            map3.put("DeptCode",
                                    array2.getJSONObject(k).getString("DeptCode"));
                            map3.put("DeptName",
                                    array2.getJSONObject(k).getString("DeptName"));

                            arrayList2.add(map3);

                        }
                        names = new String[arrayList2.size()];
                        for (int i = 0; i < arrayList2.size(); i++) {
                            names[i] = arrayList2.get(i).get("TimeStart").toString();
                        }
                        catalogWheel.setVisibility(View.VISIBLE);
                        catalogWheel.setVisibleItems(5);
                        // catalogWheel.setCyclic(true);

                        ArrayWheelAdapter<String> array3 = new ArrayWheelAdapter<String>(
                                CHK_TimeSelect.this, names);
                        catalogWheel.setViewAdapter(array3);
                        catalogWheel.setMinimumHeight(600);
                        catalogWheel.setCurrentItem(0);
                        tv_chktiem.setText(arrayList2.get(0).get("TimeStart").toString());

                        // 刷新listView
                        adapter.notifyDataSetChanged();
                        btn_timeselect.setVisibility(View.VISIBLE);
                        layout_progress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            layout_progress.setVisibility(View.VISIBLE);
            JsonAsyncTask_Info task = new JsonAsyncTask_Info(
                    CHK_TimeSelect.this, true, doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><ScheduleTime>%s</ScheduleTime><DoctorNO>%s</DoctorNO><DepartNO>%s</DepartNO></Request>";

            str = String.format(str, new Object[]{DoctorID, HospitalID, strNow, DoctorNO, DepartNO});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
                    SOAP_URL, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

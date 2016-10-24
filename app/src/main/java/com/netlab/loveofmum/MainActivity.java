package com.netlab.loveofmum;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.netlab.loveofmum.Fragment.Fragment1;
import com.netlab.loveofmum.Fragment.Fragment2;
import com.netlab.loveofmum.Fragment.Fragment3;
import com.netlab.loveofmum.Fragment.Fragment4;
import com.netlab.loveofmum.activity.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 蔺子岭
 *	功能描述：自定义TabHost
 */
public class MainActivity extends BaseActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {Fragment1.class,Fragment2.class,Fragment3.class,Fragment4.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab1,R.drawable.tab2,R.drawable.tab3,
            R.drawable.tab4};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "孕社区", "私人医生", "我的"};
    View[] tabtitles;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_main);
        versioninquiry();
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        //得到fragment的个数
        int count = fragmentArray.length;
        tabtitles = new View[count];
        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            View item = getTabItemView(i);
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(item);
            //将Tab按钮添加进Tab选项卡中
            tabtitles[i] =item;
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for(View v:tabtitles){
                    TextView textView = (TextView) v.findViewById(R.id.textview);

                    if(v==mTabHost.getCurrentTabView()){
                        textView.setTextColor(getResources().getColor(R.color.pink));
                    }else{
                        textView.setTextColor(getResources().getColor(R.color.tabscolor));
                    }
                }
            }
        });
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        if(index==0){
            textView.setTextColor(getResources().getColor(R.color.pink));
        }else{
            textView.setTextColor(getResources().getColor(R.color.tabscolor));
        }
        return view;
    }
    private void versioninquiry() {
//        try {
//            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
//                public void processJsonObject(Object result) {
//                  String resultstr = result.toString();
//                    if (resultstr == null) {
//
//                    } else {
//                        Map<String, Object> map;
//                        // 下边是具体的处理
//                        try {
//                            JSONObject mySO = (JSONObject) result;
//                            JSONArray array = mySO
//                                    .getJSONArray("VersionInquiry");
//
//                            if (array.getJSONObject(0).has("MessageCode")) {
//
//                            } else {
//                                for (int i = 0; i < array.length(); i++) {
//                                    newCode = Integer.valueOf(array
//                                            .getJSONObject(i).getString(
//                                                    "VersionID"));
//                                    int VersionFlag = Integer.valueOf(array
//                                            .getJSONObject(i).getString(
//                                                    "VersionFlag"));
//
//                                    PackageManager manager = MainActivity.this
//                                            .getPackageManager();
//                                    PackageInfo info = manager.getPackageInfo(
//                                            MainActivity.this.getPackageName(), 0);
//                                    String appVersion = info.versionName; // 版本名
//                                    oldCode = info.versionCode; // 版本号
//
//                                    if (oldCode < newCode) {
//                                        // SharedPreferences prefs =
//                                        // Index.this.getSharedPreferences("version",
//                                        // Context.MODE_PRIVATE);
//                                        //
//                                        // if(prefs.getInt("Version", 0) ==
//                                        // newCode)
//                                        // {
//                                        // versionlog
//                                        // }
//                                        // else
//                                        // {
//                                        versionlog = array.getJSONObject(i)
//                                                .getString("VersionLog")
//                                                .replace("@", "\n");
//                                        if (VersionFlag == 1) {
//
//                                            showForceUpdate();
//                                        } else {
//                                            showUpdateDialog();
//                                        }
//                                        // }
//                                    }
//                                }
//                            }
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//            };
//
//            JsonAsyncTask_Info task = new JsonAsyncTask_Info(MainActivity.this, true,
//                    doProcess);
//            Map<String, Object> paramMap = new HashMap<String, Object>();
//            String str = "<Request></Request>";
//
//            paramMap.put("str", str);
//
//            // 必须是这5个参数，而且得按照顺序
//            task.execute(SOAP_NAMESPACE, SOAP_ACTION3, SOAP_METHODNAME3,
//                    SOAP_URL, paramMap);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

//    private void showForceUpdate() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View layout = inflater.inflate(R.layout.dialog_default_ensure, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(layout);
//        builder.setCancelable(false);
//        builder.create().show();
//        TextView tvok = (TextView) layout.findViewById(R.id.dialog_default_click_ensure);
//        TextView tvtitel = (TextView) layout.findViewById(R.id.dialog_default_click_text_title);
//        tvtitel.setText("检测到新版本");
//        TextView tvcontent = (TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
//        tvcontent.setText(versionlog);
//        tvok.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//                        showMissingPermissionDialog(this);
//                        return;
//                    }
//                }
//
//                final Intent it = new Intent(MainActivity.this,
//                        DownloadServiceForAPK.class);
//                startService(it);
//
//            }
//        });
//    }
//
//    private void showUpdateDialog() {
//        DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
//                Index.this).setDialogMsg("检测到新版本", versionlog, "下载")
//                .setOnClickListenerEnsure(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//                                showMissingPermissionDialog(Index.this);
//                                return;
//                            }
//                        }
//                        final Intent it = new Intent(Index.this,
//                                DownloadServiceForAPK.class);
//                        startService(it);
//
//                    }
//                });
//        DialogUtils.showSelfDialog(Index.this, dialogEnsureCancelView);
//        // AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        // builder.setTitle("检测到新版本");
//        // builder.setMessage(versionlog);
//        // builder.setPositiveButton("下载", new DialogInterface.OnClickListener()
//        // {
//
//    }
}
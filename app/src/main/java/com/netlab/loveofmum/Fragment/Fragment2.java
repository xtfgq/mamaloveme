package com.netlab.loveofmum.fragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.News_Detail;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.SimpleWebView;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.IOUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import im.delight.android.webview.AdvancedWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends BaseFragment implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView=null;

    private View vTop;
    private User user;
    private long mExitTime;
    private MyApplication myApplication;
    String ClientID;
    // 定义Web Service相关的字符串
    private final String SOAP_NAMESPACE = MMloveConstants.URL001;
    private final String SOAP_ACTION = MMloveConstants.URL001 + MMloveConstants.UserLogin;
    private final String SOAP_METHODNAME = MMloveConstants.UserLogin;
    private final String SOAP_URL = MMloveConstants.URL002;
    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("action.login")) {
                ClientID = PushManager.getInstance().getClientid(getActivity());
                user=LocalAccessor.getInstance(getActivity()).getUser();
                String url =  MMloveConstants.URLWEB+"/molms/index.jhtml?redirectUrl=forum"+ "&UserID=" + user.UserID
                        + "&YuBirthTime=" + user.YuBirthDate + "&ClientID="
                        + ClientID;
//				String url =  MMloveConstants.URLWEB+"redirectUrl=forum"+ "&UserID=" + user.UserID
//						+ "&BithdayTime=" + user.YuBirthDate + "&ClientID="
//					+ ClientID;
                mWebView.loadUrl(url);

            }
        }

    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2, container, false);
        mWebView = (AdvancedWebView) v.findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();

        mWebView.setWebViewClient(new MyWebViewClient());

        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);

        myApplication = MyApplication.getInstance();
        user= LocalAccessor.getInstance(getActivity()).getUser();
        PushManager.getInstance().initialize(getActivity().getApplicationContext());
        ClientID = PushManager.getInstance().getClientid(getActivity());
        String url;
        if(TextUtils.isEmpty(myApplication.urls)) {
            url= MMloveConstants.URLWEB + "/molms/index.jhtml?redirectUrl=forum" + "&UserID=" + user.UserID
                    + "&YuBirthTime=" + user.YuBirthDate + "&ClientID="
                    + ClientID;
        }else{
            url=myApplication.urls;
        }
        mWebView.loadUrl(url);


//		社区入口：http://125.46.31.185:8080/molms/index.jhtml?redirectUrl=forum
//			特卖：http://125.46.31.185:8080/molms/index.jhtml?redirectUrl=shop
//			积分商城入口：http://125.46.31.185:8080/molms/index.jhtml?redirectUrl=commodity
        mWebView.setListener(getActivity(), this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.login");
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        return  v;
    }

    @Override
    public void initData() {

    }

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public void onClick(View v) {

    }
    class MyWebViewClient extends WebViewClient {
        // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



//					http://125.46.31.185:8080/molms/loginSuccess.jhtml?momSuccess=true&res=molms/r/cms/momloveme/mobile&location=http://125.46.31.185:8080/molms/login.jspx?username=15637147870&password=123abc&locale=zh_CN&_start_time=1456541982820&resSys=/molms/r/cms
//			if (url.contains("http://momloveme/?returnflag=commall")||url.contains("http://momloveme/?returnflag=shop")||url.contains("http://momloveme/?returnflag=mytopic")||url.contains("http://momloveme/?returnflag=appindex")) {
//				if(view.canGoBack()){
//					view.goBack();
//				}
//
//			}else {
//				if(url.contains("https://lovestaging.topmd.cn/WebServices/NewsShare.aspx?")){
//					Intent i=new Intent(getActivity(),News_Detail.class);
//					int beginIndex = url.indexOf("ID=");
//					String ID=url.substring(beginIndex+3,url.length());
//					i.putExtra("ID",ID);
//					startActivity(i);
//
//				}else {
//					String ss = url.toString();
//					try {
//						ss = new String(URLDecoder.decode(ss, "iso8859-1").getBytes("iso8859-1"), "utf-8");
//					} catch (UnsupportedEncodingException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					String urls = "";
//					try {
//
//						if (ss.contains("momSuccess=true")) {
//							int beginIndex = 0;
//							int endIndex = 0;
//							beginIndex = ss.indexOf("username");
//							endIndex = ss.indexOf("locale") - 1;
//							urls = ss.substring(beginIndex, endIndex);
//
//							urls = urls.replace("&", "=").trim();
//							String[] strs = urls.split("\\=");
//							String username = strs[1];
//							String pass = ss.substring(ss.indexOf("password=") + 9, ss.indexOf("locale") - 1);
////						String decryptString= Des.encode("123456");
////						Log.i("vvvv",decryptString);
//
//							pass = Des.decode(pass);
//							UserLogin(username, pass);
//						}
//						if(!url.contains("/molms/forum.jhtml")) {
//							Intent i = new Intent(getActivity(),SimpleWebView.class);
//							i.putExtra("url", url);
//							startActivity(i);
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
            if(!url.contains("/molms/forum.jhtml")) {
                if(url.contains("https://lovestaging.topmd.cn/WebServices/NewsShare.aspx?")) {
                    Intent i=new Intent(getActivity(),News_Detail.class);
                    int beginIndex = url.indexOf("ID=");
                    String ID=url.substring(beginIndex+3,url.length());
                    i.putExtra("ID",ID);
                    startActivity(i);
                }else if(url.contains("/molms/index.jhtml")){
                    view.loadUrl(url);
                }
                else {
                    Intent i = new Intent(getActivity(), SimpleWebView.class);
                    i.putExtra("url", url);
                    startActivity(i);
                }
            }
            else{
                view.loadUrl(url);
            }

//					view.loadUrl(url);
//				}


//			}
            return true;

        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			handler.cancel();
            handler.proceed();

        }
    }


//    @Override
//    protected void onDestroy() {
//
//        mWebView.onDestroy();
//        try {
//            getActivity(). unregisterReceiver(mRefreshBroadcastReceiver);
//
//            mRefreshBroadcastReceiver = null;
//        } catch (Exception e) {
//        }
////		try {
////			unregisterReceiver(mReceiver);
////
////			mReceiver = null;
////		} catch (Exception e) {
////		}
//        // ...
//        super.onDestroy();
//    }

    private void UserLogin(String uername, String pass)
    {
        try
        {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
            {
                public void processJsonObject(Object result)
                {
                    String Phone_Login = result.toString();
                    if (Phone_Login == null)
                    {

                    }
                    else
                    {
                        // 下边是具体的处理
                        try
                        {
                            JSONObject mySO = (JSONObject) result;
                            org.json.JSONArray array = mySO
                                    .getJSONArray("UserLogin");

                            if (array.getJSONObject(0).has("MessageCode"))
                            {
                                Toast.makeText(getActivity(),array.getJSONObject(0).getString("MessageContent").toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String UserID = array.getJSONObject(0).getString("UserID").toString();

                                User user = LocalAccessor.getInstance(getActivity()).getUser();
                                user.UserID = Integer.valueOf(UserID);
                                user.NickName = array.getJSONObject(0).getString("NickName").toString();
                                if(!array.getJSONObject(0).getString("YuBirthTime").toString().equals(""))
                                {
                                    user.YuBirthDate = IOUtils.formatStrDate(array.getJSONObject(0).getString("YuBirthTime").toString());

//								user.YuBirthDate = IOUtils.formatStrDate("2016-06-13");
                                }



                                String url=array.getJSONObject(0).getString("PictureURL").toString().replace("~", "").replace("\\", "/");
                                if(url==null){
                                    url=MMloveConstants.JE_BASE_URL3+"/img/icon_user_normal.png";

                                }else if(url.contains("vine.gif")){
                                    int end=url.lastIndexOf("/");
                                    url=url.substring(0, end);
                                    url=MMloveConstants.JE_BASE_URL3+url+"/icon_user_normal.png";
                                }else{
                                    url=MMloveConstants.JE_BASE_URL3+url;
                                }
                                user.PicURL =url;
                                user.RealName = array.getJSONObject(0).getString("RealName").toString();
                                user.UserMobile = array.getJSONObject(0).getString("Mobile").toString();
                                user.UserNO = array.getJSONObject(0).getString("UserNO").toString();

                                LocalAccessor.getInstance(getActivity()).updateUser(user);

                            }

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();

                        }
                    }
                }
            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(getActivity(), true,
                    doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><UserMobile>%s</UserMobile><Password>%s</Password><ClientID>%s</ClientID><YuBirthTime>%s</YuBirthTime><PhoneType>%s</PhoneType><DeviceToken>%s</DeviceToken></Request>";
            str = String.format(str, new Object[]
                    { uername, pass,ClientID,user.YuBirthDate,"0",""});
            paramMap.put("str", str);

            // 必须是这5个参数，而且得按照顺序
            task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
                    SOAP_URL, paramMap);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//    @SuppressLint("NewApi")
//    @Override
    public void onPause()  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }
        super.onPause();
        mWebView.onPause();
//        MobclickAgent.onPause(getActivity());
    }
//    @SuppressLint("NewApi")
//    @Override
    public void onResume() {


        super.onResume();
        mWebView.onResume();
//        MobclickAgent.onResume(this);
    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (!mWebView.onBackPressed()) { return true; }
//        }
//
//        return getActivity().onKeyDown(keyCode, event);
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        mWebView.onActivityResult(requestCode, resultCode, intent);
//
//    }
    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) { }

    @Override
    public void onExternalPageRequest(String url) { }

}

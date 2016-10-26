package com.netlab.loveofmum;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.utils.Des;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.sharesdk.framework.ShareSDK;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Administrator on 2016/5/26.
 */
public class SearchWebView extends Activity {
    private WebView mWebView = null;
    private TextView txtHead;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.layout_wb);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setWebViewClient(new WebViewClient());
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        mWebView.setWebViewClient(new MyWebViewClient());
        String url =MMloveConstants.JE_BASE_URL+"CanEat/FoodCategrey.aspx";
        mWebView.loadUrl(url);

//        txtHead = (TextView) findViewById(R.id.txtHead);
//
//        imgBack = (ImageView) findViewById(R.id.img_left);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                // 返回键退回
//                if (mWebView.canGoBack()) {
//                    mWebView.goBack();
//                } else {
//                    finish();
//                }
//            }
//        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title);
//                if(!TextUtils.isEmpty(title)) {
//                    if (title.length() > 10) {
//                        title = title.substring(0, 9) + "...";
//                    }
//                }
//                txtHead.setText(title);
            }
        });


    }
    class MyWebViewClient extends WebViewClient {
        // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



//					http://125.46.31.185:8080/molms/loginSuccess.jhtml?momSuccess=true&res=molms/r/cms/momloveme/mobile&location=http://125.46.31.185:8080/molms/login.jspx?username=15637147870&password=123abc&locale=zh_CN&_start_time=1456541982820&resSys=/molms/r/cms
            if (url.contains("back")) {
              finish();

            }else{

                view.loadUrl(url);
            }
            return true;

        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			handler.cancel();
            handler.proceed();

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 返回键退回
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }  else {
               finish();


            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

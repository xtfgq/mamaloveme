package com.netlab.loveofmum.widget;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.User_Set;
import com.netlab.loveofmum.alipay.PayActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.ShareModel;
import com.netlab.loveofmum.myadapter.ShareAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.netlab.loveofmum.model.ShareModel;
 import com.netlab.loveofmum.myadapter.ShareAdapter;
import com.netlab.loveofmum.utils.ImageUtil;
import com.netlab.loveofmum.utils.SystemUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SharePopupWindow extends PopupWindow {

    private Context mcontext;

    private ShareParams shareParams;

//    Button btn_cancel;

    public SharePopupWindow(Context cx) {
        this.mcontext = cx;

    }





    public void showShareWindow() {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.share_layout,null);
        GridView gridView = (GridView) view.findViewById(R.id.share_gridview);
        ShareAdapter adapter = new ShareAdapter(mcontext);
        gridView.setAdapter(adapter);




        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明

        // 设置SelectPicPopupWindow弹出窗体的背景ffb0c9
        ColorDrawable dw = new ColorDrawable(0xffeeeeee);

        this.setBackgroundDrawable(dw);


        gridView.setOnItemClickListener(new ShareItemClickListener(this));

    }

    private class ShareItemClickListener implements OnItemClickListener {
        private PopupWindow pop;

        public ShareItemClickListener(PopupWindow pop) {
            this.pop = pop;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            share(position);
            pop.dismiss();

        }
    }
    public  void showShare(final Context context,
                                 String platform, boolean isDialog) {
        final OnekeyShare oks = new OnekeyShare();
        if (platform != null) {
            oks.setPlatform(platform);
        }

        if (isDialog) {
            // 令编辑页面显示为Dialog模式
            oks.setDialogMode();
        }


        // 关闭sso授权

        oks.disableSSOWhenAuthorize();
        if(platform.equals(Wechat.NAME)){

            oks.setTitle(shareParams.getTitle());
        }else if(platform.equals(WechatMoments.NAME)){
            oks.setTitle(shareParams.getText());
        }
        else {

            oks.setTitle(shareParams.getTitle());  //最多30个字符
        }
        // text是分享文本：所有平台都需要这个字段
        if(platform.equals(SinaWeibo.NAME)){
            oks.setText(shareParams.getText()+shareParams.getUrl());
//           oks.setImagePath(ImageUtil.saveResTolocal(mcontext.getResources(),R.drawable.banner3,"mmlove_banner"));
        }else{
            oks.setText(shareParams.getText());
            oks.setImagePath(shareParams.getImagePath());//网络图片rul
        }




        oks.setUrl(shareParams.getUrl());
        oks.setSite(shareParams.getTitle());
        oks.setSiteUrl(shareParams.getUrl());
        oks.setComment(shareParams.getText());


        // Url：仅在QQ空间使用
        oks.setTitleUrl(MMloveConstants.JE_BASE_URL+"Share/index.html");  //网友点进链接后，可以看到分享的详情



        // 启动分享GUI
        oks.show(context);

    }

    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {


        switch (position){
            case 0:
               if(isQQClientAvailable(mcontext)){
                   showShare(mcontext,QQ.NAME,true);}
                else{
                   Toast.makeText(mcontext,"请安装qq",1).show();
               }

                break;
            case 1:
                if(isQQClientAvailable(mcontext)){
                    showShare(mcontext,QZone.NAME,true);
                }
                else{
                    Toast.makeText(mcontext,"请安装qq",1).show();
                }

                break;
            case 2:
                showShare(mcontext,Wechat.NAME,true);
                break;
            case 3:
                showShare(mcontext,WechatMoments.NAME,true);
                break;
            case 4:
                showShare(mcontext,SinaWeibo.NAME,true);
//                showSina();
                break;


        }
    }



    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

//    /**
//     * sina 分享
//     */
//    private void showSina() {
//        mcontext.startActivity(new Intent(mcontext,FakeSinaActivity.class));
//
//    }

    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            ShareParams sp = new ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setTitle(shareModel.getTitle());
            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImagePath(shareModel.getImgPath());
            shareParams = sp;
        }
    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = QQ.NAME;
//                platform = "Wechat";
                break;
            case 1:
                platform = QZone.NAME;
                break;
            case 2:
                platform = Wechat.NAME;
                break;
            case 3:
                platform = WechatMoments.NAME;
                break;
            case 4:
                platform = SinaWeibo.NAME;
                break;

        }
        return platform;
    }


//    /**
//     * 分享到QQ空间
//     */
//    private void qzone() {
//        ShareParams sp = new ShareParams();
//        sp.setTitle(shareParams.getTitle());
//        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
//        sp.setText(shareParams.getText());
//        sp.setImagePath(shareParams.getImagePath());
//        sp.setComment("我对此分享内容的评论");
//        sp.setSite(shareParams.getUrl());
//        sp.setSiteUrl(shareParams.getUrl());
//        sp.setUrl(shareParams.getUrl());
//        Platform qzone = ShareSDK.getPlatform(context, "QZone");
//
//        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //
//        // 执行图文分享
//        qzone.share(sp);
//    }
//
//    private void qq() {
//        ShareParams sp = new ShareParams();
//        sp.setTitle(shareParams.getTitle());
//
//        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接
//        sp.setUrl(shareParams.getUrl());
//        sp.setText(shareParams.getText());
//        sp.setImageUrl(shareParams.getImageUrl());
//        sp.setComment("我对此分享内容的评论");
//        sp.setSite(shareParams.getUrl());
//        sp.setSiteUrl(shareParams.getUrl());
//        Platform qq = ShareSDK.getPlatform(context, "QQ");
//        qq.setPlatformActionListener(platformActionListener);
//        qq.share(sp);
//    }
//    private void sina() {
//        ShareParams sp = new ShareParams();
//        sp.setTitle("妈妈爱我");  //最多30个字符
//        sp.setText(shareParams.getText()+shareParams.getUrl());  //最多40个字符
//
//
//        sp.setImagePath(shareParams.getImagePath());
////        sp.setImageUrl(MMloveConstants.JE_BASE_URL+"img/icon.jpg");//网络图片rul
//
//        // url：仅在微信（包括好友和朋友圈）中使用
//        sp.setUrl(MMloveConstants.JE_BASE_URL+"Share/index.html");//网友点进链接后，可以看到分享的详情
//        Platform sina = ShareSDK.getPlatform(context, SinaWeibo.NAME);
//        sina.setPlatformActionListener(platformActionListener);
//        sina.share(sp);
//    }
//
//
//    /**
//     * 分享到短信
//     */
//    private void shortMessage() {
//        ShareParams sp = new ShareParams();
//        sp.setAddress("");
//        sp.setText(shareParams.getText()+"这是网址《"+shareParams.getUrl()+"》很给力哦！");
//
//        Platform circle = ShareSDK.getPlatform(context, "ShortMessage");
//        circle.setPlatformActionListener(platformActionListener); // 设置分享事件回调
//        // 执行图文分享
//        circle.share(sp);
//    }

}

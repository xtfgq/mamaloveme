package com.netlab.loveofmum;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.timepicker.Util;
import com.netlab.loveofmum.utils.ScreenUtils;
import com.netlab.loveofmum.widget.RoundProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mersens on 2016/7/22.
 */
public class MyFragment extends LazyFragment {
    private View mView;
    public static final String length = "lenth";
    public static final String Weight = "weight";
    public static final String imgBack="img";
    public static final String day="day";
    private RoundProgressBar mRoundProgressBar,mLeft,mRight;
    private String tvlength,tvweight;
    private int img,childday;

    private TextView mTextLenth,mTextWeight,tvleft,tvright,tvcenter,tvday,txtcontent;
    private ImageView mImagview,ivprogress,ivchildleft,ivchildright;
    private boolean isPrepared;

    private Index mActivity;
    private final String SOAP_NAMESPACE = MMloveConstants.URL001;
    private final String SOAP_TIPSACTION = MMloveConstants.URL001
            + MMloveConstants.TipsInquiry;
    private final String SOAP_TIPSMETHODNAME = MMloveConstants.TipsInquiry;
    private final String SOAP_URL = MMloveConstants.URL002;
    private Animation animation1 = null;

    private int[] weekIcons = new int[] {R.drawable.week1,R.drawable.week2,R.drawable.week3,R.drawable.week4,R.drawable.week5,R.drawable.week6,R.drawable.week7,R.drawable.week8,
            R.drawable.week9,R.drawable.week10,R.drawable.week11,R.drawable.week12,R.drawable.week13,R.drawable.week14,R.drawable.week15,R.drawable.week16,R.drawable.week17,R.drawable.week18,
            R.drawable.week19,R.drawable.week20,R.drawable.week21,R.drawable.week22,R.drawable.week23,R.drawable.week24,R.drawable.week25,R.drawable.week26,R.drawable.week27,R.drawable.week28,
            R.drawable.week29,R.drawable.week30,R.drawable.week31,R.drawable.week32,R.drawable.week33,R.drawable.week34,R.drawable.week35,R.drawable.week36,R.drawable.week37,R.drawable.week38,R.drawable.week39,R.drawable.week40};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_view_child, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        mView = view;
        tvlength = getArguments().getString(length);
        tvweight=getArguments().getString(Weight);
        img=getArguments().getInt(imgBack);
        childday=getArguments().getInt(day);

        lazyLoad();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = (Index) activity;
        } catch (Exception e) {
            Log.e("MyFragment", "类型转换错误：" + e.getMessage().toString());
        }
    }

    public static Fragment getInstance(String lenth, String weight, int drawable, int childay) {
        Bundle b = new Bundle();
        b.putString(length, lenth);
        b.putString(Weight,weight);
        b.putInt(imgBack,drawable);
        b.putInt(day,childay);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }

        mImagview=(ImageView)mView.findViewById(R.id.ivprogress);
        ivchildleft=(ImageView)mView.findViewById(R.id.ivchildleft);
        ivchildright=(ImageView)mView.findViewById(R.id.ivchildright);
        tvcenter=(TextView)mView.findViewById(R.id.tvcenter);
        tvleft=(TextView)mView.findViewById(R.id.tvleft);
        tvright=(TextView)mView.findViewById(R.id.tvright);

        txtcontent=(TextView)mView.findViewById(R.id.txt_index_content);
        ViewGroup.LayoutParams para;

		para =  mImagview.getLayoutParams();
		para.width = Util.getScreenWidth(getActivity())*25/88;
		para.height = Util.getScreenWidth(getActivity())*25/88;
        mImagview.setLayoutParams(para);

        mRoundProgressBar=(RoundProgressBar)mView.findViewById(R.id.roundProgressBar);
        mLeft=(RoundProgressBar)mView.findViewById(R.id.roundProgressBarleft);
        mRight=(RoundProgressBar) mView.findViewById(R.id.roundProgressBarright);
        mLeft.setText("");
        mLeft.setText2("");
        mLeft.getTextPaint1().setTextSize(28 * ScreenUtils.getScreenHeight(getActivity()) / 720);
        mLeft.getTextPaint2().setTextSize(58 * ScreenUtils.getScreenHeight(getActivity()) / 720);

        mRoundProgressBar.setText("");
        mRoundProgressBar.setText2("");
        mRoundProgressBar.getTextPaint1().setTextSize(28 * ScreenUtils.getScreenHeight(getActivity()) / 720);
        mRoundProgressBar.getTextPaint2().setTextSize(58 * ScreenUtils.getScreenHeight(getActivity()) / 720);

        mRight.setText("");
        mRight.setText2("");
        mRight.getTextPaint1().setTextSize(28 * ScreenUtils.getScreenHeight(getActivity()) / 720);
        mRight.getTextPaint2().setTextSize(58 * ScreenUtils.getScreenHeight(getActivity()) / 720);

//        mTextLenth.setText(tvlength+"mm");
//        mTextWeight.setText(tvweight+"g");


        setTextView(tvleft,childday-1,ivchildleft,1);
        setTextView(tvcenter,childday,mImagview,2);
        setTextView(tvright,childday+1,ivchildright,1);
        ivchildleft.setVisibility(View.VISIBLE);
        mImagview.setVisibility(View.VISIBLE);
        ivchildright.setVisibility(View.VISIBLE);

//        ivchildleft.getBackground().setAlpha(100);
//        mImagview.getBackground().setAlpha(255);
//        ivchildright.getBackground().setAlpha(100);

//        tvday.setText((280-childday)+"天");

        int per = (int) (childday / 2.8);

        if (100 < per) {
            per = 100;
        }
        if (per <= 0) {
            per = 1;
        }
        mRoundProgressBar.setSweepAngle((int) (per * 3.6));
        mRoundProgressBar.startCustomAnimation();

        mLeft.setSweepAngle((int) (per * 3.6));
        mLeft.startCustomAnimation();

        mRight.setSweepAngle((int) (per * 3.6));
        mRight.startCustomAnimation();

        mActivity.OnSelcet(childday);
    }
    private void setTextView(TextView tv,int day,ImageView iv,int type){
        if(day/7==0){
            if(day==0){
                tv.setVisibility(View.INVISIBLE);
                iv.setBackgroundResource(weekIcons[0]);
            }else {
                tv.setVisibility(View.VISIBLE);
                tv.setText(day % 7 + "天");
                iv.setBackgroundResource(weekIcons[0]);
            }
        }else if(day%7==0){
            tv.setVisibility(View.VISIBLE);
            tv.setText(day/7 + "周");
            iv.setBackgroundResource(weekIcons[day/7-1]);
        }else{
            if(day>280){
                tv.setVisibility(View.INVISIBLE);
                iv.setBackgroundResource(weekIcons[39]);
            }else {
                tv.setVisibility(View.VISIBLE);
                iv.setBackgroundResource(weekIcons[day / 7 - 1]);
                setTextViewStyle(tv, type, day / 7 + "周+" + day % 7 + "天");
            }
        }
    }
    private void setTextViewStyle(TextView tv,int type,String str){
        if(type==1){
            String spantxtyun = str;
            SpannableString styleyun = new SpannableString (
                    spantxtyun);

            styleyun.setSpan(new TextAppearanceSpan(getActivity(), R.style.styleIndexSmallColor), str.length()-3, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(styleyun);
        }else{
            String spantxtyun = str;
            SpannableString styleyun = new SpannableString (
                    spantxtyun);
            styleyun.setSpan(new TextAppearanceSpan(getActivity(), R.style.styleBigColor), str.length()-3, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(styleyun);
        }
    }

}

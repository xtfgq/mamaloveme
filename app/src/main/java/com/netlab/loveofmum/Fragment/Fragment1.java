package com.netlab.loveofmum.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netlab.loveofmum.Index;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.YunWeekAll;
import com.netlab.loveofmum.utils.DetialGallery;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ScreenUtils;
import com.netlab.loveofmum.widget.ImageCycleView;
import com.netlab.loveofmum.widget.ListViewForScrollView;
import com.netlab.loveofmum.widget.RoundProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends BaseFragment  {
    TextView update,currentdate,nextdate;
    DetialGallery babyGallery;
    TextView babyHeight,babyBirth,babyWeight;
    ImageCycleView baby_ad;


    TextView searchkey;


    ImageCycleView community_ads;
    ListViewForScrollView community_listView;



    TextView reminder_titleLabel;
    ImageCycleView reminder_ads;
    ListViewForScrollView reminder_listView;

    private User user;
//    public GalleryAdapter adapterGallery = null;
    final List<YunWeekAll> yunlist = new ArrayList<YunWeekAll>();
    private int[] weekIcons = new int[]{R.drawable.week1, R.drawable.week2, R.drawable.week3, R.drawable.week4, R.drawable.week5, R.drawable.week6, R.drawable.week7, R.drawable.week8,
            R.drawable.week9, R.drawable.week10, R.drawable.week11, R.drawable.week12, R.drawable.week13, R.drawable.week14, R.drawable.week15, R.drawable.week16, R.drawable.week17, R.drawable.week18,
            R.drawable.week19, R.drawable.week20, R.drawable.week21, R.drawable.week22, R.drawable.week23, R.drawable.week24, R.drawable.week25, R.drawable.week26, R.drawable.week27, R.drawable.week28,
            R.drawable.week29, R.drawable.week30, R.drawable.week31, R.drawable.week32, R.drawable.week33, R.drawable.week34, R.drawable.week35, R.drawable.week36, R.drawable.week37, R.drawable.week38, R.drawable.week39, R.drawable.week40};

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_1, container, false);
        //头部宝宝周期模块控件初始化
        update = (TextView)v.findViewById(R.id.update);
        currentdate = (TextView)v.findViewById(R.id.currentdate);
        nextdate = (TextView)v.findViewById(R.id.nextdate);
        babyGallery = (DetialGallery)v.findViewById(R.id.babygallery);
        babyHeight = (TextView)v.findViewById(R.id.baby_height);
        babyBirth = (TextView)v.findViewById(R.id.baby_birth);
        babyWeight = (TextView)v.findViewById(R.id.baby_weight);

        baby_ad = (ImageCycleView)v.findViewById(R.id.baby_ad);
        v.findViewById(R.id.tosingn).setOnClickListener(this);

        //搜索模块初始化
        searchkey = (TextView)v.findViewById(R.id.searchkey);
        v.findViewById(R.id.search).setOnClickListener(this);


        v.findViewById(R.id.community_title).setOnClickListener(this);
        community_ads = (ImageCycleView)v.findViewById(R.id.community_ads);
        community_listView = (ListViewForScrollView)v.findViewById(R.id.community_listview);

        reminder_titleLabel = (TextView)v.findViewById(R.id.reminder_titlelabel);
        reminder_ads = (ImageCycleView)v.findViewById(R.id.reminder_ads);
        reminder_listView = (ListViewForScrollView)v.findViewById(R.id.reminder_listview);
        v.findViewById(R.id.reminder_title).setOnClickListener(this);
        return v;
    }

    @Override
    public void initData() {
        //初始化首页顶部Gallery的Adapter
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
//        adapterGallery = new GalleryAdapter(getActivity(), yunlist);
//        babyGallery.setAdapter(adapterGallery);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.community_title:

            break;
        case R.id.reminder_title:

            break;
        case R.id.tosingn:

            break;
        case R.id.search:

            break;
    }
    }
    public int selectNum = 0;//全局变量，保存被选中的item
    public Boolean isPost = false;
    int days, childday;
    private Boolean isAlready=false;
    private Date dNow, dYun;
    String[] lengths;
    String[] weights;
//    public class GalleryAdapter extends BaseAdapter {
//        public List<YunWeekAll> list = null;
//        public Context ctx = null;
//
//        public GalleryAdapter(Context ctx, List<YunWeekAll> list) {
//            this.list = list;
//            this.ctx = ctx;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final ViewHolder holder;
//            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(ctx, R.layout.image_item, null);
//                holder.image = (ImageView) convertView.findViewById(R.id.image);
//                holder.imageRel = (RelativeLayout) convertView.findViewById(R.id.image_rel);
//                holder.rlimg = (RelativeLayout) convertView.findViewById(R.id.rlimg);
//                holder.tvcontent = (TextView) convertView.findViewById(R.id.tvcontent);
//                holder.mRoundProgressBar = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBarright);
//                holder.mRoundProgressBar.setText("");
//                holder.mRoundProgressBar.setText2("");
//                holder.mRoundProgressBar.getTextPaint1().setTextSize(28 * ScreenUtils.getScreenHeight(getActivity()) / 720);
//                holder.mRoundProgressBar.getTextPaint2().setTextSize(58 * ScreenUtils.getScreenHeight(getActivity()) / 720);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            holder.image.setImageDrawable(list.get(position).getImage());
//            holder.tvcontent.setText(list.get(position).getDay());
//
//            if (selectNum == position) {
//                isPost = false;
//
////				int width = 220 * ScreenUtils.getScreenHeight(Index.this) / 1080;
////				int width =110 *ScreenUtils.getScreenHeight(Index.this) / 1080;
////				ScaleAnimation animation = new ScaleAnimation(1, 1.5f, 1, 1.5f,
////						                       Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
////				holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
////				               //从1倍到1.5倍需要1秒钟
////				                 animation.setDuration(1000);
////				                 //开始执行动画
////				holder.rlimg.startAnimation(animation);
//
//                width =210 *ScreenUtils.getScreenHeight(getActivity()) / 1080;
//                holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
////				holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width, width));//如果被选择则放大显示
////				holder.mRoundProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(375, 375));
////				holder.tvcontent.setLayoutParams(new TextView());
//                if (position + 1 == 281) {
//                    childday = 280;
//                    isAlready=true;
//                } else {
//                    childday = position + 1;
//                    isAlready=false;
//                }
//
//                holder.tvcontent.setTextSize(18);
//                setText(holder.tvcontent, list.get(position).getDay(), 2);
//                holder.image.setAlpha(250);
//                holder.mRoundProgressBar.setAlpha(1.0f);
//                try {
//                    if(isAlready&&position + 1 == 281){
//                        Date date1 = fmt.parse(user.YuBirthDate);
//                        if((new Date().getTime())>(date1.getTime()+24*60*60*1000)){
//                            dYun = new Date();
//
//                        }else{
//
//                            dYun=fmt.parse(fmt.format(date1.getTime()+24*60*60*1000));
//                        }
//                        setTitle(dYun);
//                    }else{
//                        Date date1 = fmt.parse(user.YuBirthDate);
//                        String curr = fmt.format(IOUtils.getDayBefore(date1, "yyyy-MM-dd", (280 - childday)));
//                        setTitle(fmt.parse(curr));
//                        dYun = fmt.parse(curr);
//                    }
//
//
//                    babyHeight.setText(lengths[childday - 1] + "mm");
//                    babyWeight.setText(weights[childday - 1] + "g");
//                    tvday.setText((280 - childday) + "天");
//                    if (childday - 1 == 0) {
//                        llchildleft.setVisibility(View.INVISIBLE);
//                        llchildright.setVisibility(View.VISIBLE);
//                    } else if (position + 1 == 281) {
//                        llchildleft.setVisibility(View.VISIBLE);
//                        llchildright.setVisibility(View.INVISIBLE);
//
//                    } else {
//                        llchildleft.setVisibility(View.VISIBLE);
//                        llchildright.setVisibility(View.VISIBLE);
//                    }
//                    if (position != 280) {
//                        if (childday == (280 - days)) {
//                            tvgotaday.setVisibility(View.INVISIBLE);
//                        } else {
//                            tvgotaday.setVisibility(View.VISIBLE);
//                            tvgotaday.startAnimation(animation1);
//                        }
//                    } else {
//                        tvgotaday.setVisibility(View.INVISIBLE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                isPost = true;
//                mHandler.sendEmptyMessageDelayed(1010, 50);
//
//            } else {
//                int width2 = 110 *ScreenUtils.getScreenHeight(getActivity()) / 1080;
//                holder.rlimg.setLayoutParams(new RelativeLayout.LayoutParams(width2, width2));//否则正常
////				holder.mRoundProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(175, 175));
//                holder.tvcontent.setTextSize(12);
//                setText(holder.tvcontent, list.get(position).getDay(), 1);
//
//                holder.image.setAlpha(100);
//                if(position==279){
//                    if(isAlready&&selectNum==280){
//                        holder.rlimg.setVisibility(View.INVISIBLE);
//                        holder.tvcontent.setVisibility(View.INVISIBLE);
//                    }else{
//                        holder.rlimg.setVisibility(View.VISIBLE);
//                        holder.tvcontent.setVisibility(View.VISIBLE);
//                    }
//                }
//                holder.mRoundProgressBar.setAlpha(0.5f);
//            }
//            int per = (int) (childday / 2.8);
//
//            if (100 < per) {
//                per = 100;
//            }
//            if (per <= 0) {
//                per = 1;
//            }
//            holder.mRoundProgressBar.setSweepAngle((int) (per * 3.6));
//            holder.mRoundProgressBar.startCustomAnimation();
//            return convertView;
//        }
//
//        private void setText(TextView tv, String content, int type) {
//            if (content.indexOf("+") >= 0) {
//                if (type == 1) {
//                    String spantxtyun = content;
//                    SpannableString styleyun = new SpannableString(
//                            spantxtyun);
//
//                    styleyun.setSpan(new TextAppearanceSpan(getActivity(), R.style.styleIndexSmallColor), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    tv.setText(styleyun);
//                } else {
//                    String spantxtyun = content;
//                    SpannableString styleyun = new SpannableString(
//                            spantxtyun);
//                    styleyun.setSpan(new TextAppearanceSpan(getActivity(), R.style.styleBigColor), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tv.setText(styleyun);
//                }
//
//            }
//
//        }
//
//
//    }

    class ViewHolder {
        ImageView image;
        RelativeLayout imageRel;
        RelativeLayout rlimg;
        RoundProgressBar mRoundProgressBar;
        TextView tvcontent;
    }
    private void setTitle(Date d) {
        DateFormat fmt2 = new SimpleDateFormat("M月d日");

        update.setText(fmt2.format(d.getTime() - 24 * 60 * 60 * 1000));

        currentdate.setText(fmt2.format(d.getTime()));
        nextdate.setText(fmt2.format(d.getTime() + 24 * 60 * 60 * 1000));


//				tvyesterday.setText("10月15日");
//		tvcenter.setText("10月15日");
//		tvnext.setText("10月15日");
    }
}

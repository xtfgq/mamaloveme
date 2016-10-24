package com.netlab.loveofmum.myadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.netlab.loveofmum.MyCHK_OrderAdd;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.SpaceImageDetailActivity;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.PictureItem;
import com.netlab.loveofmum.utils.ScreenUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class EditPhotoUploadAdapter extends BaseAdapter{
    private ImageLoader mImageLoader;
    private Context context;
    private MyCHK_OrderAdd mAct;
    public static final int ITEM_TYPE_ONE = 0;
    public static final int ITEM_TYPE_TWO = 1;

    public static final int ITEM_TYPE_COUNT = 2;
    private LayoutInflater mInflater;
    private List<String> datas =new ArrayList<String>();
    public List<PictureItem> getList() {
        return list;
    }
    private int width;
    private int height;

    public void setList(List<PictureItem> list) {
        this.list = list;
        for(int i=0;i<list.size()-1;i++)
        {
            datas.add(list.get(i).getPath());
        }
    }

    private List<PictureItem> list;
    public EditPhotoUploadAdapter(Context context) {
        width = (ScreenUtils.getScreenWidth(context) - ScreenUtils.dip2px(context, 50))/4;
        height = width;
this.context=context;
        this.mAct=(MyCHK_OrderAdd)context;
        mImageLoader = ImageLoader.getInstance();
        this.mInflater = LayoutInflater.from(context);

    }
    static class AddViewHolder {
        public ImageView mImg;
    }
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public ImageView mImg;
        public ImageView mStatus;
    }
    public int getCount() {
        return list==null?0:list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        // 自定义视图
        ViewHolder mViewHolder = null;
        AddViewHolder mAddViewHolder = null;
        // 初始化视图
        if (convertView == null) {
            switch (type) {
                case ITEM_TYPE_ONE:
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.component_select_picture_item_add, null);
                    mAddViewHolder = new AddViewHolder();
                    mAddViewHolder.mImg = (ImageView) convertView
                            .findViewById(R.id.img001_item_myorder);

                    convertView.setTag(mAddViewHolder);
                    break;
                case ITEM_TYPE_TWO:
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.component_select_picture_item, null);
                    mViewHolder = new ViewHolder();
                    mViewHolder.mImg = (ImageView) convertView
                            .findViewById(R.id.img001_item_myorderadd);


                    convertView.setTag(mViewHolder);
                    break;

                default:
                    break;
            }

        } else {
            switch (type) {
                case ITEM_TYPE_ONE:
                    mAddViewHolder = (AddViewHolder) convertView.getTag();

                    break;
                case ITEM_TYPE_TWO:
                    mViewHolder = (ViewHolder) convertView.getTag();
                    break;

                default:
                    break;
            }

        }
        PictureItem mItem = (PictureItem)getItem(position);
        switch (type) {
            case ITEM_TYPE_ONE:
                ViewGroup.LayoutParams params = mAddViewHolder.mImg.getLayoutParams();
                params.height = height;
                params.width = width;
                mAddViewHolder.mImg.setLayoutParams(params);

                setAdd(mAddViewHolder, mItem, position);
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mAct.hasInternetConnected())
                        {
                            mAct.showPicturePicker(context,false);
                        }
                        else
                        {
                            Toast.makeText(context, R.string.msgUninternet,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            case ITEM_TYPE_TWO:
                setImg(mViewHolder, mItem, position);
                final int p=position;
                final ImageView iv=(ImageView)mViewHolder.mImg;
                ViewGroup.LayoutParams params2 = mViewHolder.mImg.getLayoutParams();
                params2.height = height;
                params2.width = width;
                mViewHolder.mImg.setLayoutParams(params2);
                mViewHolder.mImg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SpaceImageDetailActivity.class);
                        intent.putExtra("images", (ArrayList<String>) datas);
                        intent.putExtra("position", p);
                        int[] location = new int[2];
                        iv.getLocationOnScreen(location);
                        intent.putExtra("locationX", location[0]);
                        intent.putExtra("locationY", location[1]);

                        intent.putExtra("width", iv.getWidth());
                        intent.putExtra("height", iv.getHeight());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(0, 0);
                    }
                });
                break;

            default:
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        boolean click = list.get(position).isAdd();
        if (click) {
            return ITEM_TYPE_ONE;
        } else {
            return ITEM_TYPE_TWO;
        }
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }
    private void setAdd(AddViewHolder mViewHolder, PictureItem mItem,
                        int position) {
        if(position>0){
            mViewHolder.mImg
                    .setBackgroundResource(R.drawable.icon_tjzp);
        }else {
            mViewHolder.mImg
                    .setBackgroundResource(R.drawable.album_carame);
        }
    }

    private void setImg(ViewHolder mViewHolder, PictureItem mItem,
                        final int position) {


mImageLoader.displayImage(mItem.getPath(),mViewHolder.mImg);



    }
}

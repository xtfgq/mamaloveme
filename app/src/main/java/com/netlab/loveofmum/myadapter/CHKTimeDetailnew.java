package com.netlab.loveofmum.myadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.PostList;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.activity.LoginActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.ListItemClickHelp;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.widget.CircularImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CHKTimeDetailnew extends BaseAdapter
{
    private LayoutInflater mInflater;

    private List<Map<String, Object>> listData;

    private Context context;
    private User user;

    //private ImageLoader image;

    private ListItemClickHelp callback;
    private int start=0,end=0;
    private String TopicID;
    private String TopicTitle;
    private String LeftID;
    private String returnvalue001;
    private ListView mListView;
    public int goodcount;
    private int lastItem;
    //private String returnvalue002;

    // 定义Web Service相关的字符串
    private final String				SOAP_NAMESPACE	= MMloveConstants.URL001;
    private final String				SOAP_ACTION		= MMloveConstants.URL001
            + MMloveConstants.NewsTopicUpdate;
    private final String				SOAP_METHODNAME	= MMloveConstants.NewsTopicUpdate;
    private final String				SOAP_URL		= MMloveConstants.URL002;

//	private final String				SOAP_ACTION2		= Constants.URL001+ Constants.NewsTopicAdd;
//	private final String				SOAP_METHODNAME2	= Constants.NewsTopicAdd;


    private boolean mBusy = false; //标识是否存在滚屏操作
    public CHKTimeDetailnew(Context context, List<Map<String, Object>> listData,ListView listview,ListItemClickHelp callback)
    {

        this.mInflater = LayoutInflater.from(context);
        this.listData = listData;
        this.context = context;
        this.callback = callback;
        this.mListView=listview;
        mListView.setOnScrollListener(onScrollListener);

        user = LocalAccessor.getInstance(context).getUser();
//		start=0;end=9;
        //this.image = imageloader;
    }


    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {

                case  AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //Idle态，进行实际数据的加载显示
                    mBusy = false;
                    int first = view.getFirstVisiblePosition();
                    int count = view.getChildCount();
                    for (int i = first; i < count; i++) {
                        LinearLayout layout = (LinearLayout)view.getChildAt(i);
                        if(layout.getTag()!=null){
                            TextView txt001 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt001);
                            TextView txt002 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt002);
                            TextView txt003 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt003);
                            final TextView txt004 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt004);
                            TextView txt005 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt005);
                            TextView txt006 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt006);
                            CircularImage img004 = (CircularImage)layout.findViewById(R.id.img000_item_chkdetail_list);
                            TextView txt007 = (TextView)layout.findViewById(R.id.item_chkdetail_list_txt007);
                            txt001.setText(listData.get(i).get("NickName").toString());
                            txt002.setText(listData.get(i).get("YuBirthTime").toString());
                            txt003.setText(Html.fromHtml(listData.get(i).get("TopicTitle").toString()));
                            txt004.setText(listData.get(i).get("GoodCount").toString());
                            txt005.setText(listData.get(i).get("PostsCount").toString());
                            txt007.setText(listData.get(i).get("CreatedDate").toString());
                            if(listData.get(i).get("PictureURL").toString().contains("vine.gif"))
                            {
                                img004.setImageResource(R.drawable.icon_user_normal);
                            }
                            else
                            {
                                ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + listData.get(i).get("PictureURL").toString().replace("~", "").replace("\\", "/"), img004
                                        , ImageOptions.getCHKListOptions());
                            }
                        }
                    }


                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    mBusy = true;

                    break;
                case  AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    mBusy = true;
                    break;
                default:

                    break;

            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub

        }};
    public int getCount()
    {
        return listData.size();
    }

    protected void loadImage(ListView mListView, int start, int end) {
        // TODO Auto-generated method stub
        for (int i=start; i<=end; i++) {
            LinearLayout layout = (LinearLayout)mListView.getChildAt(i-start);
            CircularImage img004 = (CircularImage)layout.findViewById(R.id.img000_item_chkdetail_list);

            if(listData.get(i).get("PictureURL").toString().contains("vine.gif"))
            {
                img004.setImageResource(R.drawable.icon_user_normal);
            }
            else
            {
                ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + listData.get(i).get("PictureURL").toString().replace("~", "").replace("\\", "/"),img004
                        ,ImageOptions.getCHKListOptions());
            }

        }
    }

    public Object getItem(int position)
    {
        return listData.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public boolean isEnabled(int position)
    {
//		return super.isEnabled(position);
        return false;
    }

    public View getView(final int position, View convertView, final ViewGroup parent)
    {
//		 ViewHolder holder = null;

//        if(convertView==null){
        convertView = mInflater.inflate(R.layout.item_chkdetail_list, null);
//		holder = new ViewHolder();
//		holder.img001 = (ImageButton) convertView
//				.findViewById(R.id.img001_item_chkdetail_list);
//		holder.img002 = (ImageButton) convertView
//				.findViewById(R.id.img002_item_chkdetail_list);
//		holder.img003 = (ImageButton) convertView
//			.findViewById(R.id.img003_item_chkdetail_list);
        TextView txt001 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt001);
        TextView txt002 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt002);
        TextView txt003 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt003);
        TextView txt004 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt004);
        TextView txt005 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt005);

        TextView txt006 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt006);

        TextView txt007 = (TextView)convertView.findViewById(R.id.item_chkdetail_list_txt007);

        ImageView img004 = (CircularImage)convertView.findViewById(R.id.img000_item_chkdetail_list);


//        }else{
//        	return convertView;
//        }
//     if(!mBusy){

        if(listData.get(position).get("PictureURL").toString().contains("vine.gif"))
        {
            img004.setImageResource(R.drawable.icon_user_normal);
        }

        else
        {
            ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + listData.get(position).get("PictureURL").toString().replace("~", "").replace("\\", "/"), img004,ImageOptions.getCHKListOptions());
        }

//		TopicID = listData.get(position).get("ID").toString();
//		TopicTitle = listData.get(position).get("TopicTitle").toString();
//		LeftID = listData.get(position).get("LeftID").toString();


        txt001.setText(listData.get(position).get("NickName").toString());
        txt002.setText(listData.get(position).get("YuBirthTime").toString());
        txt003.setText(Html.fromHtml(listData.get(position).get("TopicTitle").toString()));
        txt004.setText(listData.get(position).get("GoodCount").toString());
        txt005.setText(listData.get(position).get("PostsCount").toString());

        txt007.setText(listData.get(position).get("CreatedDate").toString());

        final View view = convertView;
        final int num=Integer.valueOf(listData.get(position).get("PostsCount").toString());
        final int p = position;
        final int one = txt004.getId();
        final int two = txt005.getId();
        final int three =txt006.getId();
        // goodcount = Integer.parseInt(listData.get(position).get("GoodCount").toString());
        txt004.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(user.UserID == 0)
                {
                    Intent i1 = new Intent(context, LoginActivity.class);
                    context.startActivity(i1);

                }else{

                    GoodSet(listData.get(position).get("ID").toString(),position);
                }

                callback.onClick(view, parent, p, one);
                //goodcount = goodcount+1;
                //txt004.setText(String.valueOf(goodcount));
            }
        });

        txt005.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                if(num>0){
                    Intent i = new Intent(context,
                            PostList.class);
                    i.putExtra("TopicID",listData.get(p).get("ID").toString());
                    i.putExtra("TopicType",listData.get(p).get("TopicType").toString());
                    i.putExtra("TopicTitle",listData.get(p).get("TopicTitle").toString());
                    i.putExtra("LeftID",listData.get(p).get("LeftID").toString());
                    ((Activity)context).startActivityForResult(i, 7);
                }else{
                    Toast.makeText(context,R.string.titl_pinglunnume,1).show();
                }
            }
        });



        txt006.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                callback.onClick(view, parent, p, three);
            }
        });
//        convertView.setTag(holder);
//     }
//      else{
//    	  holder.img004.setImageResource(R.drawable.icon_user_normal);
//    		holder.txt001.setText("正在加载");
//    		holder.txt002.setText("");
//    		holder.txt003.setText("");
//    		holder.txt004.setText("");
//    		holder.txt005.setText("");
//
//    		holder.txt007.setText("正在加载");
//    		convertView.setTag(context);
//      }
        return convertView;
    }
    public static class ViewHolder {

        public TextView txt001;
        public TextView txt002;
        public TextView txt003;
        public TextView txt004;
        public TextView txt005;
        public TextView txt006;
        public TextView txt007;
        public  CircularImage img004;
    }


    /*
     * 点赞
     */
    private void GoodSet(String topicID,final int posistion) {
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    returnvalue001 = result.toString();
                    if (returnvalue001 == null) {

                    } else {
//						Map<String, Object> map;
                        // 下边是具体的处理
                        try {
                            JSONObject mySO = (JSONObject) result;
                            JSONArray array = mySO
                                    .getJSONArray("NewsTopicUpdate");
                            if(array.getJSONObject(0).getString("MessageCode").equals("0"))
                            {
//								Intent i = new Intent(context,
//										CHKTimeDetail.class);
//								context.startActivity(i);

                            }
                            // 添加listView点击事件

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            if (IOUtils.hasInternetConnected(context))
            {
                JsonAsyncTask_Info task = new JsonAsyncTask_Info(context, true,
                        doProcess);
                Map<String, Object> paramMap = new HashMap<String, Object>();
                String str = "<Request><ID>%s</ID></Request>";

                str = String.format(str, new Object[]
                        { topicID });
                paramMap.put("str", str);

                // 必须是这5个参数，而且得按照顺序
                task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
                        SOAP_URL, paramMap);
            }
            else
            {
                Toast.makeText(context, R.string.msgUninternet,
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

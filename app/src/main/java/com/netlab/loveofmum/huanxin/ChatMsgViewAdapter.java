package com.netlab.loveofmum.huanxin;

import java.util.List;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMsgViewAdapter extends BaseAdapter{
	private ImageLoader mImageLoader;
	
    public static interface IMsgViewType{
   	 int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
    }
    private static final String TAG  = ChatMsgViewAdapter.class.getSimpleName();
    private List<ChatMsgEntity> coll;
    private LayoutInflater mInflater;
	 private MediaPlayer mMediaPlayer = new MediaPlayer();
    private Context ctx;
    Resources res;
    
  public ChatMsgViewAdapter(Context context,List<ChatMsgEntity> coll){
	   ctx = context;
	   this.coll = coll;
	   mInflater= LayoutInflater.from(context);
	   res=context.getResources();
		mImageLoader = ImageLoader.getInstance();
	 
  }
    
	@Override
	public int getCount() {
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		ChatMsgEntity entity = coll.get(position);
		if(entity.getMsgType()){
			return IMsgViewType.IMVT_COM_MSG;
		}else{
			return IMsgViewType.IMVT_TO_MSG;
		}
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(final int postion, View converView, ViewGroup parent) {
		final ChatMsgEntity entity = coll.get(postion);
		boolean isComMsg = entity.getMsgType();
		ViewHolder viewHolder = null;
		if(converView==null){
			if(isComMsg){
				converView=mInflater.inflate(R.layout.chat_left, null);
			}else{
				converView=mInflater.inflate(R.layout.chat_right, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.ivhead=(ImageView) converView.findViewById(R.id.iv_userhead);
			viewHolder.tvSendTime = (TextView)converView.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent = (TextView)converView.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView)converView.findViewById(R.id.tv_time);
//			viewHolder.tvUserName = (TextView)converView.findViewById(R.id.tv_username);
			viewHolder.ivcount=(ImageView) converView.findViewById(R.id.iv_chatcontent);
		    ViewHolder.isComMsg = isComMsg;
		    converView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)converView.getTag();
		}
		int type=getItemViewType(postion);
		switch (type) {
		case IMsgViewType.IMVT_COM_MSG:
			mImageLoader.displayImage(entity.getHeadurl(), viewHolder.ivhead, ImageOptions.getFaceOptions(120));
			break;
	case IMsgViewType.IMVT_TO_MSG:
		mImageLoader.displayImage(entity.getHeadurl(), viewHolder.ivhead, ImageOptions.getHeaderDoctorOptions(120));
			break;

		default:
			break;
		}
	
		 if(1==entity.getMsgtype()){
		    viewHolder.tvContent.setVisibility(View.GONE);
			viewHolder.ivcount.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(entity.getContent(), viewHolder.ivcount);
		 }else{
			 viewHolder.tvContent.setVisibility(View.VISIBLE);
			 viewHolder.ivcount.setVisibility(View.GONE);
			 viewHolder.tvContent.setText(entity.getText());
		 }
		
	
		viewHolder.tvSendTime.setText(entity.getDate());
		if(entity.getText().contains(".amr")){
			viewHolder.tvTime.setText(entity.getTime());
			viewHolder.tvContent.setText("");
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
		}else{
//			viewHolder.tvContent.setText(entity.getText());
//			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
			SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(ctx, entity.getText());
			viewHolder.tvContent.setText(spannableString);
			viewHolder.tvTime.setText(entity.getTime());
		}
		viewHolder.ivcount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
				   // String id=entity.getText().toString();
					bundle.putInt("ID", postion);
					String path=entity.getContent();
					bundle.putString("Url", path);
					intent.putExtras(bundle);
					intent.setClass(ctx, PhotoActivity.class);
					ctx.startActivity(intent);
				
				}
			
		});
	
		return converView;
	}
static class ViewHolder{
	public TextView 
	       tvSendTime,
	     
	       tvContent,
	       tvTime;
	public ImageView ivhead,ivcount;
	public static boolean isComMsg = true;
	 
}

/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void stop() {
	}
}
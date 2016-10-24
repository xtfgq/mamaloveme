package com.netlab.loveofmum.huanxin;

import java.io.Serializable;
import java.util.List;

import com.netlab.loveofmum.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class TestPicActivity extends Activity implements OnClickListener{
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	TextView tv;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_image_bucket);
	    
	    helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
	}
	private void initData()
	{
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
	}
	public void initView(){
		gridView = (GridView) findViewById(R.id.gridview);
		tv = (TextView)findViewById(R.id.quxiao);
		tv.setOnClickListener(this);
		adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(TestPicActivity.this,
						ImageGridActivity.class);
				intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}
	
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quxiao:
			Intent intent = new Intent(TestPicActivity.this, MainChatActivity.class);
			startActivity(intent);
			break;
		}
		
		
	}
}

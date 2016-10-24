package com.netlab.loveofmum.huanxin;

import com.netlab.loveofmum.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity{
	ImageView camera;
	Object imgUrl;
	private ImageLoader iMageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setContentView(R.layout.activity_camera);
		iMageLoader=iMageLoader.getInstance();
		init();
	}
	private void init() {
		camera=(ImageView) findViewById(R.id.camera);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		imgUrl = bundle.get("camera");
		//获取相机返回的数据，并转换为图片格式
		//Bitmap bitmap = (Bitmap)msg.get("data");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		Bitmap bitmap =BitmapFactory.decodeFile(imgUrl.toString(),options);
		camera.setImageBitmap(bitmap);
	}
	public void btnClick(View v){
		switch (v.getId()) {
		case R.id.btn_back_chat:
			CameraActivity.this.finish();
			break;
		case R.id.cancel_camera:
			CameraActivity.this.finish();
			break;
		case R.id.confirm_camera:
			//告诉聊天界面需要发送数据到服务器，并且在聊天界面中显示
			//Toast.makeText(CameraActivity.this, "发送图片", 1).show();
			Intent intent=new Intent();
			intent.putExtra("imgUrl", imgUrl.toString());
			CameraActivity.this.setResult(0x000002,intent);
			CameraActivity.this.finish();
			break;
		}
	}
	
}

package com.netlab.loveofmum.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.TimeSet;
import com.netlab.loveofmum.User_AuthChange;
import com.netlab.loveofmum.api.BaseActivity;

import com.netlab.loveofmum.api.ImageTools;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.PermissionsChecker;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CircularImage;
import com.netlab.loveofmum.widget.CustomerSpinner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;


public class User_InfoEditActivity extends BaseActivity implements View.OnClickListener
{
	private CircularImage avatar;         //头像
	private TextView txtYuchan;
	int REQUEST_CODE;
	
	private EditText realname;
	private EditText identID;
	private EditText checkID;
	
	private EditText nickname;
	
	private EditText year;
	
	private String returnvalue001;

	private String RealName;
	private String UserNO;
	private String UserSex;
	private String City = "郑州";
	private String UserAge;
	private CustomerSpinner selectHospital;
	private int HospitalID;
	private TextView yuchTxt;

	
	private String NickName;

	
	private RadioGroup group;
	
	RadioButton radio001;
	RadioButton radio002;
	
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath();
	private static final String IMGPATH = ACCOUNT_DIR;

	private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";
	

	private User user;
	
	private byte[] mContent;
	public ArrayAdapter<String> adapter;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	private static final int SCALE = 5;// 照片缩小比例

	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private static final String PACKAGE_URL_SCHEME = "package:";
	private PermissionsChecker mPermissionsChecker; // 权限检测器
	// 所需的全部权限
	static final String[] PERMISSIONS = new String[]{

			android.Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.UserInfoChange;
	private final String SOAP_METHODNAME = MMloveConstants.UserInfoChange;
	private final String SOAP_URL = MMloveConstants.URL002;
	
	private final String SOAP_ACTION2 = MMloveConstants.URL001
			+ MMloveConstants.UserInquiry;
	private final String SOAP_METHODNAME2 = MMloveConstants.UserInquiry;
	
	
	private final String SOAP_NAMESPACE3 = MMloveConstants.URL001Topmd;
	private final String SOAP_ACTION3 = MMloveConstants.URL001Topmd
			+ MMloveConstants.UploadUserPhoto;
	private final String SOAP_METHODNAME3 = MMloveConstants.UploadUserPhoto;
	private final String SOAP_URL3 = MMloveConstants.URL002Topmd;
	
	public static ArrayList<String> list = new ArrayList<String>();
		//请求医院列表
		private final String SOAP_ACTION4 = MMloveConstants.URL001
				+ MMloveConstants.HospitalInquiry;
		private final String SOAP_METHODNAME4 = MMloveConstants.HospitalInquiry;
	boolean crop;
	AlertDialog.Builder builder;
	AlertDialog dialog;
	private TextView tvok;
	private TextView tvphoto;
	private TextView tvalbum;
	private Intent mIntent;
	private String taginfo="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTranslucentStatus() ;
		setContentView(R.layout.layout_userinfochange);
		MyApplication.getInstance().addActivity(this);
		mPermissionsChecker = new PermissionsChecker(this);
		iniView();
		mIntent=this.getIntent();
		if(mIntent.getStringExtra("tag_info")!=null&&"info".equals(mIntent.getStringExtra("tag_info"))){

		taginfo=mIntent.getStringExtra("tag_info");
			yuchTxt.setEnabled(false);
		}
		
		setListeners();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


	}
	
	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.home);//状态栏无背景
	}

	@Override
	protected void iniView()
	{
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.txtHead)).setText("个人信息设置");
		findViewById(R.id.img_left).setOnClickListener(this);

		avatar = (CircularImage)findViewById(R.id.avatar);

		


		findViewById(R.id.editpwd).setOnClickListener(this);


		realname = (EditText)findViewById(R.id.name_title_bar10);
		identID = (EditText)findViewById(R.id.id_title_bar11);
		group = (RadioGroup)findViewById(R.id.radioGroup);
		
		radio001 = (RadioButton)findViewById(R.id.radioMale);
		radio002 = (RadioButton)findViewById(R.id.radioFemale);
		txtYuchan=(TextView) findViewById(R.id.txt0001_user_set);
		yuchTxt=(TextView)findViewById(R.id.txt0001_user_set);


		nickname = (EditText)findViewById(R.id.nickname);

		year = (EditText)findViewById(R.id.year);
		findViewById(R.id.selectavatar).setOnClickListener(this);
		findViewById(R.id.selectbirth).setOnClickListener(this);
		selectHospital = (CustomerSpinner)findViewById(R.id.selectHospital);
		checkID=(EditText)findViewById(R.id.checkID);

	}
	public void submit(View v){
		if(hasInternetConnected())
		{
			// TODO Auto-generated method stub
			if(realname.getText().toString().trim().equals(""))
			{
				Toast.makeText(User_InfoEditActivity.this, R.string.str003_userinfochange,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				if(!IOUtils.isName(realname.getText().toString().trim()))
				{
					Toast.makeText(User_InfoEditActivity.this, R.string.str007_userinfochange,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					RealName = realname.getText().toString().trim();
				}
			}
			if(identID.getText().toString().trim().equals(""))
			{
				Toast.makeText(User_InfoEditActivity.this, R.string.str004_userinfochange,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				if(!IOUtils.isUserNO(identID.getText().toString().trim()))
				{
					Toast.makeText(User_InfoEditActivity.this, R.string.str007_register,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					UserNO = identID.getText().toString().trim();
				}
			}
			if(nickname.getText().toString().trim().length()>20){
				Toast.makeText(User_InfoEditActivity.this, "昵称不能超过20个字符",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if(nickname.getText().toString().trim().equals(""))
			{
				Toast.makeText(User_InfoEditActivity.this, R.string.str005_userinfochange,
						Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				NickName = nickname.getText().toString().trim();
			}

			UserSex = "01";

			int radioButtonId = group.getCheckedRadioButtonId();
			RadioButton rb = (RadioButton)User_InfoEditActivity.this.findViewById(radioButtonId);

			if(rb.getText().toString().trim().equals("男"))
			{
				UserSex = "01";
			}
			else
			{
				UserSex = "02";
			}


			if(!year.getText().toString().trim().equals(""))
			{
				if(!IOUtils.isBeyondAge(Integer.parseInt(year.getText().toString().trim())))
				{
					Toast.makeText(User_InfoEditActivity.this, R.string.str008_register,
							Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					UserAge = year.getText().toString().trim();
				}
			}
			else
			{
				Toast.makeText(User_InfoEditActivity.this, R.string.str006_userinfochange,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!hasInternetConnected()){
				Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
				return;
			}

			UserInfoChange();


		}
		else
		{
			Toast.makeText(User_InfoEditActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}
	}
	private void setListeners()
	{
		selectHos();
	}

	private void selectHos() {
		if(list.size()>0){
			list.clear();
		}
		int isDefault=0;
		for(int i=0;i<arrayList.size();i++){
			list.add(arrayList.get(i).get("HospitalName").toString());
			if(user.HospitalID==Integer.valueOf(arrayList.get(i).get("HospitalID").toString())){
				isDefault=i;
			}
		}
		selectHospital.setList(list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		selectHospital.setAdapter(adapter);
		selectHospital.setPosition(isDefault);
		selectHospital.setSelection(isDefault,true);
		if(taginfo.equals("info")){
			selectHospital.setEnabled(false);

			return;}
		selectHospital.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				Map<String, Object> mapDetail = new HashMap<String, Object>();
				mapDetail =arrayList .get(position);
				int hosID = Integer.valueOf(mapDetail.get("HospitalID").toString());

				
				if(hosID == HospitalID)
				{
					
				}
				else
				{
					user.HospitalID = hosID;
					user.HospitalName = mapDetail.get("HospitalName").toString();
					HospitalID = hosID;
					try
					{
						LocalAccessor.getInstance(User_InfoEditActivity.this).updateUser(user);
						
						
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		user=LocalAccessor.getInstance(User_InfoEditActivity.this).getUser();
		if(user.YuBirthDate!=null)
		{
			txtYuchan.setText("预产期设置："+user.YuBirthDate);
		}
		if(hasInternetConnected())
		{

			UserInquiry();
			searchHospital();
		}
		else
		{
			Toast.makeText(User_InfoEditActivity.this, R.string.msgUninternet,
					Toast.LENGTH_SHORT).show();
		}

		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	/*
	 * 查询UserInquiry
	 */
	private void UserInquiry()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("UserInquiry");
							for (int i = 0; i < array.length(); i++)
							{
								realname.setText(array.getJSONObject(i)
										.getString("RealName"));

								identID.setText(array.getJSONObject(i)
										.getString("UserNO"));

								nickname.setText(array.getJSONObject(i)
										.getString("NickName"));

								checkID.setText(array.getJSONObject(i)
											.getString("MedicalCard").toString());

								if(array.getJSONObject(i)
										.getString("UserSex").equals("01"))
								{
									
									radio001.setChecked(true);
									radio002.setChecked(false);
								}
								else
								{
									radio001.setChecked(false);
									radio002.setChecked(true);
								}
								
								if(array.getJSONObject(i)
										.getString("UserAge").equals("0"))
								{
									year.setText("");
								}
								else
								{
									year.setText(array.getJSONObject(i)
											.getString("UserAge"));
								}
								
								
								
								if(array.getJSONObject(i)
										.getString("PictureURL").toString().contains("vine.gif"))
								{
									avatar.setImageResource(R.drawable.icon_user_normal);
								}
								else
								{
									ImageLoader.getInstance().displayImage(MMloveConstants.JE_BASE_URL3 + array.getJSONObject(i)
												.getString("PictureURL").toString().replace("~", "").replace("\\", "/"), avatar);
									user.PicURL=MMloveConstants.JE_BASE_URL3 + array.getJSONObject(i)
											.getString("PictureURL").toString().replace("~", "").replace("\\", "/");
									LocalAccessor.getInstance(User_InfoEditActivity.this).updateUser(user);
								}
								
								
								
							}
							
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					User_InfoEditActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><UserMobile>%s</UserMobile></Request>";
			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),user.UserMobile });
			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION2, SOAP_METHODNAME2,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.editpwd:
				if (!hasInternetConnected()){
					Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
					return;
				}
				Intent i = new Intent(User_InfoEditActivity.this, User_AuthChange.class);
				startActivity(i);
				break;
			case R.id.selectavatar:
				if (!hasInternetConnected()){
					Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
					return;
				}
				showPicturePicker(User_InfoEditActivity.this, true);
				break;
			case R.id.dialog_default_click_ensure:
				if(dialog!=null){
					dialog.dismiss();
				}

				break;
			case R.id.dialog_phtoto_tv:
				Uri imageUri = null;
				String fileName = null;
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (crop)
				{
					REQUEST_CODE = CROP;
					// 删除上一次截图的临时文件
					SharedPreferences sharedPreferences = getSharedPreferences(
							"temp", Context.MODE_WORLD_WRITEABLE);
					ImageTools.deletePhotoAtPathAndName(Environment
							.getExternalStorageDirectory()
							.getAbsolutePath(), sharedPreferences
							.getString("tempName", ""));

					// 保存本次截图临时文件名字
					fileName = String.valueOf(System
							.currentTimeMillis()) + ".jpg";
					Editor editor = sharedPreferences.edit();
					editor.putString("tempName", fileName);
					editor.commit();
				}
				else
				{
					REQUEST_CODE = TAKE_PICTURE;
					fileName = "image.jpg";
				}
				imageUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), fileName));
				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						imageUri);
				startActivityForResult(openCameraIntent, REQUEST_CODE);
				if(dialog!=null){
					dialog.dismiss();
				}
				break;
         case R.id.dialog_album:
			 Intent openAlbumIntent = new Intent(
					 Intent.ACTION_GET_CONTENT);
			 if (crop)
			 {
				 REQUEST_CODE = CROP;
			 }
			 else
			 {
				 REQUEST_CODE = CHOOSE_PICTURE;
			 }


			 openAlbumIntent.setDataAndType(
					 MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					 "image/*");
			 startActivityForResult(openAlbumIntent, REQUEST_CODE);
			 if(dialog!=null){
				 dialog.dismiss();
			 }
			 break;
			case R.id.img_left:
				finish();
				break;
			case R.id.selectbirth:
				if(taginfo.equals("info")){
					return;
				}
				if (!hasInternetConnected()){
					Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
					return;
				}

				Intent a = new Intent(User_InfoEditActivity.this, TimeSet.class);
				startActivity(a);
				break;
		}
	}

	private void UserInfoChange()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO.getJSONArray("UserInfoChange");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
							
								
								User user = LocalAccessor.getInstance(User_InfoEditActivity.this).getUser();
								
								user.RealName = RealName;
								user.UserNO = UserNO;
								user.NickName = NickName;
								
								
								LocalAccessor.getInstance(User_InfoEditActivity.this).updateUser(user);
								Intent intent = new Intent();
								intent.setAction("action.login");
								sendBroadcast(intent);
							
								setResult(1);
								finish();
							}
							else
							{
								Toast.makeText(User_InfoEditActivity.this, R.string.str002_userinfochange,
										Toast.LENGTH_SHORT).show();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					User_InfoEditActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><UserID>%s</UserID><UserName>%s</UserName><UserPWD>%s</UserPWD><UserMobile>%s</UserMobile><UserNO>%s</UserNO><RealName>%s</RealName><NickName>%s</NickName><UserSex>%s</UserSex><YuBirthTime>%s</YuBirthTime><PictureURL>%s</PictureURL><City>%s</City><ClientID>%s</ClientID><NickName>%s</NickName><UserAge>%s</UserAge><MedicalCard>%s</MedicalCard></Request>";
			str = String.format(str, new Object[]
			{String.valueOf(user.UserID),"","","",UserNO,RealName,"",UserSex,"","",City,"",NickName,UserAge,checkID.getText().toString()});
			
			
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
	
	
	private void UploadPicture(String uploadBuffer)
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("UploadUserPhoto");

							if (array.getJSONObject(0).getString("MessageCode")
									.equals("0"))
							{
								  if (!hasInternetConnected()){
										Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
										return;
										}
								UserInquiry();
							}
							else
							{
								Toast.makeText(User_InfoEditActivity.this, "头像上传失败，请重试！",
										Toast.LENGTH_SHORT).show();
							}

						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					User_InfoEditActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("UserID", String.valueOf(user.UserID));
			paramMap.put("fileName", getPhotoFileName());
			paramMap.put("img", uploadBuffer);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE3, SOAP_ACTION3, SOAP_METHODNAME3,
					SOAP_URL3, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getPhotoFileName()
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss"+user.UserID);
		return dateFormat.format(date) + ".jpg";
	}
	
	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getOrderID()
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		return dateFormat.format(date) + user.UserID;
	}
	

	public static Bitmap rotateImage(Bitmap bmp, int degrees)
	{
		if (degrees != 0)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(degrees);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrix, true);
		}
		return bmp;
	}

	protected String getAbsoluteImagePath(Uri uri)
	{
		// can post image
		String[] proj =
		{ MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private int getDegree(ExifInterface exif)
	{
		int degree = 0;

		if (exif != null)
		{

			// 读取图片中相机方向信息

			int ori = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,

					ExifInterface.ORIENTATION_UNDEFINED);

			// 计算旋转角度

			switch (ori)
			{

				case ExifInterface.ORIENTATION_ROTATE_90:

					degree = 90;

					break;

				case ExifInterface.ORIENTATION_ROTATE_180:

					degree = 180;

					break;

				case ExifInterface.ORIENTATION_ROTATE_270:

					degree = 270;

					break;

				default:

					degree = 0;

					break;

			}
		}
		return degree;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case TAKE_PICTURE:
					// 将保存在本地的图片取出并缩小后显示在界面上
					Bitmap bitmap = BitmapFactory.decodeFile(Environment
							.getExternalStorageDirectory() + "/image.jpg");
					Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
							bitmap.getWidth() / SCALE, bitmap.getHeight()
									/ SCALE);
					// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
					bitmap.recycle();

					String url = Environment.getExternalStorageDirectory() + "/image.jpg";
					
					ExifInterface exif2 = null;
					try
					{
						exif2 = new ExifInterface(url);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					int degree2 = getDegree(exif2);
					if(degree2!=0)
					{
						newBitmap = rotateImage(newBitmap,degree2);
					}
					
					// 将处理过的图片显示在界面上，并保存到本地
					//iv_image.setImageBitmap(newBitmap);
					ImageTools.savePhotoToSDCard(newBitmap, Environment
							.getExternalStorageDirectory().getAbsolutePath(),
							String.valueOf(System.currentTimeMillis()));

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					newBitmap.compress(Bitmap.CompressFormat.JPEG,
							100, baos);
					mContent = baos.toByteArray();

					String uploadBuffer = new String(
							Base64.encode(mContent));
					  if (!hasInternetConnected()){
							Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
							return;
							}

					UploadPicture(uploadBuffer);
					System.gc();
					break;

				case CHOOSE_PICTURE:
					ContentResolver resolver = getContentResolver();
					// 照片的原始资源地址
					Uri originalUri = data.getData();
					try
					{
						// 使用ContentProvider通过URI获取原始图片
						Bitmap photo = MediaStore.Images.Media.getBitmap(
								resolver, originalUri);

						ExifInterface exif = null;
						if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
						{
							exif = new ExifInterface(IOUtils.getPath(User_InfoEditActivity.this, originalUri));
						}
						else
						{
							exif = new ExifInterface(
									getAbsoluteImagePath(originalUri));
						}
						
						
						int degree = getDegree(exif);
						
						if (photo != null)
						{
							// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
							Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
									photo.getWidth() / SCALE, photo.getHeight()
											/ SCALE);
							// 释放原始图片占用的内存，防止out of memory异常发生
							photo.recycle();
							if(degree!=0)
							{
								smallBitmap = rotateImage(smallBitmap,degree);
							}
							//iv_image.setImageBitmap(smallBitmap);

							ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
							smallBitmap.compress(Bitmap.CompressFormat.JPEG,
									100, baos2);
							mContent = baos2.toByteArray();

							String uploadBuffer2 = new String(
									Base64.encode(mContent));
							  if (!hasInternetConnected()){
									Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
									return;
									}
							UploadPicture(uploadBuffer2);
							System.gc();
						}
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					break;

				case CROP:
					ContentResolver resolver2 = getContentResolver();
					Uri uri = null;
					
					if (data != null)
					{
						uri = data.getData();
						System.out.println("Data");
					}
					else
					{
						System.out.println("File");
						String fileName = getSharedPreferences("temp",
								Context.MODE_WORLD_WRITEABLE).getString(
								"tempName", "");
						uri = Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), fileName));
					}
					
					//cropImageUriAfterKikat(uri, 500, 500, CROP_PICTURE);
					if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
					{
						cropImageUriAfterKikat(uri, 500, 500, CROP_PICTURE);
					}
					else
					{
						cropImageUri(uri, 500, 500, CROP_PICTURE);
					}
					
					break;

				case CROP_PICTURE:
					Bitmap photo = null;
					if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT)
					{
						//Uri photoUri = data.getData();
						//Bundle extra = data.getExtras();
						
						photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
							//photo = decodeUriAsBitmap(photoUri);//decode bitmap
							//photo = data.getParcelableExtra("data");
							//photo = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));

							
							//photo = (Bitmap) extra.get("data");
						
						if (photo != null)
						{
							//photoUri = (Uri)extra.get("data");
							//photo = BitmapFactory.decodeFile(photoUri.getPath());
							//photo = (Bitmap) extra.get("data");
//							updateUserPortrait(photo);
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							photo.compress(Bitmap.CompressFormat.JPEG, 100,
									stream);
							
							mContent = stream.toByteArray();

							String uploadBuffer2 = new String(
									Base64.encode(mContent));
							  if (!hasInternetConnected()){
									Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
									return;
									}
							UploadPicture(uploadBuffer2);
							System.gc();
						}
						 
					}
					else
					{
						if (data == null) {
				            return;
				        }//剪裁后的图片
				        Bundle extras = data.getExtras();
				        if (extras == null) {
				            return;
				        }
				        photo = extras.getParcelable("data");
				        
				        ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.JPEG, 100,
								stream);
//						updateUserPortrait(photo);
						mContent = stream.toByteArray();

						String uploadBuffer2 = new String(
								Base64.encode(mContent));
						  if (!hasInternetConnected()){
								Toast.makeText(User_InfoEditActivity.this, "网络数据获取失败，请检查网络设置", 1).show();
								return;
								}
						UploadPicture(uploadBuffer2);
						System.gc();
					}
					
					//iv_image.setImageBitmap(photo);
					
					
					break;
				default:
					break;
			}
		}
	}
//	public void updateUserProfile(final CommUser user) {
//		mCommSDK.updateUserProfile(user, new CommListener() {
//
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onComplete(Response response) {
//              
//                if (response.errCode == ErrorCode.NO_ERROR) {
//                    CommConfig.getConfig().loginedUser = user;
//                    saveUserInfo(response, user);
//                    BroadcastUtils.sendUserUpdateBroadcast(User_InfoChange.this, user);
//                	Toast.makeText(User_InfoChange.this, R.string.str001_userinfochange,
//							Toast.LENGTH_SHORT).show();
//                	 UserInfoChange();
//                } 
//                else{
//                	 showResponseToast(response);
//                
//                }
//            }
//        });
//    }
//	   private void showResponseToast(Response data) {
//	        if (data.errCode == ErrorCode.NO_ERROR) {
//	            ToastMsg.showShortMsgByResName("umeng_comm_update_info_success");
//	        } else if (data.errCode == ErrorCode.SENSITIVE_ERR_CODE) { // 昵称含有敏感词
//	            ToastMsg.showShortMsgByResName("umeng_comm_username_sensitive");
//	        } else if (data.errCode == ErrorCode.ERR_CODE_USER_NAME_DUPLICATE) { // 昵称重复
//	            ToastMsg.showShortMsgByResName("umeng_comm_duplicate_name");
//	        } else if (data.errCode == ErrorCode.ERR_CODE_USER_NAME_ILLEGAL_CHAR) {
//	            ToastMsg.showShortMsgByResName("umeng_comm_user_name_illegal_char");
//	        }else {
//	            ToastMsg.showShortMsgByResName("umeng_comm_update_userinfo_failed");
//	        }
//	    }
//	  private void saveUserInfo(Response data, CommUser newUser) {
//	        DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(newUser);
//	        CommonUtils.saveLoginUserInfo(this, newUser);
//	       
//	    }
//	 private void updateUserInfo() {
//	        final CommUser newUser = CommConfig.getConfig().loginedUser;
//	        newUser.id=user.UserID+"";
//	        newUser.name = NickName;
//	        newUser.iconUrl=user.PicURL;
//	        updateUserProfile(newUser);
//	    }
	 /**
     * 更新用户头像
     */
//    private void updateUserPortrait(final Bitmap bmp) {
//
//     
//
//        CommunityFactory.getCommSDK(getContext()).updateUserProtrait(bmp,
//                new SimpleFetchListener<PortraitUploadResponse>() {
//
//                    @Override
//                    public void onStart() {
//                    }
//
//                    @Override
//                    public void onComplete(PortraitUploadResponse response) {
//                        if (response != null && response.errCode == ErrorCode.NO_ERROR) {
//                            Log.d("", "头像更新成功 : " + response.mJsonObject.toString());
//                            CommUser user = CommConfig.getConfig().loginedUser;
//                            
//                            user.iconUrl = response.mIconUrl;
//
//                            Log.d("", "#### 登录用户的头像 : "
//                                    + CommConfig.getConfig().loginedUser.iconUrl);
//                            // 同步到数据库中
//                            syncUserIconUrlToDB(user);
//                            CommonUtils.saveLoginUserInfo(getContext(), user);
//                            BroadcastUtils.sendUserUpdateBroadcast(getContext(), user);
//                        } else {
//                            ToastMsg.showShortMsgByResName("umeng_comm_update_icon_failed");
//                        }
//                    }
//
//                });
//    }
//    private void syncUserIconUrlToDB(CommUser user) {
//        DatabaseAPI.getInstance().getUserDBAPI().saveUserInfoToDB(user);
//    }
	
	private Bitmap decodeUriAsBitmap(Uri uri)
	{
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
//	   private void mockLoginData(String nickname ) {
//		    CommunitySDK sdk = CommunityFactory.getCommSDK(this);
//		    CommUser youmenguser = new CommUser();
//		    youmenguser.name =  nickname;
//		    youmenguser.id =  user.UserID+"";
//		    youmenguser.iconUrl=user.PicURL;
//		    youmenguser.source=Source.SELF_ACCOUNT;
//		    sdk.loginToUmengServer(this, youmenguser, new LoginListener() {
//		        @Override
//		        public void onStart() {
//		        	
//		        }
//
//		        @Override
//		        public void onComplete(int stCode, CommUser commUser) {
//		            if (ErrorCode.NO_ERROR==stCode) {
//		            
//		            }
//
//		       }
//		    });

	       

	    
//	   }
public void showPicturePicker(final Context context,boolean isCrop){
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
			showMissingPermissionDialog(context);
			return;
		}
	}

	crop = isCrop;
	LayoutInflater inflater = LayoutInflater.from(this);
	View layout=inflater.inflate(R.layout.dialog_photo,null);

	builder =new AlertDialog.Builder(User_InfoEditActivity.this);
	builder.setView(layout);
	builder.setCancelable(false);
	dialog=builder.show();
	tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
	tvphoto=(TextView) layout.findViewById(R.id.dialog_phtoto_tv);
	tvalbum=(TextView) layout.findViewById(R.id.dialog_album);
	tvok.setOnClickListener(this);
	tvphoto.setOnClickListener(this);
	tvalbum.setOnClickListener(this);
}
	public void showMissingPermissionDialog(final Context context){
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout=inflater.inflate(R.layout.dialog_default_ensure,null);

		AlertDialog.Builder builder =new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(false);
		builder.create().show();
		TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		tvcontent.setText(" 请点击设置，打开所需存储权限");
		tvok.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
				startActivity(intent);
				finish();
			}
		});

	}

//	public void showPicturePicker(Context context, boolean isCrop)
//	{
//		final boolean crop = isCrop;
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle("图片来源");
//		builder.setNegativeButton("取消", null);
//		builder.setItems(new String[]
//		{ "拍照", "相册" }, new DialogInterface.OnClickListener()
//		{
//			// 类型码
//			int REQUEST_CODE;
//
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				switch (which)
//				{
//					case TAKE_PICTURE:
//						Uri imageUri = null;
//						String fileName = null;
//						Intent openCameraIntent = new Intent(
//								MediaStore.ACTION_IMAGE_CAPTURE);
//						if (crop)
//						{
//							REQUEST_CODE = CROP;
//							// 删除上一次截图的临时文件
//							SharedPreferences sharedPreferences = getSharedPreferences(
//									"temp", Context.MODE_WORLD_WRITEABLE);
//							ImageTools.deletePhotoAtPathAndName(Environment
//									.getExternalStorageDirectory()
//									.getAbsolutePath(), sharedPreferences
//									.getString("tempName", ""));
//
//							// 保存本次截图临时文件名字
//							fileName = String.valueOf(System
//									.currentTimeMillis()) + ".jpg";
//							Editor editor = sharedPreferences.edit();
//							editor.putString("tempName", fileName);
//							editor.commit();
//						}
//						else
//						{
//							REQUEST_CODE = TAKE_PICTURE;
//							fileName = "image.jpg";
//						}
//						imageUri = Uri.fromFile(new File(Environment
//								.getExternalStorageDirectory(), fileName));
//						// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//						openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//								imageUri);
//						startActivityForResult(openCameraIntent, REQUEST_CODE);
//						break;
//
//					case CHOOSE_PICTURE:
//						Intent openAlbumIntent = new Intent(
//								Intent.ACTION_GET_CONTENT);
//						if (crop)
//						{
//							REQUEST_CODE = CROP;
//						}
//						else
//						{
//							REQUEST_CODE = CHOOSE_PICTURE;
//						}
//
//
//						openAlbumIntent.setDataAndType(
//								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//								"image/*");
//						startActivityForResult(openAlbumIntent, REQUEST_CODE);
//						break;
//
//					default:
//						break;
//				}
//			}
//		});
//		builder.create().show();
//	}

	// 截取图片
//	public void cropImage(Uri uri, int outputX, int outputY, int requestCode)
//	{
//		
//		Intent intent = new Intent("com.android.camera.action.CROP",null);
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", outputX);
//		intent.putExtra("outputY", outputY);
//		intent.putExtra("scale", true);
//        intent.putExtra("scaleUpIfNeeded", true);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		intent.putExtra("noFaceDetection", true);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, requestCode);
//	}
	
	/**  
     * <br>功能简述: 4.4及以上选取照片后剪切方法 
     * <br>功能详细描述: 
     * <br>注意: 
     * @param uri 
     */  
	private void cropImageUriAfterKikat(Uri uri, int outputX, int outputY,
			int requestCode)
	{
		
		String url=getPath(User_InfoEditActivity.this,uri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false); // 返回数据bitmap
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT,  
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));  
		
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}
	
	/**  
     * <br>功能简述:4.4以下从相册选照片并剪切 
     * <br>功能详细描述: 
     * <br>注意: 
     */  
    private void cropImageUri(Uri uri, int outputX, int outputY,
			int requestCode) {  
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
//        intent.setType("image/*");  
//        intent.putExtra("crop", "true");  
//        intent.putExtra("aspectX", 1);  
//        intent.putExtra("aspectY", 1);  
//        intent.putExtra("outputX", 640);  
//        intent.putExtra("outputY", 640);  
//        intent.putExtra("scale", true);  
//        intent.putExtra("scaleUpIfNeeded", true);
//        intent.putExtra("return-data", false);  
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,  
//                Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));  
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());  
//        intent.putExtra("noFaceDetection", true); // no face detection  
//        startActivityForResult(intent, requestCode);  
    	
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);  
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
        
    } 
	
    /**  
     * <br>功能简述:对拍照的图片剪切 
     * <br>功能详细描述: 
     * <br>注意: 
     * @param uri 
     */  
    private void cameraCropImageUri(Uri uri, int outputX, int outputY,
			int requestCode) {  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/jpeg");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", 640);  
        intent.putExtra("outputY", 640);  
        intent.putExtra("scale", true);  
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            intent.putExtra("return-data", true);  
        } else {  
            intent.putExtra("return-data", false);  
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);  
        }  
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());  
        intent.putExtra("noFaceDetection", true);  
        startActivityForResult(intent, requestCode);  
    }  
    
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	private void searchHospital()
	{
		try
		{
			JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete()
			{
				public void processJsonObject(Object result)
				{
					returnvalue001 = result.toString();
					if (returnvalue001 == null)
					{

					}
					else
					{
						Map<String, Object> map;
						// 下边是具体的处理
						//{"HospitalInquiry":[{"Sort":"","ImageURL":"\/UpLoadFile\/ueditor\/2015-07-06\/60e75667-af1a-425c-af40-e024a5453799.jpg","Description":"<p><img src=\"\/UpLoadFile\/ueditor\/2015-08-06\/35648627-dd9a-41ec-aa88-fd7f1a07f996.png\" title=\"QQ截图20150806103016.png\"\/><\/p>","Phone":"","TopmdHospitalID":"1","DeleteFlag":"0","Province":"","City":"","Traffic":"","HospitalName":"郑州大学第一附属医院","UpdatedDate":"2015\/8\/6 10:27:01","IsOpen":"0","HospitalID":"46","Address":"","CreatedDate":"2015\/8\/6 10:13:37","CreatedBy":"","UpdatedBy":""},{"Sort":"","ImageURL":"\/UpLoadFile\/ueditor\/2015-07-06\/60e75667-af1a-425c-af40-e024a5453799.jpg","Description":"<p><img src=\"\/UpLoadFile\/ueditor\/2015-07-06\/60e75667-af1a-425c-af40-e024a5453799.jpg\" title=\"yifuyuan.jpg\"\/><\/p>","Phone":"","TopmdHospitalID":"","DeleteFlag":"0","Province":"","City":"","Traffic":"","HospitalName":"郑州大学第三附属医院","UpdatedDate":"2015\/7\/6 9:04:31","IsOpen":"0","HospitalID":"1","Address":"","CreatedDate":"","CreatedBy":"","UpdatedBy":""}]}
						try
						{
							JSONObject mySO = (JSONObject) result;
							JSONArray array = mySO
									.getJSONArray("HospitalInquiry");
							if(arrayList.size()>0){
								arrayList.clear();
							}
//							list.clear();
							
							for (int i = 0; i < array.length(); i++)
							{
								map = new HashMap<String, Object>();
								map.put("HospitalName", array.getJSONObject(i)
										.getString("HospitalName"));
								map.put("HospitalID", array.getJSONObject(i)
										.getString("HospitalID"));
							
								map.put("picUrl",(MMloveConstants.JE_BASE_URL+array.getJSONObject(i)
										.getString("ImageURL")).toString().replace("~", "").replace("\\", "/"));
								list.add(array.getJSONObject(i)
										.getString("HospitalName"));
								arrayList.add(map);
//								list.add(array.getJSONObject(i)
//										.getString("HospitalName"));
//								
//								if(HospitalID==Integer.valueOf(array.getJSONObject(i)
//										.getString("HospitalID")))
//								{
//									isDefault = i;
//								}
								
							}

								selectHos();


							
							
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}

			
			};

			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					User_InfoEditActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><HospitalID>0</HospitalID></Request>";

			paramMap.put("str", str);

			// 必须是这5个参数，而且得按照顺序
			task.execute(SOAP_NAMESPACE, SOAP_ACTION4, SOAP_METHODNAME4,
					SOAP_URL, paramMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	 @SuppressLint("NewApi")  
     public static String getPath(final Context context, final Uri uri) {  

         final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

         // DocumentProvider  
         if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
             // ExternalStorageProvider  
             if (isExternalStorageDocument(uri)) {  
                 final String docId = DocumentsContract.getDocumentId(uri);  
                 final String[] split = docId.split(":");  
                 final String type = split[0];  

                 if ("primary".equalsIgnoreCase(type)) {  
                     return Environment.getExternalStorageDirectory() + "/" + split[1];  
                 }  

             }  
             // DownloadsProvider  
             else if (isDownloadsDocument(uri)) {  
                 final String id = DocumentsContract.getDocumentId(uri);  
                 final Uri contentUri = ContentUris.withAppendedId(  
                         Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

                 return getDataColumn(context, contentUri, null, null);  
             }  
             // MediaProvider  
             else if (isMediaDocument(uri)) {  
                 final String docId = DocumentsContract.getDocumentId(uri);  
                 final String[] split = docId.split(":");  
                 final String type = split[0];  

                 Uri contentUri = null;  
                 if ("image".equals(type)) {  
                     contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                 } else if ("video".equals(type)) {  
                     contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
                 } else if ("audio".equals(type)) {  
                     contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
                 }  

                 final String selection = "_id=?";  
                 final String[] selectionArgs = new String[] {
                         split[1]  
                 };  

                 return getDataColumn(context, contentUri, selection, selectionArgs);  
             }  
         }  
         // MediaStore (and general)  
         else if ("content".equalsIgnoreCase(uri.getScheme())) {  
             // Return the remote address  
             if (isGooglePhotosUri(uri))  
                 return uri.getLastPathSegment();  

             return getDataColumn(context, uri, null, null);  
         }  
         // File  
         else if ("file".equalsIgnoreCase(uri.getScheme())) {  
             return uri.getPath();  
         }  

         return null;  
     }  

     /** 
      * Get the value of the data column for this Uri. This is useful for 
      * MediaStore Uris, and other file-based ContentProviders. 
      * 
      * @param context The context. 
      * @param uri The Uri to query. 
      * @param selection (Optional) Filter used in the query. 
      * @param selectionArgs (Optional) Selection arguments used in the query. 
      * @return The value of the _data column, which is typically a file path. 
      */  
     public static String getDataColumn(Context context, Uri uri, String selection,  
             String[] selectionArgs) {  

         Cursor cursor = null;  
         final String column = "_data";  
         final String[] projection = {  
                 column  
         };  

         try {  
             cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
                     null);  
             if (cursor != null && cursor.moveToFirst()) {  
                 final int index = cursor.getColumnIndexOrThrow(column);  
                 return cursor.getString(index);  
             }  
         } finally {  
             if (cursor != null)  
                 cursor.close();  
         }  
         return null;  
     }  


     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is ExternalStorageProvider. 
      */  
     public static boolean isExternalStorageDocument(Uri uri) {  
         return "com.android.externalstorage.documents".equals(uri.getAuthority());  
     }  

     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is DownloadsProvider. 
      */  
     public static boolean isDownloadsDocument(Uri uri) {  
         return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
     }  

     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is MediaProvider. 
      */  
     public static boolean isMediaDocument(Uri uri) {  
         return "com.android.providers.media.documents".equals(uri.getAuthority());  
     }  

     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is Google Photos. 
      */  
     public static boolean isGooglePhotosUri(Uri uri) {  
         return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
     }  
}

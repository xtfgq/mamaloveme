package com.netlab.loveofmum.huanxin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.netlab.loveofmum.CHK_network_anomalytwo;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.ImageTools;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.IOUtils;
import com.netlab.loveofmum.utils.ImageOptions;
import com.netlab.loveofmum.utils.ImageUtil;
import com.netlab.loveofmum.utils.PermissionsChecker;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.widget.CustomProgressDialog;
import com.netlab.loveofmum.widget.DialogEnsureCancelView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainChatActivity extends BaseActivity implements OnClickListener{
	private Timer mTimer;
	private Button mBtnSend;
	private ImageView mBtnBack;
	private TextView mBtnExit;
	private EditText mEditTextContent;
	private ListView mListView;
	private RelativeLayout mBottom;
	private TextView mBtnRcd;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDateArrays = new ArrayList<ChatMsgEntity>();
	Bitmap bitmap;
	//语音
	private ImageView chatting_mode_btn,volume;
	private boolean btn_voice = false;
	private LinearLayout del_re;
	private int flag =1;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
			voice_rcd_hint_tooshort;
	private View rcChat_popup;
	private Handler mHandler = new Handler();
	private boolean isShosrt = false;
	private ImageView img1, sc_img1,btn_photo;
	private String voiceName;
	private long startVoiceT, endVoiceT;
	private SoundMeter mSensor;
	//private ImageButton btn_face;
	private CustomProgressDialog progressDialog = null;
//	ProgressDialog pbarDialog;
	public static final int SHOW_ALL_PICTURE = 0x14;//查看图片
	public static final int SHOW_PICTURE_RESULT = 0x15;//查看图片返回
	public static final int CLOSE_INPUT = 0x01;//关闭软键盘
	public static Handler handlerInput;//用于软键盘+
	//private String photoName;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.ConsultationInquiry;
	private final String SOAP_METHODNAME = MMloveConstants.ConsultationInquiry;
	private final String SOAP_URL = MMloveConstants.URL002;

	private RelativeLayout rlbottom;
	private final String SOAP_ACTIONDES = MMloveConstants.URL001
			+ MMloveConstants.DoctorInquiry;
	private final String SOAP_METHODNAMEDES = MMloveConstants.DoctorInquiry;


	private Intent mIntnet;
	private ImageView ivhead;
	int status;
	String AskOrderID,DoctorId,hosId,DoctorUrl,DoctorName,HosName,Title;
	private TextView tv_doc,tv_hos,tv_tilte;
	private User user;
	private final String SOAP_ACTIONADD = MMloveConstants.URL001
			+ MMloveConstants.ConsultationInsert;
	private final String SOAP_METHODNAMEADD = MMloveConstants.ConsultationInsert;

	private final String SOAP_ACTIONUP = MMloveConstants.URL001
			+ MMloveConstants.AskOrderUpdate;
	private final String SOAP_METHODNAMEUP = MMloveConstants.AskOrderUpdate;
	String filename;
	private byte[] mContent;
	private final String SOAP_ACTIONPIC = MMloveConstants.URL001
			+ MMloveConstants.UploadAskPic;
	private final String SOAP_METHODNAMEPIC = MMloveConstants.UploadAskPic;

	private final String SOAP_ACTIONORDERINQUIRY= MMloveConstants.URL001
			+ MMloveConstants.AskOrderInquiry;
	private final String SOAP_METHODNAMEORDERINQUIRY= MMloveConstants.AskOrderInquiry;
	private static final int SCALE = 5;// 照片缩小比例
	private static final int ADAPTER1 = 1;
	private static final String PACKAGE_URL_SCHEME = "package:";
	private PermissionsChecker mPermissionsChecker; // 权限检测器
	// 所需的全部权限
	static final String[] PERMISSIONS = new String[]{

			android.Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	private Handler doActionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int msgId = msg.what;
			switch (msgId) {
				case 2:
					AskOrderInquiry();
					break;
				default:
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTranslucentStatus() ;
		setContentView(R.layout.chat);
		//设置启动不弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				FaceConversionUtil.getInstace().getFileText(getApplication());
//			}
//		}).start();
		mIntnet=this.getIntent();
		CHK_network_anomalytwo a = new CHK_network_anomalytwo();
		a.finish();
		iniView();
		user=LocalAccessor.getInstance(MainChatActivity.this).getUser();
		mPermissionsChecker=new PermissionsChecker(MainChatActivity.this);
		if(mIntnet!=null){
			if(mIntnet.getStringExtra("doctorpush")!=null&&mIntnet.getStringExtra("doctorpush").equals("push")){
				AskOrderID=mIntnet.getStringExtra("ID");

				initData();

			}else {
				AskOrderID = mIntnet.getStringExtra("AskOrderID");
				DoctorId = mIntnet.getStringExtra("DoctorId");
				hosId = mIntnet.getStringExtra("HospitalID");
				DoctorUrl = mIntnet.getStringExtra("DoctorImageUrl");
				DoctorName = mIntnet.getStringExtra("DoctorName");
				HosName = mIntnet.getStringExtra("Hosname");
				Title = mIntnet.getStringExtra("Title");
				status = mIntnet.getIntExtra("Status", -1);
				if (status != -1 && status == 2) {
//					Intent i = new Intent(MainChatActivity.this, MainchatDialog.class);
//					i.putExtra("content", "请直接描述您的症状、疾病和身体情况，便于医生快速帮您解决问题。");
//					startActivity(i);
					status = 3;
					AskOrderUpdate(status);
				}

				user = LocalAccessor.getInstance(MainChatActivity.this).getUser();
				tv_doc.setText(DoctorName);
				tv_hos.setText(HosName);
				tv_tilte.setText(Title);
				ImageLoader.getInstance().displayImage(DoctorUrl, ivhead, ImageOptions.getTalkOptions(120));
				initData();
			}
		}



		mEditTextContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

		//改变默认的单行模式
		mEditTextContent.setSingleLine(false);
		//水平滚动设置为False
		mEditTextContent.setHorizontallyScrolling(false);


	}
	@Override
	protected void onNewIntent(Intent intent) {
		user=LocalAccessor.getInstance(MainChatActivity.this).getUser();
		if(intent!=null){
			if(intent.getStringExtra("doctorpush")!=null&&intent.getStringExtra("doctorpush").equals("push")){
				if(mTimer!=null){
					mTimer.cancel();
				}
				AskOrderID=intent.getStringExtra("ID");
				initData();
			}
		}

	}

	private void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在加载中...");
		}

		progressDialog.show();
	}

	private void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	private void setTimerTask() {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 2;
				doActionHandler.sendMessage(message);
			}
		}, 2000, 6000);
	}
	private void showExit(){
		DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
				MainChatActivity.this).setDialogMsg("友情提示",
				"确定要结束本次咨询", "确定").setOnClickListenerEnsure(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						AskOrderUpdate(4);

					}
				});
		DialogUtils.showSelfDialog(MainChatActivity.this, dialogEnsureCancelView);
//		new AlertDialog.Builder(MainChatActivity.this)
//		.setTitle("友情提示")
//		.setMessage("确定要结束本次咨询！")
//		.setPositiveButton("确定",
//				new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(
//							DialogInterface dialog,
//							int which)
//					{
//						AskOrderUpdate(4);
//					}
//
//				})
//	.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			dialog.dismiss();
//		}
//	})
//				.show();// 在按键响应事件中显示此对话框
	}
	protected void AskOrderUpdate(final int status) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
//				{"AskOrderUpdate":[{"MessageContent":"成功！","MessageCode":"0"}]}
				try {
					JSONObject mySO = (JSONObject) result;

					JSONArray array = mySO
							.getJSONArray("AskOrderUpdate");
					if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
//					 finish();
						if(status==4){
							finish();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MainChatActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();

		String str = "<Request><AskOrderID>%s</AskOrderID><OrderStatus>%s</OrderStatus></Request>";
		str = String.format(str, new Object[]
				{ AskOrderID,"0"+status});

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONUP, SOAP_METHODNAMEUP,
				SOAP_URL, paramMap);
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
		tintManager.setStatusBarTintResource(R.drawable.bg_header);//状态栏无背景
	}
	//按下语音录制
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!Environment.getExternalStorageDirectory().exists()){
			Toast.makeText(this, "无内存卡，请安装..", 2).show();
			return false;
		}
		if(btn_voice){
			System.out.println("1");
			int[] location = new int[2];
			mBtnRcd.getLocationInWindow(location);
			int btn_rc_Y = location[1];
			int btn_rc_X = location[0];
			int[] del_location = new int[2];
			del_re.getLocationInWindow(del_location);
			int del_Y = del_location[1];
			int del_x = del_location[0];
			if(event.getAction()==MotionEvent.ACTION_DOWN&&flag==1){
				if(!Environment.getExternalStorageDirectory().exists()){
					Toast.makeText(this, "无内存卡，请安装..", 2).show();
					return false;
				}
				System.out.println("2");
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//判断手势按下的位置是否是语音录制按钮的范围内
					System.out.println("3");
					mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				}
			}else if(event.getAction() == MotionEvent.ACTION_UP && flag == 2){
				System.out.println("4");
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					stop();
					flag = 1;
					File file = new File(Environment.getExternalStorageDirectory()+"/"
							+ voiceName);
					if (file.exists()) {
						file.delete();
					}
				}else{
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					endVoiceT = SystemClock.currentThreadTimeMillis();
					flag = 1;
					int time = (int) ((endVoiceT - startVoiceT) / 50);
					if (time < 1) {
						isShosrt = true;
						voice_rcd_hint_loading.setVisibility(View.GONE);
						voice_rcd_hint_rcding.setVisibility(View.GONE);
						voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
						mHandler.postDelayed(new Runnable() {
							public void run() {
								voice_rcd_hint_tooshort
										.setVisibility(View.GONE);
								rcChat_popup.setVisibility(View.GONE);
								isShosrt = false;
							}
						}, 500);
						return false;
					}


					mListView.setSelection(mListView.getCount() - 1);
					rcChat_popup.setVisibility(View.GONE);
				}
			}
			if (event.getY() < btn_rc_Y) {//手势按下的位置不在语音录制按钮的范围内
				System.out.println("5");
				Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(this,
						R.anim.cancel_rc2);
				img1.setVisibility(View.GONE);
				del_re.setVisibility(View.VISIBLE);
				del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
				if (event.getY() >= del_Y
						&& event.getY() <= del_Y + del_re.getHeight()
						&& event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
					sc_img1.startAnimation(mLitteAnimation);
					sc_img1.startAnimation(mBigAnimation);
				}
			} else {

				img1.setVisibility(View.VISIBLE);
				del_re.setVisibility(View.GONE);
				del_re.setBackgroundResource(0);
			}
		}
		return super.onTouchEvent(event);
	}

	private void start(String name){
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}
	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private static final int POLL_INTERVAL = 1000;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	public void initData(){
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){
			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("ConsultationInquiry");

					if(mDateArrays.size()>0){
						mDateArrays.clear();

					}
					DoctorId=array.getJSONObject(0).getString("DoctorID").toString();

//					if(mIntnet.getStringExtra("doctorpush")!=null&&mIntnet.getStringExtra("doctorpush").equals("push")) {
						searchDocDes();
//					}

						for (int i = 0; i < array.length(); i++) {
							ChatMsgEntity entity = new ChatMsgEntity();
							String time = array.getJSONObject(i).getString("CreatedDate").toString().replace("/", "-");
							entity.setDate(time);

//					entity.setName(array.getJSONObject(i).getString("AuthorName").toString());
							if ("0".equals(array.getJSONObject(i).getString("UserType").toString())) {
								entity.setMsgType(false);
								entity.setHeadurl(user.PicURL);
							} else {

								String sss = array.getJSONObject(i).getString("DocPicURL").toString();
								entity.setHeadurl(MMloveConstants.JE_BASE_URL2 + array.getJSONObject(i).getString("DocPicURL").toString().replace("~", "").replace("\\", "/"));
								entity.setMsgType(true);
							}
							if ("0".equals(array.getJSONObject(i).getString("MsgType").toString())) {
								entity.setMsgtype(0);
								entity.setText(array.getJSONObject(i).getString("ConsultationContent").toString());
								entity.setContent("");
							} else {
								entity.setMsgtype(1);
								entity.setContent(MMloveConstants.JE_BASE_URL2 + array.getJSONObject(i).getString("PicURL").toString().replace("~", "").replace("\\", "/"));
								entity.setText("");
							}

							mDateArrays.add(entity);
						}
						if (mAdapter == null) {
							mAdapter = new ChatMsgViewAdapter(MainChatActivity.this, mDateArrays);
							mListView.setAdapter(mAdapter);
						} else {
							mAdapter.notifyDataSetChanged();
						}
						mEditTextContent.setText("");
						mListView.setSelection(mListView.getCount() - 1);


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MainChatActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><DoctorID>%s</DoctorID><AuthorID>%s</AuthorID><AskOrderID>%s</AskOrderID><UserType>%s</UserType><IsViewed>%s</IsViewed></Request>";
//		str = String.format(str, new Object[]
//				{ DoctorId,user.UserID+"",AskOrderID,"1",""});

		str = String.format(str, new Object[]
				{ "","",AskOrderID,"1",""});
		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
				SOAP_URL, paramMap);


	}
	/**
	 * 查询医生详情
	 */
	private void searchDocDes() {
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub

				String returnvalue = result.toString();



				if (returnvalue != null) {
					Map<String, Object> map;
					try {
						JSONObject mySO = (JSONObject) result;

						JSONArray array = mySO.getJSONArray("DoctorInquiry");


						hosId=array.getJSONObject(0).getString("HospitalID");


								Title=array.getJSONObject(0).getString("Title").toString();
						DoctorName=array.getJSONObject(0).getString("DoctorName").toString();
						DoctorUrl=MMloveConstants.JE_BASE_URL2
								+ array.getJSONObject(0).getString("ImageURL")
								.replace("~", "").replace("\\", "/");
						HosName=array.getJSONObject(0)
								.getString("HospitalName").toString();
						tv_doc.setText(DoctorName);
						tv_hos.setText(HosName);
						tv_tilte.setText(Title);
						ImageLoader.getInstance().displayImage(DoctorUrl, ivhead, ImageOptions.getTalkOptions(120));
						AskOrderPushInquiry();
//						mTimer = new Timer();
//						setTimerTask();





					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		};
		// TODO Auto-generated method stub
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(MainChatActivity.this, true,
				doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();

		String str = "<Request><DoctorID>%s</DoctorID><UserID>%s</UserID><HospitalID>%s</HospitalID><IsPMD>%s</IsPMD></Request>";

			str = String.format(str, new Object[] { DoctorId, user.UserID + "",
					"", 1+"" });


		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONDES, SOAP_METHODNAMEDES, SOAP_URL,
				paramMap);

	}
	public void upData(){
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){
			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				//{"ConsultationInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}
			
				JSONObject mySO = (JSONObject) result;

				try {
					JSONArray array = mySO.getJSONArray("ConsultationInquiry");
					if(array.getJSONObject(0).has("MessageCode")){

					}
					else{
//					mDateArrays.clear();
						for(int i=0;i<array.length();i++){
							ChatMsgEntity entity = new ChatMsgEntity();
							String time = array.getJSONObject(i).getString("CreatedDate").toString().replace("/", "-");
							entity.setDate(time);
							entity.setName(array.getJSONObject(i).getString("AuthorName").toString());
							if("0".equals(array.getJSONObject(i).getString("UserType").toString())){
								entity.setMsgType(false);
								entity.setHeadurl(user.PicURL);
							}else{
								entity.setHeadurl(DoctorUrl);
								entity.setMsgType(true);
							}
							if("0".equals(array.getJSONObject(i).getString("MsgType").toString())){
								entity.setMsgtype(0);
								entity.setText(array.getJSONObject(i).getString("ConsultationContent").toString());
								entity.setContent("");
							}else{
								entity.setMsgtype(1);
								entity.setContent( MMloveConstants.JE_BASE_URL2+array.getJSONObject(i).getString("PicURL").toString().replace("~", "").replace("\\", "/"));
								entity.setText("");
							}
							mDateArrays.add(entity);
						}

						mAdapter.notifyDataSetChanged();
						mListView.setSelection(mListView.getCount()-1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MainChatActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><DoctorID>%s</DoctorID><AuthorID>%s</AuthorID><AskOrderID>%s</AskOrderID><UserType>%s</UserType><IsViewed>%s</IsViewed></Request>";
		str = String.format(str, new Object[]
				{ DoctorId,user.UserID+"",AskOrderID,"1","0"});
		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME,
				SOAP_URL, paramMap);


	}
	private void AskOrderInquiry() {


		// 必须是这5个参数，而且得按照顺序
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				try {
//					{"AskOrderInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}

					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
							.getJSONArray("AskOrderInquiry");
					int status=Integer.valueOf(array.getJSONObject(0)
							.getString("OrderStatus").toString());
					if(status==4){

						mTimer.cancel();

						ExitChatDialog(MainChatActivity.this);

					}else{

						upData();
					}



				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();


				}

			}};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MainChatActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>" +
				"<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><AskOrderID>%s</AskOrderID></Request>";

		str = String.format(str, new Object[]
				{ user.UserID+"","","","","",hosId,HosName,"","",DoctorId,DoctorName,user.RealName,user.UserNO,user.UserMobile,AskOrderID});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONORDERINQUIRY, SOAP_METHODNAMEORDERINQUIRY,
				SOAP_URL, paramMap);
	}
	private void AskOrderPushInquiry() {


		// 必须是这5个参数，而且得按照顺序
		JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue= result.toString();
				try {
//					{"AskOrderInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}

					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO
							.getJSONArray("AskOrderInquiry");
					int status=Integer.valueOf(array.getJSONObject(0)
							.getString("OrderStatus").toString());
					if(status==4){
							rlbottom.setVisibility(View.INVISIBLE);
						mBtnExit.setVisibility(View.INVISIBLE);
					}else{
						mTimer = new Timer();
						setTimerTask();
						mBtnExit.setVisibility(View.VISIBLE);
					}



				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();


				}

			}};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(
				MainChatActivity.this, true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>" +
				"<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><AskOrderID>%s</AskOrderID></Request>";

		str = String.format(str, new Object[]
				{ user.UserID+"","","","","",hosId,HosName,"","",DoctorId,DoctorName,user.RealName,user.UserNO,user.UserMobile,AskOrderID});

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONORDERINQUIRY, SOAP_METHODNAMEORDERINQUIRY,
				SOAP_URL, paramMap);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
//			if (hasInternetConnected()){
//			showExit();
//			}
				finish();

				break;
			case R.id.btn_send:
				send();
//        	((FaceRelativeLayout)findViewById(R.id.FaceRelativeLayout)).hideFaceView();
				break;
			case R.id.btn_photo:
				InputMethodManager imm = (InputMethodManager)getSystemService(MainChatActivity.this.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(),0);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
						showMissingPermissionDialog(MainChatActivity.this);
						return;
					}
				}

				new PopupWindows(MainChatActivity.this,btn_photo);
				// 隐藏表情选择框
//        	((FaceRelativeLayout)findViewById(R.id.FaceRelativeLayout)).hideFaceView();
				break;
			case R.id.btn_exit:
				showExit();
				break;
			case R.id.et_sendmessage:
//        	mEditTextContent.addTextChangedListener(new TextWatcher() {
//				@Override
//				public void afterTextChanged(Editable s) {
//					String text = mEditTextContent.getText().toString();
//					if(!(text.length()>0)){
//						//Toast.makeText(MainActivity.this,mEditTextContent.getText()+"请输入聊天内容", 2).show();
//						btn_photo.setVisibility(View.VISIBLE);
//						mBtnSend.setVisibility(View.GONE);
//					}else{
//						//Toast.makeText(MainActivity.this,"输入的聊天内容为："+mEditTextContent.getText(), 2).show();
//						btn_photo.setVisibility(View.GONE);
//						mBtnSend.setVisibility(View.VISIBLE);
//					}
//				}
//				@Override
//				public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//				}
//				@Override
//				public void beforeTextChanged(CharSequence s, int arg1, int arg2,
//						int arg3) {
//				}
//			});
				// 隐藏表情选择框
//        	((FaceRelativeLayout)findViewById(R.id.FaceRelativeLayout)).hideFaceView();
		}
	}
	public void ExitChatDialog(final Context context){
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout=inflater.inflate(R.layout.dialog_default_ensure,null);

		Builder builder =new Builder(MainChatActivity.this);
		builder.setView(layout);
		builder.setCancelable(false);
		builder.create().show();
		TextView tvok=(TextView) layout.findViewById(R.id.dialog_default_click_ensure);
		TextView tvcontent=(TextView) layout.findViewById(R.id.dialog_default_click_text_msg);
		tvcontent.setText("该医生已经结束咨询");
		tvok.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}
		});

	}
	public void showMissingPermissionDialog(final Context context){
		DialogEnsureCancelView dialogEnsureCancelView = new DialogEnsureCancelView(
				MainChatActivity.this).setDialogMsg("提示",  "请点击设置，打开所需存储权限", "确定")
				.setOnClickListenerEnsure(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
						startActivity(intent);

					}
				});
		DialogUtils.showSelfDialog(MainChatActivity.this, dialogEnsureCancelView);


	}

	public void send(){

//		startProgressDialog();
		mBtnSend.setBackgroundResource(R.drawable.send_post_un);
		mBtnSend.setEnabled(false);
		String conString = Html.toHtml(mEditTextContent.getText());
		String strl=mEditTextContent.getText().toString().trim();
		InputMethodManager imm = (InputMethodManager)getSystemService(MainChatActivity.this.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(),0);
		if(strl.length()>0&&conString.length()>0){
			ChatMsgEntity entity = new ChatMsgEntity();

			JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

				@Override
				public void processJsonObject(Object result) {
					// TODO Auto-generated method stub
					String returnvalue= result.toString();
//					{"ConsultationInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
					try {  JSONObject mySO = (JSONObject) result;

						JSONArray array = mySO
								.getJSONArray("ConsultationInsert");
//					stopProgressDialog();
						mBtnSend.setEnabled(true);
						if("0".equals(array.getJSONObject(0).getString("MessageCode"))){
							initData();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mBtnSend.setEnabled(true);
					}

				}};


			JsonAsyncTask_Info task = new JsonAsyncTask_Info(
					MainChatActivity.this, true, doProcess);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String str = "<Request><ConsultationContent>%s</ConsultationContent><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><AuthorID>%s</AuthorID><AuthorName>%s</AuthorName>" +
					"<Fromto>%s</Fromto><UserType>%s</UserType><AskOrderID>%s</AskOrderID><MsgType>%s</MsgType><FileName>%s</FileName>" +
					"<Img>%s</Img></Request>";


			str = String.format(str, new Object[]
					{ conString.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\n\t"),DoctorId,hosId,user.UserID+"",user.NickName,"5","0",AskOrderID,"0","",""});

			paramMap.put("str", str);
			task.execute(SOAP_NAMESPACE, SOAP_ACTIONADD, SOAP_METHODNAMEADD,
					SOAP_URL, paramMap);

			// TODO Auto-generated catch block


//	        mDateArrays.add(entity);
//
//	        mAdapter.notifyDataSetChanged();
//	        mEditTextContent.setText("");
//	        mListView.setSelection(mListView.getCount()-1);
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	public String getDate(){
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH)+1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year+"-"+month+"-"+day+"-"+hour+":"+mins);
		return sbBuffer.toString();
	}
	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
			case 0:
			case 1:
				volume.setImageResource(R.drawable.amp1);
				break;
			case 2:
			case 3:
				volume.setImageResource(R.drawable.amp2);

				break;
			case 4:
			case 5:
				volume.setImageResource(R.drawable.amp3);
				break;
			case 6:
			case 7:
				volume.setImageResource(R.drawable.amp4);
				break;
			case 8:
			case 9:
				volume.setImageResource(R.drawable.amp5);
				break;
			case 10:
			case 11:
				volume.setImageResource(R.drawable.amp6);
				break;
			default:
				volume.setImageResource(R.drawable.amp7);
				break;
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mTimer!=null) {
			mTimer.cancel();
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

	private Uri imageUri;// 图片的路径
	public class PopupWindows extends PopupWindow
	{

		public PopupWindows(Context mContext, View parent)
		{

			super(mContext);

			View view = View
					.inflate(mContext, R.layout.item_popubwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
//				photo();
//				dismiss();
//				Intent intent = new Intent();
//				Intent intent_camera = getPackageManager().getLaunchIntentForPackage("com.android.camera");
//				if (intent_camera != null) {
//					intent.setPackage("com.android.camera");
//				}
//				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

					Uri imageUri = null;
					String fileName = null;
					Intent openCameraIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);


					fileName = "tmpimage.jpg";

					imageUri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), fileName));
					// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							imageUri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE);

					dismiss();

//				if (Environment.MEDIA_MOUNTED.equals(Environment
//						.getExternalStorageState())) {
//					fileTMP = new File(new File(Environment
//							.getExternalStorageDirectory().getPath() + "/mmlove"),
//							System.currentTimeMillis() + ".png");
//				} else {
//					fileTMP = new File(this.getCacheDir(), System.currentTimeMillis()
//							+ ".png");
//				}
//				imageUri = Uri.fromFile(fileTMP);
//
//
//				// 指定调用相机拍照后照片的储存路径
//				//cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, tmpImageUri);
//				Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				// 指定调用相机拍照后照片的储存路径
//				cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//				MainChatActivity.this.startActivityForResult(cameraintent, TAKE_PICTURE);
//				dismiss();
				}


			});
			bt2.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
//				Intent intent = new Intent(MainActivity.this,
//						TestPicActivity.class);
//				startActivity(intent);
//				dismiss();
				/*Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
				startActivity(intent);
				dismiss();*/

					Intent openAlbumIntent = new Intent(
							Intent.ACTION_GET_CONTENT);
					openAlbumIntent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(openAlbumIntent, SHOW_ALL_PICTURE);

//				openAlbumIntent.setDataAndType(
//						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//						"image/*");
//				startActivityForResult(openAlbumIntent, REQUEST_CODE);
//				Intent intent=new Intent(MainChatActivity.this,ScaleImageFromSdcardActivity.class);

					dismiss();
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);//设置切换动画，从右边进入，左边退出
				}
			});
			bt3.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					dismiss();
				}
			});

		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==-1){
			if(requestCode==TAKE_PICTURE){

//	   new android.text.format.DateFormat();
//	   String name= android.text.format.DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
//	   Bundle bundle = data.getExtras();
//	   filename = null;
//	   bitmap = (Bitmap)bundle.get("data");
//	   ByteArrayOutputStream fout = null;
//	 //定义文件存储路径
//	   File file = new File("/sdcard/cloudteam/");
//	   if(!file.exists()){
//		   file.mkdirs();
//	   }
//	   filename=name;
				Bitmap bitmap,newBitmap;
				if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
					newBitmap = BitmapFactory.decodeFile(ImageUtil.doPicture(Environment
							.getExternalStorageDirectory() + "/tmpimage.jpg"));
				}else{
					bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/tmpimage.jpg");
					newBitmap = ImageTools.zoomBitmap(bitmap,
							bitmap.getWidth() / SCALE, bitmap.getHeight()
									/ SCALE);
					// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
					bitmap.recycle();
				}



				String url = Environment.getExternalStorageDirectory() + "/tmpimage.jpg";
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
				filename=getPhotoFileName();
				String uploadBuffer2 = new String(
						Base64.encode(mContent));
				startProgressDialog();
				JsonAsyncTaskOnComplete doProcess=new JsonAsyncTaskOnComplete(){

					@Override
					public void processJsonObject(Object result) {
						// TODO Auto-generated method stub
						String returnvalue= result.toString();
//					{"ConsultationInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
						try {  JSONObject mySO = (JSONObject) result;

							JSONArray array = mySO
									.getJSONArray("ConsultationInsert");
						stopProgressDialog();
							if("0".equals(array.getJSONObject(0).getString("MessageCode"))){

								initData();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}};

				JsonAsyncTask_Info task = new JsonAsyncTask_Info(
						MainChatActivity.this, true, doProcess);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				String str = "<Request><ConsultationContent>%s</ConsultationContent><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><AuthorID>%s</AuthorID><AuthorName>%s</AuthorName>" +
						"<Fromto>%s</Fromto><UserType>%s</UserType><AskOrderID>%s</AskOrderID><MsgType>%s</MsgType><FileName>%s</FileName>" +
						"<Img>%s</Img></Request>";
				str = String.format(str, new Object[]
						{ "",DoctorId,hosId,user.UserID+"",user.NickName,"5","0",AskOrderID,"1",filename,uploadBuffer2});

				paramMap.put("str", str);
				task.execute(SOAP_NAMESPACE, SOAP_ACTIONPIC, SOAP_METHODNAMEPIC,
						SOAP_URL, paramMap);
//	        mDateArrays.add(entity);
//	        mAdapter.notifyDataSetChanged();
//	        mEditTextContent.setText("");
//	        mListView.setSelection(mListView.getCount()-1);

			}

			else if(requestCode==SHOW_ALL_PICTURE) {
				try {
					ContentResolver resolver = getContentResolver();
					// 照片的原始资源地址
					Uri originalUri = data.getData();
					long mid = ImageUtil.getAutoFileOrFilesSize(ImageUtil.getImageAbsolutePath(MainChatActivity.this, originalUri));
					if (mid > 6 * 1024 * 1024) {
						Toast.makeText(MainChatActivity.this, "图片尺寸过大", 1).show();
						return;
					}
					// 使用ContentProvider通过URI获取原始图片

//		   Bitmap photo = MediaStore.Images.Media.getBitmap(
//				   resolver, originalUri);

					Bitmap photo  = BitmapFactory.decodeFile(ImageUtil.doPicture(ImageUtil.getImageAbsolutePath(MainChatActivity.this, originalUri)));

					ExifInterface exif = null;
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
						exif = new ExifInterface(IOUtils.getPath(MainChatActivity.this, originalUri));
					} else {
						exif = new ExifInterface(
								getAbsoluteImagePath(originalUri));
					}

					int degree = getDegree(exif);
					String path = ImageUtil.doPicture(ImageUtil.getUriString(data.getData(), getContentResolver()));
					startProgressDialog();
					if (photo != null) {
						filename = getPhotoFileName();
//						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
//								photo.getWidth() / SCALE, photo.getHeight()
//										/ SCALE);
//						photo.recycle();
						if (degree != 0) {
							photo = rotateImage(photo, degree);
						}
						ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.JPEG,
								100, baos2);
						mContent = baos2.toByteArray();
						String uploadBuffer1 = new String(
								Base64.encode(mContent));
						JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

							@Override
							public void processJsonObject(Object result) {
								// TODO Auto-generated method stub
								String returnvalue = result.toString();
//						{"ConsultationInsert":[{"MessageContent":"成功！","MessageCode":"0"}]}
								try {
									JSONObject mySO = (JSONObject) result;

									JSONArray array = mySO
											.getJSONArray("ConsultationInsert");
								stopProgressDialog();
									if ("0".equals(array.getJSONObject(0).getString("MessageCode"))) {

										initData();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						};

						JsonAsyncTask_Info task = new JsonAsyncTask_Info(
								MainChatActivity.this, true, doProcess);
						Map<String, Object> paramMap = new HashMap<String, Object>();
						String str = "<Request><ConsultationContent>%s</ConsultationContent><DoctorID>%s</DoctorID><HospitalID>%s</HospitalID><AuthorID>%s</AuthorID><AuthorName>%s</AuthorName>" +
								"<Fromto>%s</Fromto><UserType>%s</UserType><AskOrderID>%s</AskOrderID><MsgType>%s</MsgType><FileName>%s</FileName>" +
								"<Img>%s</Img></Request>";
						str = String.format(str, new Object[]
								{"", DoctorId, hosId, user.UserID + "", user.NickName, "5", "0", AskOrderID, "1", filename, uploadBuffer1});

						paramMap.put("str", str);
						task.execute(SOAP_NAMESPACE, SOAP_ACTIONPIC, SOAP_METHODNAMEPIC,
								SOAP_URL, paramMap);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		AssetFileDescriptor fileDescriptor =null;
		try {
			fileDescriptor = this.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
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
	private static final int TAKE_PICTURE = 0x000000;
	private static final int SHOW_CAMERA = 0x000001;
	private static final int SHOW_CAMERA_RESULT = 0x000002;
	private String path = "";
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


	public void photo()
	{
		String status=Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED))
		{
			File dir=new File(Environment.getExternalStorageDirectory() + "/myimage/");
			if(!dir.exists())dir.mkdirs();

			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(dir, String.valueOf(System.currentTimeMillis())
					+ ".jpg");
			path = file.getPath();
			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
//		ChatMsgEntity entity = new ChatMsgEntity();
//		entity.setDate(getDate());
//		entity.setName("古月哥欠");
//		entity.setMsgType(false);
//		entity.setText(path);
//		mDateArrays.add(entity);
//		mAdapter.notifyDataSetChanged();
//		mListView.setSelection(mListView.getCount() - 1);

		}
		else{
			Toast.makeText(MainChatActivity.this, "没有储存卡",Toast.LENGTH_LONG).show();
		}
	}
	public void head_xiaohei(View v) { // 标题栏 返回按钮

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		tv_doc = (TextView) findViewById(R.id.tv_doc);
		tv_hos=(TextView) findViewById(R.id.tv_hos);
		tv_tilte=(TextView)findViewById(R.id.tv_tiltle);
		btn_photo = (ImageView)findViewById(R.id.btn_photo);
		mListView = (ListView)findViewById(R.id.listview);
		mBtnSend = (Button)findViewById(R.id.btn_send);
		mBtnBack = (ImageView)findViewById(R.id.btn_back);
		chatting_mode_btn = (ImageView)findViewById(R.id.ivPopUp);
		mEditTextContent = (EditText)findViewById(R.id.et_sendmessage);
		mBtnRcd = (TextView)findViewById(R.id.btn_rcd);
		mBottom = (RelativeLayout)findViewById(R.id.btn_bottom);
		del_re = (LinearLayout)this.findViewById(R.id.del_re);
		volume = (ImageView)this.findViewById(R.id.volume);
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		voice_rcd_hint_rcding = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this
				.findViewById(R.id.voice_rcd_hint_tooshort);
		mBtnExit=(TextView) findViewById(R.id.btn_exit);
		ivhead=(ImageView) findViewById(R.id.ivhead);
		rlbottom=(RelativeLayout)findViewById(R.id.rl_bottom);
		mSensor = new SoundMeter();
		mBtnBack.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);
		btn_photo.setOnClickListener(this);
		mEditTextContent.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);
		//btn_face.setOnClickListener(this);
		chatting_mode_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(btn_voice){
					mBtnRcd.setVisibility(View.GONE);
					mBottom.setVisibility(View.VISIBLE);
					btn_voice = false;
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);
//				((FaceRelativeLayout)findViewById(R.id.FaceRelativeLayout)).hideFaceView();
				}else{
					mBtnRcd.setVisibility(View.VISIBLE);
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_voice = true;
//				((FaceRelativeLayout)findViewById(R.id.FaceRelativeLayout)).hideFaceView();
				}
			}
		});
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		mEditTextContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String text = mEditTextContent.getText().toString();
				if(!(text.length()>0)){
					//Toast.makeText(MainActivity.this,mEditTextContent.getText()+"请输入聊天内容", 2).show();
					btn_photo.setVisibility(View.VISIBLE);
					mBtnSend.setVisibility(View.GONE);
				}else{
					//Toast.makeText(MainActivity.this,"输入的聊天内容为："+mEditTextContent.getText(), 2).show();
					btn_photo.setVisibility(View.GONE);
					mBtnSend.setVisibility(View.VISIBLE);
					mBtnSend.setBackgroundResource(R.drawable.send_post);
				}
			}
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
										  int arg3) {
			}
		});
	}

	public Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int)scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

}

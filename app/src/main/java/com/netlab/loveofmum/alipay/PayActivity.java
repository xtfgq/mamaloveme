package com.netlab.loveofmum.alipay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.netlab.loveofmum.CHK_network_anomalythree;
import com.netlab.loveofmum.CHK_network_anomalytwo;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.BaseActivity;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.api.MyApplication;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.utils.DialogUtils;
import com.netlab.loveofmum.utils.SystemStatusManager;
import com.netlab.loveofmum.utils.SystemUtils;
import com.netlab.loveofmum.widget.DialogEnsureView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends BaseActivity {
	private IWXAPI api;
	private RadioButton rb_weixin, rb_zhifubao;
	private ImageView iv_back;
	private TextView tvhead;
	private View vTop;
	private String AskOrderID, DoctorId, Hosid;
	private Intent mIntnet;
	private User user;
	private String DoctorUrl;
	private Button pay;
	private MyApplication myApp;
	String AskByOne, DoctorName, HosName, Title;
	int paytype;
	private TextView jiage;
	private TextView tvdocname;
	// 定义Web Service相关的字符串
	private final String SOAP_NAMESPACE = MMloveConstants.URL001;
	private final String SOAP_ACTION = MMloveConstants.URL001
			+ MMloveConstants.AskOrderUpdate;
	private final String SOAP_METHODNAME = MMloveConstants.AskOrderUpdate;
	private final String SOAP_URL = MMloveConstants.URL002;

	private final String SOAP_ZHIFUACTION = MMloveConstants.URL001
			+ MMloveConstants.GetAccount;
	private final String SOAP_ZHIFUMETHODNAME = MMloveConstants.GetAccount;
	// 合作身份者id，以2088开头的16位纯数字
	public static String PARTNER;
	// 收款支付宝账号
	public static String SELLER;

	// 商户私钥，自助生成
	public static String RSA_PRIVATE;
	public static String RSA_PUBLIC;
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private static final String libSoName = "LoveOfMum";

	private final String SOAP_ACTIONORDERINQUIRY = MMloveConstants.URL001
			+ MMloveConstants.AskOrderInquiry;
	private final String SOAP_METHODNAMEORDERINQUIRY = MMloveConstants.AskOrderInquiry;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT)
							.show();
					// AskOrderUpdate 结束本次咨询
					// string OrderID = "";
					// string OrderStatus = "";
					// string Reason = "";
					// string PayType = "";
					// string PayStatus = "";
					user.PayId = AskOrderID;

					if (hasInternetConnected()) {
						myApp.DoctorId = DoctorId;
						myApp.Hosid = Hosid;
						myApp.DoctorUrl = DoctorUrl;
						myApp.DoctorName = DoctorName;
						myApp.HosName = HosName;
						myApp.Title = Title;
						AskOrderUpdate(1);
					} else {
						Intent i = new Intent(PayActivity.this,
								CHK_network_anomalythree.class);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", DoctorId);
						i.putExtra("HospitalID", Hosid);
						i.putExtra("DoctorImageUrl", DoctorUrl);
						startActivity(i);
					}
					try {
						LocalAccessor.getInstance(PayActivity.this).updateUser(
								user);
						if (!hasInternetConnected()) {
							Toast.makeText(PayActivity.this,
									"网络数据获取失败，请检查网络设置", 1).show();
							return;
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

						Toast.makeText(PayActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
			pay.setEnabled(true);
		};
	};
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	};
   @Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	MobclickAgent.onPause(this);
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTranslucentStatus();
		setContentView(R.layout.layout_weixinqqzhifu);
		api = WXAPIFactory.createWXAPI(this, "wx2a644a43882160a9");
		iniView();
		mIntnet = this.getIntent();
		RSA_PRIVATE = getStringFromJNI();
		RSA_PUBLIC = getStringFromPubJNI();
		myApp = MyApplication.getInstance();
		if (mIntnet != null) {
			AskOrderID = mIntnet.getStringExtra("AskOrderID");
			DoctorId = mIntnet.getStringExtra("DoctorId");
			Hosid = mIntnet.getStringExtra("HospitalID");
			DoctorUrl = mIntnet.getStringExtra("DoctorImageUrl");
			DoctorName = mIntnet.getStringExtra("DoctorName");
			HosName = mIntnet.getStringExtra("Hosname");
			AskByOne = mIntnet.getStringExtra("AskByOne");
			Title = mIntnet.getStringExtra("Title");
			tvdocname.setText(DoctorName);
			jiage.setText(AskByOne + "元");

		}
		user = LocalAccessor.getInstance(PayActivity.this).getUser();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			int h = getStatusHeight(this);
			LayoutParams params = vTop.getLayoutParams();
			params.height = h;
			params.width = LayoutParams.FILL_PARENT;
			vTop.setLayoutParams(params);
		} else {
			vTop.setVisibility(View.GONE);
		}
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		if(hasInternetConnected()){
			getZhifuBao();

		}else{
			Toast.makeText(PayActivity.this,R.string.msgUninternet,1).show();
			return;
		}
		paytype = 0;
		rb_weixin.setChecked(true);
		rb_zhifubao.setChecked(false);
		// AskOrderUpdate(1);
	}

	private void getZhifuBao() {
		// TODO Auto-generated method stub
		// { \"GetAccount\":[{ \"Partner\":
		// \"2088911769975053\",\"Seller\":\"hlzhao@ha.edu.cn\"}]}
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				String returnvalue = result.toString();
				JSONObject mySO = (JSONObject) result;
				try {
					JSONArray array = mySO.getJSONArray("GetAccount");
					PARTNER = array.getJSONObject(0).getString("Partner");
					SELLER = array.getJSONObject(0).getString("Seller");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(PayActivity.this,
				true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();

		String str = "<Request></Request>";

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ZHIFUACTION, SOAP_ZHIFUMETHODNAME,
				SOAP_URL, paramMap);
	}

	public native String getStringFromJNI();

	public native String getStringFromPubJNI();

	/**
	 * 载入JNI生成的so库文件
	 */
	static {
		System.loadLibrary(libSoName);
	}

	protected void AskOrderUpdate(int paytype) {
		// TODO Auto-generated method stub
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue = result.toString();
				// {"AskOrderUpdate":[{"MessageContent":"成功！","MessageCode":"0"}]}
				try {
					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO.getJSONArray("AskOrderUpdate");
					if ("0".equals(array.getJSONObject(0).getString(
							"MessageCode"))) {
						user.PayId = "";
						LocalAccessor.getInstance(PayActivity.this).updateUser(
								user);
						Intent i = new Intent(PayActivity.this,
								CHK_network_anomalytwo.class);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", DoctorId);
						i.putExtra("HospitalID", Hosid);
						i.putExtra("DoctorImageUrl", DoctorUrl);
						i.putExtra("DoctorName", DoctorName);
						i.putExtra("Hosname", HosName);
						i.putExtra("Title", Title);
						i.putExtra("Status", 2);
						startActivity(i);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		JsonAsyncTask_Info task = new JsonAsyncTask_Info(PayActivity.this,
				true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();

		String str = "<Request><AskOrderID>%s</AskOrderID><OrderStatus>%s</OrderStatus><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus></Request>";

		str = String.format(str, new Object[] { AskOrderID, "02", "",
				paytype + "", "" });

		paramMap.put("str", str);

		// 必须是这5个参数，而且得按照顺序
		task.execute(SOAP_NAMESPACE, SOAP_ACTION, SOAP_METHODNAME, SOAP_URL,
				paramMap);
	}

	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.home);// 状态栏无背景
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		AskOrderInquiry();

		// startActivity(new Intent(PayActivity.this,MainChatActivity.class));
	}

	public void gopay() {
		boolean result = new SystemUtils(PayActivity.this)
				.isInstallWx("com.tencent.mm");

		if (paytype == 0) {
			// pay.setEnabled(false);
			// String url =
			// "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
			if (result) {
//				int price=Integer.valueOf(AskByOne)*100;
				int price = 1;
				final String url = MMloveConstants.JE_BASE_URL2
						+ "/wxPay.aspx?price=" + price + "&myorderid="
						+ AskOrderID;
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						payweixin(url);
					}
				}).start();
			} else {
				Toast.makeText(PayActivity.this, "请安装微信", 1).show();
			}

		} else if (paytype == 1) {
			pay.setEnabled(false);
			if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
					|| TextUtils.isEmpty(SELLER)) {

				DialogEnsureView dialogEnsureView = new DialogEnsureView(
						PayActivity.this).setDialogMsg("警告",
						"需要配置PARTNER | RSA_PRIVATE| SELLER", "确定")
						.setOnClickListenerEnsure(new OnClickListener() {

							@Override
							public void onClick(View v) {
								finish();
							}
						});
				DialogUtils.showSelfDialog(this, dialogEnsureView);
			}
			// 订单
//			 String orderInfo = getOrderInfo("咨询单价", "该次咨询的详细描述", AskByOne);
			String orderInfo = getOrderInfo("咨询单价", "该次咨询的详细描述", "0.01");
			// 对订单做RSA 签名
			String sign = sign(orderInfo);
			try {
				// 仅需对sign 做URL编码
				sign = URLEncoder.encode(sign, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 完整的符合支付宝参数规范的订单信息
			final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
					+ getSignType();

			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(PayActivity.this);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo);

					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
					finish();
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}
	}

	private void AskOrderInquiry() {

		// 必须是这5个参数，而且得按照顺序
		JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {

			@Override
			public void processJsonObject(Object result) {
				// TODO Auto-generated method stub
				String returnvalue = result.toString();
				try {
					// {"AskOrderInquiry":[{"MessageContent":"数据为空！","MessageCode":"2"}]}

					JSONObject mySO = (JSONObject) result;
					JSONArray array = mySO.getJSONArray("AskOrderInquiry");
					int status = Integer.valueOf(array.getJSONObject(0)
							.getString("OrderStatus").toString());
					AskOrderID = array.getJSONObject(0).getString("AskOrderID")
							.toString();
					if (status == 2 || status == 3) {

						Intent i = new Intent(PayActivity.this,
								MainChatActivity.class);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorId", DoctorId);
						i.putExtra("AskOrderID", AskOrderID);
						i.putExtra("DoctorName", DoctorName);
						i.putExtra("DoctorImageUrl", DoctorUrl);
						i.putExtra("HospitalID", Hosid);
						i.putExtra("DoctorName", DoctorName);
						i.putExtra("Hosname", HosName);
						i.putExtra("Title", Title);
						i.putExtra("Status", status);
						startActivity(i);
					} else {
						gopay();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
		};

		JsonAsyncTask_Info task = new JsonAsyncTask_Info(PayActivity.this,
				true, doProcess);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String str = "<Request><UserID>%s</UserID><Reason>%s</Reason><PayType>%s</PayType><PayStatus>%s</PayStatus><CustomerIP>%s</CustomerIP><HospitalID>%s</HospitalID><HospitalName>%s</HospitalName><DeptRowID>%s</DeptRowID><DeptDesc>%s</DeptDesc>"
				+ "<DoctorID>%s</DoctorID><DoctorName>%s</DoctorName><RealName>%s</RealName><UserNO>%s</UserNO><UserMobile>%s</UserMobile><AskOrderID>%s</AskOrderID></Request>";

		str = String.format(str, new Object[] { user.UserID + "", "", "", "",
				"", Hosid, HosName, "", "", DoctorId, DoctorName,
				user.RealName, user.UserNO, user.UserMobile, "" });

		paramMap.put("str", str);
		task.execute(SOAP_NAMESPACE, SOAP_ACTIONORDERINQUIRY,
				SOAP_METHODNAMEORDERINQUIRY, SOAP_URL, paramMap);
	}

	private void payweixin(String url) {
		try {
			byte[] buf = httpGet(url);
			if (buf != null && buf.length > 0) {
				String content = new String(buf);
				
				JSONObject json = new JSONObject(content);
				if (null != json && !json.has("retcode")) {
					PayReq req = new PayReq();
					// req.appId = "wxf8b4f85f3a794e77"; // 测试用appId
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					user.PayId = AskOrderID;
					LocalAccessor.getInstance(PayActivity.this)
							.updateUser(user);

					myApp.DoctorId = DoctorId;
					myApp.Hosid = Hosid;
					myApp.DoctorUrl = DoctorUrl;
					myApp.DoctorName = DoctorName;
					myApp.HosName = HosName;
					myApp.Title = Title;
					// Toast.makeText(PayActivity.this, "正常调起支付",
					// Toast.LENGTH_SHORT).show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					api.sendReq(req);
					finish();

				} else {
					
					Toast.makeText(PayActivity.this,
							"返回错误" + json.getString("retmsg"),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				
				Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			Toast.makeText(PayActivity.this, "异常：" + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	public void selectWeixin(View v) {
		paytype = 0;
		rb_weixin.setChecked(true);

		rb_zhifubao.setChecked(false);
	}

	public void selectZhifu(View v) {
		paytype = 1;
		rb_weixin.setChecked(false);
		rb_zhifubao.setChecked(true);
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		// https://lovestaging.topmd.cn/notify_url.aspx
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + MMloveConstants.JE_BASE_URL2
				+ "/notify_url.aspx" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {

		// SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
		// Locale.getDefault());
		// Date date = new Date();
		// String key = format.format(date);
		//
		// Random r = new Random();
		// key = key + r.nextInt();
		// key = key.substring(0, 15);
		return AskOrderID;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	protected void iniView() {
		// TODO Auto-generated method stub
		vTop = findViewById(R.id.v_top);
		rb_weixin = (RadioButton) findViewById(R.id.rb_weixin);
		pay = (Button) findViewById(R.id.pay);

		rb_zhifubao = (RadioButton) findViewById(R.id.rb_zhifubao);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tvhead = (TextView) findViewById(R.id.txtHead);
		tvdocname = (TextView) findViewById(R.id.tvdocname);
		jiage = (TextView) findViewById(R.id.jiage);
	}

	public static byte[] httpGet(final String url) {
		if (url == null || url.length() == 0) {
			return null;
		}

		HttpClient httpClient = getNewHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse resp = httpClient.execute(httpGet);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			return EntityUtils.toByteArray(resp.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	private static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}

package com.netlab.loveofmum.getui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.netlab.loveofmum.Activity_ReplyPage;
import com.netlab.loveofmum.CHK_DoctorList;
import com.netlab.loveofmum.DoctorHomeAct;
import com.netlab.loveofmum.GetuiSdkDemoActivity;
import com.netlab.loveofmum.MainTabActivity;
import com.netlab.loveofmum.MyCHK_Timeline;
import com.netlab.loveofmum.News_Detail;
import com.netlab.loveofmum.SmallTalkAct;
import com.netlab.loveofmum.StaticWebView;
import com.netlab.loveofmum.Welcome;
import com.netlab.loveofmum.api.LocalAccessor;
import com.netlab.loveofmum.huanxin.MainChatActivity;
import com.netlab.loveofmum.model.User;
import com.netlab.loveofmum.model.Yuchan;
import com.netlab.loveofmum.testwebview.TestWebView;
import com.netlab.loveofmum.utils.IOUtils;

public class PushDemoReceiver extends BroadcastReceiver
{
	Yuchan yu;
	private User user;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
	
		switch (bundle.getInt(PushConsts.CMD_ACTION))
		{

			case PushConsts.GET_MSG_DATA:
				// 获取透传数据
				// String appid = bundle.getString("appid");
				byte[] payload = bundle.getByteArray("payload");

				// String url = bundle.getString("taskid");

				String taskid = bundle.getString("taskid");
				String messageid = bundle.getString("messageid");
				// P0005 小贴士
				// P0006 热点
				// P0007 个人中心
				// P0008 妈妈足迹
				// P0009 微专题
				// P0010 医生首页
				// P0011 医生文章
				// P0012 孕健康
				// P0013 话题
				// P0014 商城
				// P0015 商品购买
				// P0016 选择医生
				// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
				boolean result = PushManager.getInstance().sendFeedbackMessage(
						context, taskid, messageid, 90001);
				System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
				Log.i("wwwwoshigetui", "Got Payload:nnnnnnnnnnnnnnnnnn" );

				if (payload != null)
				{
					String data = new String(payload);
					
					try
					{
						Log.i("wwwwoshigetuivvvv2222", "Got Payload:" + data);
						yu = new Yuchan();
						if(!data.equals(""))
						{
							Date date;
							JSONObject mySO = new JSONObject(data);
							org.json.JSONArray array = mySO
									.getJSONArray("ReturnValue");
						
							DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
							user = LocalAccessor.getInstance(context).getUser();
							try {
								date = fmt.parse(user.YuBirthDate);
								yu = IOUtils.WeekInfo(date);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							{ "ReturnValue":[{ "MessageCode": "P0009","MessageContent":"84"}]}


							String MessageCode=array.getJSONObject(0)
									.getString("MessageCode").toString();
							String MessageContent=array.getJSONObject(0)
									.getString("MessageContent").toString();
							Log.i("wwwwoshigetuivvvv333333", "Got Payload:" + MessageCode+":::"+MessageContent);
							if(MessageCode.equals("P0001")||MessageCode.equals("P0002")){
								Log.i("wwwwoshigetuivvvv44444", "5555555=======");
								Intent i = new Intent(context, MainTabActivity.class);
								i.putExtra("TabIndex", "A_TAB");

								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(i);
								Log.i("wwwwoshigetuivvvv44444", "5555555");
							}else if(MessageCode.equals("P0007")){
								Log.i("wwwwoshigetuivvvv44444", "6666666========");
								Intent i = new Intent(context,
										MainTabActivity.class);
								i.putExtra("TabIndex", "D_TAB");
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(i);
								Log.i("wwwwoshigetuivvvv44444", "6666666");
							}
							else {
								Log.i("wwwwoshigetuivvvv44444", "77777777========");
								Intent intent1 = new Intent(context, MainTabActivity.class);
								intent1.putExtra("TabIndex", "A_TAB");
								intent1.putExtra("Push", "Push");
								intent1.putExtra("MessageCode", MessageCode);
								intent1.putExtra("MessageContent", MessageContent);
								intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent1);
								Log.i("wwwwoshigetuivvvv44444", "77777777");
							}
						}
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Log.d("GetuiSdkDemo", "Got Payload:" + data);
					if (GetuiSdkDemoActivity.tLogView != null)
						GetuiSdkDemoActivity.tLogView.append(data + "\n");
				}
				break;
			case PushConsts.GET_CLIENTID:
				// 获取ClientID(CID)
				// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
				String cid = bundle.getString("clientid");
				if (GetuiSdkDemoActivity.tView != null)
					GetuiSdkDemoActivity.tView.setText(cid);
				break;
			case PushConsts.THIRDPART_FEEDBACK:
				/*
				 * String appid = bundle.getString("appid"); String taskid =
				 * bundle.getString("taskid"); String actionid =
				 * bundle.getString("actionid"); String result =
				 * bundle.getString("result"); long timestamp =
				 * bundle.getLong("timestamp");
				 * 
				 * Log.d("GetuiSdkDemo", "appid = " + appid);
				 * Log.d("GetuiSdkDemo", "taskid = " + taskid);
				 * Log.d("GetuiSdkDemo", "actionid = " + actionid);
				 * Log.d("GetuiSdkDemo", "result = " + result);
				 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
				 */
				break;
			default:
				break;
		}
	}


}

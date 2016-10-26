package com.netlab.loveofmum.utils;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mob.tools.utils.SharePrefrenceHelper;
import com.netlab.loveofmum.R;
import com.netlab.loveofmum.api.JsonAsyncTaskOnComplete;
import com.netlab.loveofmum.api.JsonAsyncTask_Info;
import com.netlab.loveofmum.api.MMloveConstants;
import com.netlab.loveofmum.model.Hospital;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 蔺子岭 on 2016/8/31 0031.
 */
public class AppUtils {
    public static void getHospital(Context cxt,ComCallBack callBack){
        SharePrefrenceHelper helper = new SharePrefrenceHelper(cxt);
        helper.open("required_data");
        String listJSON  = helper.getString("hospital");
        if(listJSON==null||listJSON.equals("")) {
            getHospitalInner(cxt,callBack);
        }else{
            List<Hospital> list = JSON.parseArray(listJSON, Hospital.class);
            callBack.onCallBack(list);
        }
    }
    private static void getHospitalInner(final Context cxt,final ComCallBack callBack){
        try {
            JsonAsyncTaskOnComplete doProcess = new JsonAsyncTaskOnComplete() {
                public void processJsonObject(Object result) {
                    if(result==null||result.equals("")){
                        Toast.makeText(cxt,"获取医院列表失败！",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONObject resultJSON = JSON.parseObject(result.toString());
                        String listJSON = resultJSON.getJSONArray("HospitalInquiry").toJSONString();
                        SharePrefrenceHelper helper = new SharePrefrenceHelper(cxt);
                        helper.open("required_data");
                        helper.putString("hispotal",listJSON);
                        List<Hospital> list = JSON.parseArray(listJSON,Hospital.class);
                        callBack.onCallBack(list);
                    }
//                    if (result == null) {
//
//                    } else {
//                        Map<String, Object> map;
//
//                        try {
//                            JSONObject mySO = (JSONObject) result;
//                            JSONArray array = mySO
//                                    .getJSONArray("HospitalInquiry");
//                            for (int i = 0; i < array.length(); i++) {
//                                map = new HashMap<String, Object>();
//                                map.put("HospitalName", array.getJSONObject(i)
//                                        .getString("HospitalName"));
//                                map.put("SoundFlag", array.getJSONObject(i)
//                                        .getString("SoundFlag"));
//
//                                map.put("HospitalID", array.getJSONObject(i)
//                                        .getString("HospitalID"));
//
//                                map.put("picUrl",
//                                        (MMloveConstants.JE_BASE_URL + array
//                                                .getJSONObject(i).getString(
//                                                        "ImageURL")).toString()
//                                                .replace("~", "")
//                                                .replace("\\", "/"));
//                                if (user.UserID != 0) {
//                                    if (user.HospitalID == Integer.valueOf(array.getJSONObject(i)
//                                            .getString("HospitalID"))) {
//                                        if (Integer.valueOf(array.getJSONObject(i)
//                                                .getString("SoundFlag")) == 1) {
//                                            isHosiptal = true;
//                                        } else {
//                                            isHosiptal = false;
//                                        }
//                                    }
//                                }
//                                hosList.add(map);
//                                if (isHosiptal) {
//                                    findViewById(R.id.tvyuyue).setVisibility(View.GONE);
//                                    findViewById(R.id.lltime).setVisibility(View.VISIBLE);
//                                    tvchao.setText("进入");
//                                } else {
//                                    findViewById(R.id.tvyuyue).setVisibility(View.VISIBLE);
//                                    findViewById(R.id.lltime).setVisibility(View.GONE);
//                                    tvchao.setText("查询");
//                                }
//                            }
//                            Message message = Message.obtain();
//                            message.what = 0;
//                            mHandler.sendMessage(message);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
                }

            };

            JsonAsyncTask_Info task = new JsonAsyncTask_Info(cxt, true,
                    doProcess);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String str = "<Request><HospitalID>0</HospitalID></Request>";
            paramMap.put("str", str);
            // 必须是这5个参数，而且得按照顺序
            task.execute(MMloveConstants.URL001, MMloveConstants.URL001
                            + MMloveConstants.HospitalInquiry,  MMloveConstants.HospitalInquiry,
                    MMloveConstants.URL002, paramMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void refreshUserInfo(Context cxt,ComCallBack callBack){

    }
}

package com.netlab.loveofmum.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.loveofmum.R;

/**
 * Created by 蔺子岭 on 2016/8/29 0029.
 */
public class VisitCardAddActivity extends BaseActivity {
    TextView hospitalname;
    TextView cardno;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_visitcardadd);
        findViewById(R.id.img_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.txtHead)).setText("添加就诊卡");
        hospitalname =(TextView)findViewById(R.id.hospitalname);
        cardno =(TextView)findViewById(R.id.cardno);
        TextView  label =(TextView)findViewById(R.id.label);
        label.setText("就诊卡是医院存储您就诊资料的磁性卡，是您与医院挂号，付费等交互时的唯一实体证据（某些地区可用医保卡代替）,"+
        "由于医疗的特殊和保密性需要，每家医院的就诊卡不通用，为保证您的挂号等操作的顺利惊醒，请正确填写您正在使用的就诊卡卡号。没有就诊卡的患者，"+
        "请到您的就诊医院凭个人身份证办理就诊卡。");
    }
    public void selecthospital(View v){

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left:
                finish();

                break;
        }
    }
    public void showdemo(View v){
        PopupWindow window = new PopupWindow();
        ImageView imageView = new ImageView(this);
        window.setContentView(imageView);
        window.setOutsideTouchable(true);
        window.showAtLocation(v, Gravity.CENTER,0,0);

    }
    public void submit(View v){
        if(hospitalname.getText()==null||hospitalname.getText().toString().equals("")){
            Toast.makeText(VisitCardAddActivity.this, "请选择就诊卡所属医院！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(cardno.getText()==null||cardno.getText().toString().equals("")){
            Toast.makeText(VisitCardAddActivity.this, "请输入您的卡号！", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}

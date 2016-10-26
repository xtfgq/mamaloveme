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
public class VisitCardEditActivity extends BaseActivity {
    TextView hospitalname;
    TextView cardno;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_visitcardedit);
        findViewById(R.id.img_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.txtHead)).setText("编辑就诊卡");
        hospitalname =(TextView)findViewById(R.id.hospitalname);
        cardno =(TextView)findViewById(R.id.cardno);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left:
                finish();

                break;
            case R.id.title_right:
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
    public void delete(View v){
        PopupWindow window = new PopupWindow();
        ImageView imageView = new ImageView(this);
        window.setContentView(imageView);
        window.setOutsideTouchable(true);
        window.showAtLocation(v, Gravity.CENTER,0,0);

    }
    public void submit(View v){
//        if(hospitalname.getText()==null||hospitalname.getText().toString().equals("")){
//            Toast.makeText(VisitCardEditActivity.this, "请选择就诊卡所属医院！", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(cardno.getText()==null||cardno.getText().toString().equals("")){
            Toast.makeText(VisitCardEditActivity.this, "请输入您的卡号！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}

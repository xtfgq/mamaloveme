package com.netlab.loveofmum.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.netlab.loveofmum.R;

/**
 * Created by 蔺子岭 on 2016/8/29 0029.
 */
public class VisitCardManagerActivity extends BaseActivity implements ListView.OnItemClickListener{
    ListView listView;
    boolean select;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_visitcardmanager);
        findViewById(R.id.img_left).setOnClickListener(this);
        ((TextView) findViewById(R.id.txtHead)).setText("就诊卡管理");
        listView= (ListView)findViewById(R.id.listview);
        select = getIntent().getBooleanExtra("select",false);
        listView.setOnItemClickListener(this);
    }
    public void addcard(View v){

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left:
                finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

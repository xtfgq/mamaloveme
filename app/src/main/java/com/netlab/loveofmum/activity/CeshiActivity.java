package com.netlab.loveofmum.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.netlab.loveofmum.R;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class CeshiActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_1);
    }
}

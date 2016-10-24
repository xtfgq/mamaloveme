package com.netlab.loveofmum.alipay;

import com.alipay.sdk.app.PayTask;
import com.netlab.loveofmum.R;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class ExternalFragment extends Fragment {
	private RadioButton rb_weixin,rb_zhifubao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.layout_weixinqqzhifu, container, false);
		rb_weixin=(RadioButton) view.findViewById(R.id.rb_weixin);
		rb_zhifubao=(RadioButton) view.findViewById(R.id.rb_zhifubao);
		return view;
	}
	public void selectWeixin(View v) {
		rb_weixin.setChecked(true);
		rb_zhifubao.setChecked(false);
	}
	public void selectZhifu(View v) {
		rb_weixin.setChecked(false);
		rb_zhifubao.setChecked(true);
	}
}

package com.netlab.loveofmum.widget;

import java.util.ArrayList;

import com.netlab.loveofmum.R;
import com.netlab.loveofmum.utils.SysUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class CustomerSpinner extends Spinner implements OnItemClickListener
{

	public static SelectDialog dialog = null;
	private ArrayList<String> list;
	public static String text;

	public int pos = -1;
	
	public CustomerSpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	// 如果视图定义了OnClickListener监听器，调用此方法来执行
	@Override
	public boolean performClick()
	{
		Context context = getContext();
		final LayoutInflater inflater = LayoutInflater.from(getContext());
		final View view = inflater.inflate(R.layout.formcustomspinner, null);
		final ListView listview = (ListView) view
				.findViewById(R.id.formcustomspinner_list);
		ListviewAdapter adapters = new ListviewAdapter(context, getList(),pos);
		listview.setAdapter(adapters);
		listview.setOnItemClickListener(this);
		if(!SysUtils.isFastClick()){
			dialog = new SelectDialog(context, R.style.mydialog);// 创建Dialog并设置样式主题
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			dialog.show();
			dialog.addContentView(view, params);
		}



		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> q, View itemView, int position,
			long id)
	{
		pos = position;
		setSelection(position);
		setText(list.get(position));
		
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
	}

	public ArrayList<String> getList()
	{
		return list;
	}

	public void setList(ArrayList<String> list)
	{
		this.list = list;
	}
	
	public void setPosition(int position)
	{
		this.pos = position;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}

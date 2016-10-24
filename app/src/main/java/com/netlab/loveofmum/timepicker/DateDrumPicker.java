package com.netlab.loveofmum.timepicker;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

/**
 * 
 * @author yagi
 * 
 */
public class DateDrumPicker extends DrumPicker {
	private final static String TAG = DateDrumPicker.class.getSimpleName();
	private final static String[] YEAR = {
			"2016", "2015"
	};
	private final static String[] MONTH = {
			"12", "11", "10", "09", "08", "07", "06", "05", "04", "03", "02", "01"
	};
	private final static String[] DAYS = {
			"31", "30", "29", "28", "27", "26", "25", "24", "23", "22", "21", "20", "19", "18", "17", "16", "15", "14", "13", "12", "11", "10", "09", "08", "07", "06", "05", "04", "03", "02", "01"
	};
	DatePicker.OnDateChangedListener mListener = null;
	private int mYear = 0;
	private int mMonth = 0;
	private int mDay = 0;
	private Calendar mCalendar = null;
	
	//public Calendar dd=null;

	public DateDrumPicker(Context context) {
		this(context, null);
	}

	public DateDrumPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init();
	}

	public void init(Calendar c) {
		//mCalendar = Calendar.getInstance();
		mCalendar = c;
		addRow(YEAR, 150);
		addRow(MONTH, 100);
		addRow(DAYS, 100);

		String year = Integer.toString(mCalendar.get(Calendar.YEAR));
		for (int i = 0; i < YEAR.length; i++) {
			String y = YEAR[i];
			if (year.equals(y)) {
				setPosition(0, i); 
				break;
			}
		}

		setPosition(1, 11 - mCalendar.get(Calendar.MONTH));
		setPosition(2, 30);

		int oldy = mCalendar.get(Calendar.YEAR);
		int oldm = mCalendar.get(Calendar.MONTH);
		int oldd = mCalendar.get(Calendar.DAY_OF_MONTH);
		resizeDay(oldy, oldm);
		mCalendar.add(Calendar.MONTH, 1);
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);
		mCalendar.add(Calendar.DAY_OF_MONTH, -1);
		final int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		setPosition(2, day - oldd);
//		if(oldm == 0)
//		{
//			setPosition(2, day - oldd);
//		}
//		else if(oldm == 1)
//		{
//			if(day == 28)
//			{
//				setPosition(2, day - oldd+3);
//			}
//			else if(day == 29)
//			{
//				setPosition(2, day - oldd+2);
//			}
//		}
//		else if(oldm == 2)
//		{
//			setPosition(2, day - oldd-2);
//		}
//		else if(oldm == 3)
//		{
//			setPosition(2, day - oldd);
//		}
//		else if(oldm == 4)
//		{
//			setPosition(2, day - oldd-1);
//		}
//		else if(oldm == 5)
//		{
//			setPosition(2, day - oldd);
//		}
//		else if(oldm == 6)
//		{
//			setPosition(2, day - oldd-1);
//		}
//		else if(oldm == 7)
//		{
//			setPosition(2, day - oldd-1);
//		}
//		else if(oldm == 8)
//		{
//			setPosition(2, day - oldd);
//		}
//		else if(oldm == 9)
//		{
//			setPosition(2, day - oldd-1);
//		}
//		else if(oldm == 10)
//		{
//			setPosition(2, day - oldd);
//		}
//		else if(oldm == 11)
//		{
//			setPosition(2, day - oldd-1);
//		}
		mCalendar.set(Calendar.YEAR, oldy);
		mCalendar.set(Calendar.MONTH, oldm);
		mCalendar.set(Calendar.DAY_OF_MONTH, oldd);
		// 儕僗僫乕
		setOnPostionChangedListener(new OnPositionChangedListener() {
			@Override
			public void onPositionChanged(int itemPos, int pos) {
				if (pos < 0) {
					return;
				}
				switch (itemPos) {
				case 0:
					mYear = Integer.parseInt(YEAR[pos]);
					resizeDay(mYear, mMonth);
					break;
				case 1:
					mMonth = Integer.parseInt(MONTH[pos]);
					resizeDay(mYear, mMonth);
					break;
				case 2:
					int adjust = monthDays(mYear, mMonth);
					mDay = Integer.parseInt(DAYS[pos + (adjust>-1?31-adjust:0)]);
				}
				if (mListener != null) {
					mListener.onDateChanged(null, mYear, mMonth, mDay);
				}
			}
		});
	}

	private void resizeDay(int year, int month) {
		// 擔
		int oldy = mCalendar.get(Calendar.YEAR);
		int oldm = mCalendar.get(Calendar.MONTH);
		int oldd = mCalendar.get(Calendar.DAY_OF_MONTH);

		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, month);
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);
		mCalendar.add(Calendar.DAY_OF_MONTH, -1);
		final int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		//Log.d(TAG, "day=" + day + "::" + month);

		resize(2, new IsGoneListener() {
			@Override
			public boolean isGone(View item, int pos) {
				if (!(30 - pos < day)) {
					return true;
				}
				return false;
			}
		});

		mCalendar.set(Calendar.YEAR, oldy);
		mCalendar.set(Calendar.MONTH, oldm);
		mCalendar.set(Calendar.DAY_OF_MONTH, oldd);
	}

	public void setOnDateChangedListener(DatePicker.OnDateChangedListener listener) {
		mListener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = (int) (150 * Util.getDisplayScale(getContext()));
		int h = MeasureSpec.makeMeasureSpec(MeasureSpec.getMode(heightMeasureSpec), height);
		super.onMeasure(widthMeasureSpec, h);
	}

	boolean isGone(View item) {
		return false;
	}

	public void setYear(int year) {
		if (year >= 1970 && year < 2030) {
			setPosition(0, YEAR.length - 1 - (year - 1970));
		}
	}

	public void setMonth(int month) {
		if (month >= 1 && month < 13) {
			setPosition(1, MONTH.length - 1 - month);
		}
	}

	public void setDay(int day) {
		if (day >= 1 && day < 32) {
			int days = monthDays(mYear, mMonth);
			setPosition(2, days - 1 - day);
		}
	}

	static int monthDays(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return -1;
	}

	static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

}

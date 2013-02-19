package com.example.betaplanner.Adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.betaplanner.R;


public class CalendarGridViewAdapter extends BaseAdapter {

	private Calendar calStartDate = Calendar.getInstance();// ��ǰ��ʾ������
	private Calendar calSelected = Calendar.getInstance(); // ѡ�������
	private GridView mgv;
	
	public void setSelectedDate(Calendar cal)
	{
		calSelected=cal;
	}
	
	private Calendar calToday = Calendar.getInstance(); // ����
	private int iMonthViewCurrentMonth = 0; // ��ǰ��ͼ��
	// ���ݸı�����ڸ�������
	// ��������ؼ���
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // ���óɵ��µ�һ��
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// �õ���ǰ������ʾ����

		// ����һ��2 ��������1 ���ʣ������
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

		calStartDate.add(Calendar.DAY_OF_MONTH, -1);// ���յ�һλ

	}
	ArrayList<java.util.Date> titles;
	private ArrayList<java.util.Date> getDates() {

		UpdateStartDateForMonth();

		ArrayList<java.util.Date> alArrayList = new ArrayList<java.util.Date>();
		//��������
		for (int i = 1; i <= 42; i++) {
			alArrayList.add(calStartDate.getTime());
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return alArrayList;
	}

	private Activity activity;
	Resources resources;
	// construct
	public CalendarGridViewAdapter(Activity a,Calendar cal, GridView gv) {
		calStartDate=cal;
		activity = a;
		resources=activity.getResources();
		titles = getDates();
		mgv = gv;
	}
	
	public CalendarGridViewAdapter(Activity a) {
		activity = a;
		resources=activity.getResources();
	}


	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public Object getItem(int position) {
		return titles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout iv = new LinearLayout(activity);
		iv.setId(position + 5000);
		LinearLayout imageLayout = new LinearLayout(activity);
		imageLayout.setOrientation(0);
		iv.setGravity(Gravity.CENTER);
		iv.setOrientation(1);
		iv.setBackgroundColor(resources.getColor(R.color.white));
		int pHight = mgv.getHeight();


		Date myDate = (Date) getItem(position);
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(myDate);

		final int iMonth = calCalendar.get(Calendar.MONTH);
		final int iDay = calCalendar.get(Calendar.DAY_OF_WEEK);

	
		// �ж���������
		iv.setBackgroundColor(resources.getColor(R.color.white));
		if (iDay == 7) {
			// ����
			iv.setBackgroundColor(resources.getColor(R.color.text_6));
		} else if (iDay == 1) {
			// ����
			iv.setBackgroundColor(resources.getColor(R.color.text_7));
		} else {

		}
		// �ж��������ս���

		TextView txtToDay = new TextView(activity);// �ձ��ϻ���
		txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
		txtToDay.setTextSize(9);//�����С
		if (equalsDate(calToday.getTime(), myDate)) {
			// ��ǰ����
//			iv.setBackgroundColor(resources.getColor(R.color.event_center));
			txtToDay.setText("TODAY!");
		}

		// ���ñ�����ɫ
		if (equalsDate(calSelected.getTime(), myDate)) {
			// ѡ���
			iv.setBackgroundColor(resources.getColor(R.color.selection));
//			Log.w("select date", calSelected.getTime()+"");
		} else {
			if (equalsDate(calToday.getTime(), myDate)) {
				// ��ǰ����
				iv.setBackgroundColor(resources.getColor(R.color.calendar_zhe_day));
			}
		}
		// ���ñ�����ɫ����

		// ���ڿ�ʼ
		TextView txtDay = new TextView(activity);// ����
		txtDay.setGravity(Gravity.CENTER_HORIZONTAL);
		txtDay.setTextSize(25);
		
		// �ж��Ƿ��ǵ�ǰ��
		if (iMonth == iMonthViewCurrentMonth) {
			txtToDay.setTextColor(resources.getColor(R.color.ToDayText));
			txtDay.setTextColor(resources.getColor(R.color.Text));
		} else {
			txtDay.setTextColor(resources.getColor(R.color.noMonth));
			txtToDay.setTextColor(resources.getColor(R.color.noMonth));
		}

		int day = myDate.getDate(); // ����
		txtDay.setText(String.valueOf(day));
		txtDay.setId(position + 500);
		iv.setTag(myDate);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		iv.addView(txtDay, lp);

		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		iv.addView(txtToDay, lp1);
		// ���ڽ���
		// iv.setOnClickListener(view_listener);

		return iv;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private Boolean equalsDate(Date date1, Date date2) {

		if (date1.getYear() == date2.getYear()
				&& date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()) {
			return true;
		} else {
			return false;
		}

	}

}

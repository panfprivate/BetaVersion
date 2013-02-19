package com.example.betaplanner.Calendar;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;


public class CalendarGridView extends GridView{
	
	private Context mContext;

	public CalendarGridView(Context context) {
		super(context);
		mContext = context;
		
		setGirdView();
	}

	private void setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setLayoutParams(params);
		setNumColumns(7);// ����ÿ������
		setGravity(Gravity.CENTER_VERTICAL);// λ�þ���
		setVerticalSpacing(1);// ��ֱ���
		setHorizontalSpacing(1);// ˮƽ���
		//���ñ���
//		setBackgroundColor(getResources().getColor(R.color.calendar_background));
	//���ò���
		WindowManager windowManager = ((Activity)mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		setPadding(x, 0, 0, 0);// ����
	}
}

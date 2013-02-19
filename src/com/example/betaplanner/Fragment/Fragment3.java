package com.example.betaplanner.Fragment;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.betaplanner.R;
import com.example.betaplanner.Activity.EventEditor;
import com.example.betaplanner.Adapter.CalendarGridViewAdapter;
import com.example.betaplanner.Adapter.MyAdapter;
import com.example.betaplanner.Calendar.CalendarGridView;
import com.example.betaplanner.Calendar.NumberHelper;
import com.example.betaplanner.Manager.DBMgr;

public class Fragment3 extends Fragment implements OnTouchListener{

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	private static final int EVENT_CREATE=0;
    private static final int EVENT=1;
	
	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
	
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;

	GestureDetector mGesture = null;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGesture.onTouchEvent(event);
	}
	
//	private Context mContext;
	private GridView title_gView;
	private GridView gView1;// last month
	private GridView gView2;// current month
	private GridView gView3;// next month
	private ListView lv;
	private DBMgr mDbHelper;
	private View mv;
	
	boolean bIsSelection = false;
	private Calendar calStartDate = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance(); 
	private Calendar calToday = Calendar.getInstance();
	private CalendarGridViewAdapter gAdapter;
	private CalendarGridViewAdapter gAdapter1;
	private CalendarGridViewAdapter gAdapter3;
	ArrayAdapter<String> adapter;
	String products[] = {
			"Dell Inspiron", "HTC One X", "HTCWildfire S", "HTC Sense", "HTC Sensation XE",   "iPhone4S", "Samsung Galaxy Note 800",       "SamsungGalaxy S3", "MacBook Air", "Mac Mini", "MacBookPro"};
	
	private TextView tv;
	private Button btnpre, btnnxt;
	private LinearLayout mainLayout;

	private int iMonthViewCurrentMonth = 0; 
	private int iMonthViewCurrentYear = 0; 
	private int iFirstDayOfWeek = Calendar.MONDAY;

	private static final int mainLayoutID = 88;
	private static final int titleLayoutID = 77; 
	private static final int caltitleLayoutID = 66; 
	private static final int calLayoutID = 55; 
	
	AlertDialog.OnKeyListener onKeyListener = new AlertDialog.OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				getActivity().finish();
			}
			return false;

		}

	};

	AnimationListener animationListener=new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			//call after the animation
			CreateGirdView();
		}
	};
	
	
	class GestureListener extends SimpleOnGestureListener {
		@Override
		//在onFling方法中, 判断是不是一个合理的swipe动作;
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE	&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
					setNextViewItem();
					
					return true;

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//这里的viewFlipper是含有多个view的一个container, 可以很方便的调用prev/next view; 

					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					setPrevViewItem();
					
					return true;

				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// ListView lv = getListView();

			int pos = gView2.pointToPosition((int) e.getX(), (int) e.getY());
			LinearLayout txtDay = (LinearLayout) gView2.findViewById(pos + 5000);
			if (txtDay != null) {
				if (txtDay.getTag() != null) {
					Date date = (Date) txtDay.getTag();
					calSelected.setTime(date);

					gAdapter.setSelectedDate(calSelected);
					gAdapter.notifyDataSetChanged();

					gAdapter1.setSelectedDate(calSelected);
					gAdapter1.notifyDataSetChanged();

					gAdapter3.setSelectedDate(calSelected);
					gAdapter3.notifyDataSetChanged();
				}
			}

			//2013-1-1 is 11301
			Log.i("TEST", "onSingleTapUp -  pos="+calSelected.getTime().getYear()+calSelected.getTime().getMonth()+calSelected.getTime().getDate());

			return false;
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mDbHelper = new DBMgr(getActivity());
        mDbHelper.open();
		mGesture = new GestureDetector(getActivity(), new GestureListener());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mv = inflater.inflate(R.layout.fragment_cal, container, false);
		mv = generateContentView();
		mv.setClickable(true);
		mv.setFocusable(true);
		
		
		UpdateStartDateForMonth();
		registerForContextMenu(gView1);
		registerForContextMenu(gView2);
		registerForContextMenu(gView3);
		
		return mv;
	}
	

	private void UpdateStartDateForMonth() {
		
		calStartDate.set(Calendar.DATE, 1); 

		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);
		
		Log.w("asdkjhdfjhadf",s);
		tv.setText(s);

		
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

	}
	
	
	
	private void setPrevViewItem() {
		iMonthViewCurrentMonth--;
		
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1); 
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth); 
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear); 

	}

	
	private void setNextViewItem() {
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);

	}
	
	
	private View generateContentView() {

		viewFlipper = new ViewFlipper(getActivity());
		viewFlipper.setId(calLayoutID);


		mainLayout = new LinearLayout(getActivity()); 
		lv = new ListView(getActivity());
		RelativeLayout.LayoutParams params_main = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mainLayout.setLayoutParams(params_main);
//		mainLayout.setClickable(true);
		mainLayout.setOrientation(1);
		mainLayout.setId(mainLayoutID);
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		

		LinearLayout layTopControls = createLayout(LinearLayout.HORIZONTAL); 

		generateTopButtons(layTopControls); 
		RelativeLayout.LayoutParams params_title = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_title.topMargin = 5;

//		layTopControls.setClickable(true);
		layTopControls.setId(titleLayoutID);
		mainLayout.addView(layTopControls, params_title);

		calStartDate = getCalendarStartDate();

		setTitleGirdView();
		RelativeLayout.LayoutParams params_cal_title = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_cal_title.addRule(RelativeLayout.BELOW, titleLayoutID);
		
		mainLayout.addView(title_gView, params_cal_title);

		CreateGirdView();

		RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_cal.addRule(RelativeLayout.BELOW, caltitleLayoutID);

		mainLayout.addView(viewFlipper, params_cal);
		
		LinearLayout br = new LinearLayout(getActivity());
		RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 1);
		params_br.addRule(RelativeLayout.BELOW, calLayoutID);

		br.setBackgroundColor(getResources().getColor(R.color.calendar_background));
		mainLayout.addView(br, params_br);
		
/*		
		ArrayAdapter<String> adapter;
		String products[] = {
				"Dell Inspiron", "HTC One X", "HTCWildfire S", "HTC Sense", "HTC Sensation XE",   "iPhone4S", "Samsung Galaxy Note 800",       "SamsungGalaxy S3", "MacBook Air", "Mac Mini", "MacBookPro"};
		adapter= new ArrayAdapter<String>(
	    		  getActivity(), R.layout.event_row, R.id.event_row_title,products);
*/
		
		
		lv.setAdapter(fillData());
		
		mainLayout.addView(lv);

		return mainLayout;

	}
	
	
	private void CreateGirdView() {

		Calendar tempSelected1 = Calendar.getInstance(); 
		Calendar tempSelected2 = Calendar.getInstance(); 
		Calendar tempSelected3 = Calendar.getInstance(); 
		tempSelected1.setTime(calStartDate.getTime());
		tempSelected2.setTime(calStartDate.getTime());
		tempSelected3.setTime(calStartDate.getTime());

		gView1 = new CalendarGridView(getActivity());
//		gView1.setClickable(true);
		tempSelected1.add(Calendar.MONTH, -1);
		gAdapter1 = new CalendarGridViewAdapter(getActivity(), tempSelected1, gView1);
		gView1.getHeight();
		gView1.setAdapter(gAdapter1);
		gView1.setId(calLayoutID);

		gView2 = new CalendarGridView(getActivity());
//		gView2.setClickable(true);
		gAdapter = new CalendarGridViewAdapter(getActivity(), tempSelected2, gView2);
		gView2.setAdapter(gAdapter);
		gView2.setId(calLayoutID);

		gView3 = new CalendarGridView(getActivity());
//		gView3.setClickable(true);
		tempSelected3.add(Calendar.MONTH, 1);
		gAdapter3 = new CalendarGridViewAdapter(getActivity(), tempSelected3, gView3);
		gView3.setAdapter(gAdapter3);
		gView3.setId(calLayoutID);

		gView2.setOnTouchListener(this);
		gView1.setOnTouchListener(this);
		gView3.setOnTouchListener(this);
		
		

		if (viewFlipper.getChildCount() != 0) {
			viewFlipper.removeAllViews();
		}

		viewFlipper.addView(gView2);
		viewFlipper.addView(gView3);
		viewFlipper.addView(gView1);

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);

		tv.setText(s);

	}
	
	
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		return calStartDate;
	}
	
	private void setTitleGirdView() {

		title_gView = setGirdView();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		title_gView.setLayoutParams(params);
		title_gView.setVerticalSpacing(0);
		title_gView.setHorizontalSpacing(0);
		TitleGridAdapter titleAdapter = new TitleGridAdapter(getActivity());
		title_gView.setAdapter(titleAdapter);
		title_gView.setId(caltitleLayoutID);
	}
	
	
	private GridView setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		GridView gridView = new GridView(getActivity());
//		gridView.setClickable(true);
		gridView.setLayoutParams(params);
		gridView.setNumColumns(7);
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);

		
		
		WindowManager windowManager = getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		gridView.setPadding(x, 0, 0, 0);

		return gridView;
	}
	
	
	
	public class TitleGridAdapter extends BaseAdapter {
		
		int[] titles = new int[] { R.string.Sun, R.string.Mon, R.string.Tue,
				R.string.Wed, R.string.Thu, R.string.Fri, R.string.Sat };

		private Activity activity;

		// construct
		public TitleGridAdapter(Activity a) {
			activity = a;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object getItem(int position) {
			return titles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout iv = new LinearLayout(activity);
			TextView txtDay = new TextView(activity);
			txtDay.setFocusable(false);
			txtDay.setBackgroundColor(Color.TRANSPARENT);
			iv.setOrientation(1);

			txtDay.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

			int i = (Integer) getItem(position);

			txtDay.setTextColor(Color.WHITE);
			Resources res = getResources();

			if (i == R.string.Sat) {
				
				txtDay.setBackgroundColor(res.getColor(R.color.title_text_6));
			} else if (i == R.string.Sun) {
				
				txtDay.setBackgroundColor(res.getColor(R.color.title_text_7));
			} else {

			}

			txtDay.setText((Integer) getItem(position));

			iv.addView(txtDay, lp);

			return iv;
		}
	}
	
	private void generateTopButtons(LinearLayout layTopControls) {
		
		tv = new TextView(getActivity());
//		btnToday = new Button(getActivity());
		btnpre = new Button(getActivity());
		btnnxt = new Button(getActivity());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 20;
		
		tv.setLayoutParams(lp);
		btnpre.setLayoutParams(lp);
		btnnxt.setLayoutParams(lp);
		
		btnpre.setText("Pre");
		btnnxt.setText("Next");
		
		tv.setTextSize(25);

		
		btnpre.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				viewFlipper.showPrevious();
				setPrevViewItem();
				CreateGirdView();
				Log.w("tag", "prev");
			}
		});
		
		btnnxt.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				viewFlipper.showNext();
				setNextViewItem();
				CreateGirdView();
				Log.w("tag", "nxt");
			}
		});
		
		layTopControls.setGravity(Gravity.CENTER_HORIZONTAL);
		layTopControls.addView(btnpre);
		layTopControls.addView(tv);
		layTopControls.addView(btnnxt);
	}
	
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(getActivity());
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;
		
		
		lay.setLayoutParams(params);
		lay.setOrientation(iOrientation);
		lay.setGravity(Gravity.LEFT);
		return lay;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		menu.add(0, INSERT_ID, 0, R.string.menu_newevent);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        long buttonId = info.id;
        
		switch(item.getItemId()) {
		case INSERT_ID:
//			Log.i("TEST", "OnContext -  pos="+calSelected.getTime().getYear()+calSelected.getTime().getMonth()+calSelected.getTime().getDate());
			createEvent(calSelected.getTime().getYear()+calSelected.getTime().getMonth()+calSelected.getTime().getDate()+"");
			return true;
        case DELETE_ID:
//            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//            mDbHelper.deleteTask(info.id);
//            setListAdapter(fillData());
            return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void createEvent(String date){
		
		Intent i = new Intent(getActivity(), EventEditor.class);
		i.putExtra(DBMgr.EVENT_DATE, date);
		startActivityForResult(i, EVENT_CREATE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		lv.setAdapter(fillData());
	}
	
	
	private SimpleCursorAdapter fillData() {
		String s = calSelected.getTime().getYear()+calSelected.getTime().getMonth()+calSelected.getTime().getDate()+"";
        Cursor eventCursor = mDbHelper.fetchEventsByDate(s);
        getActivity().startManagingCursor(eventCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DBMgr.EVENT_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.event_row_title};

        // Now create a simple cursor adapter and set it to display
        MyAdapter events = 
                new MyAdapter(getActivity(), R.layout.event_row, eventCursor, from, to);
        return events;
    }
}


package com.example.betaplanner.Adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.TextView;

import com.example.betaplanner.R;
import com.example.betaplanner.Manager.DBMgr;

public class MyAdapter extends SimpleCursorAdapter{

	private ArrayList<Integer> selection = new ArrayList<Integer>();
	private int mCheckBoxId = 0;
	private String mColumnId;
	
	private DBMgr mDbHelper;
	
	public MyAdapter(Context context, int layout, Cursor c,  
            String[] from, int[] to){
		
		super(context, layout, c, from, to);
		mDbHelper = new DBMgr(context);
		mDbHelper.open();
//		mCheckBoxId = checkBoxId;
//		mColumnId = columnId;
	}

	@Override
	public int getCount() {

		return super.getCount();
	}

	@Override
	public Object getItem(int position) {

		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {

		return super.getItemId(position);
	}
/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int p = position;
		View v = super.getView(position, convertView, parent);
		CheckBox cb = (CheckBox) v.findViewById(mCheckBoxId);
		
		cb.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {

				Cursor c = getCursor();
				c.moveToPosition(p);  
				int rowId = c.getInt(c.getColumnIndexOrThrow(mColumnId));
				
				
			}
		});
		
		Cursor c = getCursor();  
        c.moveToPosition(position);  
        int rowId = c.getInt(c.getColumnIndexOrThrow(mColumnId));  
        if (selection.indexOf(rowId)!= -1) {    
            cb.setChecked(true);    
        } else {    
            cb.setChecked(false);    
        }    
        return v; 
	}
*/
	
	
	
	@Override
	public void bindView(View v, final Context c, Cursor cur) {
		
		final CheckBox cb = (CheckBox) v.findViewById(R.id.cb);
		final TextView tv = (TextView) v.findViewById(R.id.tv2);
		final Cursor c11 = cur;
		
//		cb.setText(cur.getString(cur.getColumnIndex(DBMgr.TASK_CHECKED)));
/*		
		if(cur.getString(cur.getColumnIndex(DBMgr.TASK_CHECKED)).equals("false")){
			
			cb.setChecked(false);
			cb.setText("tmmasdasd");
//			cb.setText(cur.getString(cur.getColumnIndex(DBMgr.TASK_CHECKED)));
			
		}
		else {
			cb.setChecked(true);
//			cb.setText(cur.getString(cur.getColumnIndex(DBMgr.TASK_CHECKED)));
			
		}
*/		final String s1 = cur.getString(cur.getColumnIndex(DBMgr.KEY_ROWID));
		final String s2 = cur.getString(cur.getColumnIndex(DBMgr.TASK_CHECKED));
		final String where = "_id =" + s1;
		
//		Log.w(s1, s2);
/*		
		if(Math.random() > 0.5){
			cb.setChecked(true);
		}
		else{
			cb.setChecked(false);
		}
*/		
		if(cur.getString(cur.getColumnIndex(DBMgr.TASK_CHECKED)).equals("true")){
			cb.setChecked(true);
			tv.setTextColor(c.getResources().getColor(R.color.Color_Done));
		}
		else {
			cb.setChecked(false);
			tv.setTextColor(c.getResources().getColor(R.color.Black));
		}
		
		if(cur.getString(cur.getColumnIndex(DBMgr.TASK_PRIOR)).equals("Low")){
			cb.setBackgroundResource(R.color.green);
		}
		else if(cur.getString(cur.getColumnIndex(DBMgr.TASK_PRIOR)).equals("Medium")){
			cb.setBackgroundResource(R.color.yellow);
		}
		else if(cur.getString(cur.getColumnIndex(DBMgr.TASK_PRIOR)).equals("High")){
			cb.setBackgroundResource(R.color.red);
		}
		else {
			
		}
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			Cursor cc = c11;
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if(isChecked == true){
					tv.setTextColor(c.getResources().getColor(R.color.Color_Done));
					
					ContentValues args = new ContentValues();
			    	args.put("status", "true");
//			    	mDbHelper.taskCheckedChange(cc.getColumnIndex(DBMgr.KEY_ROWID), "true");
//			    	mDbHelper.checkedchange(args, cc.getColumnIndex(DBMgr.KEY_ROWID));
//					Log.w(s1, s2);
					mDbHelper.updateCheck("tasks", args, where);
//					mDbHelper.update("N", "true", s1 + "=" + s1, null);
				}
				else {
					tv.setTextColor(c.getResources().getColor(R.color.Black));
					ContentValues args = new ContentValues();
			    	args.put("status", "false");
					mDbHelper.updateCheck("tasks", args, where);
//			    	mDbHelper.taskCheckedChange(cc.getColumnIndex(DBMgr.KEY_ROWID), "false");			    	
//			    	mDbHelper.checkedchange(args, cc.getColumnIndex(DBMgr.KEY_ROWID));
//					Log.w(s1, s2);
				}
			}
			
		});
		super.bindView(v, c, cur);
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return super.getFilter();
	}
	
}

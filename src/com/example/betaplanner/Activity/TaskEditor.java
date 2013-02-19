package com.example.betaplanner.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.betaplanner.R;
import com.example.betaplanner.Adapter.MyAdapter;
import com.example.betaplanner.Alarm.Alarm;
import com.example.betaplanner.Alarm.DTpicker;
import com.example.betaplanner.Manager.DBMgr;

public class TaskEditor extends Activity implements DTpicker.DTpickerListener{
	
	private static final int SUBTASK_CREATE=0;
    private static final int SUBTASK_EDIT=1;

    private EditText mTitleText;
    private EditText mCommentText;
    private EditText mTimeAlert, mLocAlert;
    private ToggleButton mTb1, mTb2;
    private Spinner	mPriorSpn;
    private Calendar c;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    private Long mRowId;
    private Long mParentRowId;
    private DBMgr mDbHelper;
    private AlarmManager aManager;
    private ListView mlv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mDbHelper = new DBMgr(this);
        mDbHelper.open();
        c = Calendar.getInstance();
        Dialog dialog = new Dialog(this);

        setContentView(R.layout.activity_task_editor);
        setTitle("Task Editor");

        mTitleText = (EditText) findViewById(R.id.task_title_et);
        mCommentText = (EditText) findViewById(R.id.task_comment_et);
        mPriorSpn = (Spinner) findViewById(R.id.priority_spn);
        mlv = (ListView) findViewById(R.id.subtask_list);
        mTimeAlert = (EditText) findViewById(R.id.task_time_alert_et);
        mLocAlert = (EditText) findViewById(R.id.task_location_alert_et);
        mTb1 = (ToggleButton) findViewById(R.id.task_time_alert_tb);
        mTb2 = (ToggleButton) findViewById(R.id.task_location_alert_tb);
        aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        
        dialog.setContentView(R.layout.dtdialog);
		dialog.setTitle("Custom Dialog");
        
//        mTimeAlert.setText(sdf.format(c.getTime()) + sdf2.format(c.getTime()));

        Button confirmButton = (Button) findViewById(R.id.task_save_bt);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DBMgr.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DBMgr.KEY_ROWID)
									: null;
		}
		
		mTb1.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
				if(arg1)
				{
					DTpicker df = new DTpicker();
					df.show(getFragmentManager(), "dt_picker");
				}
				else
				{
					mTimeAlert.setText("");
				}	
			}		
		});
		
		
		mTb2.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
					Intent i = new Intent(TaskEditor.this, Alarm.class);
					startActivity(i);
			}		
		});
		
		Log.w("Show Rowid", mRowId+"");
		mParentRowId = mRowId;

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	setResult(RESULT_OK);
                finish();
            }

        });
	}

	
    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchTask(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(DBMgr.TASK_TITLE)));
            mCommentText.setText(note.getString(
                    note.getColumnIndexOrThrow(DBMgr.TASK_BODY)));

            String s = note.getString(note.getColumnIndexOrThrow(DBMgr.TASK_PRIOR));
            
            if(s.equals("High")){
            	mPriorSpn.setSelection(3);
            }
            else if(s.equals("Medium")){
            	mPriorSpn.setSelection(2);
            }
            else if(s.equals("Low")){
            	mPriorSpn.setSelection(1);
            }
            else{
            	mPriorSpn.setSelection(0);
            }
            
            mlv.setAdapter(fillSubtask());
       }
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_task_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.new_subtask:
//			Log.w("adkjfhakjsdf", "fajkdhfkjahdlf");
            createSubtask();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DBMgr.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mCommentText.getText().toString();
        String prior = mPriorSpn.getSelectedItem().toString();
//        Log.w("prioriey", prior+"");

        if (mRowId == null) {
            long id = mDbHelper.createTask(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTask(mRowId, title, body, prior);
            
        }
    }
    
    private long getParentRowId(long rowId){
    	
    	mParentRowId = rowId;
    	return mParentRowId;
    }
    
    private void createSubtask(){
    	
    	Intent i = new Intent(TaskEditor.this, TaskEditor.class);
    	
    	Log.w("Mrowid", mRowId+"");
//    	i.putExtra(DBMgr.KEY_ROWID, id);
//    	i.putExtra(DBMgr.SUBTASK_PARENT, mRowId);
    	
        startActivityForResult(i, SUBTASK_CREATE);
    }
    
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mlv.setAdapter(fillSubtask());
	}
	
	
	public MyAdapter fillSubtask(){
		Cursor tasksCursor = mDbHelper.fetchAllTasks();
        startManagingCursor(tasksCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DBMgr.TASK_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.tv2};

        // Now create a simple cursor adapter and set it to display
        MyAdapter tasks = 
                new MyAdapter(this, R.layout.task_row, tasksCursor, from, to);
        return tasks;
	}


	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id, args);
	}
}
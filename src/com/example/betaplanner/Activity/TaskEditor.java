package com.example.betaplanner.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.betaplanner.R;
import com.example.betaplanner.Adapter.MyAdapter;
import com.example.betaplanner.Manager.DBMgr;

public class TaskEditor extends Activity {
	
	private static final int SUBTASK_CREATE=0;
    private static final int SUBTASK_EDIT=1;

    private EditText mTitleText;
    private EditText mCommentText;
    private Spinner	mPriorSpn;
    private Long mRowId;
    private DBMgr mDbHelper;
    private ListView mlv;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mDbHelper = new DBMgr(this);
        mDbHelper.open();

        setContentView(R.layout.activity_task_editor);
        setTitle("Task Editor");

        mTitleText = (EditText) findViewById(R.id.task_title_et);
        mCommentText = (EditText) findViewById(R.id.task_comment_et);
        mPriorSpn = (Spinner) findViewById(R.id.priority_spn);
        mlv = (ListView) findViewById(R.id.subtask_list);

        Button confirmButton = (Button) findViewById(R.id.task_save_bt);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DBMgr.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DBMgr.KEY_ROWID)
									: null;
		}
		
		Log.w("Show Rowid", mRowId+"");

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
    
    private void createSubtask(){
    	
    	Intent i = new Intent(this, TaskEditor.class);
//    	Bundle b = new Bundle();
//    	Log.w("Mrowid", mRowId+"");
//    	b.putString("parentRow", mRowId+"");
//    	i.putExtras(b);
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
}
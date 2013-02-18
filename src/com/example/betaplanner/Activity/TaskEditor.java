package com.example.betaplanner.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.betaplanner.R;
import com.example.betaplanner.Manager.DBMgr;

public class TaskEditor extends Activity {

    private EditText mTitleText;
    private EditText mCommentText;
    private Spinner	mPriorSpn;
    private Long mRowId;
    private DBMgr mDbHelper;
    private CheckBox mCb;
    
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
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
        Log.w("prioriey", prior+"");

        if (mRowId == null) {
            long id = mDbHelper.createTask(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTask(mRowId, title, body, prior);
            
        }
    }
}
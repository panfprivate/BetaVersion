package com.example.betaplanner.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.betaplanner.R;
import com.example.betaplanner.Manager.DBMgr;

public class EventEditor extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DBMgr mDbHelper;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DBMgr(this);
        mDbHelper.open();
        
        Intent i = getIntent();
        date = i.getExtras().getString(DBMgr.EVENT_DATE);

        setContentView(R.layout.activity_event_editor);
        setTitle("Event");

        mTitleText = (EditText) findViewById(R.id.event_title_et);
        mBodyText = (EditText) findViewById(R.id.event_body_et);

        Button confirmButton = (Button) findViewById(R.id.btn_save_event);
        
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DBMgr.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null && extras.getString(DBMgr.EVENT_DATE) == null ? extras.getLong(DBMgr.KEY_ROWID)
									: null;
		}

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
           
            	setResult(RESULT_OK);
                finish();
            }

        });
    }

    
    private void saveState() {
    	Log.w("Where", "SAVESTATE()!!");
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();


        if (mRowId == null) {
        	
            long id = mDbHelper.createEvent(title, body, date);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateEvent(mRowId, title, body, date);
        }
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


    
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor event = mDbHelper.fetchEvent(mRowId, date);
            
            startManagingCursor(event);
            mTitleText.setText(event.getString(
                    event.getColumnIndexOrThrow(DBMgr.EVENT_TITLE)));
            mBodyText.setText(event.getString(
                    event.getColumnIndexOrThrow(DBMgr.EVENT_BODY)));
        }
    }

}
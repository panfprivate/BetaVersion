package com.example.betaplanner.Activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.betaplanner.R;
import com.example.betaplanner.Manager.DBMgr;

public class NoteEditor extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DBMgr mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DBMgr(this);
        mDbHelper.open();
        
        Log.w("Where", "1");
        
        setContentView(R.layout.activity_note_editor);
        setTitle(R.string.title_noteedit);

        mTitleText = (EditText) findViewById(R.id.note_title);
        mBodyText = (EditText) findViewById(R.id.note_body);

        Log.w("Where", "2");
        
        Button confirmButton = (Button) findViewById(R.id.save);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DBMgr.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DBMgr.KEY_ROWID)
									: null;
		}

		Log.w("Where", "3 :" + mRowId);
		
		populateFields();

		Log.w("Where", "8");
		
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    
    private void populateFields() {
    	Log.w("Where", "4");
        if (mRowId != null) {
        	Log.w("Where", "5 + " + mRowId);
            Cursor note = mDbHelper.fetchNote(mRowId);
            Log.w("Where", "6");
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(DBMgr.NOTE_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(DBMgr.NOTE_BODY)));
            Log.w("Where", "7");
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

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body);
        }
    }

}

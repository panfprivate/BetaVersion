package com.example.betaplanner.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBMgr {

    public static final String NOTE_TITLE = "notetitle";
    public static final String NOTE_BODY = "notebody";
    public static final String TASK_TITLE = "tasktitle";
    public static final String TASK_BODY = "taskbody";
    public static final String TASK_CHECKED = "status";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_NOTETABLE = "notes";
    private static final String DATABASE_TASKTABLE = "tasks";
    private static final int DATABASE_VERSION = 2;


    private static final String NOTEDB_CREATE =
        "create table notes (_id integer primary key autoincrement, "
        + "notetitle text not null, notebody text not null);";
    private static final String TASKDB_CREATE =
        "create table tasks (_id integer primary key autoincrement, "
        + "tasktitle text not null, taskbody text not null, status text not null);";//, check text not null

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(NOTEDB_CREATE);
            db.execSQL(TASKDB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            db.execSQL("DROP TABLE IF EXISTS tasks");
            onCreate(db);
        }
    }


    public DBMgr(Context ctx) {
        this.mCtx = ctx;
    }


    public DBMgr open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    
    public void close() {
        mDbHelper.close();
    }



    public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NOTE_TITLE, title);
        initialValues.put(NOTE_BODY, body);

        return mDb.insert(DATABASE_NOTETABLE, null, initialValues);
    }


    public boolean deleteNote(long rowId) {

        return mDb.delete(DATABASE_NOTETABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

 
    public long createTask(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TASK_TITLE, title);
        initialValues.put(TASK_BODY, body);
        initialValues.put(TASK_CHECKED, "false");

        return mDb.insert(DATABASE_TASKTABLE, null, initialValues);
    }


    public boolean deleteTask(long rowId) {

        return mDb.delete(DATABASE_TASKTABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_NOTETABLE, 
        		new String[] {KEY_ROWID, 
        					NOTE_TITLE,
        					NOTE_BODY
        		}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    
    public Cursor fetchAllTasks() {

        return mDb.query(DATABASE_TASKTABLE, 
        		new String[] {KEY_ROWID, 
			        		TASK_TITLE,
			                TASK_BODY,
			                TASK_CHECKED
                }, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public void taskCheckedChange(long rowId, String status){
    	ContentValues args = new ContentValues();
    	args.put(TASK_CHECKED, status);
    	
    	mDb.update(DATABASE_TASKTABLE, args, KEY_ROWID + "=" + rowId, null);
    }

    
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_NOTETABLE, new String[] {KEY_ROWID,
                    NOTE_TITLE, NOTE_BODY}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(NOTE_TITLE, title);
        args.put(NOTE_BODY, body);

        return mDb.update(DATABASE_NOTETABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    
    public Cursor fetchTask(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TASKTABLE, new String[] {KEY_ROWID,
                    TASK_TITLE, TASK_BODY, TASK_CHECKED}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public boolean updateTask(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(TASK_TITLE, title);
        args.put(TASK_BODY, body);

        return mDb.update(DATABASE_TASKTABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public void updateCheck(String tablename, ContentValues cv, String where){
    	
    	mDb.update(tablename, cv, where, null);
    }
}
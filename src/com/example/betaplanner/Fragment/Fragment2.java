package com.example.betaplanner.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.betaplanner.R;
import com.example.betaplanner.Activity.NoteEditor;
import com.example.betaplanner.Activity.TaskEditor;
import com.example.betaplanner.Adapter.MyAdapter;
import com.example.betaplanner.Manager.DBMgr;

public class Fragment2 extends ListFragment {
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private DBMgr mDbHelper;
	private MyAdapter msca;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mDbHelper = new DBMgr(getActivity());
        mDbHelper.open();
        
        msca = fillData();
        setListAdapter(msca);
	}
	
	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		registerForContextMenu(inflater.inflate(R.layout.activity_fragment1, container, false));
		return inflater.inflate(R.layout.fragment_task, container, false);
	}
	

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(getActivity(), TaskEditor.class);
		i.putExtra(DBMgr.KEY_ROWID, id);
		startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mDbHelper.deleteTask(info.id);
            setListAdapter(fillData());
            return true;
		}
		return super.onContextItemSelected(item);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case INSERT_ID:
            createTask();
            return true;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	private MyAdapter fillData() {
        Cursor tasksCursor = mDbHelper.fetchAllTasks();
        getActivity().startManagingCursor(tasksCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DBMgr.TASK_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.tv2};

        // Now create a simple cursor adapter and set it to display
        MyAdapter tasks = 
                new MyAdapter(getActivity(), R.layout.task_row, tasksCursor, from, to);
//        mll.setAdapter(notes);
        return tasks;
    }


	
	private void createTask() {
        Intent i = new Intent(getActivity(), TaskEditor.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		setListAdapter(fillData());
	}
}

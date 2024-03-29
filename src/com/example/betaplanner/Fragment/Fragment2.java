package com.example.betaplanner.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.betaplanner.R;
import com.example.betaplanner.Activity.TaskEditor;
import com.example.betaplanner.Adapter.MyAdapter;
import com.example.betaplanner.Manager.DBMgr;

public class Fragment2 extends ListFragment{
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private DBMgr mDbHelper;
	private MyAdapter msca;
	private EditText mSet;
	ArrayAdapter<String> adapter;
	private View mv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
//		getListView().setFastScrollEnabled(true);
		mDbHelper = new DBMgr(getActivity());
        mDbHelper.open();
//        mSet = (EditText) getActivity().findViewById(R.id.search_et);
        
        
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
		
		mv = inflater.inflate(R.layout.fragment_task, container, false);
		mSet = (EditText) mv.findViewById(R.id.search_et);
		
		msca = fillData();
		setListAdapter(msca);

		/*
		msca.setFilterQueryProvider(new FilterQueryProvider() {

	        public Cursor runQuery(CharSequence constraint) {
	            String partialItemName = null;
	            if (constraint != null) {
	                partialItemName = constraint.toString();
	            }
	            return mDbHelper.suggestItemCompletions(partialItemName);
	        }
	    });
		*/
		
		mSet.addTextChangedListener(new TextWatcher() {
			 
			 public void onTextChanged(CharSequence cs, int start, int before, int count) {
			   //get the text in the EditText
			   String searchString=mSet.getText().toString();
			   int textLength=searchString.length();
			 
			          //clear the initial data set
//			   searchResults.clear();
			 
			   
			   
			   msca.notifyDataSetChanged();
			 }
			 
			 public void beforeTextChanged(CharSequence s, int start, int count,
			     int after) {
			 
			 
			   }
			 
			   public void afterTextChanged(Editable s) {
			 
			 
			   }
			  });

		
		return mv;
	}
	

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(getActivity(), TaskEditor.class);
		i.putExtra(DBMgr.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
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

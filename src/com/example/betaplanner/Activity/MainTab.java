package com.example.betaplanner.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.example.betaplanner.R;
import com.example.betaplanner.Fragment.Fragment1;
import com.example.betaplanner.Fragment.Fragment2;
import com.example.betaplanner.Fragment.Fragment3;
import com.example.betaplanner.Manager.TabManager;

public class MainTab extends FragmentActivity {

	private TabHost mTabHost;
	private TabManager mTabManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
        mTabHost.setCurrentTab(0);
        mTabManager.addTab(
            mTabHost.newTabSpec("Fragment1").setIndicator("NOTE"),
            Fragment1.class, null);
        mTabManager.addTab(
            mTabHost.newTabSpec("Fragment2").setIndicator("TASK"),
            Fragment2.class, null);
        mTabManager.addTab(
            mTabHost.newTabSpec("Fragment3").setIndicator("CALENDAR"),
            Fragment3.class, null);

        
        DisplayMetrics dm = new DisplayMetrics();   
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenWidth = dm.widthPixels;  
           
        TabWidget tabWidget = mTabHost.getTabWidget();  
        int count = tabWidget.getChildCount(); 
        if (count == 3) {   
            for (int i = 0; i < count; i++) {   
                tabWidget.getChildTabViewAt(i)
                      .setMinimumWidth((screenWidth)/3); 
            }   
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_tab, menu);
		return true;
	}
	
	public void dosomething(View v){
		
    	Toast toast = Toast.makeText(this, "¸µÅÍÄãÕæÅ£±Æ£¡", Toast.LENGTH_SHORT);
    	toast.show();
	}

}

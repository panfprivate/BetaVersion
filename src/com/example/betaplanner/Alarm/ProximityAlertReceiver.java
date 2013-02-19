/**
 * 
 */
package com.example.betaplanner.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.betaplanner.R;

public class ProximityAlertReceiver extends BroadcastReceiver
{
	
	MediaPlayer alarmMusic;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
//		tv = (TextView) findViewById(R.id.textView3);//findViewById(R.id.textView3);
		
	
		boolean isEnter = intent.getBooleanExtra(
			LocationManager.KEY_PROXIMITY_ENTERING, false);
		if(isEnter)
		{
			
			Toast.makeText(context
				, "you are entering."
				, Toast.LENGTH_LONG)
				.show();
			
			alarmMusic = MediaPlayer.create(context, R.raw.alarm);
			alarmMusic.setLooping(false);

			alarmMusic.start();
		}
		else
		{
			
			Toast.makeText(context
				, "you are leaving"
				, Toast.LENGTH_LONG)
				.show();		
		}
	}
}

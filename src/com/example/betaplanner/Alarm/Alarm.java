package com.example.betaplanner.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.example.betaplanner.R;

public class Alarm extends Activity {
	
		MediaPlayer alarmMusic;
		ToggleButton tb;

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

	//		tb = (ToggleButton) findViewById(R.id.toggleButton1);

			alarmMusic = MediaPlayer.create(this, R.raw.alarm);
			alarmMusic.setLooping(true);

			alarmMusic.start();

			new AlertDialog.Builder(Alarm.this)
				.setTitle("Alarm")
				.setMessage("Ring!!!")
				.setPositiveButton(
					"Confirm" ,
					new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog , int which)
						{

							alarmMusic.stop();
		//					tb.setChecked(false);
							
							Alarm.this.finish();
						}
					}
				)
				.show();

		}
	}
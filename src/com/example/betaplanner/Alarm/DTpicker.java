package com.example.betaplanner.Alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.betaplanner.R;

public class DTpicker extends DialogFragment {

	private DatePicker mDp;
	private TimePicker mTp;
	private DTtransfer mDtt;
	private EditText mEt;
	private int day, month, year, hour, minute;
	private AlarmManager mAmgr;
	private MediaPlayer alarmMusic;
//	private DTpicker mDtp;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		mEt = (EditText) getActivity().findViewById(R.id.task_time_alert_et);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mAmgr = (AlarmManager) getActivity().getSystemService(Service.ALARM_SERVICE);
       
        builder.setView(inflater.inflate(R.layout.dtdialog, null))
        	.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                   
        		
        		@Override
                   public void onClick(DialogInterface dialog, int id) {
        				
        				mDp = (DatePicker) getDialog().findViewById(R.id.dp);
        				mTp = (TimePicker) getDialog().findViewById(R.id.tp);
        				
        				int a =  mDp.getMonth() + 1;
                	   
        				day = mDp.getDayOfMonth();
        				month = mDp.getMonth() + 1; 
        				year = mDp.getYear();
        				hour = mTp.getCurrentHour();
        				minute = mTp.getCurrentMinute();
        				
        				mEt.setText(year+"-"+month+"-"+day+"  "+hour+":"+minute);
        				
        				Intent intent = new Intent(getActivity(), Alarm.class);
        		        PendingIntent pintent = PendingIntent.getActivity(getActivity(), 0,intent, 0);
        				Calendar c = Calendar.getInstance();
        				c.setTimeInMillis(System.currentTimeMillis());
        				c.clear();
        				
        				c.set(Calendar.YEAR, year);
        				c.set(Calendar.MONTH, month-1);
        				c.set(Calendar.DAY_OF_MONTH, day); 
        				c.set(Calendar.HOUR_OF_DAY, hour);
        				c.set(Calendar.MINUTE, minute);
        				c.set(Calendar.SECOND, 0);
        				
        				mAmgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pintent);
        				
        				Toast.makeText(getActivity(), "Successful", 1).show();
        				
        				mListener.onDialogPositiveClick(DTpicker.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   DTpicker.this.getDialog().cancel();
                	   mListener.onDialogNegativeClick(DTpicker.this);
                   }
               });      
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	public DTtransfer getDTt(){
		
		return new DTtransfer(day, month, year, hour, minute);
	}
	
	public interface DTpickerListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	DTpickerListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the DTpickerListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DTpickerListener so we can send events to the host
            mListener = (DTpickerListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DTpickerListener");
        }
    }
}

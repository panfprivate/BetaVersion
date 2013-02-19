package com.example.betaplanner.Alarm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocAlerter extends Activity{
	
	private String mLocService;
	private LocationManager mLocMgr;
	
	private double mLatitude;
	private double mLongitude;
	private float mRadius;
/*	
	public LocAlerter(double latitude, double longitude, float radius){
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mRadius = radius;
		
//		Log.w("adfadfadfadf", latitude + "," + longitude+ "," + radius +"");
		
		Log.w("dfjajhkjh", mLatitude + "," + mLongitude+ "," + mRadius +"");
//		alertAtLoc();
		Log.w("jkjg gg",mLatitude + "," + mLongitude+ "," + mRadius +"");
	}
*/	
	public void alertAtLoc(double latitude, double longitude, float radius){
		
		Log.w("ngfddytudh",latitude + "," + longitude+ "," + radius +"");
		mLocService = Context.LOCATION_SERVICE;
		mLocMgr = (LocationManager) getSystemService(mLocService);
		Log.w("vdfgsfdg",latitude + "," + longitude+ "," + radius +"");
		Intent i = new Intent(this, ProximityAlertReceiver.class);
		
		PendingIntent pi = PendingIntent.getBroadcast(this, -1, i, 0);			
		mLocMgr.addProximityAlert(latitude, longitude, radius, -1, pi);	
	}

}

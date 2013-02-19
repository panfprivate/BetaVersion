package com.example.betaplanner.Alarm;

public class DTtransfer {

	 private int day;
	 private int month;
	 private int year;
	 private int hour;
	 private int minute;
	 
	 public DTtransfer(int day, int month, int year, int hour, int minute){
		 
		 this.day = day;
		 this.month = month;
		 this.year = year;
		 this.hour = hour;
		 this.minute = minute;
	 }
	 
	 public int getDay(){
		 
		 return day;
	 }
	 
	 public int getMonth(){
		 return month;
	 }
	 
	 public int getYear(){
		 return year;
	 }
	 
	 public int getHour(){
		 return hour;
	 }
	 
	 public int getMinute(){
		 return minute;
	 }
}

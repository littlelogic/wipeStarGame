package com.gdx.common;

public class LogW {
	
	public interface ALog {

		 public void  i(String string1, String string2);

		 public void  d(String string1, String string2);

		 public void  v(String string1, String string2);

		 public void  w(String string1, String string2);

		 public void  e(String string1, String string2);
		 
		 public void  writeInfo(String text_str);

		 public  void  singleTouchRemoveMark(boolean mark);

	}
	
	private static ALog mALog;
	
	public static void setALog(ALog mALog_){
		mALog=mALog_;
	}
	
	 public  static  void  i(String string1,String string2){
		 mALog.i(string1,string2);	 //Green
	 }
	 public  static  void  e(String string1,String string2){
		 mALog.e(string1,string2);	 //Red
	 }
	 public  static  void  d(String string1,String string2){
		 mALog.d(string1,string2);	 //蓝色-blue
	 }
	 public  static  void  v(String string1,String string2){
		 mALog.v(string1,string2);	 //黑色-black
	 }
	 public  static  void  w(String string1,String string2){
		 mALog.w(string1,string2);	 //黄色-
	 }
	 public  static  void  first(String string1,String string2){
		 mALog.e(string1,string2);	 //Green
	 }

	 public  static  void  singleTouchRemoveMark(boolean mark){
		mALog.singleTouchRemoveMark(mark);	 //Green
	 }
	 
	 
//	 public  static  void  i(String string1,String string2){
//	 }
//	 public  static  void  e(String string1,String string2){
//	 }
//	 public  static  void  d(String string1,String string2){
//	 }
//	 public  static  void  v(String string1,String string2){
//	 }
//	 public  static  void  w(String string1,String string2){
//	 }
//	 public  static  void  first(String string1,String string2){
//	 }
	 
	 
	
	

}

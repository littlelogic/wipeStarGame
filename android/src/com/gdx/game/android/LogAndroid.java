package com.gdx.game.android;

import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.gdx.common.LogW.ALog;

public class LogAndroid implements ALog{

	@Override
	public void i(String string1, String string2) {
		Log.i(string1,string2);
	}

	@Override
	public void d(String string1, String string2) {
		 Log.d(string1,string2);
	}

	@Override
	public void v(String string1, String string2) {
		 Log.v(string1,string2);	 
	}

	@Override
	public void w(String string1, String string2) {
		Log.w(string1,string2);
	}

	@Override
	public void e(String string1, String string2) {
		 Log.e(string1,string2);	 
	}

	public void writeInfo(String text_str){
		CrashHandler.getInstance().saveCrashInfoToFile_others(text_str);
	}

	public static TextView mTextView;
	android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper());

	public void  singleTouchRemoveMark(boolean mark){
		try {
			if(mTextView != null){
				String text_str="";
				if(mark){
					text_str = "单点消除";
				}else{
					text_str = "双点消除";
				}
				final String text_str_final=text_str;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTextView.setText(text_str_final);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}

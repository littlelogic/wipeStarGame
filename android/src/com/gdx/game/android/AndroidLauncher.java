package com.gdx.game.android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.W_AndroidApplication;
import com.gdx.screen.StarGame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AndroidLauncher extends W_AndroidApplication {
//public class AndroidLauncher extends AndroidApplication {
	//ipone5�ֱ���   1136��640=727040--��-1.775--------------
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//--setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
		//--setSCREEN_ORIENTATION_SENSOR_PORTRAIT();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//--initialize(new MyGdxGame(), config);
		//--nitialize(new ParticleEmitterTest(), config);
		initialize(new StarGame(new LogAndroid()), config);
		
		
		
//		this.setContentView(view);
//		getWindow().setContentView(view);
//		//FrameLayout.LayoutParams
//		ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
//		rootView.getChildCount();
//		for(int i=0;i<rootView.getChildCount();i++){
//			/*rootView.getChildAt(i).toString();
//			rootView.getChildAt(i).getClass().getName();*/
//			Log.e("zddz_001", "--AndroidLauncher--onCreate--"+i+"-->>"+rootView.getChildAt(i).toString());
//			Log.e("zddz_001", "--AndroidLauncher--onCreate--"+i+"--getName-->>"+rootView.getChildAt(i).getClass().getName());
//		}


		
		
		
		
//		Pixmap mPixmap=new Pixmap(bg_byte_array,0,
//				MainActivity.bg_byte_array.length);
//		Texture mbg=new Texture(mPixmap);
//		mbg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		backgroundRegion = new TextureRegion(mbg);
		
		
		
		
		
	}
	
	//================================================================================
	
	private static void disableRotation(Activity activity)
	{       
	    final int orientation = activity.getResources().getConfiguration().orientation;
	    final int rotation = activity.getWindowManager().getDefaultDisplay().getOrientation();

	    // Copied from Android docs, since we don't have these values in Froyo 2.2
	    int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
	    int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;

	    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO)
	    {
	        SCREEN_ORIENTATION_REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	        SCREEN_ORIENTATION_REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	    }

	    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
	    {
	        if (orientation == Configuration.ORIENTATION_PORTRAIT)
	        {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        }
	        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
	        {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        }
	    }
	    else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270)
	    {
	        if (orientation == Configuration.ORIENTATION_PORTRAIT)
	        {
	            activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);
	        }
	        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
	        {
	            activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
	        }
	    }
	}

	private static void enableRotation(Activity activity)
	{
	    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
	
	
	OrientationEventListener mScreenOrientationEventListener;
	/**
	 * 1-正常竖屏，2-翻转竖屏
	 */
	int ScreenOrientation_wjw;
	/**
	 * 1-正常竖屏，2-翻转竖屏,0--其他
	 */
	int ScreenEnter_wjw;
	final int ScreenTurnTime_wjw=500;
	long ScreenLastTime_wjw=0;
	
	public void setSCREEN_ORIENTATION_SENSOR_PORTRAIT(){
		
		
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		
		ScreenOrientation_wjw=1;
		ScreenEnter_wjw=1;
		ScreenLastTime_wjw=0;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.mScreenOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int i) {
                // i的范围是0～359
                // 屏幕左边在顶部的时候 i = 90;
                // 屏幕顶部在底部的时候 i = 180;
                // 屏幕右边在底部的时候 i = 270;
                // 正常情况默认i = 0;

            	Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--i-->>"+i);
            	Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--ScreenOrientation_wjw-->>"+ScreenOrientation_wjw);
            	
            	
            	/*if(270 < i || i < 90) {*/
            	if(315 < i || i < 45&&ScreenOrientation_wjw==2) {
            		Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--偏正向->>");
            		if(ScreenEnter_wjw==1){
            			if((System.currentTimeMillis()-ScreenLastTime_wjw)>ScreenTurnTime_wjw){
                     		ScreenOrientation_wjw=1;
                    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                		}
            		}else{
            			ScreenEnter_wjw=1;
            			ScreenLastTime_wjw= System.currentTimeMillis();
            		}
            	}else if(225 > i && i > 135&&ScreenOrientation_wjw==1) {
            	/*}else if(270 > i || i > 90) {*/
            		Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--偏反向->>");
            		if(ScreenEnter_wjw==2){
            			if((System.currentTimeMillis()-ScreenLastTime_wjw)>ScreenTurnTime_wjw){
            			    ScreenOrientation_wjw=2;
                    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            			}
            		}else{
            			ScreenEnter_wjw=2;
            			ScreenLastTime_wjw= System.currentTimeMillis();
            		}
            	}else{
            		ScreenEnter_wjw=0;
            	}
            	 
            	/*int mScreenExifOrientation;
                if(45 <= i && i < 135) {
                    mScreenExifOrientation = ExifInterface.ORIENTATION_ROTATE_180;
                    Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--ORIENTATION_ROTATE_180-->>");	
                } else if(135 <= i && i < 225) {
                    mScreenExifOrientation = ExifInterface.ORIENTATION_ROTATE_270;
                    Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--ORIENTATION_ROTATE_270-->>");	
                } else if(225 <= i && i < 315) {
                    mScreenExifOrientation = ExifInterface.ORIENTATION_NORMAL;
                    Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--ORIENTATION_NORMAL-->>");	
                } else {
                    mScreenExifOrientation = ExifInterface.ORIENTATION_ROTATE_90;
                    Log.i("zddz_001", "--AndroidLauncher--OrientationEventListener--orientation--ORIENTATION_ROTATE_90-->>");	
                }*/
            }
        };
        mScreenOrientationEventListener.enable();
	}
	
	//--------------------------------------
	
	public void setSensorEventListene(){
		SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		
		mSensorManager.registerListener(new MySensorEventListener(), mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	class MySensorEventListener implements SensorEventListener {
		 
	    @Override
	    public void onSensorChanged(SensorEvent event) {
	        // TODO Auto-generated method stub
	        float a = event.values[0];
	        //azimuthAngle.setText(a + "");
	        float b = event.values[1];
	       // pitchAngle.setText(b + "");
	        float c = event.values[2];
	       // rollAngle.setText(c + "");
	        
	        Log.i("zddz_001", "--MActivity--onSensorChanged--z-->>"+c);
	    }
	 
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	        // TODO Auto-generated method stub
	 
	    }
	 
	}
	//================================================================================
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	
	        	break;
	        	//return true;
	        case KeyEvent.KEYCODE_MENU:
	        return true;
		    default:
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	    
	        	break;
	        	//return true;
	        case KeyEvent.KEYCODE_MENU:
	        	deal_KEYCODE_MENU();
	        	return true;
		    default:
        }
        return super.onKeyUp(keyCode, event);
    }
	
	private void deal_KEYCODE_MENU(){
		
		
	}

	//================================================================================

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);   
	    Log.i("zddz_001", "--MActivity--onConfigurationChanged转屛--orientation-->>"+this.getResources().getConfiguration().orientation);
	    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        // 加入横屏要处理的代码   
	    } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	        // 加入竖屏要处理的代码   
	    }   
	} 

	//================================================================================

	
	public void getVersion_v(){  
	    String[] version={"null","null","null","null"};
	    String str1 = "/proc/version";
	    String str2;
	    String[] arrayOfString = null;
	    try {  
	        FileReader localFileReader = new FileReader(str1);
	        BufferedReader localBufferedReader = new BufferedReader(
	                localFileReader, 8192);  
	        str2 = localBufferedReader.readLine();  
	        arrayOfString = str2.split("\\s+");  
	        version[0]=arrayOfString[2];//KernelVersion  
	        localBufferedReader.close();  
	    } catch (IOException e) {
	    }  
	    version[1] = Build.VERSION.RELEASE;// firmware version
	    version[2]= Build.MODEL;//model
	    version[3]= Build.DISPLAY;//system version
	    //return version;  
	    
	    for(int i=0;i<version.length;i++){
	    	 Log.i("zddz_001", "--AndroidLauncher--getVersion_v--version[i]-->>"+version[i]);
	    }
	    for(int i=0;i<arrayOfString.length;i++){
	    	 Log.i("zddz_001", "--AndroidLauncher--getVersion_v--arrayOfString[i]-->>"+arrayOfString[i]);
	    }
	   
	}
	
}

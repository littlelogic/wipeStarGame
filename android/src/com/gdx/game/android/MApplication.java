package com.gdx.game.android;//com.gdx.game.android.MApplication

import android.app.Application;
import android.content.res.Configuration;


public class MApplication extends Application {
	
	public static float BatteryLevel = -1;   
  
    @Override
	public void onCreate() {
		super.onCreate();
		myLog.i("Activity", "---MApplication----onCreate----->>>");

		// 异常处理，不需要处理时注释掉这两句即可！   
	    CrashHandler crashHandler = CrashHandler.getInstance();
	    // 注册crashHandler    
	    crashHandler.init(getApplicationContext());    
  
/*	    // 优化内存,以下非必须！   
//	    VMRuntime.getRuntime().setTargetHeapUtilization(HEAP_UTILIZATION);   
//	    VMRuntime.getRuntime().setMinimumHeapSize(MIN_HEAP_SIZE);    
	        //changeMetrics(this);//修改屏幕Density   
*/
	}

	@Override
	public void onTerminate() {
		myLog.e("Activity", "---MApplication----onTerminate-->>>");
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		myLog.e("Activity", "---MApplication----onConfigurationChanged-->>>");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		myLog.e("Activity", "---MApplication----onLowMemory-->>>");
		super.onLowMemory();
	}

	///////////////////////////////////////////////////////////////////////////

}
package com.gdx.game.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

public class CrashHandler implements UncaughtExceptionHandler {

	 /** Debug Log tag*/    
    public static final String TAG = "CrashHandler";
    /** 是否开启日志输出,在Debug状态下开启,   
    * 在Release状态下关闭以提示程序性能   
    * */    
    public static final boolean DEBUG = false;    
    /** 系统默认的UncaughtException处理类 */    
    private UncaughtExceptionHandler mDefaultHandler;
    /** CrashHandler实例 */    
    private static CrashHandler INSTANCE;
    /** 程序的Context对象 */    
    private static Context mContext;
    /** 使用Properties来保存设备的信息和错误堆栈信息*/    
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /** 错误报告文件的扩展名 */    
//    private static final String CRASH_REPORTER_EXTENSION = ".cr";    
    private static final String CRASH_REPORTER_EXTENSION = ".txt";
    private static final String Crash_Folder_Name = "wjw_gdx-star-wjw-a-android";
    private static final String Crash_File_Prefix ="测13_";

    /** 保证只有一个CrashHandler实例 */    
    private CrashHandler() {}    
       
    /** 获取CrashHandler实例 ,单例模式*/    
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {    
            INSTANCE = new CrashHandler();
        }    
        return INSTANCE;    
    }    
       
    /**   
    * 初始化,注册Context对象,   
    * 获取系统默认的UncaughtException处理器,   
    * 设置该CrashHandler为程序的默认处理器   
    * @param ctx   
    */    
    public void init(Context ctx) {
        mContext = ctx;    
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }    
       
    /**   
    * 当UncaughtException发生时会转入该函数来处理   
    */    
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {    
            //如果用户没有处理则让系统默认的异常处理器来处理    
            mDefaultHandler.uncaughtException(thread, ex);    
        } else {    
        	 mDefaultHandler.uncaughtException(thread, ex);  
            //Sleep一会后结束程序    
            try {    
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }    
            android.os.Process.killProcess(android.os.Process.myPid());    
            System.exit(10);
        }    
        
//        ImageAdaptive.getMemoryInfo22();
    }    
       
    /**   
    * 自定义错误处理,收集错误信息   
    * 发送错误报告等操作均在此完成.   
    * 开发者可以根据自己的情况来自定义异常处理逻辑   
    * @param ex   
    * @return true:如果处理了该异常信息;否则返回false   
    */    
    
    private boolean handleException(Throwable ex) {
//    	ImageAdaptive.getMemoryInfo22();
    	
        if (ex == null) {    
            Log.w(TAG, "handleException --- ex==null");
            return true;    
        }    
        final String msgTemp = ex.getLocalizedMessage();
        final String msg ;
        if(msgTemp == null) {   
//            return false;   
        	msg="没有获得到系统发出的Throwable信息";
        }else{
        	msg=msgTemp;
        }   
        //使用Toast来显示异常信息    
        new Thread() {
            @Override
            public void run() {    
                Looper.prepare();
//                Toast toast = Toast.makeText(mContext, "程序出错，即将退出:\r\n" + msg,Toast.LENGTH_LONG);   
                Toast toast = Toast.makeText(mContext, "Sorry!游戏出错了，请重新再试..." + msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();   
//              MsgPrompt.showMsg(mContext, "程序出错啦", msg+"\n点确认退出");   
                Looper.loop();
            }    
        }.start();    
        //收集设备信息    
//        collectCrashDeviceInfo(mContext);    
        //保存错误报告文件    
//        saveCrashInfoToFile(ex);    
        String fileName=saveCrashInfoToFile_2(ex);
        //发送错误报告到服务器    
        sendCrashReportsToServer(mContext);    
        
        try {
			postWeb(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return true;    
    }    
       
    /**
     * 单个处理异常
     * @param ex
     * @return
     */
    public static boolean handleException_self(Throwable ex, String text) {
//    	ImageAdaptive.getMemoryInfo22();
    	
        if (ex == null) {    
            Log.w(TAG, "handleException --- ex==null");
            return true;    
        }    
        final String msgTemp = ex.getLocalizedMessage();
        final String msg ;
        if(msgTemp == null) {   
//            return false;   
        	msg="没有获得到系统发出的Throwable信息";
        }else{
        	msg=msgTemp;
        }   
        //使用Toast来显示异常信息    
        new Thread() {
            @Override
            public void run() {    
                Looper.prepare();
//                Toast toast = Toast.makeText(mContext, "程序出错，即将退出:\r\n" + msg,Toast.LENGTH_LONG);   
                Toast toast = Toast.makeText(mContext, "Sorry!游戏出错了，请重新再试..." + msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();   
//              MsgPrompt.showMsg(mContext, "程序出错啦", msg+"\n点确认退出");   
                Looper.loop();
            }    
        }.start();    
        //收集设备信息    
//        collectCrashDeviceInfo(mContext);    
        //保存错误报告文件    
//        saveCrashInfoToFile(ex);    
        saveCrashInfoToFile_3(ex,text);    
        //发送错误报告到服务器    
        sendCrashReportsToServer(mContext);    
        return true;    
    } 
    
    public static boolean handleException_self(String text) {
//    	ImageAdaptive.getMemoryInfo22();   
        //收集设备信息    
//        collectCrashDeviceInfo(mContext);    
        //保存错误报告文件    
//        saveCrashInfoToFile(ex);    
    	saveCrashInfoToFile_final(text);    
        //发送错误报告到服务器    
        return true;    
    }
    
    /**   
    * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告   
    */    
    public void sendPreviousReportsToServer() {    
        sendCrashReportsToServer(mContext);    
    }    
    /**   
    * 把错误报告发送给服务器,包含新产生的和以前没发送的.   
    * @param ctx   
    */    
    private static void sendCrashReportsToServer(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0) {    
        TreeSet<String> sortedFiles = new TreeSet<String>();
        sortedFiles.addAll(Arrays.asList(crFiles));
        for (String fileName : sortedFiles) {
        File cr = new File(ctx.getFilesDir(), fileName);
        postReport(cr);    
        cr.delete();// 删除已发送的报告    
        }    
        }    
    }    
    private static void postReport(File file) {
        // TODO 发送错误报告到服务器    
    }    
       
    /**   
    * 获取错误报告文件名   
    * @param ctx   
    * @return   
    */    
    private static String[] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);    
            }    
        };    
        return filesDir.list(filter);    
    }    
       
    /**   
    * 保存错误信息到文件中   
    * @param ex   
    * @return   
    */    
    private String saveCrashInfoToFile(Throwable ex) {
    	String mm=getSDPath();
        if(mm.equals("false")){
        	return null;  
        }
    	
    	if(ex==null){
    		return "false";
    	}
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        
        ex.printStackTrace(printWriter);    
        Throwable cause = ex.getCause();
        while (cause != null) {    
            cause.printStackTrace(printWriter);    
            cause = cause.getCause();    
        }    
        String result = info.toString();
        printWriter.close();    
        mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());   
        mDeviceCrashInfo.put(STACK_TRACE, result);    
        try {    
            //long timestamp = System.currentTimeMillis();    
            Time t = new Time("GMT+8");
            t.setToNow(); // 取得系统时间   
            int date = t.year * 10000 + t.month * 100 + t.monthDay;   
            int time = t.hour * 10000 + t.minute * 100 + t.second;   
            String fileName = Crash_File_Prefix + "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace;
            
//            trace= mContext.openFileOutput(fileName, Context.MODE_PRIVATE);    
            String tmpFileString= Environment.getExternalStorageDirectory().getPath()+"/"+Crash_Folder_Name+"/";
            
            File tmpFile = new File(tmpFileString);
            if (!tmpFile.exists()) {
    			myLog.i("bbb", "----下载进度--保存路径不存在-->>>");
    			if (tmpFile.mkdir() == false) {
    				//创建目录不成功
    				return "";
    			}
    		}
            trace = new FileOutputStream(new File(tmpFileString,fileName));
//            PrintStream  mm=new PrintStream();
            
            mDeviceCrashInfo.store(trace, "");    
            trace.flush();    
            trace.close();    
            Log.e("ababab", "--saveCrashInfoToFile--last->>>");
            return fileName;    
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
//            tmpFileString=Environment.getExternalStorageDirectory().getPath()+"/"+"Eku8Data"+"/";
        }    
        return null;    
    }    
    
    private String saveCrashInfoToFile_2(Throwable ex) {
    	String Path_1=getSDPath();
        if(Path_1.equals("false")){
        	return null;  
        }
    	
    	if(ex==null){
    		return "false";
    	}
   
        try {    
            SimpleDateFormat sDateFormat3   =   new SimpleDateFormat("yyyyMMdd-HHmmss");//24小时
            String date5=sDateFormat3.format(new java.util.Date());
            String fileName = "crash-" +date5 + CRASH_REPORTER_EXTENSION;
            
            FileOutputStream trace;
//          trace= mContext.openFileOutput(fileName, Context.MODE_PRIVATE);    

            String tmpFileString=Path_1+"/"+Crash_Folder_Name+"/";
            
            File tmpFile = new File(tmpFileString);
            if (!tmpFile.exists()) {
    			myLog.i("bbb", "----下载进度--保存路径不存在-->>>");
    			if (tmpFile.mkdir() == false) {
    				//创建目录不成功
    				return "";
    			}
    		}
            trace = new FileOutputStream(new File(tmpFileString,fileName));
            PrintStream myPrintStream=new PrintStream(trace);
            
            ex.printStackTrace(myPrintStream);    
            Throwable cause = ex.getCause();
            while (cause != null) {    
                cause.printStackTrace(myPrintStream);    
                cause = cause.getCause();    
            }    
            myPrintStream.flush();    
            trace.flush();    
            trace.close();    
            Log.e("ababab", "--saveCrashInfoToFile--last->>>");
            return tmpFileString+fileName;    
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
//            tmpFileString=Environment.getExternalStorageDirectory().getPath()+"/"+"Eku8Data"+"/";
        }    
        
        //ex.toString();
        
        return null;    
    } 
    
    private static String saveCrashInfoToFile_3(Throwable ex, String text) {
    	String Path_1=getSDPath();
        if(Path_1.equals("false")){
        	return null;  
        }
    	
    	if(ex==null){
    		return "false";
    	}
   
        try {    
//            Time t = new Time("GMT+8");    
//            t.setToNow(); // 取得系统时间   
//            int date = t.year * 10000 + t.month * 100 + t.monthDay;   
//            int time = t.hour * 10000 + t.minute * 100 + t.second;   
//            String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION;    
            
            SimpleDateFormat sDateFormat3   =   new SimpleDateFormat("yyyyMMdd-HHmmss");//24小时
            String date5=sDateFormat3.format(new java.util.Date());
            String fileName = "crash-" +date5 + CRASH_REPORTER_EXTENSION;
            
            FileOutputStream trace;
//          trace= mContext.openFileOutput(fileName, Context.MODE_PRIVATE);    

            String tmpFileString=Path_1+"/"+Crash_Folder_Name+"/";
            
            File tmpFile = new File(tmpFileString);
            if (!tmpFile.exists()) {
    			myLog.i("bbb", "----下载进度--保存路径不存在-->>>");
    			if (tmpFile.mkdir() == false) {
    				//创建目录不成功
    				return "";
    			}
    		}
            trace = new FileOutputStream(new File(tmpFileString,fileName));
            PrintStream myPrintStream=new PrintStream(trace);
            
            ex.printStackTrace(myPrintStream);    
            Throwable cause = ex.getCause();
            while (cause != null) {    
                cause.printStackTrace(myPrintStream);    
                cause = cause.getCause();    
            }    
            myPrintStream.println();
            byte[] b=text.getBytes(); 
            myPrintStream.write(b);              
            myPrintStream.flush();    
            trace.flush();    
            trace.close();    
            Log.e("ababab", "--saveCrashInfoToFile--last->>>");
            return fileName;    
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
//            tmpFileString=Environment.getExternalStorageDirectory().getPath()+"/"+"Eku8Data"+"/";
        }    
        return null;    
    }
    
    private static String saveCrashInfoToFile_final(String text) {
    	String Path_1=getSDPath();
        if(Path_1.equals("false")){
        	return null;  
        }
   
        try {    
//            Time t = new Time("GMT+8");    
//             t.setToNow(); // 取得系统时间   
//            int date = t.year * 10000 + t.month * 100 + t.monthDay;   
//            int time = t.hour * 10000 + t.minute * 100 + t.second;   
//            String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION;    
            
            SimpleDateFormat sDateFormat3   =   new SimpleDateFormat("yyyyMMdd-HHmmss");//24小时
            String date5=sDateFormat3.format(new java.util.Date());
            String fileName = "crash-" +date5 + CRASH_REPORTER_EXTENSION;
            
            FileOutputStream trace;
//          trace= mContext.openFileOutput(fileName, Context.MODE_PRIVATE);    

            String tmpFileString=Path_1+"/"+Crash_Folder_Name+"/";
            
            File tmpFile = new File(tmpFileString);
            if (!tmpFile.exists()) {
    			myLog.i("bbb", "----下载进度--保存路径不存在-->>>");
    			if (tmpFile.mkdir() == false) {
    				//创建目录不成功
    				return "";
    			}
    		}
            trace = new FileOutputStream(new File(tmpFileString,fileName));
            PrintStream myPrintStream=new PrintStream(trace);
            myPrintStream.println();
            byte[] b=text.getBytes(); 
            myPrintStream.write(b);              
            myPrintStream.flush();    
            trace.flush();    
            trace.close();    
            Log.e("ababab", "--saveCrashInfoToFile--last->>>");
            return fileName;    
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
//            tmpFileString=Environment.getExternalStorageDirectory().getPath()+"/"+"Eku8Data"+"/";
        }    
        return null;    
    }
    
    public static String saveCrashInfoToFile_others(String text) {
    	String Path_1=getSDPath();
        if(Path_1.equals("false")){
        	return null;  
        }
   
        try {    
            SimpleDateFormat sDateFormat3   =   new SimpleDateFormat("yyyyMMdd-HHmmss");//24小时
            String date5=sDateFormat3.format(new java.util.Date());
            //String fileName = "crash-" +date5 + CRASH_REPORTER_EXTENSION;    
            String fileName = "__11--" +date5 + CRASH_REPORTER_EXTENSION;
            
            FileOutputStream trace;
            String tmpFileString=Path_1+"/"+Crash_Folder_Name+"/";
            
            File tmpFile = new File(tmpFileString);
            if (!tmpFile.exists()) {
    			myLog.i("bbb", "----下载进度--保存路径不存在-->>>");
    			if (tmpFile.mkdir() == false) {
    				//创建目录不成功
    				return "";
    			}
    		}
            trace = new FileOutputStream(new File(tmpFileString,fileName));
            PrintStream myPrintStream=new PrintStream(trace);
            myPrintStream.println();
            byte[] b=text.getBytes(); 
            myPrintStream.write(b);              
            myPrintStream.flush();    
            trace.flush();    
            trace.close();    
            Log.e("ababab", "--saveCrashInfoToFile--last->>>");
            return fileName;    
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
//            tmpFileString=Environment.getExternalStorageDirectory().getPath()+"/"+"Eku8Data"+"/";
        }    
        return null;    
    }
    
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        Log.i("ababab1", "--CrashHandler--getSDPath--sdCardExist->>"+sdCardExist);
        if(sdCardExist){   
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            Log.i("ababab1", "--CrashHandler--getSDPath--sdCardExist--sdDir.getPath()->>"+sdDir.getPath());
            return sdDir.getPath(); 
        }else{
        	return false+""; 
        }   
    }
    
  
    /**   
    * 收集程序崩溃的设备信息   
    *   
    * @param ctx   
    */    
    public void collectCrashDeviceInfo(Context ctx) {
        try {    
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {    
                mDeviceCrashInfo.put(VERSION_NAME,    
                        pi.versionName == null ? "not set" : pi.versionName);    
                mDeviceCrashInfo.put(VERSION_CODE, ""+pi.versionCode);    
            }    
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }    
        //使用反射来收集设备信息.在Build类中包含各种设备信息,    
        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息    
        //具体信息请参考后面的截图    
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {    
                field.setAccessible(true);    
                mDeviceCrashInfo.put(field.getName(), ""+field.get(null));    
                if (DEBUG) {    
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }    
            } catch (Exception e) {
                Log.e(TAG, "Error while collect crash info", e);
            }    
        }    
    }    

    //==============================================================================\
    
    
    public void postWeb(String fileName){
	    if(fileName==null||fileName.equals("")||fileName.equals("false")){
     	    return ;    
        }
	
        String charset="UTF-8";
        BufferedReader reader = null;
        String mString="";
        if(mContext!=null){//记录所在的类名，包名
        	try {
				mString += mContext.getClass().getName()+"\\n";//转义
				mString +=getRunningActivityName()+"\\n";//转义
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        try {//记录版本名称，号码
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) { 
            	mString +="版本名称-->"+pi.versionName+"\\n";//转义
            	mString +="版本号码-->"+pi.versionCode+"\\n";//转义
            }    
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		} 
        
       
        try {
		    reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),charset));
		    String tempString = null;
		    while ((tempString = reader.readLine())!= null){ 
		    	///*//mString+=tempString+"\n";*/
		        mString+=tempString+"\\n";//转义
		    }
		    reader.close();
        } catch (UnsupportedEncodingException e1) {
 			e1.printStackTrace();
 		} catch (FileNotFoundException e1) {
 			e1.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		} catch (Exception e) {
 			e.printStackTrace();
 		} finally {
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e1) {
                 }
             }
        }
 		
 		try {//记录堆本内存信息
 			mString += "=======================================================>>"+"\\n";//转义
 			mString += "=======================================================>>"+"\\n";//转义
 			//--mString += "--屏幕分辨率-->>"+ImageAdaptive.targetWidth+"*"+ImageAdaptive.targeHeight+"\\n";//转义
 			//--mString += "--玩家ID-->>"+UserInformation.userID+"\\n";//转义
 			//--mString += "--玩家帐号-->>"+UserInformation.getAccount_Local()+"\\n";//转义
 			//--mString += "--玩家昵称-->>"+UserInformation.Nick+"\\n";//转义
         	mString += "--当前进程navtive堆本身总的内存大小(KB)-->>"+ Debug.getNativeHeapSize()/1024+"\\n";//转义
         	mString += "--当前进程navtive堆中已使用的内存大小(KB)--->>"+ Debug.getNativeHeapAllocatedSize()/1024+"\\n";//转义
         	mString += "--当前进程navtive堆中已经剩余的内存大小(KB)->>"+ Debug.getNativeHeapFreeSize()/1024+"\\n";//转义
         	mString += "--Runtime.getRuntime().maxMemory()---->>"+ Runtime.getRuntime().maxMemory()/1024+"\\n";//转义
         	mString += "--Runtime.getRuntime().totalMemory()-->>"+ Runtime.getRuntime().totalMemory()/1024+"\\n";//转义
         	mString += "--Runtime.getRuntime().freeMemory()--->>"+ Runtime.getRuntime().freeMemory()/1024+"\\n";//转义
        }catch (Exception e) {
 			e.printStackTrace();
 		} 
    	
    	myLog.first("zzzz", "--CrashHandler--postWeb--mString-->>"+mString);
    	
    	try {
    		//--YuYingDataHelper.sendYuYingData(mContext,YuYingDataHelper.WrongToWeb,mString);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private String getRunningActivityName(){
    	if(mContext==null){
    		return "";
    	}
        ActivityManager activityManager=(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;                 
	}

}
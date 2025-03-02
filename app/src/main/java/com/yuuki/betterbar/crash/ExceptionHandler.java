package com.yuuki.betterbar.crash;
import android.content.*;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler
{
	private ExceptionHandler(){}
	Context context;
	private static ExceptionHandler inthis;
	public static ExceptionHandler getInstance(Context context){
		if(inthis == null) inthis = new ExceptionHandler();
		inthis.context = context;
		return inthis;
	}
	@Override
	public void uncaughtException(Thread p1, Throwable p2)
	{
		// TODO: Implement this method
		try{
			p2.printStackTrace();
			Intent intent =new Intent(context,BugActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("bug",p2);
			context.startActivity(intent);
			
		}catch(Throwable e){
			
		}finally{
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
	public void init(){
		if(inthis == null) return;
		Thread.setDefaultUncaughtExceptionHandler(inthis);
	}
}

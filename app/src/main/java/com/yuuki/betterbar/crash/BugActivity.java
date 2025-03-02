package com.yuuki.betterbar.crash;
import android.app.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import android.text.method.ScrollingMovementMethod;

public class BugActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try{
			TextView tv = new TextView(this);
			setContentView(tv);
			Object obj = getIntent().getSerializableExtra("bug");
			Throwable error = (Throwable) obj;
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			error.printStackTrace(pw);
			tv.setText("请先尝试激活此模块再打开\n\n" + "Bug\n"+sw.toString());
			tv.setMovementMethod(ScrollingMovementMethod.getInstance());
			tv.setTextIsSelectable(true);
		}catch(Throwable e){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
}

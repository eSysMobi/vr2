package pkg.ns.regapp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoadingActivity extends Activity implements OnClickListener {
	private static Context context;
	private Button startBtn;
	private Button settingsBtn;
	private Button exitBtn;
	private NotificationManager notifyMgr;
	static Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try
		{
			initialize(); 
		}
		catch(Exception e)
		{
			Utils.ShowMessage(context.getString(R.string.errors_initialize));
		}
		try
		{
			stopService(new Intent(LoadingActivity.this, RegAppService.class));
		}
		catch(Exception e)
		{
			Utils.ShowMessage(e.getMessage());
		}
		activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);		
		startBtn = (Button)findViewById(R.id.start);
		settingsBtn = (Button)findViewById(R.id.settings);
		exitBtn = (Button)findViewById(R.id.exit);
		
		startBtn.setOnClickListener(this);
		settingsBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		
		notifyMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notifyMgr.cancel(Notify.NOTIFY_ID);
		try
		{
			VideoActivity.context.finish();
		}
		catch(Exception e)
		{
//			Utils.ShowMessage(e.getMessage());
		}
	}
	@Override
	public void onClick(View view) {
		switch(view.getId())
		{
			case R.id.start:
			{			
				startService(new Intent(LoadingActivity.this, RegAppService.class));
				startApp();
				break;
			}
			case R.id.settings:
			{
				startActivity(new Intent(context, SettingsActivity.class));
				break;
			}
			case R.id.exit:
			{
				notifyMgr.cancel(Notify.NOTIFY_ID);
				RegAppService.context.stopService(getIntent());
				this.finish();
				break;
			}
		}
	}		
	static Context GetContext()
	{
		return context;
	}
	private void startApp()
	{
		CharSequence tickerText = context.getString(R.string.notify_startapp);
		long when = System.currentTimeMillis();
		CharSequence contentTitle = context.getString(R.string.app_name);
		CharSequence contentText = context.getString(R.string.notify_text);
		Intent  notifyIntent = new Intent(this, LoadingActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);
		Notification notify = new Notification(R.drawable.regapp, tickerText, when);
		notify.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notifyMgr.notify(Notify.NOTIFY_ID, notify);
		this.finish();
	}	
	private void createDefaultSettings()
	{
		Notify.Settings.clear();
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_LANG, context.getString(R.string.param_lang_ru)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_AUTOSTART, context.getString(R.string.param_yes)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_TYPE, context.getString(R.string.param_type_foto)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_SOUND_ENABLE, context.getString(R.string.param_yes)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_TIME_FOTO, this.getString(R.string.param_default_time_foto)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_TIME_VIDEO, this.getString(R.string.param_default_time_video)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_START_TIME_WORK, this.getString(R.string.param_default_timework)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_QUALITY_FOTO, context.getString(R.string.param_quality_medium)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_QUALITY_VIDEO, context.getString(R.string.param_quality_medium)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_QUALITY_SOUND, context.getString(R.string.param_quality_medium)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_CATALOG, Notify.APP_NAME));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_CATALOG_SIZE, "5"));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_SAVE_STATE, context.getString(R.string.param_yes)));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_TIME_SAVE_STATE, "60"));
		Notify.Settings.add(new Pair<String, String>(Notify.TAG_SYNC, context.getString(R.string.param_yes)));		
		String content = Utils.CreateXml(Notify.TAG_ROOT_SETTINGS, Notify.Settings, Notify.ENCODING_UTF8);
		Utils.saveDataToFile(Notify.APP_NAME, Notify.FILE_NAME_SETTINGS, content);
	}	
	private void getParams(String path, String fileName)
	{
		String sdState = android.os.Environment.getExternalStorageState();
    	if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) 
    	{
    	    File sdDir = android.os.Environment.getExternalStorageDirectory();
    	    File filePath = new File(sdDir.getAbsolutePath() + "/" + path);
    	    File file = new File(filePath + "/" + fileName);
    	    Utils.GetParamsFromXmlFile(file);    	    
    	}
	}
	private boolean isFileExist(String path, String fileName)
	{
    	String sdState = android.os.Environment.getExternalStorageState();
    	if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) 
    	{
    	    File sdDir = android.os.Environment.getExternalStorageDirectory();
    	    File filePath = new File(sdDir.getAbsolutePath() + "/" + path);
    	    File file = new File(filePath + "/" + fileName);
    	    if (filePath.isDirectory() && file.exists())
    	    	return true; 
    	} 
		return false;
	}	
	private void initialize()
	{
		context = getApplicationContext();
		Notify.Settings = new ArrayList<Pair<String, String>>();
		
		if (!isFileExist(Notify.APP_NAME, Notify.FILE_NAME_SETTINGS))
			createDefaultSettings();
		else
			getParams(Notify.APP_NAME, Notify.FILE_NAME_SETTINGS);

	}
}

package pkg.ns.regapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class RegAppService extends Service{
	private final Handler handlerWait = new Handler() 
	{
		public void handleMessage(Message message)
		{
			try
			{
				Notify.TimerWork = new Timer();
				if (type.trim().equalsIgnoreCase(context.getString(R.string.param_type_video)))
					Notify.TimerWork.scheduleAtFixedRate(new WorkTask(), 0, 2000+timeVideo*1000);			
			}
			catch(Exception e)
			{
				Utils.ShowMessage(e.getMessage());
			}
			Notify.TimerWait.cancel();
		}
	};
	private final Handler handlerWork = new Handler()
	{
		public void handleMessage(Message message)
		{
			try
			{
				if (!Notify.flagNotFinishedActivity)
				{
					if (context.getString(R.string.param_type_video).trim().equalsIgnoreCase(type))
					{
						Notify.flagNotFinishedActivity = true; 
						LoadingActivity.GetContext().startActivity(Notify.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					}
				}
				else
				{
					VideoActivity.context.finish();
					Notify.TimerWait = new Timer();
					Notify.TimerWait.scheduleAtFixedRate(new WaitTask(), timeWork*1000, timeWork*1000);
					Notify.TimerWork.cancel();
					Notify.flagNotFinishedActivity = false;
				}
			}
			catch(Exception e)
			{
				Utils.ShowMessage(e.getMessage());
			}
		}
	};
	private int timeWork;
	private int timeVideo;
	private int timeFoto;
	private String type;
	static Context context;
	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	@Override
	public void onCreate()
	{
		try
		{
			Notify.flagNotFinishedActivity = false;
			context = this;
			init();
		}catch(Exception e)
		{
			
		}
		super.onCreate();
	}
	@Override
	public void onStart(Intent intent, int startid)
	{
		try
		{
			Notify.TimerWork = new Timer();
			if (type.trim().equalsIgnoreCase(this.getString(R.string.param_type_video)))
				Notify.TimerWork.scheduleAtFixedRate(new WorkTask(), 0, timeVideo*1000);			
		}
		catch(Exception e)
		{
			Utils.ShowMessage(e.getMessage());
		}
	}
	@Override
	public void onDestroy()
	{

	}
	private class WorkTask extends TimerTask
	{
		@Override
		public void run() {
			handlerWork.sendEmptyMessage(0);			
		}	
	}
	private class WaitTask extends TimerTask
	{
		@Override
		public void run() {
			handlerWait.sendEmptyMessage(0);			
		}
	}
	private void init()
	{
		try
		{
			timeFoto = Integer.parseInt(Utils.GetParam(Notify.TAG_TIME_FOTO));
		}
		catch(Exception e)
		{
			timeFoto = Integer.parseInt(LoadingActivity.GetContext().getString(R.string.param_default_time_foto));
			Utils.ShowMessage(e.getMessage());
		}
		try
		{
			timeVideo = Integer.parseInt(Utils.GetParam(Notify.TAG_TIME_VIDEO));
		}
		catch(Exception e)
		{
			timeVideo =  Integer.parseInt(this.getString(R.string.param_default_time_video));
			Utils.ShowMessage(e.getMessage());
		}
		try
		{
			timeWork = Integer.parseInt(Utils.GetParam(Notify.TAG_START_TIME_WORK));
		}
		catch(Exception e)
		{
			timeWork = Integer.parseInt(this.getString(R.string.param_default_timework));
			Utils.ShowMessage(e.getMessage());
		}
		try
		{
			type = Utils.GetParam(Notify.TAG_TYPE);
		}
		catch(Exception e)
		{
			type = this.getString(R.string.param_type_video);
			Utils.ShowMessage(e.getMessage());
		}
//		Utils.ShowMessage("foto " + timeFoto + "\n" + "foto " + timeVideo + "\n" + "foto " + timeWork + "\n");
	}
}


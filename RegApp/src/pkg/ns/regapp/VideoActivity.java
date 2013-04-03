package pkg.ns.regapp;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.CamcorderProfile;
import android.media.CameraProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class VideoActivity extends Activity implements OnClickListener, SurfaceHolder.Callback 
{ 
	MediaRecorder recorder; 
	SurfaceHolder holder; 
	boolean recording = false; 
	static Activity context;
	private int duration = 60*1000;
	private long maxSize = 50*1000000;
	private int quality = CameraProfile.QUALITY_LOW;	
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		init();
		super.onCreate(savedInstanceState);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		recorder = new MediaRecorder();
		initRecorder(); 
		setContentView(R.layout.activity_video); 
		final SurfaceView cameraView = (SurfaceView) findViewById(R.id.videoView); 
		holder = cameraView.getHolder(); 
		holder.addCallback(this); 
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
//		cameraView.setClickable(true); 
//		cameraView.setOnClickListener(this);
		//

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run()
			{
				
				try
				{
//					setDir();
				}
				catch(Exception e)
				{
					
				}
				try
				{
					onClick(cameraView);
				}
				catch(Exception e)
				{
					
				}
			}
		}, 750);
	}
	private void initRecorder() 
	{ 
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT); 
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT); 
		CamcorderProfile cpHigh = CamcorderProfile .get(quality); 
		recorder.setProfile(cpHigh); 
		File sdDir = android.os.Environment.getExternalStorageDirectory();
	    File filePath = new File(sdDir.getAbsolutePath() + "/" + Notify.APP_NAME);
		recorder.setOutputFile(filePath.getAbsolutePath()+ "/" + Calendar.DAY_OF_MONTH + Calendar.MONTH + Calendar.YEAR  + Time.HOUR + Time.MINUTE + Time.SECOND + ".mp4");  
		recorder.setMaxDuration(duration); 
		recorder.setMaxFileSize(maxSize);
	} 
	private void prepareRecorder() { 
		recorder.setPreviewDisplay(holder.getSurface()); 
		try 
		{ 
			recorder.prepare(); 
		} 
		catch (IllegalStateException e) 
		{ 
			e.printStackTrace(); 
			finish(); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
			finish(); 
		} 
	} 
	public void onClick(View v) { 
		if (recording) 
		{ 
			recorder.stop(); 
			recording = false; 
			initRecorder(); 
			prepareRecorder(); 
		} 
		else 
		{ 
			recording = true; 
			recorder.start(); 
		} 
	} 
	public void surfaceCreated(SurfaceHolder holder) 
	{ 
		prepareRecorder(); 
	} 
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{ } 
	public void surfaceDestroyed(SurfaceHolder holder) { 
		if (recording) 
		{ 
			recorder.stop(); 
			recording = false; 
		} 
		recorder.release(); 
		finish(); 
	} 
	public void onStop()
	{
		super.onStop();
		Notify.flagNotFinishedActivity = false;
	}
	static void StopVideo()
	{
		try
		{
			if (context != null)
				context.finish();
		}
		catch(Exception e)
		{
			Utils.ShowMessage(e.getMessage());
		}
	}
	private void init()
	{
		try
		{
			duration = 1000*Integer.parseInt(Utils.GetParam(Notify.TAG_TIME_VIDEO));
//			Utils.ShowMessage("duration " + duration);
		}
		catch(Exception e)
		{
			duration = 60*1000;
		}
		try
		{
			maxSize = 1000000*Integer.parseInt(Utils.GetParam(Notify.TAG_CATALOG_SIZE));
//			Utils.ShowMessage("maxSize " + maxSize);
		}
		catch(Exception e)
		{
			maxSize = 50*1000000;
		}
		try
		{
			String qual = Utils.GetParam(Notify.TAG_TIME_VIDEO);
			if (qual.trim().equalsIgnoreCase(this.getString(R.string.param_quality_low)))
				quality = CamcorderProfile.QUALITY_LOW;
			else if (qual.trim().equalsIgnoreCase(this.getString(R.string.param_quality_medium)))
				quality = CamcorderProfile.QUALITY_720P;
			else if (qual.trim().equalsIgnoreCase(this.getString(R.string.param_quality_hight)))
				quality = CamcorderProfile.QUALITY_HIGH;
//			Utils.ShowMessage("duration " + quality);
		}
		catch(Exception e)
		{
			quality = CamcorderProfile.QUALITY_LOW;
		}
	}
	private void setDir()
	{
		File sdDir = android.os.Environment.getExternalStorageDirectory();
	    File filePath = new File(sdDir.getAbsolutePath() + "/" + Notify.APP_NAME);
		recorder.setOutputFile(filePath.getAbsolutePath()+ "/" + Calendar.DAY_OF_MONTH + Calendar.MONTH + Calendar.YEAR  + Time.HOUR + Time.MINUTE + Time.SECOND + ".mp4");
		Utils.ShowMessage(Calendar.DAY_OF_MONTH + Calendar.MONTH + Calendar.YEAR  + Time.HOUR + Time.MINUTE + Time.SECOND + ".mp4");
	}
} 

package pkg.ns.regapp;
import java.util.List;
import java.util.Timer;

import android.content.Intent;
import android.util.Pair;

public class Notify {
	final static String STRING_EMPTY = "";
	
	final static int NOTIFY_ID = 101;
	
	final static String APP_NAME = "RegApp";	
	final static String FILE_NAME_SETTINGS = "settings.xml";
	
	final static String TAG_ROOT_SETTINGS = "settings";
	final static String TAG_LANG = "lang";
	final static String TAG_AUTOSTART = "autoStart";
	final static String TAG_TYPE = "type";
	final static String TAG_SOUND_ENABLE = "soundEnable";
	final static String TAG_TIME_FOTO = "timeFoto";
	final static String TAG_TIME_VIDEO = "timeVideo";
	final static String TAG_START_TIME_WORK = "startTimeWork";
	final static String TAG_QUALITY_FOTO = "qualityFoto";
	final static String TAG_QUALITY_VIDEO = "qualityVideo";
	final static String TAG_QUALITY_SOUND = "qualitySound";
	final static String TAG_CATALOG = "catalog";
	final static String TAG_CATALOG_SIZE = "catalogSize";
	final static String TAG_SAVE_STATE = "saveState";
	final static String TAG_TIME_SAVE_STATE = "timeSaveState";
	final static String TAG_SYNC = "Sync";
		
	final static String PARAM_DEFAULT_CATALOG = "/storage/";
	final static String ENCODING_UTF8 = "utf-8";
	
	static List<Pair<String,String>> Settings;
	static boolean flagNotFinishedActivity = false;
	static Intent intent = new Intent(LoadingActivity.GetContext(), VideoActivity.class);
	static Timer TimerWait = new Timer();
	static Timer TimerWork = new Timer();
}

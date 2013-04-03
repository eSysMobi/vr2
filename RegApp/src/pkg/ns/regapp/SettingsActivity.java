package pkg.ns.regapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity implements OnClickListener, OnTouchListener{
	private final int IDD_AUTOSTART = 102;
	private final int IDD_TYPE = 103;
	private final int IDD_SOUND_ENABLE = 104;
	private final int IDD_QUALITY_FOTO = 105;
	private final int IDD_QUALITY_VIDEO = 106;
	private final int IDD_QUALITY_SOUND = 107;
	private final int IDD_SAVE_STATE = 108;
	private final int IDD_SYNC = 109;
	
	private EditText autostart;
	private EditText type;
	private EditText soundEnable;
	private EditText timeFoto;
	private EditText timeVideo;
	private EditText timeWork;
	private EditText qualityFoto;
	private EditText qualityVideo;
	private EditText qualitySound;
	private EditText catalog;
	private EditText catalogSize;
	private EditText saveState;
	private EditText timeSaveState;
	private EditText sync;
	private EditText selectedView;
	private Button save;
	
	private String[] items = {LoadingActivity.GetContext().getString(R.string.param_yes), LoadingActivity.GetContext().getString(R.string.param_no)};
	private String[] items_quality = {LoadingActivity.GetContext().getString(R.string.param_quality_hight), LoadingActivity.GetContext().getString(R.string.param_quality_medium),
			LoadingActivity.GetContext().getString(R.string.param_quality_low)};
	private String[] items_type = {LoadingActivity.GetContext().getString(R.string.param_type_foto), LoadingActivity.GetContext().getString(R.string.param_type_video)};
	private List<Pair<Integer, String>> viewToTag = new ArrayList<Pair<Integer, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		initialize();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		autostart = (EditText)findViewById(R.id.autostart);
		autostart.setOnClickListener(this);
		autostart.setOnTouchListener(this);
		autostart.setInputType(InputType.TYPE_NULL);
		
		type = (EditText)findViewById(R.id.type);
		type.setOnClickListener(this);
		type.setOnTouchListener(this);
		type.setInputType(InputType.TYPE_NULL);
		
		soundEnable = (EditText)findViewById(R.id.sound_enable);
		soundEnable.setOnClickListener(this);
		soundEnable.setOnTouchListener(this);
		soundEnable.setInputType(InputType.TYPE_NULL);
		
		timeFoto = (EditText)findViewById(R.id.time_foto);
		timeFoto.setOnClickListener(this);
		timeFoto.setOnTouchListener(this);
		
		
		timeVideo = (EditText)findViewById(R.id.time_video);
		timeVideo.setOnClickListener(this);
		timeVideo.setOnTouchListener(this);
		
		timeWork = (EditText)findViewById(R.id.time_work);
		timeWork.setOnClickListener(this);
		timeWork.setOnTouchListener(this);
		
		qualityFoto = (EditText)findViewById(R.id.quality_foto);
		qualityFoto.setOnClickListener(this);
		qualityFoto.setOnTouchListener(this);
		qualityFoto.setInputType(InputType.TYPE_NULL);
		
		qualityVideo = (EditText)findViewById(R.id.quality_video);
		qualityVideo.setOnClickListener(this);
		qualityVideo.setOnTouchListener(this);
		qualityVideo.setInputType(InputType.TYPE_NULL);
		
		qualitySound = (EditText)findViewById(R.id.quality_sound);
		qualitySound.setOnClickListener(this);
		qualitySound.setOnTouchListener(this);
		qualitySound.setInputType(InputType.TYPE_NULL);
		
		catalog = (EditText)findViewById(R.id.catalog);
		catalog.setOnClickListener(this);
		catalog.setOnTouchListener(this);
//		catalog.setInputType(InputType.TYPE_NULL);
		
		catalogSize = (EditText)findViewById(R.id.catalog_size);
		catalogSize.setOnClickListener(this);
		catalogSize.setOnTouchListener(this);
		
		saveState = (EditText)findViewById(R.id.save_state);
		saveState.setOnClickListener(this);
		saveState.setOnTouchListener(this);
		saveState.setInputType(InputType.TYPE_NULL);
		
		timeSaveState = (EditText)findViewById(R.id.time_save_state);
		timeSaveState.setOnClickListener(this);
		timeSaveState.setOnTouchListener(this);
		
		sync = (EditText)findViewById(R.id.sync);
		sync.setOnClickListener(this);
		sync.setOnTouchListener(this);
		sync.setInputType(InputType.TYPE_NULL);
		
		save = (Button)findViewById(R.id.save);
		save.setOnClickListener(this);
		try
		{
			readSettings();
		}
		catch(Exception e)
		{
			Utils.ShowMessage(this.getString(R.string.errors_settings_read));
		}
	}
	@Override
	public void onClick(View view) {
		hideKeyboard(view);
		if (view.getId() == R.id.save)
		{
			try
			{
				saveSettings();
				this.finish();
			}
			catch(Exception e)
			{
				Utils.ShowMessage(this.getString(R.string.errors_settings_save));
			}
		}
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		selectedView = (EditText) view;
		hideKeyboard(view);
		switch(view.getId())
		{
			case R.id.autostart:
			{
				showDialog(IDD_AUTOSTART);
				break;
			}
			case R.id.sound_enable:
			{
				showDialog(IDD_SOUND_ENABLE);
				break;
			}
			case R.id.save_state:
			{
				showDialog(IDD_SAVE_STATE);
				break;
			}
			case R.id.sync:
			{
				showDialog(IDD_SYNC);
				break;
			}	
			case R.id.type:
			{
				showDialog(IDD_TYPE);
				break;
			}
			case R.id.quality_foto:
			{
				showDialog(IDD_QUALITY_FOTO);
				break;
			}
			case R.id.quality_video:
			{
				showDialog(IDD_QUALITY_VIDEO);
				break;
			}
			case R.id.quality_sound:
			{
				showDialog(IDD_QUALITY_SOUND);
				break;
			}
		}
		return false;
	}
	protected Dialog onCreateDialog(int id)
	{
		if (id == IDD_AUTOSTART ||
				id == IDD_SOUND_ENABLE ||
				id == IDD_SAVE_STATE ||
				id == IDD_SYNC)
		{
			return getDialog(selectedView, items);
		}
		
		if (id == IDD_QUALITY_FOTO ||
				id == IDD_QUALITY_VIDEO ||
				id == IDD_QUALITY_SOUND)
		{
			return getDialog(selectedView, items_quality);
		}
		if (id == IDD_TYPE)
		{
			return getDialog(selectedView, items_type);
		}
		return null;
	}
	private Dialog getDialog(final View view, final String[] items)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getString(R.string.dialog_list_title));
		builder.setItems(items, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				((EditText)view).setText(items[item]);
			}
		});
		builder.setCancelable(false);
		return builder.create();
	}
	private void hideKeyboard(View view)
	{
		if (view.getId() == R.id.autostart ||
				view.getId() == R.id.type ||
				view.getId() == R.id.sound_enable ||
				view.getId() == R.id.quality_foto ||
				view.getId() == R.id.quality_video ||
				view.getId() == R.id.quality_sound ||
				view.getId() == R.id.catalog ||
				view.getId() == R.id.sync)
		{
			InputMethodManager imm = (InputMethodManager) 
					this.getSystemService(this.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	private void initialize()
	{
		viewToTag.add(new Pair<Integer, String>(R.id.autostart, Notify.TAG_AUTOSTART));
		viewToTag.add(new Pair<Integer, String>(R.id.type, Notify.TAG_TYPE));
		viewToTag.add(new Pair<Integer, String>(R.id.sound_enable, Notify.TAG_SOUND_ENABLE));
		viewToTag.add(new Pair<Integer, String>(R.id.time_foto, Notify.TAG_TIME_FOTO));
		viewToTag.add(new Pair<Integer, String>(R.id.time_video, Notify.TAG_TIME_VIDEO));
		viewToTag.add(new Pair<Integer, String>(R.id.time_work, Notify.TAG_START_TIME_WORK));
		viewToTag.add(new Pair<Integer, String>(R.id.quality_foto, Notify.TAG_QUALITY_FOTO));
		viewToTag.add(new Pair<Integer, String>(R.id.quality_video, Notify.TAG_QUALITY_VIDEO));
		viewToTag.add(new Pair<Integer, String>(R.id.quality_sound, Notify.TAG_QUALITY_SOUND));
		viewToTag.add(new Pair<Integer, String>(R.id.catalog, Notify.TAG_CATALOG));
		viewToTag.add(new Pair<Integer, String>(R.id.catalog_size, Notify.TAG_CATALOG_SIZE));
		viewToTag.add(new Pair<Integer, String>(R.id.save_state, Notify.TAG_SAVE_STATE));
		viewToTag.add(new Pair<Integer, String>(R.id.time_save_state, Notify.TAG_TIME_SAVE_STATE));
		viewToTag.add(new Pair<Integer, String>(R.id.sync, Notify.TAG_SYNC));
	}
	private void saveSettings()
	{
		Notify.Settings.clear();
		for(int i = 0; i < viewToTag.size(); ++i)
		{
			EditText editView = (EditText)findViewById(viewToTag.get(i).first);
			Notify.Settings.add(new Pair<String,String>(viewToTag.get(i).second, editView.getText().toString()));
		}
		String content = Utils.CreateXml(Notify.TAG_ROOT_SETTINGS, Notify.Settings, Notify.ENCODING_UTF8);
		Utils.saveDataToFile(Notify.APP_NAME, Notify.FILE_NAME_SETTINGS, content);
		Utils.ShowMessage(this.getString(R.string.notify_success_save));
	}
	private void readSettings()
	{
		if (Notify.Settings.size() == 0)
			return;
		for(int i = 0; i < Notify.Settings.size(); ++i)
		{
			EditText viewEdit = getViewFromTag(Notify.Settings.get(i).first);
			if (viewEdit != null)
				viewEdit.setText(Notify.Settings.get(i).second);
		}
	}
	private EditText getViewFromTag(String tagName)
	{
		for (int i = 0; i < viewToTag.size(); ++i)
		{
			if (tagName.trim().equalsIgnoreCase(viewToTag.get(i).second))
				return (EditText)findViewById(viewToTag.get(i).first);
		}
		return null;
	}
}

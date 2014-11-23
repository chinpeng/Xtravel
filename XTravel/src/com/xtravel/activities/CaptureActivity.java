package com.xtravel.activities;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import scu.xtravel.utils.ScanRecord;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xtravel.app.R;
import com.zxing.camera.CameraManager;
import com.zxing.decode.CaptureActivityHandler;
import com.zxing.decode.InactivityTimer;
import com.zxing.view.ViewfinderView;



public class CaptureActivity extends XActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;    //是否蜂鸣
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;    //震动
	private Button cancelScanButton,CBackButton,CHistoryButton;

	private Button openlight_bt;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v("capture","oncreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture);
		
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		//设置广告
		
		//adctr = new AdController();
		//adctr.initAd(this);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Log.v("capture","onresume");
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		//vibrate = true;
		
		vibrate = false;		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		final String resultString = result.getText();
		//FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
			
			ScanRecord scanRecord = new ScanRecord();
			scanRecord.title =resultString;
			scanRecord.time=getTime();
			scanRecord.urlString=resultString;
			Toast.makeText(this, "获取的url为"+resultString, Toast.LENGTH_LONG).show();
//			dbManager.insertScanRecord(scanRecord);
			
			// 2013-12-21修改，二维码规则变化
			//if(resultString.indexOf("zytxKind")!=-1)   //如果是智眼天下公司的二维码
			
//			if (resultString.indexOf("ZYT") != -1) 
//			{
//				Intent intent = new Intent();
//				intent.setClass(CaptureActivity.this, OperationActivity.class);
//				intent.putExtra("containsAlarm", true);
//				intent.putExtra("urlString", resultString);
//				startActivity(intent);
//				
//				/*AlertDialog.Builder builder = new AlertDialog.Builder(CaptureActivity.this);
//				builder.setTitle("请选择操作");
//				builder.setItems(new String[]{"电梯信息","维保信息","紧急报警"}, new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent();
//						switch (which) {
//						case 0:
//							intent.setClass(CaptureActivity.this, LiftInfoActivity.class);
//							intent.putExtra("urlString", resultString);
//							startActivity(intent);
//							break;
//						case 1:
//							intent.setClass(CaptureActivity.this, MaintainInfoActivity.class);
//							intent.putExtra("urlString", resultString);
//							startActivity(intent);
//							break;
//						case 2:  //报警
//							AlertDialog.Builder alarmDialog = new AlertDialog.Builder(CaptureActivity.this);
//							alarmDialog.setTitle("提示");
//							alarmDialog.setMessage("您确定要进行紧急报警?");
//							alarmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									AlarmRecord alarmRecord = new AlarmRecord();
//									alarmRecord.time =getTime();
//									alarmRecord.urlString = resultString;
//									dbManager.insertAlarmRecord(alarmRecord);
//									dialog.dismiss();
//									
//									Toast.makeText(CaptureActivity.this,"报警成功", Toast.LENGTH_SHORT).show();
//									
//									if(handler!=null)
//										handler.restartPreviewAndDecode();
//								}
//							});
//							
//							alarmDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									dialog.dismiss();
//									if(handler!=null)
//										handler.restartPreviewAndDecode();
//								}
//							});
//							alarmDialog.create().show();
//							break;
//						default:
//							break;
//						}
//						//dialog.dismiss();
//						
//					}
//				});
//				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						if(handler!=null)
//							handler.restartPreviewAndDecode();
//					}
//				});
//		
//				builder.setOnCancelListener(new OnCancelListener() {
//					
//					@Override
//					public void onCancel(DialogInterface dialog) {
//						// TODO Auto-generated method stub
//						 if(handler!=null)
//				            handler.restartPreviewAndDecode();
//					}
//				});
//				builder.create().show();*/
//			}
//			else {
//				Intent intent = new Intent();
//				intent.setClass(CaptureActivity.this, ScanResultActivity.class);
//				intent.putExtra("result", resultString);
//				intent.putExtra("scanResultEnable", true);
//				startActivity(intent);
//			}
		}
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			//CameraManager.get().openLight();   //开闪光灯
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	
	private String getTime()
	{
		SimpleDateFormat formatter =new SimpleDateFormat("MM-dd HH:mm");       
		Date curDate =new Date(System.currentTimeMillis());//获取当前时间        
		String str=formatter.format(curDate);
		return str;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
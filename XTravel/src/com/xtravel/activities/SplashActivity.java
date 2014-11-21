package com.xtravel.activities;

import scu.xtravel.utils.LocUtils;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xtravel.app.R;

/**
 * 欢迎页
 * @author YouMingyang
 * 
 */
public class SplashActivity extends XActivity {

	// private final String TAG = "SplashActivity";
	private final int DEALY_MILLIS = 2000;
	private LocUtils locUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		locUtils = new LocUtils(this);
		locUtils.start();//获取所在城市和经纬度
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				SplashActivity.this.finish();
			}
		}, DEALY_MILLIS);
	}

	@Override
	protected void onDestroy() {
		locUtils.stop();
		super.onDestroy();
	}

}

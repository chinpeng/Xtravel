package com.xtravel.activities;

import com.xtravel.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Activity基类
 */
public abstract class XActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	

	public void setActivityTitle(String title) {
		((TextView) findViewById(R.id.activity_title)).setText(title);
	}

	public void onViewClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.activity_back:
			finish();
			break;
		}
	}
}

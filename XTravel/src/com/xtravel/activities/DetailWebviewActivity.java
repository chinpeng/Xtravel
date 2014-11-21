package com.xtravel.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.xtravel.app.R;

public class DetailWebviewActivity extends Activity{
	WebView detailInfo = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_webview);
		detailInfo = (WebView)findViewById(R.id.detail_webview);
		Intent intent = getIntent();
		String url = intent.getStringExtra("detail_info");
		detailInfo.loadUrl(url);
	}
	

}

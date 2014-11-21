package com.xtravel.activities;

import scu.xtravel.utils.LocUtils;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xtravel.app.R;

/**
 * 预订项目详情
 * 
 * @author hfh
 * 
 */
@SuppressLint("InlinedApi")
public class BussinessMapActivity extends XActivity {
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moreinfo);
		setActivityTitle("商家地图");
		initView();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public void initView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDisplayZoomControls(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setBlockNetworkImage(false);
		mWebView.getSettings().setBlockNetworkLoads(false);
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "map");
		String url = "file:///android_asset/Map.html";
		mWebView.loadUrl(url);
	}

	class JavaScriptInterface {
		@JavascriptInterface
		public String getCurrentPosition() {
			String cPoX = "" + LocUtils.lon;
			String cPoY = "" + LocUtils.lat;
			return "{poX:\"" + cPoX + "\",poY:\"" + cPoY + "\"}";
		}
		@JavascriptInterface
		public String getPosition() {
			String poX = getIntent().getStringExtra("lon");
			String poY = getIntent().getStringExtra("lat");
			String address = getIntent().getStringExtra("address");
			return "{poX:\"" + poX + "\",poY:\"" + poY + "\",address:\"" + address + "\"}";
		}
	}

}

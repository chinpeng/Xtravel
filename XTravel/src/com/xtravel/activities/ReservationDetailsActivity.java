package com.xtravel.activities;

import java.util.Arrays;
import java.util.Map;

import scu.xtravel.utils.ApiTool;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
public class ReservationDetailsActivity extends XActivity {
	private final String TAG = "MoreInfoActivity";
	private final String apiUrl = "http://api.dianping.com/v1/deal/get_single_deal";
	private WebView mWebView;
	private String loadResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moreinfo);
		setActivityTitle("预订详情");
		init();
	}

	public void init() {
		initView();
		String dealId = getIntent().getStringExtra("dealId");
		new AysncLoadContent().execute(apiUrl, "deal_id", dealId);
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
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "moreinfo");

		String url = "file:///android_asset/reservation/bookhotel/wait.html";
		mWebView.loadUrl(url);
	}

	/**
	 * 从大众点评服务器获取数据
	 */
	class AysncLoadContent extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String apiUrl = params[0];
			Map<String, String> paramMap = ApiTool.genParams(Arrays.copyOfRange(params, 1, params.length));
			return ApiTool.requestApi(apiUrl, paramMap);
		}

		@Override
		protected void onPostExecute(String result) {
			if (TextUtils.isEmpty(result)) {
				Log.e(TAG, getString(R.string.result_null));
			} else {
				Log.v(TAG, result);
				loadResult = result;
				String url = "file:///android_asset/reservation/bookhotel/HotelInfo.html";
				mWebView.loadUrl(url);
			}
		}

	}

	class JavaScriptInterface {
		@JavascriptInterface
		public String getTuanGouInfo() {
			return loadResult;
		}

		// 跳转到商户详情
		@JavascriptInterface
		public void toBussinessDeatils(String businessId) {
			Intent intent = new Intent(ReservationDetailsActivity.this, BussinessDetailsActivity.class);
			System.out.println(businessId);
			intent.putExtra("businessId", businessId);
			startActivity(intent);
		}
	}

}

package com.xtravel.activities;

import java.util.Arrays;
import java.util.Map;

import scu.xtravel.utils.ApiTool;
import android.annotation.SuppressLint;
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
 * 商户详情
 * 
 * @author hfh
 * 
 */
public class BussinessCommentActivity extends XActivity {
	private final String TAG = "BussinessDetailsActivity";
	private final String apiUrl = "http://api.dianping.com/v1/review/get_recent_reviews";
	private WebView mWebView;
	private String loadResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moreinfo);
		setActivityTitle("评论详情");
		init();
	}

	public void init() {
		initView();
		String businessId = getIntent().getStringExtra("bussinessId");
		new AysncLoadContent().execute(apiUrl, "business_id", businessId);
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
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "comment");
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
				String url = "file:///android_asset/reservation/remment/Remment.html";
				mWebView.loadUrl(url);
			}
		}

	}

	class JavaScriptInterface {
		@JavascriptInterface
		public String getCommentInfo() {
			return loadResult;
		}
	}

	

}

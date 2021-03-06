package com.xtravel.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xtravel.app.R;

public class tab_two extends Fragment{
	private WebView mWebView ;
	private MyInterface mInterface;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.tab2_fragment_layout, container,false);
		mWebView  = (WebView)fragmentView.findViewById(R.id.webview2);
		return fragmentView;
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
    	if(android.os.Build.VERSION.SDK_INT > 9){
    		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		StrictMode.setThreadPolicy(policy);
    	}
    	mWebView.getSettings().setJavaScriptEnabled(true);
    	mWebView.getSettings().setDisplayZoomControls(true);
    	mWebView.getSettings().setDomStorageEnabled(true);
    	mWebView.getSettings().setBlockNetworkImage(false);
    	mWebView.getSettings().setBlockNetworkLoads(false);
    	mWebView.setWebChromeClient(new WebChromeClient());
    	mWebView.setWebViewClient(new WebViewClient());
    	mWebView.loadUrl("http://192.168.1.199:8080/test/mobile/web/remment/Remment.jsp");
    	mInterface = new MyInterface();
    	mWebView.addJavascriptInterface(mInterface, "interface");
    	super.onActivityCreated(savedInstanceState);
	}
	
	class MyInterface{
		/*@JavascriptInterface
		public String getPosition(){
			return "{poX:\"104.074148\",poY:\"30.661321\",address:\"�ɶ��滨�ֳ�������B��512\"}";
		}*/
	}
	
}

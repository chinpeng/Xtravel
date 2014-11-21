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

public class tab_one extends Fragment {
	private String url;
	private WebView mWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.tab1_fragment_layout, container, false);
		url = getArguments().getString("url");
		mWebView = (WebView) fragmentView.findViewById(R.id.webview);
		return fragmentView;
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
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
		mWebView.loadUrl(url);
		super.onActivityCreated(savedInstanceState);
	}

}

package com.xtravel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtravel.app.R;

public class RecommendFragment extends XFragment {

	private static class RecommendFragmentSingletonHolder {
		static RecommendFragment instance = new RecommendFragment();
	}

	public static RecommendFragment getInstance() {
		return RecommendFragmentSingletonHolder.instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recommend, null, false);
		return view;
	}

}

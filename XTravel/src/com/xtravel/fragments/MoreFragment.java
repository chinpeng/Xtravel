package com.xtravel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtravel.app.R;

public class MoreFragment extends XFragment {

	private static class MoreFragmentSingletonHolder {
		static MoreFragment instance = new MoreFragment();
	}

	public static MoreFragment getInstance() {
		return MoreFragmentSingletonHolder.instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, null, false);
		return view;
	}

}

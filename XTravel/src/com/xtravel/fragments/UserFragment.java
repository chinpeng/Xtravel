package com.xtravel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtravel.app.R;

public class UserFragment extends XFragment {

	private static UserFragment instance;

	public static UserFragment getInstance() {
		if (instance == null) {
			instance = new UserFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, null, false);
		return view;
	}

}

package com.xtravel.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xtravel.activities.QuickBookingActivity;
import com.xtravel.activities.RoutePlanDemo;
import com.xtravel.activities.ScenicTourActivity;
import com.xtravel.adapters.HomeFragmentListItemAdapter;
import com.xtravel.adapters.XPagerAdapter;
import com.xtravel.app.R;
import com.xtravel.views.LoopViewPager;

public class HomeFragment extends XFragment implements OnClickListener {

	private static class HomeFragmentSingletonHolder {
		static HomeFragment instance = new HomeFragment();
	}

	public static HomeFragment getInstance() {
		return HomeFragmentSingletonHolder.instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null, false);
		context = getActivity().getApplicationContext();
		initFragmentList(view);
		return view;
	}

	/**
	 * 初始化fragment header
	 * 
	 * @param view
	 *            headerView
	 */
	public View initFragmentHeader() {
		View headerView = LayoutInflater.from(context).inflate(R.layout.fragment_home_header, null, false);
		headerView.findViewById(R.id.quick_booking).setOnClickListener(this);
		headerView.findViewById(R.id.nearby_service).setOnClickListener(this);
		headerView.findViewById(R.id.scenic_tour).setOnClickListener(this);
		LoopViewPager pager = (LoopViewPager) headerView.findViewById(R.id.pager);
		pager.setAdapter(new XPagerAdapter(context));
		return headerView;
	}

	public void initFragmentList(View view) {
		ListView lists = (ListView) view.findViewById(R.id.home_lists);
		lists.addHeaderView(initFragmentHeader());
		lists.setAdapter(new HomeFragmentListItemAdapter(context));
	}

	private void startActivity(Class<?> cls) {
		getActivity().startActivity(new Intent(getActivity(), cls));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.quick_booking:
			startActivity(QuickBookingActivity.class);
			break;
		case R.id.nearby_service:
			startActivity(RoutePlanDemo.class);
			break;
		case R.id.scenic_tour:
			startActivity(ScenicTourActivity.class);
			break;
		}
	}
}

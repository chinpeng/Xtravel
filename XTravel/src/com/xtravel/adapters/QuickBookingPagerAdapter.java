package com.xtravel.adapters;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xtravel.fragments.ReservationFragment;
import com.xtravel.indicator.SlidingTabIndicator.SlidingTabItem;
import com.xtravel.indicator.SlidingTabIndicator.SlidingTabProvider;
/**
 * 快捷预订页(QuickBookingActivity)
 * @author YouMingyang
 *
 */
@SuppressLint("UseValueOf")
public class QuickBookingPagerAdapter extends FragmentStatePagerAdapter implements SlidingTabProvider {

	private String[] tabs = null;
	private String[] categories = null;
	private HashMap<String, Fragment> frgMap = null;

	public QuickBookingPagerAdapter(FragmentManager fm, String[] tabs, String[] categories) {
		super(fm);
		this.tabs = tabs;
		this.categories = categories;
		this.frgMap = new HashMap<String, Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
		return getFrament(position);
	}

	public Fragment getFrament(int position) {
		if (frgMap.containsKey(categories[position])) {
			return frgMap.get(categories[position]);
		} else {
			Fragment fragment = new ReservationFragment();
			addArgs(fragment, categories[position]);
			frgMap.put(categories[position], fragment);
			return fragment;
		}
	}

	private void addArgs(Fragment fragment, String category) {
		Bundle args = new Bundle();
		args.putCharSequence("category", category);
		fragment.setArguments(args);
	}

	@Override
	public int getCount() {
		return tabs.length;
	}

	@Override
	public SlidingTabItem getTabResId(int position) {
		return new SlidingTabItem(0, tabs[position]);
	}

}

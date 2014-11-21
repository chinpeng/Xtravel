package com.xtravel.activities;

import scu.xtravel.utils.LocUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xtravel.adapters.MainViewPagerAdapter;
import com.xtravel.app.R;
import com.xtravel.indicator.SlidingTabIndicator;
import com.xtravel.others.XPageTransformer;

/**
 * App 主界面
 * 
 * @author YouMingyang
 * 
 */
public class MainActivity extends FragmentActivity {

	public static final int SEL_LOC = 1;
	private SlidingTabIndicator mainTabs;
	private ViewPager pager;
	private TextView currentCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		setCurrentCity();
	}

	public void initView() {
		pager = (ViewPager) findViewById(R.id.pagers);
		pager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), getApplicationContext().getResources().getStringArray(R.array.main_tabs)));
		pager.setPageTransformer(true, new XPageTransformer());
		mainTabs = (SlidingTabIndicator) findViewById(R.id.main_tabs);
		mainTabs.setDrawIndicatorLine(false);
		mainTabs.setPager(pager);
	}

	public void setCurrentCity() {
		currentCity = (TextView) findViewById(R.id.current_city);
		currentCity.setText(LocUtils.currentCity);
	}

	public void onViewClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.current_city:
			startActivityForResult(new Intent(MainActivity.this, SelLocActivity.class), SEL_LOC);
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

}

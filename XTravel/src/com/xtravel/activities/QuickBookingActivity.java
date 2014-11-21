package com.xtravel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xtravel.adapters.QuickBookingPagerAdapter;
import com.xtravel.app.R;
import com.xtravel.indicator.SlidingTabIndicator;
import com.xtravel.others.XActionbar;
import com.xtravel.others.XPageTransformer;

/**
 * 快捷预订
 * 
 * @author YouMingyang
 * 
 */
public class QuickBookingActivity extends FragmentActivity implements XActionbar {
	private ViewPager bookingPager;
	private SlidingTabIndicator bookingTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_quick_booking);
		setActivityTitle("快捷预订");
		initView();
	}

	public void initView() {
		bookingTypes = (SlidingTabIndicator) findViewById(R.id.booking_types);
		bookingPager = (ViewPager) findViewById(R.id.booking_pager);
		bookingPager.setAdapter(new QuickBookingPagerAdapter(getSupportFragmentManager(), getApplicationContext().getResources().getStringArray(R.array.reservations), getApplicationContext()
				.getResources().getStringArray(R.array.categories)));
		bookingPager.setOffscreenPageLimit(2);
		bookingPager.setPageTransformer(true, new XPageTransformer());
		bookingTypes.setPager(bookingPager);
	}

	//设置标题栏
	public void setActivityTitle(String title) {
		((TextView) findViewById(R.id.activity_title)).setText(title);
	}

	public void onViewClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.activity_back:
			finish();
			break;
		}
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

}

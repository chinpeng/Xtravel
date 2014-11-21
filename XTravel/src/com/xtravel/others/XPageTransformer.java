package com.xtravel.others;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class XPageTransformer implements PageTransformer {

	final float MIN_ALPHA = 0.3f;

	@SuppressLint("NewApi")
	@Override
	public void transformPage(View view, float position) {
		if (position < -1) {
			view.setAlpha(0);
		} else if (position <= 1) {
			float alpha = 1 - Math.abs(position);
			alpha = alpha < MIN_ALPHA ? MIN_ALPHA : alpha;
			view.setAlpha(alpha);
		} else {
			view.setAlpha(0);
		}
	}

}

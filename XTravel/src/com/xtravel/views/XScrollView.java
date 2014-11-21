package com.xtravel.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class XScrollView extends ScrollView {

	private int iActionbarHeight;
	private ViewGroup actionbar;

	public XScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public XScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		actionbar = (ViewGroup) getChildAt(0);
		iActionbarHeight = actionbar.getLayoutParams().height;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(0, 0);
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
//		float scale = t * 1.0f / iActionbarHeight;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int iScrollY = this.getScrollY();
			if (iScrollY > iActionbarHeight / 2) {
				this.scrollTo(0, iActionbarHeight);
			} else {
				this.scrollTo(0, 0);
			}
			break;
		}
		return true;
	}

}

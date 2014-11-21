package com.xtravel.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtravel.app.R;

public class SlidingTabIndicator extends GridView implements android.widget.AdapterView.OnItemClickListener {

	/**
	 * Get resource id in position
	 */
	public interface SlidingTabProvider {
		public SlidingTabItem getTabResId(int position);
	}

	private ViewPager pager;
	private Paint tabPaint;
	private Paint divderPaint;
	private boolean drawIndicatorLine = true;

	private int tabCount;// number of pages
	private int currentPosition = 0;
	private int selectedPosition = 0;// select page index
	private float currentPositionOffset = 0f;

	private int tabColor = 0xFFFFFFFF;
	private int indicatorColor = 0xFF0099CC;
	private int divderlineColor = 0x2F000000;
	private int divderlineHeight = 1;
	private int indicatorHeight = 3;
	public OnPageChangeListener delegatePageListener;

	public SlidingTabIndicator(Context context) {
		super(context);
		init();
	}

	public SlidingTabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingTabIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		tabPaint = new Paint();
		tabPaint.setAntiAlias(true);
		tabPaint.setStyle(Style.FILL);
		divderPaint = new Paint();
		divderPaint.setAntiAlias(true);
		divderPaint.setStyle(Style.FILL);
	}

	public void setDrawIndicatorLine(boolean drawIndicatorLine) {
		this.drawIndicatorLine = drawIndicatorLine;
	}

	/**
	 * set reference ViewPager
	 * 
	 * @param pager
	 */
	public void setPager(ViewPager pager) {
		this.pager = pager;
		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		pager.setOnPageChangeListener(pageListener);
		this.tabCount = pager.getAdapter().getCount();
		this.setAdapter(adapter);
		this.setOnItemClickListener(this);
	}

	public void setDelegatePageListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	/**
	 * page change listener
	 */
	private OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			currentPosition = position;
			currentPositionOffset = positionOffset;

			adapter.notifyDataSetChanged();
			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
			}
			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			selectedPosition = position;

			adapter.notifyDataSetChanged();
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (drawIndicatorLine) {
			tabPaint.setColor(tabColor);
			final int height = getHeight();
			final int width = getWidth();
			canvas.drawRect(0, 0, width, height, tabPaint);
			divderPaint.setColor(divderlineColor);
			canvas.drawRect(0, height - divderlineHeight, width, height, divderPaint);
			// draw indicator line
			divderPaint.setColor(indicatorColor);

			// default: line below current tab
			View currentTab = getChildAt(currentPosition);
			float lineLeft = currentTab.getLeft();
			float lineRight = currentTab.getRight();

			// if there is an offset, start interpolating left and right
			// coordinates
			// between current and next tab
			if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
				View nextTab = getChildAt(currentPosition + 1);
				final float nextTabLeft = nextTab.getLeft();
				final float nextTabRight = nextTab.getRight();

				lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
				lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
			}
			canvas.drawRect(lineLeft + 30, height - indicatorHeight, lineRight - 30, height, divderPaint);
		}
	}

	public static class SlidingTabItem {
		public SlidingTabItem(int aImgId, String mTitle) {
			title = mTitle;
			imgId = aImgId;
		}

		String title;
		int imgId;
	}

	static class TabItemHolder {
		TextView tabTitle;
		ImageView tabIcon;
	}

	@SuppressLint("InflateParams") private BaseAdapter adapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return tabCount;
		}

		@Override
		public Object getItem(int position) {
			return ((SlidingTabProvider) pager.getAdapter()).getTabResId(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TabItemHolder holder = new TabItemHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.sliding_tab_indicator_item, null, false);
				holder.tabTitle = (TextView) convertView.findViewById(R.id.tab_title);
				holder.tabIcon = (ImageView) convertView.findViewById(R.id.tab_icon);
				convertView.setTag(holder);
			} else {
				holder = (TabItemHolder) convertView.getTag();
			}
			final SlidingTabItem item = (SlidingTabItem) getItem(position);
			holder.tabTitle.setText(item.title);
			final int tabIconId = item.imgId;
			if (tabIconId == 0) {
				holder.tabIcon.setVisibility(View.GONE);
				holder.tabTitle.setTextSize(18);
			} else {
				holder.tabTitle.setTextSize(15);
				holder.tabIcon.setVisibility(View.VISIBLE);
				holder.tabIcon.setBackgroundResource(tabIconId);
				if (position != selectedPosition) {
					holder.tabIcon.setSelected(false);
				} else {
					holder.tabIcon.setSelected(true);
				}
			}
			if (position != selectedPosition) {
				holder.tabTitle.setSelected(false);
			} else {
				holder.tabTitle.setSelected(true);
			}
			return convertView;
		}

	};

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int postion, long id) {
		selectedPosition = postion;
		currentPosition = postion;
		adapter.notifyDataSetChanged();
		pager.setCurrentItem(postion, false);
		pager.setAlpha(1.0f);
	}
}

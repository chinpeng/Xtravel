package com.xtravel.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xtravel.app.R;

public class XPagerAdapter extends PagerAdapter {

	private ImageLoader loader;
	private DisplayImageOptions options;
	private final int[] resID = { R.drawable.ban1, R.drawable.ban2, R.drawable.ban3 };
	private Context context;
	private int xCount;

	public XPagerAdapter(Context context) {
		this.context = context;
		xCount = resID.length;
		initImageLoader();
	}

	@Override
	public int getCount() {
		return xCount;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int xPosition = position % getCount();
		ImageView iView = new ImageView(context);
		iView.setScaleType(ScaleType.FIT_XY);
		loader.displayImage("drawable://" + resID[xPosition], iView, options);
		container.addView(iView);
		return iView;
	}

	public void initImageLoader() {
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false).considerExifParams(true).showImageOnLoading(R.drawable.ban1).showImageForEmptyUri(R.drawable.ban1)
				.showImageOnFail(R.drawable.ban2).bitmapConfig(Bitmap.Config.ARGB_8888).build();
	}
}

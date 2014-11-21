package com.xtravel.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xtravel.app.R;

public class HomeFragmentListItemAdapter extends XBaseAdapter {
	// private final String[] headers = { "精品推荐" };
	private List<HomeFragmentListItem> items;
	private ImageLoader loader;
	private DisplayImageOptions options;

	public HomeFragmentListItemAdapter(Context context) {
		super(context);
		initImageLoader();
	}

	public static class HomeFragmentListItem {
		int type;
		String sceneUri;
		String descStr;
	}

	static class ViewHolder {
		ViewStub header;
		ImageView scene;
		TextView desc;
	}

	@Override
	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.fragment_home_list_item, null, false);
			holder.scene = (ImageView) view.findViewById(R.id.scene);
			holder.desc = (TextView) view.findViewById(R.id.desc);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final HomeFragmentListItem item = (HomeFragmentListItem) getItem(position);
		if (position == 0 || (item.type != ((HomeFragmentListItem) getItem(position - 1)).type)) {
		}
		loader.displayImage(item.sceneUri, holder.scene, options);
		holder.desc.setText(item.descStr);
		return view;
	}

	public void initImageLoader() {
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false).considerExifParams(true).showImageOnLoading(R.drawable.ban1).showImageForEmptyUri(R.drawable.ban1)
				.showImageOnFail(R.drawable.ban2).bitmapConfig(Bitmap.Config.ARGB_8888).build();
	}

}

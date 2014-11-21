package com.xtravel.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtravel.app.R;

public class TabItemAdapter extends BaseAdapter {
	private Context context;
	private List<TabItem> tabs;

	public TabItemAdapter(Context context, List<TabItem> tabs) {
		this.context = context;
		this.tabs = tabs;
	}

	public static class TabItem {
		int iconResId;
		int titleResId;

		public TabItem(int iconResId, int titleResId) {
			super();
			this.iconResId = iconResId;
			this.titleResId = titleResId;
		}
	}

	@Override
	public int getCount() {
		if (tabs != null) {
			return tabs.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return tabs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TabItemHolder holder = new TabItemHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.tab_indicator_item, null, false);
			holder.tabIcon = (ImageView) convertView.findViewById(R.id.tab_icon);
			holder.tabTitle = (TextView) convertView.findViewById(R.id.tab_title);
			convertView.setTag(holder);
		} else {
			holder = (TabItemHolder) convertView.getTag();
		}
		final TabItem item = (TabItem) getItem(position);
		holder.tabIcon.setImageResource(item.iconResId);
		holder.tabTitle.setText(item.titleResId);
		return convertView;
	}

	static class TabItemHolder {
		ImageView tabIcon;
		TextView tabTitle;
	}

}

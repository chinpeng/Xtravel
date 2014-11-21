package com.xtravel.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xtravel.app.R;
import com.xtravel.entity.LocModel;
import com.xtravel.viewholder.SelLocItemViewHolder;

/**
 * 选择城市
 * 
 * @author YouMingyang
 * 
 */
@SuppressLint("DefaultLocale")
public class SelLocListAdapter extends BaseAdapter implements SectionIndexer {

	private Context context;
	private List<LocModel> cities;

	public SelLocListAdapter(Context context) {
		this.context = context;
	}

	public void setCities(List<LocModel> cities) {
		this.cities = cities;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (cities == null) {
			return 0;
		}
		return cities.size();
	}

	@Override
	public Object getItem(int position) {
		return cities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SelLocItemViewHolder holder = new SelLocItemViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sel_loc_item, null);
			holder.layLetter = convertView.findViewById(R.id.lay_letter);
			holder.letter = (TextView) convertView.findViewById(R.id.letter);
			holder.location = (TextView) convertView.findViewById(R.id.location);
			convertView.setTag(holder);
		} else {
			holder = (SelLocItemViewHolder) convertView.getTag();
		}
		final LocModel locModel = (LocModel) getItem(position);
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			holder.layLetter.setVisibility(View.VISIBLE);
			holder.letter.setText(locModel.sLetter);
		} else {
			holder.layLetter.setVisibility(View.GONE);
		}
		holder.location.setText(locModel.city);
		return convertView;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sLetter = ((LocModel) getItem(i)).sLetter;
			if (sLetter.toUpperCase().charAt(0) == section)
				return i;
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return ((LocModel) getItem(position)).sLetter.charAt(0);
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}

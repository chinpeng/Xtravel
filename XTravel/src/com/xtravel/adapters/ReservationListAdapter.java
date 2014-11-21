package com.xtravel.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xtravel.app.R;
import com.xtravel.entity.ReservationItem;
import com.xtravel.viewholder.ReservationItemViewHolder;

public class ReservationListAdapter extends BaseAdapter {

	// private final String TAG = "ReservationListAdapter";
	private Context context;
	private List<ReservationItem> bookingItems;

	private ImageLoader loader;
	private DisplayImageOptions options;

	public ReservationListAdapter(Context context) {
		this.context = context;
		initImageLoader();
	}

	public void setBookingItems(List<ReservationItem> bookingItems) {
		if (this.bookingItems == null) {
			this.bookingItems = bookingItems;
		} else {
			this.bookingItems.addAll(bookingItems);
		}
	}

	public List<ReservationItem> getBookingItems() {
		return bookingItems;
	}

	@Override
	public int getCount() {
		if (bookingItems != null) {
			return bookingItems.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return bookingItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReservationItemViewHolder holder = new ReservationItemViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, null);
			holder.sImage = (ImageView) convertView.findViewById(R.id.s_image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.description = (TextView) convertView.findViewById(R.id.description);
			holder.currentPrice = (TextView) convertView.findViewById(R.id.current_price);
			holder.listPrice = (TextView) convertView.findViewById(R.id.list_price);
			holder.distance = (TextView) convertView.findViewById(R.id.distance);
			convertView.setTag(holder);
		} else {
			holder = (ReservationItemViewHolder) convertView.getTag();
		}
		final ReservationItem reservationItem = (ReservationItem) getItem(position);
		loader.displayImage(reservationItem.sImageUrl, holder.sImage, options);
		holder.title.setText(reservationItem.title);
		holder.description.setText(reservationItem.description);
		SpannableString sp = new SpannableString("" + reservationItem.listPrice);
		sp.setSpan(new StrikethroughSpan(), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.listPrice.setText(sp);
		holder.currentPrice.setText("¥" + reservationItem.currentPrice);
		holder.distance.setText(reservationItem.distance + "米");
		return convertView;
	}

	public void initImageLoader() {
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false).considerExifParams(true).showImageOnLoading(R.drawable.ban1).showImageForEmptyUri(R.drawable.ban1)
				.showImageOnFail(R.drawable.ban2).bitmapConfig(Bitmap.Config.ARGB_8888).build();
	}

}

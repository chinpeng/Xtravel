package com.xtravel.fragments;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scu.xtravel.utils.ApiTool;
import scu.xtravel.utils.LocUtils;
import scu.xtravel.utils.XAppUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xtravel.activities.ReservationDetailsActivity;
import com.xtravel.adapters.ReservationListAdapter;
import com.xtravel.app.R;
import com.xtravel.entity.ReservationItem;

/**
 * 快捷预订(门票、酒店和美食预订通用)
 * 
 * @author YouMingyang
 * 
 */
public class ReservationFragment extends XFragment implements OnRefreshListener<ListView>, OnItemClickListener {

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		XAppUtils.setStrictMode();
		View view = inflater.inflate(R.layout.fragment_reservation, null, false);
		initView(view);
		return view;
	}

	private final String TAG = "ReservationFragment";
	private final String apiUrl = "http://api.dianping.com/v1/deal/find_deals";
	private final int pageSize = 10;
	private final int LOAD_CONTENT = 123;
	private final int CONTENT_LOADED = 124;
	protected String category;
	private PullToRefreshListView reservationList;
	private ReservationListAdapter adapter;
	private boolean isRefreshing;
	private int totalCount;
	private View emptyView;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "InflateParams" })
	public void initView(View view) {
		context = getActivity().getApplicationContext();
		category = getArguments().getString("category");
		reservationList = (PullToRefreshListView) view.findViewById(R.id.reservation_list);
		reservationList.getLoadingLayoutProxy().setPullLabel(getString(R.string.app_name));
		reservationList.getLoadingLayoutProxy().setRefreshingLabel(getString(R.string.app_name));
		emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.custome_empty_view, null);
		adapter = new ReservationListAdapter(getActivity().getApplicationContext());
		ListView ticketsList = reservationList.getRefreshableView();
		ticketsList.setEmptyView(emptyView);
		ticketsList.setAdapter(adapter);
		ticketsList.setOnItemClickListener(this);
		reservationList.setMode(Mode.PULL_UP_TO_REFRESH);
		reservationList.setOnRefreshListener(this);
	}

	/**
	 * 加载提示
	 * 
	 * @param tips
	 */
	public void setEmptyViewTips(String tips) {
		final TextView loadingTips = (TextView) emptyView.findViewById(R.id.loading_tips);
		final ProgressBar loading = (ProgressBar) emptyView.findViewById(R.id.loading);
		if (tips == null) {
			loading.setVisibility(View.VISIBLE);
			loadingTips.setText(R.string.loading);
		} else {
			loading.setVisibility(View.GONE);
			loadingTips.setText(tips);
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_CONTENT:
				if (XAppUtils.isNetworkConnect(context)) {
					if (isRefreshing) {
						Toast.makeText(context, "正在刷新中...", Toast.LENGTH_SHORT).show();
					} else {
						if (adapter.getCount() != 0 && adapter.getCount() == totalCount) {
							Toast.makeText(context, "已加载所有数据", Toast.LENGTH_SHORT).show();
							isRefreshing = false;
							reservationList.onRefreshComplete();
						} else {
							int pageNum = adapter.getCount() / pageSize + 1;
							isRefreshing = true;
							new AysncLoadContent().execute(apiUrl, "category", category, "city", LocUtils.currentCity, "latitude", "" + LocUtils.lat, "longitude", "" + LocUtils.lon, "radius", "5000",
									"sort", "7", "limit", "" + pageSize, "page", "" + pageNum);
						}
					}
				} else {
					setEmptyViewTips(getString(R.string.network_not_connected));
				}
			case CONTENT_LOADED:
				isRefreshing = false;
				reservationList.onRefreshComplete();
				break;
			}
		}

	};

	/**
	 * 从大众点评服务器获取数据
	 */
	class AysncLoadContent extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String apiUrl = params[0];
			Map<String, String> paramMap = ApiTool.genParams(Arrays.copyOfRange(params, 1, params.length));
			return ApiTool.requestApi(apiUrl, paramMap);
		}

		@Override
		protected void onPostExecute(String result) {
			if (TextUtils.isEmpty(result)) {
				Log.e(TAG, getString(R.string.result_null));
			} else {
				Log.v(TAG, result);
				List<ReservationItem> bookingItems = parseResult(result);
				adapter.setBookingItems(bookingItems);
				adapter.notifyDataSetChanged();
			}
			handler.sendEmptyMessage(CONTENT_LOADED);
		}

	}

	/**
	 * 解析服务器返回的json数据
	 * 
	 * @param resultStr
	 * @return
	 */
	public List<ReservationItem> parseResult(String resultStr) {
		List<ReservationItem> bookingItems = new LinkedList<ReservationItem>();
		JSONObject resultJSONObject;
		try {
			resultJSONObject = new JSONObject(resultStr);
			final String status = resultJSONObject.getString("status");
			Log.d(TAG, "status:" + status);
			if ("OK".equals(status)) {
				totalCount = resultJSONObject.getInt("total_count");
				JSONArray results = resultJSONObject.getJSONArray("deals");
				for (int i = 0; i < results.length(); i++) {
					JSONObject deal = results.optJSONObject(i);
					String dealId = deal.getString("deal_id");
					String title = deal.getString("title");
					String description = deal.getString("description");
					double listPrice = deal.getDouble("list_price");
					double currentPrice = deal.getDouble("current_price");
					int distance = deal.getInt("distance");
					String sImageUrl = deal.getString("s_image_url");
					String dealH5Url = deal.getString("deal_h5_url");
					bookingItems.add(new ReservationItem(dealId, sImageUrl, title, description, listPrice, currentPrice, distance, dealH5Url));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bookingItems;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
				| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		if (refreshView.isHeadherShow()) {
		} else {
			handler.sendEmptyMessage(LOAD_CONTENT);
		}
	}

	@Override
	public void onResume() {
		handler.sendEmptyMessage(LOAD_CONTENT);
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String dealId = adapter.getBookingItems().get(position - 1).dealId;
		Intent intent = new Intent(getActivity(), ReservationDetailsActivity.class);
		intent.putExtra("dealId", dealId);
		startActivity(intent);
	}

}

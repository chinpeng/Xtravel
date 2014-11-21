package com.xtravel.activities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import scu.xtravel.utils.PinyinUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.xtravel.adapters.SelLocListAdapter;
import com.xtravel.app.R;
import com.xtravel.entity.LocModel;
import com.xtravel.others.PinyinComparator;
import com.xtravel.views.SideBar;
import com.xtravel.views.SideBar.OnTouchingLetterChangedListener;

/**
 * 选择所在地点
 * 
 * @author YouMingyang
 * 
 */
public class SelLocActivity extends XActivity implements OnTouchingLetterChangedListener {
	// private final String TAG = "SelLocActivity";
	private ListView locListView;
	private SelLocListAdapter adapter;
	private SideBar sideBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sel_loc);
		setActivityTitle("选择所在城市");
		initView();
	}

	private void initView() {
		locListView = (ListView) findViewById(R.id.loc_list);
		adapter = new SelLocListAdapter(getApplicationContext());
		View emptyView = LayoutInflater.from(this).inflate(R.layout.custome_empty_view, null);
		locListView.setEmptyView(emptyView);
		new SortLocTask().execute(getResources().getStringArray(R.array.cities));
		sideBar = (SideBar) findViewById(R.id.sidebar);
		sideBar.setOnTouchingLetterChangedListener(this);
	}

	public void onViewClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.activity_back:
			finish();
			break;
		}
	}

	private List<LocModel> genData(String[] cities) {
		List<LocModel> locModels = new LinkedList<LocModel>();
		for (String city : cities) {
			String sLetter = PinyinUtils.getPingYin(city).substring(0, 1).toUpperCase(Locale.CHINESE);
			if (sLetter.matches("[A-Z]")) {
				locModels.add(new LocModel(city, sLetter));
			} else {
				locModels.add(new LocModel(city, "#"));
			}
		}
		return locModels;
	}

	/**
	 * 对城市名称按首字母排序
	 */
	class SortLocTask extends AsyncTask<String[], Void, List<LocModel>> {

		@Override
		protected List<LocModel> doInBackground(String[]... params) {
			List<LocModel> locModels = genData(params[0]);
			PinyinComparator comparator = new PinyinComparator();
			Collections.sort(locModels, comparator);
			return locModels;
		}

		@Override
		protected void onPostExecute(List<LocModel> result) {
			adapter.setCities(result);
			locListView.setAdapter(adapter);
		}

	}

	@Override
	public void onTouchingLetterChanged(String s) {
		// 该字母首次出现的位置
		int position = adapter.getPositionForSection(s.charAt(0));
		if (position != -1) {
			locListView.setSelection(position);
		}
	}
}

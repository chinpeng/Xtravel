package com.xtravel.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xtravel.app.R;
import com.xtravel.fragments.HomeFragment;
import com.xtravel.fragments.MoreFragment;
import com.xtravel.fragments.RecommendFragment;
import com.xtravel.fragments.UserFragment;
import com.xtravel.indicator.SlidingTabIndicator.SlidingTabItem;
import com.xtravel.indicator.SlidingTabIndicator.SlidingTabProvider;

/**
 * 显示主页面的四个fragment
 * @author YouMingyang
 * 
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter implements SlidingTabProvider {

	public final int HOMEPAGE = 0;// 主页
	public final int RECOMMEND = 1;// 推荐
	public final int USER = 2;// 用户
	public final int MORE = 3;// 更多
	private final int[] tabImgs = { R.drawable.home_homepage_bg, R.drawable.home_recommend_bg, R.drawable.home_user_bg, R.drawable.home_user_bg };
	private String[] tabTitles;

	public MainViewPagerAdapter(FragmentManager fm, String[] tabTitles) {
		super(fm);
		this.tabTitles = tabTitles;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case HOMEPAGE:
			fragment = HomeFragment.getInstance();
			break;
		case RECOMMEND:
			fragment = RecommendFragment.getInstance();
			break;
		case USER:
			fragment = UserFragment.getInstance();
			break;
		case MORE:
			fragment = MoreFragment.getInstance();
			break;
		default:
			fragment = HomeFragment.getInstance();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return tabImgs.length;
	}

	@Override
	public SlidingTabItem getTabResId(int position) {
		return new SlidingTabItem(tabImgs[position], tabTitles[position]);
	}

}

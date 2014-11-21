package com.xtravel.applications;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class XApplication extends Application {
	private static XApplication instance;
	private List<Activity> activities;
	public LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public static LatLng currentLocation = null;
	public String currentCity = null;

	@Override
	public void onCreate() {
		initImageLoader(getApplicationContext());
		SDKInitializer.initialize(getApplicationContext()); // 百度地图初始化引擎
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
//		mLocClient.start();
		super.onCreate();
	}

	public static XApplication getInstance() {
		if (instance == null) {
			instance = new XApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activities == null) {
			activities = new LinkedList<Activity>();
		}
		activities.add(activity);
	}

	/**
	 * 退出软件
	 */
	public void exit() {
		for (Activity activity : activities) {
			activity.finish();
		}
		System.exit(0);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			// MyLocationData locData = new MyLocationData.Builder()
			// .accuracy(location.getRadius())
			// // 此处设置开发者获取到的方向信息，顺时针0-360
			// .direction(100).latitude(location.getLatitude())
			// .longitude(location.getLongitude()).build();
			currentCity = location.getCity();
			currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
			Log.d("location", location.getLatitude() + "#####" + location.getLongitude() + "");

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/*
	 * @param context 初始化图片加载器
	 */
	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "Xtravel/cache");
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCache(new UnlimitedDiscCache(cacheDir)).defaultDisplayImageOptions(displayImageOptions).diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

}

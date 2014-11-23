package scu.xtravel.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.xtravel.applications.XApplication;

/**
 * 获取地理位置(百度的api)
 * 
 * @author YouMingyang
 * 
 */
public class LocUtils {

	private final String TAG = "LocUtils";
	private Context context = null;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = null;
	public static String currentCity = "成都";
	public static double lat=30.6747240000;
	public static double lon=104.1197550000;

	public LocUtils(Context context) {
		this.context = context;
		init();
	}

	public void init() {
		mLocationClient = new LocationClient(context); // 声明LocationClient类
		myListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setPriority(LocationClientOption.GpsFirst); // gps
		option.setPoiNumber(10);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			Log.v(TAG, location.getCity() + ":" + location.getLatitude() + ":" + location.getLongitude());
			//大众点评的城市参数不包含'市'
			if(location.getCity()==null)
				currentCity="成都";
			else {
				currentCity = location.getCity().contains("市") ? location.getCity().substring(0, location.getCity().length() - 1) : location.getCity();
			}
			
			lat = location.getLatitude();
			lon = location.getLongitude();
			XApplication.currentLocation=new LatLng(lat, lon);
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}

	public void start() {
		mLocationClient.start();
	}

	public void stop() {
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}
}

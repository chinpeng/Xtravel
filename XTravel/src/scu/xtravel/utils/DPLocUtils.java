package scu.xtravel.utils;

import android.app.Activity;
import android.util.Log;

import com.dianping.locate.utils.DPLocation;
import com.dianping.locate.utils.DPLocationClient;
import com.dianping.locate.utils.DPLocationClientOption;
import com.dianping.locate.utils.DPLocationClientOption.Offset_type;
import com.dianping.locate.utils.DPLocationListener;
/**
 * 获取地理位置(大众点评的api)
 * 
 * @author YouMingyang
 * 
 */
public class DPLocUtils {
	private final String TAG = "DPLocUtils";
	private DPLocationClient locClient = null;
	public static String currentCity = "成都";
	public static double lat=30.6747240000;
	public static double lon=104.1197550000;

	public DPLocUtils(Activity activity) {
		// 初始化DPLocationClient类,注册listener用于接受定位结果
		locClient = new DPLocationClient(activity);
		MyLocationListener listener = new MyLocationListener();
		locClient.registerLocationListener(listener);
	}

	public void start() {
		if (locClient != null) {
			// 设置定位参数
			DPLocationClientOption clientOption = new DPLocationClientOption();
			clientOption.setOffset_type(Offset_type.GAODE);
			locClient.setClientOption(clientOption);
			// 发起定位请求
			locClient.requestLocation();
		} else {
			Log.d(TAG, "locClient is null or not started");
		}
	}

	public void stop() {
		locClient.cancleLocation();
	}

	// 实现DPLocationListener接口，注册到locClient，用于接受返回结果
	class MyLocationListener implements DPLocationListener {
		@Override
		public void onReceiveLocation(DPLocation location) {
			if (location == null)
				return;
			Log.v(TAG,location.getCity() + ":" + location.getLatitude() + ":" + location.getLongitude());
			currentCity =location.getCity();
			lat = location.getLatitude();
			lon = location.getLongitude();
		}
	}
}

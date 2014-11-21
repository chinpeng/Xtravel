package scu.xtravel.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class XAppUtils {

	public static HashMap<String, String> imgUriMap = new HashMap<String, String>();

	/**
	 * google maps的脚本里代码
	 */
	private static double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	private static String getFormatDistance(double d) {
		String distance = null;
		int iDistance = (int) d;
		if (iDistance < 10) {
			distance = "附近";
		} else if (iDistance < 1000) {
			distance = iDistance + "米以内";
		} else if (iDistance < 1000 * 1000) {
			distance = (iDistance / 1000) + "公里";
		} else {
			distance = "千里之外";
		}
		return distance;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 */
	public static String getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double d = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		d = d * EARTH_RADIUS;
		d = Math.round(d * 100000) / 100;
		return getFormatDistance(d);
	}

	public static String comm(String urlString, HashMap<String, String> params) {
		String result = "";
		String uploadUrl = "http://192.168.1.199:8080/test/" + "/";
		String MULTIPART_FORM_DATA = "multipart/form-data";
		String BOUNDARY = "---------7d4a6d158c9"; // 数据分隔线

		if (!urlString.equals("")) {
			uploadUrl = uploadUrl + urlString;
			HttpURLConnection conn = null;
			try {
				URL url = new URL(uploadUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);// 允许输入
				conn.setDoOutput(true);// 允许输出
				conn.setUseCaches(false);// 不使用Cache
				conn.setConnectTimeout(6000);// 6秒钟连接超时
				conn.setReadTimeout(25000);// 25秒钟读数据超时
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Charset", "UTF-8");
				conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);

				StringBuilder sb = new StringBuilder();

				// 在这里上传参数
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append("--");
					sb.append(BOUNDARY);
					sb.append("\r\n");
					sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
					sb.append(entry.getValue());
					sb.append("\r\n");
				}

				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.write(sb.toString().getBytes());
				dos.writeBytes("--" + BOUNDARY + "--\r\n");
				dos.flush();

				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);

				// 返回的结果 － String类型的JSON
				result = br.readLine();

			} catch (Exception e) {
				e.printStackTrace();
				result = "{network error！！}";
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		}
		return result;
	}

	/**
	 * 解析json中的图片连接
	 * 
	 * @param resultStr
	 * @return
	 */
	public static String parseImageUri(String resultStr) {
		String imageUri = null;
		try {
			JSONObject jsonObject = new JSONObject(resultStr);
			JSONObject messageObject = new JSONObject(jsonObject.optString("message"));
			JSONArray imagesObject = messageObject.getJSONArray("imageList");
			imageUri = imagesObject.optString(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return imageUri;
	}

	/**
	 * 判断是否连接网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			switch (networkInfo.getType()) {
			case ConnectivityManager.TYPE_MOBILE:
			case ConnectivityManager.TYPE_WIFI:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	/**
	 * 获取屏幕大小
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenSize(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}

	public static void setStrictMode() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

}

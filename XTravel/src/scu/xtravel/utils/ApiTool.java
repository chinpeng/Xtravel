package scu.xtravel.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.util.URIUtil;

public class ApiTool {

	private static final String TAG = "ApiTool";
	private static final String appKey = "96597424";
	private static final String secret = "90e88ed13ab74a739d28fceaa3e11832";

	public static Map<String, String> genParams(String... sParams) {
		Map<String, String> paramMap = new HashMap<String, String>();
		if (sParams.length % 2 == 0) {
			for (int i = 0; i < sParams.length; i += 2) {
				paramMap.put(sParams[i], sParams[i + 1]);
			}
		} else {
			Log.e(TAG, "传入的参数数目不匹配");
		}
		return paramMap;
	}

	public static String getQueryString(Map<String, String> paramMap) {
		String sign = sign(paramMap);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("appkey=").append(appKey).append("&sign=")
				.append(sign);
		for (Entry<String, String> entry : paramMap.entrySet()) {
			stringBuilder.append('&').append(entry.getKey()).append('=')
					.append(entry.getValue());
		}
		String queryString = stringBuilder.toString();
		return queryString;
	}

	public static String getUrlEncodedQueryString(Map<String, String> paramMap) {
		String sign = sign(paramMap);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("appkey=").append(appKey).append("&sign=")
				.append(sign);
		for (Entry<String, String> entry : paramMap.entrySet()) {
			try {
				stringBuilder.append('&').append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		String queryString = stringBuilder.toString();
		return queryString;
	}

	public static String requestApi(String apiUrl, Map<String, String> paramMap) {
		String queryString = getQueryString(paramMap);
		Log.v(TAG, queryString);
		StringBuffer response = new StringBuffer();
		HttpClientParams httpConnectionParams = new HttpClientParams();
		httpConnectionParams.setConnectionManagerTimeout(1000);
		HttpClient client = new HttpClient(httpConnectionParams);
		HttpMethod method = new GetMethod(apiUrl);
		try {
			if (queryString != null && !queryString.isEmpty()) {
				// Encode query string with UTF-8
				String encodeQuery = URIUtil.encodeQuery(queryString, "UTF-8");
				method.setQueryString(encodeQuery);
			}
			client.executeMethod(method);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					method.getResponseBodyAsStream(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.append(line).append(
						System.getProperty("line.separator"));
			}
			reader.close();
		} catch (URIException e) {
		} catch (IOException e) {
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings("deprecation")
	public static String sign(Map<String, String> paramMap) {
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(appKey);
		for (String key : keyArray) {
			stringBuilder.append(key).append(paramMap.get(key));
		}
		stringBuilder.append(secret);
		String codes = stringBuilder.toString();
		String sign = new String(Hex.encodeHex(DigestUtils.sha(codes)))
				.toUpperCase();
		return sign;
	}
}
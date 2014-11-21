package com.xtravel.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xtravel.app.R;
import com.xtravel.applications.XApplication;

/**
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class RoutePlanDemo extends Activity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
	// 浏览路线节点相关
	Button mBtnPre = null;// 上一个节点
	Button mBtnNext = null;// 下一个节点
	int nodeIndex = -1;// 节点索引,供浏览节点时使用
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null;// 泡泡view

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	LatLng startPoint;
	LatLng endPoint;
	PlanNode stNode;
	PlanNode enNode;

	String baseUrl = "http://api.map.baidu.com/place/v2/search?";
	String queryKeyword = "美食";
	String region = "成都";

	String url = "";
	Thread searchThread;
	HttpClient client;
	SearchThread mainThread;
	// List<Map<String, Object>> data = null;

	Button food = null, hotel = null, recommand = null, tour = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_routeplan);
		// data = new ArrayList<Map<String, Object>>();
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		food = (Button) findViewById(R.id.food);
		hotel = (Button) findViewById(R.id.hotel);
		recommand = (Button) findViewById(R.id.recommand);
		tour = (Button) findViewById(R.id.tour);

//		food.setBackgroundColor(0xf9f980);
		food.setTextColor(Color.RED);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);

		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		mBaidumap.setMapStatus(MapStatusUpdateFactory
				.newLatLng(XApplication.currentLocation));
		client = new DefaultHttpClient();

		mainThread = new SearchThread();
		mainThread.start();
		try {
			Thread.sleep(1000);
			mainThread.getHandlerInThread().sendEmptyMessage(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void myOnClick(View v) {
		food.setTextColor(Color.BLACK);
		hotel.setTextColor(Color.BLACK);
		recommand.setTextColor(Color.BLACK);
		tour.setTextColor(Color.BLACK);
		switch (v.getId()) {
		case R.id.food:
			food.setTextColor(Color.RED);
//			handler.sendEmptyMessage(1);
			mainThread.getHandlerInThread().sendEmptyMessage(1);
			break;
		case R.id.hotel:
			hotel.setTextColor(Color.RED);
//			handler.sendEmptyMessage(2);
			mainThread.getHandlerInThread().sendEmptyMessage(2);
			break;
		case R.id.recommand:
			recommand.setTextColor(Color.RED);
//			handler.sendEmptyMessage(3);
			mainThread.getHandlerInThread().sendEmptyMessage(3);
			break;
		case R.id.tour:
			tour.setTextColor(Color.RED);
//			handler.sendEmptyMessage(4);
			mainThread.getHandlerInThread().sendEmptyMessage(4);
			break;
		default:
			break;
		}

	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:

				final List<Map<String, Object>> data = (List<Map<String, Object>>) msg.obj;
				mBaidumap.clear();

				for (int i = 0; i < data.size(); i++) {

					BitmapDescriptor bitmap = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marka);
					OverlayOptions option = new MarkerOptions()
							.position(
									new LatLng(Double.parseDouble(data.get(i)
											.get("nearby_lat").toString()),
											Double.parseDouble(data.get(i)
													.get("nearby_lng")
													.toString()))).icon(bitmap)
							.title(data.get(i).get("nearby_name").toString())
							.zIndex(i);
					mBaidumap.addOverlay(option);

				}

				mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {

					public boolean onMarkerClick(Marker arg0) {
						// TODO Auto-generated method stub
						Log.d("marker", arg0.getTitle() + arg0.getZIndex());
						final int position = arg0.getZIndex();
						new AlertDialog.Builder(RoutePlanDemo.this)
								.setTitle(arg0.getTitle())
								.setMessage(
										data.get(position)
												.get("nearby_address")
												.toString())
								.setPositiveButton("到这去",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub
												mBaidumap.clear();
												endPoint = new LatLng(
														Double.parseDouble(data
																.get(position)
																.get("nearby_lat")
																.toString()),
														Double.parseDouble(data
																.get(position)
																.get("nearby_lng")
																.toString()));
												if (XApplication.currentLocation != null)
													startPoint = XApplication.currentLocation;

												stNode = PlanNode
														.withLocation(startPoint);
												enNode = PlanNode
														.withLocation(endPoint);

												mSearch.drivingSearch((new DrivingRoutePlanOption())
														// 默认选择驾车去
														.from(stNode)
														.to(enNode));

											}
										})
								.setNegativeButton("查看详情",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub
												Intent intent = new Intent(
														RoutePlanDemo.this,
														DetailWebviewActivity.class);
												intent.putExtra(
														"detail_info",
														data.get(position)
																.get("detail_url")
																.toString());
												RoutePlanDemo.this
														.startActivity(intent);

											}
										}).create().show();
						return false;
					}
				});
				break;
			 case 1:
			 Log.d("handler","case 1...");
			  food.setBackgroundColor(0xffffff);
			  hotel.setBackgroundColor(0xffffff);
			  recommand.setBackgroundColor(0xffffff);
			  tour.setBackgroundColor(0xffffff);
			  food.setBackgroundColor(0xff0000);
			
			 break;
			 case 2:
			 Log.d("handler","case 2...");
			  food.setBackgroundColor(0xffffff);
			  hotel.setBackgroundColor(0xffffff);
			  recommand.setBackgroundColor(0xffffff);
			  tour.setBackgroundColor(0xffffff);
			  hotel.setBackgroundColor(0xff0000);
			
			 break;
			 case 3:
			 Log.d("handler","case 3...");
			 
			  food.setBackgroundColor(0xffffff);
			  hotel.setBackgroundColor(0xffffff);
			  recommand.setBackgroundColor(0xffffff);
			  tour.setBackgroundColor(0xffffff);
			  recommand.setBackgroundColor(0xff0000);
			
			 break;
			 case 4:
			 Log.d("handler","case 4...");
			  food.setBackgroundColor(0xffffff);
			  hotel.setBackgroundColor(0xffffff);
			  recommand.setBackgroundColor(0xffffff);
			  tour.setBackgroundColor(0xffffff);
			  tour.setBackgroundColor(0xff0000);
			 
			 break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	/**
	 * 发起路线规划搜索示例
	 * 
	 * @param v
	 */
	public void SearchButtonProcess(View v) {
		// 重置浏览节点的路线数据

		// 设置起终点信息，对于tranist search 来说，城市名无意义
		// PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都",
		// editSt.getText().toString());
		// PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都",
		// editEn.getText().toString());
		if (enNode != null) {
			route = null;
			mBtnPre.setVisibility(View.INVISIBLE);
			mBtnNext.setVisibility(View.INVISIBLE);
			mBaidumap.clear();
			// 实际使用中请对起点终点城市进行正确的设定
			if (v.getId() == R.id.drive) {
				mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
						stNode).to(enNode));
			} else if (v.getId() == R.id.transit) {
				mSearch.transitSearch((new TransitRoutePlanOption())
						.from(stNode).city("成都").to(enNode));
			} else if (v.getId() == R.id.walk) {
				mSearch.walkingSearch((new WalkingRoutePlanOption()).from(
						stNode).to(enNode));
			}
		}
	}

	/**
	 * 节点浏览示例
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}
		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// 设置节点索引
		if (v.getId() == R.id.next) {
			if (nodeIndex < route.getAllStep().size() - 1) {
				nodeIndex++;
			} else {
				return;
			}
		} else if (v.getId() == R.id.pre) {
			if (nodeIndex > 0) {
				nodeIndex--;
			} else {
				return;
			}
		}
		// 获取节结果信息
		LatLng nodeLocation = null;
		String nodeTitle = null;
		Object step = route.getAllStep().get(nodeIndex);
		if (step instanceof DrivingRouteLine.DrivingStep) {
			nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
		} else if (step instanceof WalkingRouteLine.WalkingStep) {
			nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
		} else if (step instanceof TransitRouteLine.TransitStep) {
			nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
		}

		if (nodeLocation == null || nodeTitle == null) {
			return;
		}
		// 移动节点至中心
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(RoutePlanDemo.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, null));

	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	public void changeRouteIcon(View v) {
		if (routeOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			((Button) v).setText("自定义起终点图标");
			Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();

		} else {
			((Button) v).setText("系统起终点图标");
			Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();

		}
		useDefaultIcon = !useDefaultIcon;
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();

	}

	public class SearchThread extends Thread {

		Handler handlerInThread;
		List<Map<String, Object>> data;

		public SearchThread() {

			data = new ArrayList<Map<String, Object>>();

		}

		public Handler getHandlerInThread() {
			return handlerInThread;
		}

		public void setHandlerInThread(Handler handlerInThread) {
			this.handlerInThread = handlerInThread;
		}

		public List<Map<String, Object>> getData() {
			return data;
		}

		public void setData(List<Map<String, Object>> data) {
			this.data = data;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			Looper.prepare();
			handlerInThread = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					data = null;
					data = new ArrayList<Map<String, Object>>();
					// TODO Auto-generated method stub
					switch (msg.what) {
					case 1:
						queryKeyword = "美食";
						region = "成都";
						break;
					case 2:
						queryKeyword = "酒店";
						region = "成都";
						break;
					case 3:
						queryKeyword = "特色小吃";
						region = "成都";
						break;
					case 4:
						queryKeyword = "旅游景点";
						region = "成都";
						break;
					default:
						break;
					}
					try {
						queryKeyword = "q="
								+ URLEncoder.encode(queryKeyword, "UTF-8")
								+ "&";
						region = "region=" + URLEncoder.encode(region, "UTF-8")
								+ "&";

						url = baseUrl
								+ queryKeyword
								+ region
								+ "output=json&ak=lON8Wopew2GLqMIIobDu6pd1&scope=2&page_size=20&page_num=2";
						Log.d("url", url);
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					HttpGet get = new HttpGet(url);
					JSONObject json = null;
					try {
						HttpResponse res = client.execute(get);

						if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							HttpEntity entity = res.getEntity();
							String result = EntityUtils.toString(entity,
									HTTP.UTF_8);
							Log.d("result", result);
							json = new JSONObject(result);
							if (json.getInt("status") == 102) {
								Toast.makeText(RoutePlanDemo.this, "请求失败",
										Toast.LENGTH_SHORT).show();
							}
							JSONArray jsonArray = json.getJSONArray("results");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObj = jsonArray.getJSONObject(i);

								Map<String, Object> map = new HashMap<String, Object>();
								map.put("nearby_name", jsonObj.get("name"));
								map.put("nearby_address",
										jsonObj.get("address"));
								String location = jsonObj.get("location")
										.toString();
								JSONObject latAndLng = new JSONObject(location);

								double lat = latAndLng.getDouble("lat");
								double lng = latAndLng.getDouble("lng");
								LatLng current = XApplication.currentLocation;
								double distance = DistanceUtil.getDistance(
										new LatLng(lat, lng), current);
								int aboutDistance = (int) distance;
								map.put("nearby_distance", "" + aboutDistance
										+ " 米");

								map.put("nearby_lat", "" + lat);
								map.put("nearby_lng", "" + lng);

								String info = jsonObj.get("detail_info")
										.toString();
								JSONObject detailInfo = new JSONObject(info);

								map.put("detail_url",
										detailInfo.get("detail_url"));
								// map.put("nearby_price",
								// "" + "价格： " + detailInfo.get("price"));
								data.add(map);
							}
							Message mainMsg = handler.obtainMessage();
							mainMsg.what = 0;
							mainMsg.obj = data;
							handler.sendMessage(mainMsg);
							// handler.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						Log.d("result", "线程异常" + e.toString());
						e.printStackTrace();

					}
					super.handleMessage(msg);
				}

			};
			Looper.loop();

			super.run();
		}
	}

}

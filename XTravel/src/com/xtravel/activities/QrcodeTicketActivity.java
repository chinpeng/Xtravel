package com.xtravel.activities;

import java.util.Hashtable;

import scu.xtravel.utils.XAppUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xtravel.app.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class QrcodeTicketActivity extends XActivity {

	private String businessId;
//	private String ticketUrl;
	private TextView ticketDetailTextView;
	private ImageView qrcodeTicketImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode_ticket);
		setActivityTitle(getString(R.string.ticket_view_title));
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		initData();
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		ticketDetailTextView = (TextView) findViewById(R.id.ticket_detail);
		ticketDetailTextView.setText("测试\n");
		qrcodeTicketImageView = (ImageView) findViewById(R.id.qrcode_ticket_view);
		try {
			qrcodeTicketImageView.setImageBitmap(Create2DCode(businessId));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			Log.e("qrcode", "二维码生成失败");
			e.printStackTrace();
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (businessId == null)
			businessId = intent.getStringExtra("businessId");
		Log.d("businessId_ticket",businessId);
//		if (ticketUrl == null)
//			ticketUrl = intent.getStringExtra("ticketUrl");
		
	}

	public Bitmap Create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		int matrixSideLength = (int) (XAppUtils.getScreenSize(this).widthPixels*0.67);
				BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, matrixSideLength, matrixSideLength);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}
		Log.d("matrix_width", width+"");
		Log.d("matrix_height", height+"");
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}

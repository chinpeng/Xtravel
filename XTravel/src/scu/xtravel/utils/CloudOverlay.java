//package scu.xtravel.utils;
//
//import java.io.IOException;
//import java.util.List;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Toast;
//
//import com.baidu.mapapi.cloud.CustomPoiInfo;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.OverlayItem;
//import com.baidu.mapapi.map.PopupClickListener;
//import com.baidu.mapapi.map.PopupOverlay;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.xtravel.app.R;
//
//public class CloudOverlay extends ItemizedOverlay {
//
//    List<CustomPoiInfo> mLbsPoints;
//    Activity mContext;
//    Handler handler;
//    int time = 0;
//   // GeoPoint pt;
//    
//    public CloudOverlay(Activity context ,MapView mMapView,Handler handler) {
//        super(null,mMapView);
//        mContext = context;
//        this.handler = handler;
//    }
//
//    public void setData(List<CustomPoiInfo> lbsPoints) {
//        if (lbsPoints != null) {
//            mLbsPoints = lbsPoints;
//            
//            
//        }
//        for ( CustomPoiInfo rec : mLbsPoints ){
//            GeoPoint pt = new GeoPoint((int)(rec.latitude), (int)(rec.longitude));
//            OverlayItem item = new OverlayItem(pt , rec.name, rec.address);
//            Drawable marker1 = this.mContext.getResources().getDrawable(R.drawable.icon_marka);
//            item.setMarker(marker1);
//            addItem(item);
//        }
//    }
//    
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        // TODO Auto-generated method stub
//        return super.clone();
//    }
//    
//    @Override
//    protected boolean onTap(int arg0) {
////        CustomPoiInfo item = mLbsPoints.get(arg0);
////        Toast.makeText(mContext, item.address,Toast.LENGTH_LONG).show();
//    	 PopupOverlay pop = new PopupOverlay(mMapView,new PopupClickListener() {                
//		        @Override
//		        public void onClickedPopup(int index) {
//		                //在此处理pop点击事件，index为点击区域索引,点击区域最多可有三个
//		        	Message msg = handler.obtainMessage();
//		        	msg.what=8;
//		        	msg.obj = new GeoPoint((int)mLbsPoints.get(0).latitude, (int)mLbsPoints.get(0).longitude);
//		        	handler.sendMessage(msg);
//		        	Toast.makeText(mContext, "正在导航。。", Toast.LENGTH_SHORT).show();
//					
//					
//		        }
//		});
//    	 Bitmap[] bmps = new Bitmap[3];
//    	 Bitmap  bitmap = null;
//    	 try {
//			bitmap = drawTextAtBitmap(BitmapFactory.decodeStream(mContext.getAssets().open("marker2.png")),mLbsPoints.get(0).name);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//			try {
//			bmps[0] = BitmapFactory.decodeStream(mContext.getAssets().open("marker1.png"));
//			//bmps[1] = BitmapFactory.decodeStream(mContext.getAssets().open("marker2.png"));
//			bmps[1] =  bitmap;
//			bmps[2] = BitmapFactory.decodeStream(mContext.getAssets().open("marker3.png"));
//			} catch (IOException e) {
//			         e.printStackTrace();
//			}
//			
//		time++;
//		if(time%2==0){
//			pop.hidePop();
//		}
//		else
//     pop.showPopup(bmps, new GeoPoint((int)mLbsPoints.get(0).latitude, (int)mLbsPoints.get(0).longitude), 64);
//     return super.onTap(arg0);
//    }
//    
//    
//    
//    private Bitmap drawTextAtBitmap(Bitmap bitmap,String text){
//    	int x = bitmap.getWidth();
//    	int y = bitmap.getHeight();
//    	// 创建一个和原图同样大小的位图 
//    	
//        float scale = (float) ((text.length()*20-60)*1.0/y);
//    	
//    	Bitmap newbit = biggerBitmap(bitmap,scale);
//    	Canvas canvas = new Canvas(newbit);
//    	Paint paint = new Paint(); // 在原始位置0，0插入原图 
//    	canvas.drawBitmap(newbit, 0, 0, paint);
//    	paint.setColor(Color.parseColor("#ffffff"));
//    	paint.setTextSize(20); // 在原图指定位置写上字 
//    	canvas.drawText(text, 0 , 30, paint); 
//    	canvas.save(Canvas.ALL_SAVE_FLAG); // 存储
//    	canvas.restore(); 
//    	return newbit; 
//    	}
//    
//    private static Bitmap biggerBitmap(Bitmap bitmap,float scale ) {
//    	  Matrix matrix = new Matrix();
//    	  matrix.postScale(scale,1); //长和宽放大缩小的比例
//    	  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//    	  return resizeBmp;
//    	 }
//    
//    
//}

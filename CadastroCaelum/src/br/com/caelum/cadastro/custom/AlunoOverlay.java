package br.com.caelum.cadastro.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class AlunoOverlay extends Overlay {
	
	private final Address address;
	private final Bitmap bitmap;


	public AlunoOverlay(Address address, Bitmap bitmap){
		this.address = address;
		this.bitmap = bitmap;
	}
	
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();
		
		if(!shadow) {
			Double lat = address.getLatitude()*1E6;
			Double lng = address.getLongitude()*1E6;
			GeoPoint geoPoint = new GeoPoint(lat.intValue(), lng.intValue());
			
			Point myPoint = new Point();
			projection.toPixels(geoPoint, myPoint);
			
			Paint paint = new Paint();
			paint.setARGB(250, 255, 255, 255);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);

			canvas.drawBitmap(bitmap, myPoint.x, myPoint.y, paint);
		}
		
		super.draw(canvas, mapView, shadow);
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return false;
	}
}

package br.com.caelum.cadastro.tela.map;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.custom.AlunoOverlay;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.helper.AlunoConverter;
import br.com.caelum.cadastro.modelo.Aluno;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Mapa extends MapActivity {

	private MapController mapController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		mapView = (MapView) findViewById(R.mapa.map_view);

		mapController = mapView.getController();
		mapController.setZoom(17);

		mapView.setSatellite(true);// mostra imagem de satelite
//		mapView.setStreetView(true);// permite streetview se disponivel (geralmente bugado, evite)
		mapView.displayZoomControls(true);// controle de zoom
		mapView.setBuiltInZoomControls(true);//

		LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);

		String melhorProviderDisponivel = locMan.getBestProvider(
				criterioPadrao() //criterio para escolha do melhor provedor
				, true // pegar apenas os providers disponíveis
				);

		Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		atualizarMeuLocal(location);
		
		locMan.requestLocationUpdates(melhorProviderDisponivel, 2000, 20, listener);
		
		
		addAlunos();
	}

	@SuppressWarnings("unchecked")
	private void addAlunos() {
		AlunoDao dao = new AlunoDao(Mapa.this);
		new AsyncTask<Aluno, Integer, Map<Aluno, Address>>(){
			@Override
			protected Map<Aluno, Address> doInBackground(Aluno... alunos) {
				Geocoder geo = new Geocoder(Mapa.this, Locale.getDefault());
				Map<Aluno, Address> enderecos = new HashMap<Aluno, Address>();
				
				for(Aluno aluno: alunos) {
					try {
						List<Address> ends = geo.getFromLocationName(aluno.getEndereco(), 1);
						if(ends.size() > 0) {
							enderecos.put(aluno, ends.get(0));
						}
					}catch(IOException e) {
						throw new RuntimeException(e);
					}
				}
				return enderecos;
			}
			
			@Override
			protected void onPostExecute(Map<Aluno, Address> result) {
				for(Aluno aluno:result.keySet()) {
					Bitmap bitmap = BitmapFactory.decodeResource(Mapa.this.getResources(), R.drawable.icon);
					if(aluno.getFoto() != null) {
						try {
							FileInputStream fis = new FileInputStream(aluno.getFoto());
							bitmap = BitmapFactory.decodeStream(fis);
							fis.close();
							bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
						}catch(IOException e) {
							throw new RuntimeException(e);
						}
					}
					
					AlunoOverlay alunoOverlay = new AlunoOverlay(result.get(aluno), bitmap);
					mapView.getOverlays().add(alunoOverlay);
				}
				
				mapView.invalidate();
			}
		}.execute(dao.getAll().toArray(new Aluno[dao.getAll().size()]));
		dao.close();
		
		
		
		
//		AlunoDao dao = new AlunoDao(Mapa.this);
//		List<Aluno> listAlunos = dao.getAll();
//		dao.close();
//		Geocoder geo = new Geocoder(Mapa.this, Locale.getDefault());
//		
//		for(Aluno aluno:listAlunos) {
//			Address address = null;
//			try {
//				List<Address> enderecos = geo.getFromLocationName(aluno.getEndereco(), 1);
//				if(enderecos.size() > 0) {
//					address = enderecos.get(0);
//				}
//			}catch(IOException e) {
//				throw new RuntimeException(e);
//			}
//			
//			if(address != null) {
//				Bitmap bitmap = BitmapFactory.decodeResource(Mapa.this.getResources(), R.drawable.icon);
//				if(aluno.getFoto() != null) {
//					try {
//						FileInputStream fis = new FileInputStream(aluno.getFoto());
//						bitmap = BitmapFactory.decodeStream(fis);
//						fis.close();
//						bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
//					}catch(IOException e) {
//						throw new RuntimeException(e);
//					}
//				}
//				
//				AlunoOverlay alunoOverlay = new AlunoOverlay(address, bitmap);
//				mapView.getOverlays().add(alunoOverlay);
//			}
//		}
//		
//		mapView.invalidate();
		
	}

	private Criteria criterioPadrao() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		return criteria;
	}
	
	private LocationListener listener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		@Override
		public void onProviderEnabled(String provider) {}
		
		@Override
		public void onProviderDisabled(String provider) {
			atualizarMeuLocal(null);
		}
		
		@Override
		public void onLocationChanged(Location location) {
			atualizarMeuLocal(location);
		}
	};
	private MapView mapView;
	
	private void atualizarMeuLocal(Location location) {
		if(location != null) {
			Double geoLat = location.getLatitude()*1E6;
			Double geoLng = location.getLongitude()*1E6;
			GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
			
			mapController.animateTo(point);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}

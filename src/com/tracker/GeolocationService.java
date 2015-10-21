package com.tracker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GeolocationService extends Service implements LocationListener{

	private final Context ctx;
	
	double latitude;
	double longitude;
	Location location;
	boolean isGPSEnabled;
	boolean isNetworkEnabled;
	TextView texto;
	LocationManager locationManager;
	double dif;
	
	public GeolocationService(){
		super();
		this.ctx = this.getApplicationContext();
		//getLocation(c);
	}
	
	public GeolocationService(Context c){
		super();
		this.ctx = c;
		getLocation(c);
	}
	
	public void setView(View v){
		texto = (TextView)v;
		texto.setText("Coordenadas: " + latitude + "," + longitude);
	}
	
	public void getLocation(Context c){
			
		try{
			locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!isGPSEnabled && !isNetworkEnabled){
				Toast.makeText(c, "GPS and Network disabled",Toast.LENGTH_SHORT).show();
			} else {
				if (isNetworkEnabled) {
	                locationManager.requestLocationUpdates(
	                        LocationManager.NETWORK_PROVIDER,
	                        30, //MIN_TIME_BW_UPDATES,
	                        100, this); //MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                Log.d("Network", "Network Enabled");
	                if (locationManager != null) {
	                    location = locationManager
	                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                    if (location != null) {
	                        latitude = location.getLatitude();
	                        longitude = location.getLongitude();               			
                        } else {
	                    	Toast.makeText(getApplicationContext(), "Location Null", Toast.LENGTH_SHORT).show();;
	                    }
	                }
	            }
	            // if GPS Enabled get lat/long using GPS Services
	            if (isGPSEnabled) {
	                if (location == null) {
	                    locationManager.requestLocationUpdates(
	                            LocationManager.GPS_PROVIDER,
	                            30, //MIN_TIME_BW_UPDATES,
	                            100, this); //MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                    Log.d("GPS", "GPS Enabled");
	                    if (locationManager != null) {
	                        location = locationManager
	                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                        if (location != null) {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
                			
	                        } else {
		                    	Toast.makeText(getApplicationContext(), "Location Null", Toast.LENGTH_SHORT).show();;
		                    }
	                    } 
	                }
	            }
			}
			
		} catch(Exception e){
			Toast.makeText(getApplicationContext(), "No se pudo inicializar locationManager ",  Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getLatitude() {
		// TODO Auto-generated method stub
		return latitude;
	}

	public double getLongitude() {
		// TODO Auto-generated method stub
		return longitude;
	}
	
	public double getDifCoordinates(double PedidoLat, double PedidoLong, double UserLocLat, double UserLocLong){
		
		Location PedidoLoc = new Location("Pedido");
		Location UserLoc = new Location("User");
		
		PedidoLoc.setLatitude(PedidoLat);
		PedidoLoc.setLongitude(PedidoLong);
		
		UserLoc.setLatitude(UserLocLat);
		UserLoc.setLongitude(UserLocLong);
		
		dif = PedidoLoc.distanceTo(UserLoc);
		
		return dif;
	}
	
}

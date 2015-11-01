package com.tracker;

import java.util.ArrayList;

import com.tracker.R;

//import com.tracker.MapActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapShader;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MapActivity extends Activity{

	private LatLng defaultLatLng = new LatLng(-34.608359, -58.379995);
    private GoogleMap map;
    private int zoomLevel = 15;
	//private List<LatLng> ListLatLng = null;
    String serverGetData = "http://192.168.1.44:51229/api/getDeliveryPositions?"; // 192.168.30.52 cac
    ArrayList<String> listLatLng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		try {
							
	        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

	        if (map!=null){

	           map.getUiSettings().setCompassEnabled(true);

	           map.setTrafficEnabled(false);

	           map.setMyLocationEnabled(false);

	           // Move the camera instantly to defaultLatLng.

	           map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, zoomLevel));
	           
		        Bundle bundle = getIntent().getExtras();
		        ArrayList<String> lista = new ArrayList<String>();
		        lista = bundle.getStringArrayList("listLatLng");
		        
		        for (int i=0;i<lista.size();i++){
		        	String value = lista.get(i);
		        	Log.i("valor= ", value);
		        	String pedidoID = value.substring(0, value.indexOf(","));
		        	Log.i("pedidoID=", pedidoID);
		        	String latitude = value.substring(value.indexOf(",")+1, value.lastIndexOf(","));
		        	Log.i("latitude= ", latitude);
		        	String longitude = value.substring(value.lastIndexOf(",")+1);
		        	Log.i("longitude= ", longitude);
		        	
		        	Double lat = Double.valueOf(latitude);
			        Double lng = Double.valueOf(longitude);
			        
			        if (i==lista.size()-1){
				        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
			   					.title("PedidoID= " + pedidoID)
				                .snippet("Latitud: " + lat + ", Longitud: " + lng)
				                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			        } else {
				        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
			   					.title("PedidoID= " + pedidoID)
				                .snippet("Latitud: " + lat + ", Longitud: " + lng)
				                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			        }

		        }	      	 

	         }

	 

	     }catch (NullPointerException e) {

	         e.printStackTrace();

	     }

	}

	@Override

	   public void onPause() {

	        if (map != null){
	
	                        map.setMyLocationEnabled(false);
	
	                        map.setTrafficEnabled(false);
	
	        }
	
	        super.onPause();
	        
	   }

	 

	   public void onInfoWindowClick(Marker marker) {

            Intent intent = new Intent(this, MapActivity.class);

            intent.putExtra("snippet", marker.getSnippet());

            intent.putExtra("title", marker.getTitle());

            intent.putExtra("position", marker.getPosition());

            startActivity(intent);

	   }
	   
		@Override
		public void onBackPressed() {
		}
	 

	}

	
	

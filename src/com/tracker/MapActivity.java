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
		        
		        //String value = bundle.getString("valor");
		        /*Log.i("valor= ", value);
		        String latitude= value.substring(0, value.indexOf(","));
		        Log.i("latitude= ", latitude);
		        String longitude= value.substring(value.indexOf(",")+1);
		        Log.i("longitude= ", longitude);*/

		        /*Double lat = Double.valueOf(latitude);
		        Double lng = Double.valueOf(longitude);
		        LatLng latlng = new LatLng(lat, lng);
		        
		        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
	   					.title("PedidoID= ")
		                .snippet("Latitud: " + lat + ", Longitud: " + lng)
		                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		        */
		        //Toast.makeText(getApplicationContext(), "latitude= " + lat + ", longitude= " + lng, Toast.LENGTH_LONG).show();
		        
		        /*if (listLatLng.size() >0){
		        	for (int i=0; i<listLatLng.size(); i++){
		        		//LatLng latlng = new LatLng(Double.valueOf(listLatLng.get(i).latitude), Double.valueOf(listLatLng.get(i).longitude));
		        		LatLng latlng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
		   				map.addMarker(new MarkerOptions().position(new LatLng(latlng.latitude, latlng.longitude))
		   					.title("PedidoID= " + i)
			                .snippet("Latitud: " + Double.valueOf(latitude) + ", Longitud: " + Double.valueOf(longitude))
			                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		        		Toast.makeText(getApplicationContext(), "Lat= " + latlng.latitude + ", Lng= " + latlng.longitude, Toast.LENGTH_LONG).show();
		        	}
		        }*/
		        
		   		//String enviaDatos = serverGetData + "userID=" + 1; // userID
		   		//HttpGet getreq = new HttpGet(enviaDatos);
	           // fin
		   			           
	           /*try {

		   			client = new DefaultHttpClient(new BasicHttpParams());
		   			result = client.execute(getreq, handler);
		   			
		   			String data = (String) result;
		       		Gson g = new Gson();
		       		Type t = new TypeToken<EntregasGeoLocModel[]>(){}.getType();
		       		entregas = (EntregasGeoLocModel[])g.fromJson(data, t);
		       		
		   		} catch (ClientProtocolException e) {
		   			// TODO Auto-generated catch block
		   			e.printStackTrace();
		   			Log.e("Usuario= ", e.getMessage());
		   		} catch (IOException e) {
		   			// TODO Auto-generated catch block
		   			e.printStackTrace();
		   			Log.e("Usuario= ", e.getMessage());
		   		}
		   	    
		   		client.getConnectionManager().shutdown();
	           
		   		if (entregas.length > 0){
		   			//ListLatLng = new ArrayList<LatLng>();
		   			for (int i=0; i<entregas.length;i++){
		   				LatLng latlng = new LatLng(Double.valueOf(entregas[i].deliveryLatitude), Double.valueOf(entregas[i].deliveryLongitude));
		   				map.addMarker(new MarkerOptions().position(new LatLng(latlng.latitude, latlng.longitude))
		   					.title("PedidoID= " + entregas[i].deliveryID)
			                .snippet("Latitud: " + entregas[i].deliveryLatitude + ", Longitud: " + entregas[i].deliveryLongitude)
			                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		   				);
		   			}
		   		}*/
		        
	           
		   		/*ListLatLng = new ArrayList<LatLng>();
	           ListLatLng.add(new LatLng(-34.60836, -58.3801017));
	           ListLatLng.add(new LatLng(-34.609949, -58.380424));
	           ListLatLng.add(new LatLng(-34.6130463,-58.3827069));
	           ListLatLng.add(new LatLng(-34.617112, -58.380325));
	           ListLatLng.add(new LatLng(-34.617775, -58.385593));
	           ListLatLng.add(new LatLng(-34.618084, -58.390850));
	           ListLatLng.add(new LatLng(-34.618172, -58.392889));
	           ListLatLng.add(new LatLng(-34.615947, -58.393092));
	           ListLatLng.add(new LatLng(-34.613893, -58.393158));*/

	           /*for (int i=ListLatLng.size()-1;i>0;i--){
	        	   LatLng latlng = ListLatLng.get(i);
	        	   
		           map.addMarker(new MarkerOptions().position(new LatLng(latlng.latitude, latlng.longitude))
	                .title("Posicion " + String.valueOf(i)) 
	                .snippet("leyenda")
	                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		           );
	           }*/

	          /* 
	           map.addMarker(new MarkerOptions().position(new LatLng(-34.609949, -58.380424))
			     .title("Posicion 2")
			     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
			   );
	           
		       map.addMarker(new MarkerOptions().position(new LatLng(-34.6130463,-58.3827069))
				     .title("Posicion 3")
				     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
			   );*/
		        
				/*LatLng LatLng1 = new LatLng(-34.608359, -58.379995);
				listLatLng.add(LatLng1);
				LatLng LatLng2 = new LatLng(-34.609949, -58.380424);
				listLatLng.add(LatLng2);
				LatLng LatLng3 = new LatLng(-34.6130463,-58.3827069);
				listLatLng.add(LatLng3);
				LatLng LatLng4 = new LatLng(-34.617112, -58.380325);
				listLatLng.add(LatLng4);
				LatLng LatLng5 = new LatLng(-34.617775, -58.385593);
				listLatLng.add(LatLng5);
				LatLng LatLng6 = new LatLng(-34.618084, -58.390850);
				listLatLng.add(LatLng6);
				LatLng LatLng7 = new LatLng(-34.618172, -58.392889);
				listLatLng.add(LatLng7);
				LatLng LatLng8 = new LatLng(-34.615947, -58.393092);
				listLatLng.add(LatLng8);
				LatLng LatLng9 = new LatLng(-34.613893, -58.393158);
				listLatLng.add(LatLng9);*/
	           
	           /*map.addPolyline(new PolylineOptions().geodesic(true)
	                   .add(new LatLng(-34.6083666000, -58.3801017000))  // Sydney
	                   .add(new LatLng(-34.722832, -58.302925))  // Fiji
	                   .add(new LatLng(-34.711649, -58.309791))  // Hawaii
	                   .add(new LatLng(-34.6791034, -58.3487054))  // Mountain View
	           );*/


	           //map.setOnInfoWindowClickListener(this);

	 

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

	
	

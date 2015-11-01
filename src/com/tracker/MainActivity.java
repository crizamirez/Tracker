package com.tracker;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tracker.EntregasModel;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ListView lista;
	ImageView img;
	EntregasModel model;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    static final String SCAN_MODE = "com.google.zxing.client.android.SCAN.SCAN_MODE";
    String QRCode="";
    String PedidoCode="";
    String separador=";";
    String concat="&";
    Button btnActualiza;
    double PedidoLong;
    double PedidoLat;
    int tiempo;
    Button btnMaps;
    Button btnObtieneDatos;
    TextView selectedItem;
    int userID;
	int deliveryID;
    
    String serverUpdateData = "http://192.168.1.44:51229/api/updatedata?"; // 192.168.30.52 cac
    String serverGetData = "http://192.168.1.44:51229/api/getdeliveriesdata"; // 192.168.30.52 cac
    String serverGetPedidosGeoLocData = "http://192.168.1.44:51229/api/getDeliveryPositions?";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); // principal=activity_main // test=gpsLayout
		
		if (android.os.Build.VERSION.SDK_INT > 9){
        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy);
        	System.out.println("*** My thread is now configured to allow connection");
	    }
				
		lista = (ListView)findViewById(R.id.listView_pedidos);
		                	       
        btnActualiza = (Button) findViewById(R.id.btnActualiza);
        btnActualiza.setEnabled(false);
        btnActualiza.setOnClickListener(btnActualizaDatosListener);
                               
        btnMaps = (Button) findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(btnObtieneMapaListener);
		btnMaps.setEnabled(false);
		
		btnObtieneDatos = (Button) findViewById(R.id.btnObtieneDatos);
		btnObtieneDatos.setOnClickListener(btnObtieneDatosListener);
        
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				model = (EntregasModel) lista.getItemAtPosition(arg2);
				TextView selectedItem = (TextView) findViewById(R.id.selectedItem);
				selectedItem.setText(model.deliveryDesc + ", estado: " + model.deliveryStatus);
				
				btnMaps.setEnabled(true);
				btnActualiza.setEnabled(true);
				
				PedidoCode = new String (model.deliveryId + separador + model.userID + separador + model.deliveryStatus); 
				PedidoLat = Double.valueOf(model.latitude);
				PedidoLong = Double.valueOf(model.longitude);
				
				setUserID(model.userID);
				setDeliveryID(model.deliveryId);
			}
        	
		});
        
	
	}
	
	protected void setDeliveryID(Integer deliveryId) {
		this.deliveryID = deliveryId;
		
	}
	
	public int getDeliveryID(){
		int deliveryId = this.deliveryID;
		return deliveryId;
	}

	protected void setUserID(Integer user) {
		this.userID = user;
		
	}
	
	protected int getUserID(){
		int user = this.userID;
		return user;
	}
	
    public void scanQR(View v) {

        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra(SCAN_MODE, "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
        	System.out.println("User - scanSQ - ActivityNotFoundException= " + anfe.getMessage());

        }

    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

	            QRCode = contents;
	            	          
				if (QRCode.equalsIgnoreCase(PedidoCode)) {
		          					
					Toast.makeText(MainActivity.this, "Código QR coincide", Toast.LENGTH_SHORT).show();
					Toast.makeText(MainActivity.this, "Chequeando coordenadas de pedido y ubicación actual", Toast.LENGTH_LONG).show();
					
					GeolocationService geolocserv = new GeolocationService(getApplicationContext());
					 			  
					double UserLocLatitude = geolocserv.getLatitude();
					double UserLocLongitude = geolocserv.getLongitude();
										
					double dif = geolocserv.getDifCoordinates(PedidoLat, PedidoLong, UserLocLatitude, UserLocLongitude);							
					
					if (dif > 0 && dif <= 220) {
						
						Toast.makeText(MainActivity.this, "Coordenadas coinciden", Toast.LENGTH_SHORT).show();
						Toast.makeText(MainActivity.this, "Actualizando pedido...", Toast.LENGTH_SHORT).show();
						
						//Bloque de código para actualización de estado de pedido
			            HttpParams params = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(params, 15000);
						ResponseHandler<String> handler = new BasicResponseHandler();
						Object result = new Object();
						HttpClient client = new DefaultHttpClient(params);
						
						String param1= "Entregado";
						String param2=String.valueOf(model.deliveryId);
						String param3=String.valueOf(model.userID);
						String param4="NA";
						
						/*String enviaDatos = "http://192.168.30.52:51229/api/updatedata?n_estado=" + param1 + concat +
								"delivery_id=" + param2 + concat + "user_id=" + param3 + concat + "comentario=" + param4;*/
						
						String enviaDatos = serverUpdateData + "n_estado=" + param1 + concat +
								"delivery_id=" + param2 + concat + "user_id=" + param3 + concat + "comentario=" + param4 ;
												
						HttpGet getreq = new HttpGet(enviaDatos);
						
						try {
							client = new DefaultHttpClient(new BasicHttpParams());
							result = client.execute(getreq, handler);
							Toast.makeText(MainActivity.this, "Pedido actualizado", Toast.LENGTH_SHORT).show();
							btnActualiza.setEnabled(false);
							
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							//Log.e("Usuario= ", e.getMessage());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							//Log.e("Usuario= ", e.getMessage());
						}
						
						client.getConnectionManager().shutdown();
						
					} else {
						Toast.makeText(MainActivity.this, "No concuerdan las coordenadas entre el pedido y ubicación actual", Toast.LENGTH_SHORT).show();
					}          
		           
		           
				} else {
					Toast.makeText(MainActivity.this, "Código QR incorrecto", Toast.LENGTH_SHORT).show();
					Toast.makeText(MainActivity.this, "Valor= " + QRCode, Toast.LENGTH_SHORT).show();
				}
				 
	        }
	    }
	    
	    btnActualiza.setEnabled(false);
    }
	
	private OnClickListener btnObtieneDatosListener = new OnClickListener()
	{
	
   	 @Override
   	 public void onClick(View v) {
		  		 
   		selectedItem = (TextView) findViewById(R.id.selectedItem);
   		selectedItem.setText("");
		btnActualiza.setEnabled(false);
		btnMaps.setEnabled(false);
   		 
    	HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 15000);
		ResponseHandler<String> handler = new BasicResponseHandler();
		Object result = new Object();
		HttpClient client = new DefaultHttpClient(params);

		HttpGet getreq = new HttpGet(serverGetData);
			    
	    try {

			client = new DefaultHttpClient(new BasicHttpParams());
			result = client.execute(getreq, handler);
			
			String data = (String) result;
    		Gson g = new Gson();
    		Type t = new TypeToken<EntregasModel[]>(){}.getType();
    		EntregasModel [] entregas = (EntregasModel[])g.fromJson(data, t);

    		ArrayAdapter<EntregasModel> adapter = new ArrayAdapter<EntregasModel>(getApplicationContext(), android.R.layout.simple_spinner_item, entregas);
    		lista.setAdapter(adapter);
    		
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("Usuario= ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Usuario= ", e.getMessage());
		}
	    
		client.getConnectionManager().shutdown();
        
        
   	 	}
	
	}; 
	
	private OnClickListener btnObtieneMapaListener = new OnClickListener()
	{
	
		@Override
		public void onClick(View v) {
		    
		    try {	    
			    
		    	ArrayList<String> listLatLng;
		    	
		    	listLatLng = new ArrayList<String>(getPedidosGeoLoc());
			    			    			    
			    Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
			    mapIntent.putStringArrayListExtra("listLatLng",listLatLng);
			    startActivity(mapIntent);
			    
		    } catch (NullPointerException e){
		    	Toast.makeText(getApplicationContext(), "userID= NULL", Toast.LENGTH_LONG).show();
		    }
			 
		}
	
	}; 

	
	private OnClickListener btnActualizaDatosListener = new OnClickListener()
	{
	
	    public void onClick(View v)
	    {   
 
			 try {

				scanQR(v);

			 } catch (Exception e) {
			 
			 e.printStackTrace();
			 
			 }
			 
	   } 
	
	}; 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
	}
	
	public ArrayList<String> getPedidosGeoLoc(){

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
   		ResponseHandler<String> handler = new BasicResponseHandler();
   		Object result = new Object();
   		HttpClient client = new DefaultHttpClient(params);
       
   		String enviaDatos = serverGetPedidosGeoLocData + "userID=" + getUserID() + "&deliveryID=" + getDeliveryID();
   		HttpGet getreq = new HttpGet(enviaDatos);
   		
   		ArrayList<String> listaPosiciones = new ArrayList<String>();
   		EntregasGeoLocModel[] entregas = null;
	   			           
		try {

	   			client = new DefaultHttpClient(new BasicHttpParams());
	   			result = client.execute(getreq, handler);
	   			
	   			String data = (String) result;
	       		Gson g = new Gson();
	       		Type t = new TypeToken<EntregasGeoLocModel[]>(){}.getType();
	       		entregas = (EntregasGeoLocModel[])g.fromJson(data, t);
	       		
		   		if (entregas.length > 0){
		   			//ListLatLng = new ArrayList<LatLng>();
		   			for (int i=0; i<entregas.length;i++){
		   				listaPosiciones.add(String.valueOf(entregas[i].userID) + "," + String.valueOf(entregas[i].latitude) + "," + String.valueOf(entregas[i].longitude));	   				
		   			}
		   		}
	       		
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
           		
	   		return listaPosiciones;
	}
	
	}

	


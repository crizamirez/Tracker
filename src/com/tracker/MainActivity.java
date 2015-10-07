package com.tracker;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
    String QRCode="";
    String PedidoCode="";
    String separador=";";
    String concat="&";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9){
        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy);
        	System.out.println("*** My thread is now configured to allow connection");
	    }
		
		lista = (ListView)findViewById(R.id.listView_pedidos);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos);
        //lista.setAdapter(adapter);
        
        Button btnObtieneDatos = (Button) findViewById(R.id.btnObtieneDatos);	
        btnObtieneDatos.setOnClickListener(btnListener);
        
        final Button btnActualiza = (Button) findViewById(R.id.btnActualiza);
        btnActualiza.setOnClickListener(btnListener2);
        
        /*Descomentar cuando se pueda acceder a la web api*/
        btnActualiza.setEnabled(false);
        
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//Log.e("arg2= ", String.valueOf(arg2));
				model = (EntregasModel) lista.getItemAtPosition(arg2);
				//Log.e("model= ", String.valueOf(model.userID));
				TextView selectedItem = (TextView) findViewById(R.id.selectedItem);
				selectedItem.setText(model.deliveryDesc + ", estado: " + model.deliveryStatus);
				btnActualiza.setEnabled(true);
				PedidoCode = new String (model.deliveryId + separador + model.userID + separador + model.deliveryStatus); 
				
			}
        	
		});
        
	
	}
	
    public void scanQR(View v) {

        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            Log.e("No Scanner Found","");

        }

    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            //Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
	            //toast.show();
	            //Toast toast_result = Toast.makeText(this, "QR Pedido= " + contents, 1000);
	            //toast_result.show();
	            QRCode = contents;
	          
	            /*Reemplazar result1 por PedidoCode, que es el código generado por la selección de usuario
	             */
				//String result1 = "1;2;iniciado";

				/*Reemplazar result1 por PedidoCode*/
				if (QRCode.equalsIgnoreCase(PedidoCode)) {
		           Toast toast_result = Toast.makeText(MainActivity.this, "Código QR correcto, actualizando...", 1000);
		           toast_result.show();
		           
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
					
					String enviaDatos = "http://192.168.0.17:51229/api/updatedata?n_estado=" + param1 + concat +
							"delivery_id=" + param2 + concat + "user_id=" + param3 + concat + "comentario=" + param4;
					HttpGet getreq = new HttpGet(enviaDatos);
					
					try {
						client = new DefaultHttpClient(new BasicHttpParams());
						result = client.execute(getreq, handler);
						Log.e("Resultado=", (String) result);
					
						
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
		           
				} else {
		           Toast toast_result = Toast.makeText(MainActivity.this, "Código QR erróneo", 1000);
		           toast_result.show();
				}
				 
	        }
	    }
    }
	
	
	private OnClickListener btnListener = new OnClickListener()
	{

	    public void onClick(View v)
	    {   
	      //do the same stuff or use switch/case and get each button ID and do different   

	      //stuff depending on the ID
	    	HttpParams params = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(params, 15000);
			ResponseHandler<String> handler = new BasicResponseHandler();
			Object result = new Object();
			HttpClient client = new DefaultHttpClient(params);

			HttpGet getreq = new HttpGet("http://192.168.0.17:51229/api/getdeliveriesdata");
			
			try {

				client = new DefaultHttpClient(new BasicHttpParams());
				result = client.execute(getreq, handler);
				Log.e("Usuario=", (String) result);
				
				String data = (String) result;
	    		Gson g = new Gson();
	    		Type t = new TypeToken<EntregasModel[]>(){}.getType();
	    		EntregasModel [] entregas = (EntregasModel[])g.fromJson(data, t);

	    		ArrayAdapter<EntregasModel> adapter = new ArrayAdapter<EntregasModel>(getApplicationContext(), android.R.layout.simple_spinner_item, entregas);
	    		lista.setAdapter(adapter);
	    		
	    		
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
	    } 

	}; 
	
	

   
private OnClickListener btnListener2 = new OnClickListener()
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
	
	}

	


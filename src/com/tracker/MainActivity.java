package com.tracker;

import java.io.IOException;
import java.lang.reflect.Type;
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
import com.tracker.EntregasModel;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	ListView lista;
    String [] datos = {"Lista desplegable","dato_2","dato_3"};
	
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
        
        Button btn = (Button) findViewById(R.id.button1);	
        btn.setOnClickListener(btnListener);
		
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
			HttpGet getreq = new HttpGet("http://192.168.0.17:51228/Pedidos/api/values");
			
			try {
				client = new DefaultHttpClient(new BasicHttpParams());
				result = client.execute(getreq, handler);
				Log.e("Usuario=", (String) result);
				
				String data = (String) result;
	    		Gson g = new Gson();
	    		Type t = new TypeToken<EntregasModel[]>(){}.getType();
	    		EntregasModel [] entregas = (EntregasModel[])g.fromJson(data, t);
	    		//ListView list = getListView();
	    		ArrayList<String> correos_list = new ArrayList<String>();
	    		int i = entregas.length;
	    		Log.e("Entregas= " , String.valueOf(i));
	    		
	    		for (i=0;i<entregas.length;i++){
	    			correos_list.add("User= " + entregas[i].userEmail + ", " + "ID Entrega= " +entregas[i].deliveryId + ", " + "\n"
	    					+ " Estado= " + entregas[i].deliveryStatus);
	    			//Log.e("correos=",entregas[i].userEmail);
	    		}
	    			    		
	    		//int j=correos_list.size();
	    		//Log.e("Valor de J=", String.valueOf(j));
	    		String [] correos = new String[correos_list.size()];
	    		
	    		for (int k=0;k<correos_list.size();k++){
	    			correos[k] = correos_list.get(k);
	    			Log.e("correos["+k+"]= ",correos[k]);
	    		}
	    		
	    		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, correos);
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
	
	
	/*public void obtenerPedidos(){
		APICaller ap = new APICaller();
		ap.llamaApi();
	}*/
	
	/*public void obtenerPedidos(View view) {
		HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 15000);
		ResponseHandler<String> handler = new BasicResponseHandler();
		Object result = new Object();
		HttpClient client = new DefaultHttpClient(params);
		HttpGet getreq = new HttpGet("http://192.168.0.17:51228/Pedidos/api/values");
		
		try {
			client = new DefaultHttpClient(new BasicHttpParams());
			result = client.execute(getreq, handler);
			Log.e("Usuario=", (String) result);
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
		
		
		
	}*/
	
}

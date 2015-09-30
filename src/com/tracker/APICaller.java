package com.tracker;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class APICaller {

	public void llamaApi() {
		HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 15000);
		ResponseHandler<String> handler = new BasicResponseHandler();
		Object result = new Object();
		HttpClient client = new DefaultHttpClient(params);
		HttpGet getreq = new HttpGet("http://192.168.0.17:51228/Pedidos/api/values");
		
		try {
			client = new DefaultHttpClient(new BasicHttpParams());
			result = client.execute(getreq, handler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Usuario= ", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.getConnectionManager().shutdown();
	}
	
	
}

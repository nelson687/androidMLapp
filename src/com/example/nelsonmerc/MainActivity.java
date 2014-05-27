package com.example.nelsonmerc;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	public final static String OAUTH_URL = "com.example.nelsonmerc.URL";
	public final static String CALLBACK_URL = "http://localhost/Callback";
	String response;
	Meli meli;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		meli = new Meli(7763151957178019L, "OGlECpM8EOXVWzTzvv0NoLcyt2c5QX8S");
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void testSdk(View view){
		String redirectUrl = meli.getAuthUrl(CALLBACK_URL);
		Intent intent = new Intent(this, OauthLogin.class);
		intent.putExtra(OAUTH_URL, redirectUrl);
		startActivityForResult(intent, 1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		meli.authorize(data.getStringExtra(OauthLogin.CODE), CALLBACK_URL);
	}
	
	public void testGet(View view){
		RequestParams params = new RequestParams();
		params.put("access_token", meli.getAccessToken());
		
		Handler responseHandler = new Handler(){
		    @Override
		    public void handleMessage(Message msg){
		        if(msg.what == 0){ //means success
		        	JSONObject response = (JSONObject)msg.obj;
		        	TextView result = (TextView) findViewById(R.id.textView1); 
					try {
						result.setText("User name: " + response.get("nickname").toString());
					} catch (JSONException e) {
						//DO SOMETHING
					}
		        }else{
		        	//msg.what == 1 means failure
		        	//DO SOMETHING
		        }
		    }
		};
		meli.get("/users/me.json", params, responseHandler);
	}
	
}

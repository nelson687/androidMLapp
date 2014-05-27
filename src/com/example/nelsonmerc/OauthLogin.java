package com.example.nelsonmerc;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;

public class OauthLogin extends ActionBarActivity {

	public final static String CODE = "com.example.nelsonmerc.CODE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oauth_login);
		//View view = getLayoutInflater().inflate(R.layout.activity_oauth_login, false);
		WebView webview = new WebView(this);
		setContentView(webview);
		Intent intent = getIntent();
		String url = intent.getStringExtra(MainActivity.OAUTH_URL);
		//WebView webview = (WebView)findViewById(R.id.oauthWebView);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url){
				if(url.startsWith(MainActivity.CALLBACK_URL)){
					if(url.indexOf("code=") != -1){
						try{
							//extract the code from the url, could use a better approach instead of substring
							String code = url.substring(31);
							Intent intent = new Intent();
							intent.putExtra(CODE, code);
							view.setVisibility(View.INVISIBLE);
							setResult(RESULT_OK, intent);
							finish();
						}catch(StringIndexOutOfBoundsException ex){
							//DO SOMETHING
						}
					}
				}
			}
		});
		webview.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.oauth_login, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_oauth_login,
					container, false);
			return rootView;
		}
	}

}

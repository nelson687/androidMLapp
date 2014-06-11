package com.example.nelsonmerc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ShowTicketsActivity extends ActionBarActivity {

    String response;
    Meli meli;
    ArrayList<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tickets);
        meli = new Meli(7763151957178019L, "OGlECpM8EOXVWzTzvv0NoLcyt2c5QX8S");
        //should retrieve from the intent the category needed and pass to the method
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_tickets, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_show_tickets,
                    container, false);
            return rootView;
        }
    }

    private JSONObject getJSONResponse(){
        String jsonString = "";
        JSONObject result = null;
        try {
            InputStream is = getAssets().open("itemsByCat.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            result = new JSONObject(jsonString);
        } catch (IOException e) {
            String error = "fail";
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return result;
    }
    
    private void loadData(){
        //this is to get the response from a file
        //JSONObject response = this.getJSONResponse();
        //get the response from the api call
        items = new ArrayList<Item>();
        Handler responseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 0){ //means success
                    JSONObject response = (JSONObject)msg.obj;
                    items = getItems(response);
                    final CustomAdapter ca;
                    Context context = ShowTicketsActivity.this.getApplicationContext();
                    ca = new CustomAdapter(context, items);
                    ListView lv = new ListView(context);
                    setContentView(lv);
                    setTitle("Entradas");
                    View header = (View)getLayoutInflater().inflate(R.layout.listview_header, null);
                    lv.addHeaderView(header);
                    lv.setAdapter(ca);
                }else{
                    //msg.what == 1 means failure
                    //DO SOMETHING
                    String hola = "";
                }
            }

        };
        meli.get("/sites/MLA/search?category=MLA1953", null, responseHandler);
    }
    private ArrayList<Item> getItems(JSONObject response) {
        Item item;
        ArrayList<Item> items = new ArrayList<>();
        try {
            JSONArray results = response.getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                item = new Item();
                JSONObject resultItem = (JSONObject) results.get(i);
                item.setId(resultItem.getString("id"));
                item.setTitle(resultItem.getString("title"));
                item.setPrice(resultItem.getDouble("price"));
                item.setPicture(resultItem.getString("thumbnail"));
                //get seller json
                JSONObject seller = (JSONObject) resultItem.get("seller");
                item.setSellerId(seller.getString("id"));
                items.add(item);
            }
        return items;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}

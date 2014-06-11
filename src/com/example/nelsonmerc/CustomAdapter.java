package com.example.nelsonmerc;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Item> {
  private final Context context;
  private final ArrayList<Item> values;
  ImageView imageView;
  HashMap<String, Bitmap> thumbnailsMap;
  public CustomAdapter(Context context, ArrayList<Item> values) {
    super(context, R.layout.listview_row_layout, values);
    this.context = context;
    this.values = values;
    
    thumbnailsMap = new HashMap<String, Bitmap>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.listview_row_layout, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    TextView textViewPrice = (TextView) rowView.findViewById(R.id.labelPrice);
    //textViewPrice.addTextChangedListener(new CurrencyTextWatcher());
    imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView.setText(values.get(position).getTitle() + " ");
    textViewPrice.setText("$" + values.get(position).getPrice().toString());
    if(values.get(position).getPicture() != null){
        GetImageFromURL task = new GetImageFromURL(imageView, values.get(position));
        task.execute(new String[] {values.get(position).getPicture()});
    }
    
    return rowView;
  }
  
  class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference imageViewReference;
    private Item item;

    public GetImageFromURL(ImageView imageView, Item item){
        imageViewReference = new WeakReference(imageView);
        this.item = item;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap map = null;
        URL pictureURL = null;
            try {
                pictureURL = new URL(urls[0]);
                if(thumbnailsMap.containsKey(item.getId())){
                    map = thumbnailsMap.get(item.getId());
                }else{
                    map = BitmapFactory.decodeStream(pictureURL.openConnection() .getInputStream());
                    thumbnailsMap.put(item.getId(), map);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return map;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        ImageView imageView = (ImageView) imageViewReference.get();
        if(imageView != null){
            imageView.setImageBitmap(result);
        }
    }
  }
} 
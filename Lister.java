package com.in.sight.android;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Lister extends ListActivity{

 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.app_list);
  String[] captionArray=ListMenu.array;
  ItemsAdapter ItemsAdapter = new ItemsAdapter(
    Lister.this, R.layout.list,
    captionArray);
  setListAdapter(ItemsAdapter);
 }
 
  
 private class ItemsAdapter extends BaseAdapter {
  String[] items;

  public ItemsAdapter(Context context, int textViewResourceId,
    String[] items) {
   // super(context, textViewResourceId, items);
   this.items = items;
  }

  // @Override
  public View getView(int position, View convertView, ViewGroup parent) {
   
   View v = convertView;
   if (v == null) {
    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    v = vi.inflate(R.layout.list, null);
   } 
   TextView post = (TextView) v
     .findViewById(R.id.post);
   post.setText(items[position]);
   
   return v;
  }

  public int getCount() {
   return items.length;
  }

  public Object getItem(int position) {
   return position;
  }

  public long getItemId(int position) {
   return position;
  }
 }
}

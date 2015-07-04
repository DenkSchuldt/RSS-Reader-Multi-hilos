package com.sistemasoperativos.denny.rssreader.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sistemasoperativos.denny.rssreader.R;

import java.util.List;

/**
 * Created by denny on 16/06/15.
 */
public class ProducersAdapter extends ArrayAdapter<String> {

  private Context context;
  private String[] producers;
  private String[] types;

  public ProducersAdapter(Context context, String[] producers, String[] types) {
    super(context, -1, producers);
    this.context = context;
    this.producers = producers;
    this.types = types;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View root = inflater.inflate(R.layout.view_drawer_item, parent, false);
    ImageView item_logo = (ImageView) root.findViewById(R.id.item_logo);
    TextView item_name = (TextView) root.findViewById(R.id.item_name);

    switch (position) {
      case 0:
        item_logo.setImageResource(R.drawable.bbc_logo);
        break;
      case 1:
        item_logo.setImageResource(R.drawable.cnn_logo);
        break;
      case 2:
        item_logo.setImageResource(R.drawable.eluniverso_logo);
        break;
      case 3:
        item_logo.setImageResource(R.drawable.telegraph_logo);
        break;
    }

    item_name.setText(producers[position]);
    root.setTag(types[position]);
    return root;
  }
}

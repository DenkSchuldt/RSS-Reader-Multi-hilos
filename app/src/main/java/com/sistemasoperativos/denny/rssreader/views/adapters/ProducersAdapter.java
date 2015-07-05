package com.sistemasoperativos.denny.rssreader.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denny on 16/06/15.
 */
public class ProducersAdapter extends ArrayAdapter<Producer> {

  private Context context;
  private ArrayList<Producer> producers;

  public ProducersAdapter(Context context, ArrayList<Producer> producers) {
    super(context, -1, producers);
    this.context = context;
    this.producers = producers;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View root = inflater.inflate(R.layout.view_drawer_item, parent, false);
    ImageView item_logo = (ImageView) root.findViewById(R.id.item_logo);
    TextView item_name = (TextView) root.findViewById(R.id.item_name);
    ImageView item_action = (ImageView) root.findViewById(R.id.item_action);

    Producer producer = producers.get(position);
    switch (producer.getName()) {
      case Constants.ELUNIVERSO:
        item_logo.setImageResource(R.drawable.eluniverso_logo);
        break;
      case Constants.BBC:
        item_logo.setImageResource(R.drawable.bbc_logo);
        break;
      case Constants.CNN:
        item_logo.setImageResource(R.drawable.cnn_logo);
        break;
      case Constants.TELEGRAPH:
        item_logo.setImageResource(R.drawable.telegraph_logo);
        break;
    }
    item_name.setText(producer.getName());
    if (producer.isActive())
      item_action.setImageResource(R.drawable.ic_remove_black_24dp);
    else
      item_action.setImageResource(R.drawable.ic_add_black_24dp);
    root.setTag(producer);
    return root;
  }
}

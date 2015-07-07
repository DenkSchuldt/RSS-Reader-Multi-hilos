package com.sistemasoperativos.denny.rssreader.dialogfragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.interfaces.OnSettingsEvent;
import com.sistemasoperativos.denny.rssreader.models.Feed;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.sistemasoperativos.denny.rssreader.views.MainActivity;
import com.sistemasoperativos.denny.rssreader.views.SettingsActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by denny on 27/06/15.
 */
public class EntryDialogFragment extends DialogFragment {

  private Feed entry;
  private ViewHolder viewHolder;

  public static EntryDialogFragment newInstance(Feed entry) {
    EntryDialogFragment fdf = new EntryDialogFragment();
    Bundle args = new Bundle();
    args.putSerializable(Constants.ENTRY, entry);
    fdf.setArguments(args);
    return fdf;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    entry = getEntryFromArguments();
    if (entry.getImgurl().isEmpty()) {
      viewHolder = new ViewHolder(inflater.inflate(R.layout.fragment_entry_no_image, container, false));
    } else {
      viewHolder = new ViewHolder(inflater.inflate(R.layout.fragment_entry, container, false));
    }
    viewHolder.findDialogViews();
    setCancelable(false);
    updateContent();
    return viewHolder.root;
  }

  public Feed getEntryFromArguments() {
    return (Feed) getArguments().getSerializable(Constants.ENTRY);
  }

  public void updateContent() {
    if (!entry.getImgurl().isEmpty())
      Picasso.with(getActivity()).load(entry.getImgurl()).into(viewHolder.img);
    switch (entry.getSource()) {
      case Constants.ELUNIVERSO:
        viewHolder.sourceLogo.setImageResource(R.drawable.eluniverso_logo);
        break;
      case Constants.BBC:
        viewHolder.sourceLogo.setImageResource(R.drawable.bbc_logo);
        break;
    }
    viewHolder.sourceName.setText(entry.getSource());
    viewHolder.title.setText(entry.getTitle());
    viewHolder.description.setText(entry.getDescription());
    viewHolder.time.setText(entry.getPubDate());
  }

  public class ViewHolder implements View.OnClickListener {

    public View root;

    public TextView title;
    public ImageView img;
    public CircularImageView sourceLogo;
    public TextView sourceName;
    public TextView description;
    public TextView time;
    public Button close;
    public Button more;

    public ViewHolder(View view) {
      root = view;
    }

    public void findDialogViews() {
      if (!entry.getImgurl().isEmpty())
        img = (ImageView) root.findViewById(R.id.entry_detailed_img);
      title = (TextView) root.findViewById(R.id.entry_detailed_title);
      sourceLogo = (CircularImageView) root.findViewById(R.id.entry_detailed_source_logo);
      sourceName = (TextView) root.findViewById(R.id.entry_detailed_source_name);
      description = (TextView) root.findViewById(R.id.entry_detailed_description);
      time = (TextView) root.findViewById(R.id.entry_detailed_time);
      close = (Button) root.findViewById(R.id.entry_detailed_close);
      more = (Button) root.findViewById(R.id.entry_detailed_more);

      close.setOnClickListener(this);
      more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.entry_detailed_close:
          dismiss();
          break;
        case R.id.entry_detailed_more:
          dismiss();
          break;
      }
    }

  }

}

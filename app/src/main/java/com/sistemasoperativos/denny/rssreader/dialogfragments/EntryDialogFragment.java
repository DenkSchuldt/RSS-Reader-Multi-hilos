package com.sistemasoperativos.denny.rssreader.dialogfragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.pkmmte.view.CircularImageView;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.database.db.EntryDB;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.sistemasoperativos.denny.rssreader.utils.Utils;
import com.sistemasoperativos.denny.rssreader.views.WebActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by denny on 27/06/15.
 */
public class EntryDialogFragment extends DialogFragment {

  private Entry entry;
  private Point displaySize;
  private ViewHolder viewHolder;
  private EntryDB entryDB;

  public static EntryDialogFragment newInstance(Entry entry) {
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
    displaySize = Utils.getDisplaySize(getActivity());
    DBHelper helper = OpenHelperManager.getHelper(getActivity(), DBHelper.class);
    entryDB = new EntryDB(helper);
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

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setCustomDialogSize();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    setCustomDialogSize();
  }

  /**
   *
   */
  public void setCustomDialogSize() {
    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
    if (Utils.orientationIsPortrait(getActivity())) {
      params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
      params.height = (int) getResources().getDimension(R.dimen.dialog_height);
    } else if (Utils.orientationIsLandscape(getActivity())){
      params.width = (int) getResources().getDimension(R.dimen.dialog_width);
      params.height = ViewGroup.LayoutParams.MATCH_PARENT;
    }
    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
  }

  /**
   *
   */
  public Entry getEntryFromArguments() {
    return (Entry) getArguments().getSerializable(Constants.ENTRY);
  }

  /**
   *
   */
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
    if (entry.isScheduled()) {
      viewHolder.schedule.setBackgroundColor(getResources().getColor(R.color.primary_dark_material_dark));
      viewHolder.schedule.setImageResource(R.drawable.ic_schedule_white_48dp);
    }
  }

  /**
   *
   */
  public class ViewHolder implements View.OnClickListener {

    public View root;

    public TextView title;
    public ImageView img;
    public CircularImageView sourceLogo;
    public CircularImageView schedule;
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
      schedule = (CircularImageView) root.findViewById(R.id.entry_detailed_schedule);
      sourceName = (TextView) root.findViewById(R.id.entry_detailed_source_name);
      description = (TextView) root.findViewById(R.id.entry_detailed_description);
      time = (TextView) root.findViewById(R.id.entry_detailed_time);
      close = (Button) root.findViewById(R.id.entry_detailed_close);
      more = (Button) root.findViewById(R.id.entry_detailed_more);

      schedule.setOnClickListener(this);
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
          Intent web = new Intent(getActivity(), WebActivity.class);
          web.putExtra(Constants.ENTRY, entry);
          startActivity(web);
          dismiss();
          break;
        case R.id.entry_detailed_schedule:
          entry.setScheduled(!entry.isScheduled());
          entryDB.saveEntry(entry);
          if (entry.isScheduled()) {
            viewHolder.schedule.setBackgroundColor(getResources().getColor(R.color.primary_dark_material_dark));
            viewHolder.schedule.setImageResource(R.drawable.ic_schedule_white_48dp);
          } else {
            viewHolder.schedule.setBackgroundColor(getResources().getColor(R.color.white));
            viewHolder.schedule.setImageResource(R.drawable.ic_schedule_black_48dp);
          }
          break;
      }
    }

  }

}

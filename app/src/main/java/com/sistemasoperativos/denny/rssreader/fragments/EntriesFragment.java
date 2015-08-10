package com.sistemasoperativos.denny.rssreader.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.interfaces.OnEntryEvent;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.sistemasoperativos.denny.rssreader.views.EntryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by denny on 14/06/15.
 */
public class EntriesFragment extends Fragment {

  private String TAG;
  private ViewHolder viewHolder;
  private OnEntryEvent onEntryEvent;
  private ArrayList<Entry> entries;

  public static EntriesFragment newInstance(String TAG) {
    EntriesFragment fragment = new EntriesFragment();
    Bundle args = new Bundle();
    args.putString(Constants.TAG, TAG);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      onEntryEvent = (OnEntryEvent) getActivity();
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()+ " must implement OnEntryEvent");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    TAG = getArguments().getString(Constants.TAG);
    entries = new ArrayList<>();
    viewHolder = new ViewHolder(inflater.inflate(R.layout.fragment_entries,container,false));
    viewHolder.findFragmentViews();
    return viewHolder.root;
  }

  @Override
  public void onResume() {
    super.onResume();
    onEntryEvent.updateContent(TAG);
  }

  public void updateContent(final Entry entry) {
    if(!entryExits(entry)) {
      entries.add(entry);
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          viewHolder.empty.setVisibility(View.GONE);
          createCard(entry);
        }
      });
    }
  }

  public boolean entryExits(Entry entry) {
    for (Entry e : entries) {
      if (e.getTitle().equals(entry.getTitle()))
        return true;
    }
    return false;
  }

  public void createCard(Entry entry) {
    View card = getActivity().getLayoutInflater().inflate(R.layout.entry, viewHolder.entries, false);
    card.setTag(entry);

    TextView title = (TextView) card.findViewById(R.id.entry_title);
    TextView category = (TextView) card.findViewById(R.id.entry_category);
    TextView time = (TextView) card.findViewById(R.id.entry_time);
    ImageView media = (ImageView) card.findViewById(R.id.entry_media);

    title.setText(entry.getTitle());
    category.setText(entry.getCategory());

    String pubDate = entry.getPubDate();
    String[] date = pubDate.split("\\s+");

    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int pubDay = Integer.parseInt(date[1]);

    if (pubDay == day)
      time.setText("Hoy, " + date[4].substring(0,5));
    else if (pubDay == day-1)
      time.setText("Ayer, " + date[4].substring(0,5));
    else
      time.setText(date[2] + " " + date[1] + ", " + date[4].substring(0,5));

    if (!entry.getImgurl().isEmpty()) {
      Picasso.with(getActivity()).load(entry.getImgurl()).into(media);
    } else {
      media.setVisibility(View.GONE);
    }

    card.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Entry entry = (Entry) v.getTag();
        Intent detail = new Intent(getActivity(), EntryActivity.class);
        detail.putExtra(Constants.ENTRY, entry);
        startActivity(detail);
      }
    });

    if (viewHolder.entries.getChildCount()>0)
      viewHolder.entries.addView(card, 0);
    else
      viewHolder.entries.addView(card);
    Log.d("", "CARD created with title:  " + entry.getTitle());
  }

  /**
   *
   */
  public class ViewHolder {

    public View root;
    public LinearLayout entries;
    public LinearLayout empty;

    public ViewHolder(View view) {
      root = view;
    }

    public void findFragmentViews() {
      entries = (LinearLayout) root.findViewById(R.id.entries);
      empty = (LinearLayout) root.findViewById(R.id.empty);
    }

  }

}

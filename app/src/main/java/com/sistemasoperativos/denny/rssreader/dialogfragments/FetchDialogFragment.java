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
import android.widget.NumberPicker;

import com.sistemasoperativos.denny.rssreader.interfaces.OnSettingsEvent;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.views.SettingsActivity;

/**
 * Created by denny on 27/06/15.
 */
public class FetchDialogFragment extends DialogFragment {

  private ViewHolder viewHolder;
  private OnSettingsEvent onSettingsEvent;
  private int fetchTime;

  public static FetchDialogFragment newInstance(int value) {
    FetchDialogFragment fdf = new FetchDialogFragment();
    Bundle args = new Bundle();
    args.putInt(SettingsActivity.SETTINGS_FETCH_TIME, value);
    fdf.setArguments(args);
    return fdf;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    fetchTime = getFetchTimeFromArguments();
    viewHolder = new ViewHolder(inflater.inflate(R.layout.fragment_number_picker, container,false));
    viewHolder.findDialogViews();
    setCancelable(false);
    return viewHolder.root;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    onSettingsEvent = (OnSettingsEvent) activity;
    Log.i(FetchDialogFragment.class.getSimpleName(), "Fragment attached to activity.");
  }

  public int getFetchTimeFromArguments() {
    return getArguments().getInt(SettingsActivity.SETTINGS_FETCH_TIME);
  }

  public class ViewHolder implements View.OnClickListener {

    public View root;
    public NumberPicker numberPicker;
    public Button cancel;
    public Button accept;

    public ViewHolder(View view) {
      root = view;
    }

    public void findDialogViews() {
      numberPicker = (NumberPicker) root.findViewById(R.id.settings_fetch_number_picker);
      numberPicker.setMaxValue(100);
      numberPicker.setMinValue(1);
      numberPicker.setValue(fetchTime);
      cancel = (Button) root.findViewById(R.id.settings_fetch_cancel);
      accept = (Button) root.findViewById(R.id.settings_fetch_accept);

      cancel.setOnClickListener(this);
      accept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.settings_fetch_cancel:
          dismiss();
          break;
        case R.id.settings_fetch_accept:
          int value = numberPicker.getValue();
          saveToSharedPreferences(value);
          onSettingsEvent.onSettingsFetchTime();
          dismiss();
          break;
      }
    }

    public void saveToSharedPreferences(int value) {
      SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putInt(getString(R.string.shared_preferences_settings_time), value);
      editor.commit();
    }
  }

}

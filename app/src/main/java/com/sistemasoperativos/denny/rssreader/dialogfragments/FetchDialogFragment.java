package com.sistemasoperativos.denny.rssreader.dialogfragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.sistemasoperativos.denny.rssreader.R;

/**
 * Created by denny on 27/06/15.
 */
public class FetchDialogFragment extends DialogFragment {

  private ViewHolder viewHolder;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    viewHolder = new ViewHolder(inflater.inflate(R.layout.fragment_number_picker, container,false));
    viewHolder.findDialogViews();
    setCancelable(false);
    return viewHolder.root;
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
      numberPicker.setValue(50);
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
          break;
      }
    }
  }

}

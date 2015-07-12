package com.sistemasoperativos.denny.rssreader.dialogfragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new Dialog(getActivity(), getTheme()){
      @Override
      public void onBackPressed() {
        dismiss();
      }
    };
  }

  /**
   *
   */
  public void setCustomDialogSize() {
    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
    if (Utils.orientationIsPortrait(getActivity())) {
      params.width = ViewGroup.LayoutParams.MATCH_PARENT;
      params.height = (int) getResources().getDimension(R.dimen.dialog_height);
    } else if (Utils.orientationIsLandscape(getActivity())){
      params.width = (int) getResources().getDimension(R.dimen.dialog_width);
      params.height = ViewGroup.LayoutParams.MATCH_PARENT;
    }
    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
  }

  public static Bitmap drawableToBitmap (Drawable drawable) {
    Bitmap bitmap = null;
    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
      if(bitmapDrawable.getBitmap() != null) {
        return bitmapDrawable.getBitmap();
      }
    }
    if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
      bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    } else {
      bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    }
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
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
    if (!entry.getImgurl().isEmpty()) {
      Picasso.with(getActivity()).load(entry.getImgurl()).into(viewHolder.img);
      Palette.generateAsync(drawableToBitmap(viewHolder.img.getDrawable()), new Palette.PaletteAsyncListener() {
        public void onGenerated(Palette palette) {
          viewHolder.header.setBackgroundColor(palette.getMutedColor(R.color.secondary));
        }
      });
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
    public ImageView closeImg;
    public RelativeLayout header;
    public ImageView schedule;
    public TextView sourceName;
    public TextView description;
    public TextView time;
    public Button closeBtn;
    public Button more;

    public ViewHolder(View view) {
      root = view;
    }

    public void findDialogViews() {
      if (!entry.getImgurl().isEmpty()) {
        img = (ImageView) root.findViewById(R.id.entry_detailed_img);
        closeImg = (ImageView) root.findViewById(R.id.entry_detailed_close);
        closeImg.setOnClickListener(this);
      } else {
        closeBtn = (Button) root.findViewById(R.id.entry_detailed_close);
        closeBtn.setOnClickListener(this);
      }
      header = (RelativeLayout) root.findViewById(R.id.entry_detailed_header);
      title = (TextView) root.findViewById(R.id.entry_detailed_title);
      schedule = (ImageView) root.findViewById(R.id.entry_detailed_schedule);
      sourceName = (TextView) root.findViewById(R.id.entry_detailed_source_name);
      description = (TextView) root.findViewById(R.id.entry_detailed_description);
      time = (TextView) root.findViewById(R.id.entry_detailed_time);
      more = (Button) root.findViewById(R.id.entry_detailed_more);

      schedule.setOnClickListener(this);
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

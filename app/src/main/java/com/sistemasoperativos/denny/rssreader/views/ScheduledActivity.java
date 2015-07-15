package com.sistemasoperativos.denny.rssreader.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.database.db.EntryDB;
import com.sistemasoperativos.denny.rssreader.dialogfragments.EntryDialogFragment;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by denny on 27/06/15.
 */
public class ScheduledActivity extends AppCompatActivity {

  private EntryDB entryDB;
  private ViewHolder viewHolder;
  private ArrayList<Entry> entries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scheduled);
    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();
    setCustomActionBar();
    setCustomStatusBar();

    DBHelper helper = OpenHelperManager.getHelper(ScheduledActivity.this, DBHelper.class);
    entryDB = new EntryDB(helper);
    entries = entryDB.getScheduledEntries();

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        for (Entry entry : entries) {
          viewHolder.createCard(entry);
        }
      }
    });


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.right_in, R.anim.right_out);
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(R.anim.right_in, R.anim.right_out);
  }

  public void setCustomActionBar() {
    setSupportActionBar(viewHolder.toolbar);
    getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_title_scheduled));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void setCustomStatusBar() {
    getWindow().setFlags(
      WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
      WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    );
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setTintColor(Color.TRANSPARENT);
  }

  /**
   *
   *
   */
  private class ViewHolder {

    private Toolbar toolbar;
    private LinearLayout entries;

    public void findActivityViews() {
      toolbar = (Toolbar) findViewById(R.id.scheduled_toolbar);
      entries = (LinearLayout) findViewById(R.id.scheduled_entries);
    }

    public void createCard(final Entry entry) {
      View card = getLayoutInflater().inflate(R.layout.entry, entries, false);

      TextView title = (TextView) card.findViewById(R.id.entry_title);
      TextView source = (TextView) card.findViewById(R.id.entry_source);
      TextView time = (TextView) card.findViewById(R.id.entry_time);
      ImageView media = (ImageView) card.findViewById(R.id.entry_media);

      title.setText(entry.getTitle());
      source.setText(entry.getSource());

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
        Picasso.with(ScheduledActivity.this).load(entry.getImgurl()).into(media);
      } else {
        media.setVisibility(View.GONE);
      }

      card.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          EntryDialogFragment edf = EntryDialogFragment.newInstance(entry);
          edf.show(getSupportFragmentManager(), "");
        }
      });

      entries.addView(card);
    }

  }

}

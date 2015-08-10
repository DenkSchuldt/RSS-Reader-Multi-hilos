package com.sistemasoperativos.denny.rssreader.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.database.db.EntryDB;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by Denny on 10/8/2015.
 */
public class EntryActivity extends AppCompatActivity {

  private Entry entry;
  private ViewHolder viewHolder;
  private EntryDB entryDB;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DBHelper helper = OpenHelperManager.getHelper(this, DBHelper.class);
    entry = (Entry) getIntent().getSerializableExtra(Constants.ENTRY);
    entryDB = new EntryDB(helper);
    if (entry.getImgurl().isEmpty()) {
      setContentView(R.layout.activity_entry_no_image);
    } else {
      setContentView(R.layout.activity_entry);
    }
    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();
    setCustomStatusBar();
    updateContent();
  }

  /**
   *
   */
  public void updateContent() {
    if (!entry.getImgurl().isEmpty()) {
      Picasso.with(this).load(entry.getImgurl()).into(viewHolder.img);
      Palette.generateAsync(drawableToBitmap(viewHolder.img.getDrawable()), new Palette.PaletteAsyncListener() {
        public void onGenerated(Palette palette) {
          viewHolder.header.setBackgroundColor(palette.getMutedColor(R.color.secondary_text));
          viewHolder.closeBtn.setTextColor(palette.getMutedColor(R.color.secondary_text));
          viewHolder.more.setTextColor(palette.getMutedColor(R.color.secondary_text));
        }
      });
    }
    viewHolder.sourceName.setText(entry.getSource());
    viewHolder.title.setText(entry.getTitle());
    viewHolder.description.setText(entry.getDescription());

    String pubDate = entry.getPubDate();
    String[] date = pubDate.split("\\s+");
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int pubDay = Integer.parseInt(date[1]);
    if (pubDay == day)
      viewHolder.time.setText("Hoy, " + date[4].substring(0,5));
    else if (pubDay == day-1)
      viewHolder.time.setText("Ayer, " + date[4].substring(0,5));
    else
      viewHolder.time.setText(date[2] + " " + date[1] + ", " + date[4].substring(0,5));

    if (entry.isScheduled()) {
      viewHolder.schedule.setImageResource(R.drawable.ic_schedule_black_48dp);
    }
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
   * @param drawable
   * @return
   */
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
  public class ViewHolder implements View.OnClickListener {

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

    public void findActivityViews() {
      if (!entry.getImgurl().isEmpty()) {
        img = (ImageView) findViewById(R.id.entry_detailed_img);
        closeImg = (ImageView) findViewById(R.id.entry_detailed_close_img);
        closeImg.setOnClickListener(this);
      }

      header = (RelativeLayout) findViewById(R.id.entry_detailed_header);
      title = (TextView) findViewById(R.id.entry_detailed_title);
      schedule = (ImageView) findViewById(R.id.entry_detailed_schedule);
      sourceName = (TextView) findViewById(R.id.entry_detailed_source_name);
      description = (TextView) findViewById(R.id.entry_detailed_description);
      time = (TextView) findViewById(R.id.entry_detailed_time);
      closeBtn = (Button) findViewById(R.id.entry_detailed_close);
      more = (Button) findViewById(R.id.entry_detailed_more);

      schedule.setOnClickListener(this);
      closeBtn.setOnClickListener(this);
      more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.entry_detailed_close_img:
        case R.id.entry_detailed_close:
          finish();
          break;
        case R.id.entry_detailed_more:
          Intent web = new Intent(EntryActivity.this, WebActivity.class);
          web.putExtra(Constants.ENTRY, entry);
          startActivity(web);
          break;
        case R.id.entry_detailed_schedule:
          entry.setScheduled(!entry.isScheduled());
          if (entry.isScheduled()) {
            entryDB.scheduleEntry(entry);
            viewHolder.schedule.setImageResource(R.drawable.ic_schedule_black_48dp);
          } else {
            entryDB.deleteEntry(entry);
            viewHolder.schedule.setImageResource(R.drawable.ic_schedule_white_48dp);
          }
          break;
      }
    }

  }

}

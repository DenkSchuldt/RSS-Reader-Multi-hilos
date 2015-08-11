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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by Denny on 10/8/2015.
 */
public class EntryActivity extends AppCompatActivity {

  private Menu menu;
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
    setToolbar();
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateContent();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    if (entry.getImgurl().isEmpty())
      inflater.inflate(R.menu.menu_entry_no_image, menu);
    else
      inflater.inflate(R.menu.menu_entry, menu);
    this.menu = menu;
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_save:
        entry.setScheduled(!entry.isScheduled());
        if (entry.isScheduled()) {
          entryDB.scheduleEntry(entry);
          if (entry.getImgurl().isEmpty())
            menu.getItem(1).setIcon(R.drawable.ic_bookmark_black_24dp);
          else
            menu.getItem(1).setIcon(R.drawable.ic_bookmark_white_24dp);
        } else {
          entryDB.deleteEntry(entry);
          if (entry.getImgurl().isEmpty())
            menu.getItem(1).setIcon(R.drawable.ic_bookmark_border_black_24dp);
          else
            menu.getItem(1).setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
        break;
      case R.id.menu_item_share:
        System.out.println("SHARE");
      default:
    }
    return super.onOptionsItemSelected(item);
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
  }

  /**
   *
   */
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
   */
  public void setToolbar() {
    setSupportActionBar(viewHolder.toolbar);
    getSupportActionBar().setTitle("");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    if (entry.getImgurl().isEmpty())
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
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

    public Toolbar toolbar;
    public TextView title;
    public ImageView img;
    public RelativeLayout header;
    public TextView sourceName;
    public TextView description;
    public TextView time;
    public Button more;

    public void findActivityViews() {
      if (!entry.getImgurl().isEmpty()) {
        img = (ImageView) findViewById(R.id.img);
      }
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      header = (RelativeLayout) findViewById(R.id.header);
      title = (TextView) findViewById(R.id.title);
      sourceName = (TextView) findViewById(R.id.source_name);
      description = (TextView) findViewById(R.id.description);
      time = (TextView) findViewById(R.id.time);
      more = (Button) findViewById(R.id.more);
      more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.more:
          Intent web = new Intent(EntryActivity.this, WebActivity.class);
          web.putExtra(Constants.ENTRY, entry);
          startActivity(web);
          break;
      }
    }

  }

}

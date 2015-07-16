package com.sistemasoperativos.denny.rssreader.views;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.sistemasoperativos.denny.rssreader.database.db.ProducerDB;
import com.sistemasoperativos.denny.rssreader.dialogfragments.EntryDialogFragment;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

  public static final String TAG = "MainActivity";

  private ViewHolder viewHolder;
  private ActionBarDrawerToggle drawerToggle;
  private ProducerDB producerDB;
  private EntryDB entryDB;

  private boolean running;
  private Thread consumer;
  private ArrayList<Producer> producers;
  private ArrayList<Entry> entries;

  public int FETCH_TIME;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    FETCH_TIME = readFromSharedPreferences();
    setContentView(R.layout.activity_main);

    DBHelper helper = OpenHelperManager.getHelper(MainActivity.this, DBHelper.class);
    entryDB = new EntryDB(helper);
    producerDB = new ProducerDB(helper);
    producers = producerDB.getProducers();
    entries = new ArrayList<>();
    running = true;

    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();
    viewHolder.populateLists();
    viewHolder.setCustomDrawerToggle();
    viewHolder.setCustomActionBar();
    viewHolder.setCustomStatusBar();

    checkEmptyList();

    startProducers();
    startConsumer();

  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (drawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    switch (item.getItemId()) {
      case R.id.menu_item_refresh:
        viewHolder.refreshAnimation();
        running = false;
        for (Producer producer: producers) {
          producer.setRunning(running);
          producer.interrupt();
        }
        producers.clear();
        consumer = null;
        producers = producerDB.getProducers();
        running = true;
        startProducers();
        startConsumer();
        break;
      case R.id.menu_item_scheduled:
        Intent scheduled = new Intent(this, ScheduledActivity.class);
        startActivity(scheduled);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        return true;
      case R.id.menu_item_settings:
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        return true;
      default:
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public void startProducers() {
    for (Producer producer: producers) {
      if (producer.isActive()) {
        producer.setEntryDB(entryDB);
        producer.setFetchTime(FETCH_TIME);
        producer.setRunning(running);
        producer.start();
      }
    }
  }

  public void checkEmptyList() {
    if (entryDB.getEntries().isEmpty()) {
      viewHolder.emptyMain.setVisibility(View.VISIBLE);
    } else {
      viewHolder.emptyMain.setVisibility(View.GONE);
    }
  }

  public void startConsumer() {
    consumer = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("STARTED CONSUMER: " + consumer.getId());
        while (running) {
          final ArrayList<Entry> received = entryDB.getEntries();
          for (final Entry entry : received) {
            if (!entry.isEmpty() && !entryExits(entry)) {
              entries.add(entry);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  viewHolder.createCard(entry);
                }
              });
            }
          }
        }
        System.out.println("TERMINATED CONSUMER");
      }
    });
    consumer.start();
  }

  public boolean entryExits(Entry entry) {
    for (Entry e : entries) {
      if (e.getTitle().equals(entry.getTitle()))
        return true;
    }
    return false;
  }

  public int readFromSharedPreferences() {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    int time = sharedPref.getInt(getString(R.string.shared_preferences_settings_time), 1);
    System.out.println("TIME: " + time);
    return time;
  }

  /**
   *
   */
  private class ViewHolder {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private LinearLayout entries;
    private LinearLayout emptyMain;
    private LinearLayout drawerListNoticias;
    private LinearLayout drawerListOpinion;
    private LinearLayout drawerListDeportes;
    private LinearLayout drawerListVidayEstilo;
    private ObjectAnimator objectAnimator;

    public void findActivityViews() {
      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      toolbar = (Toolbar) findViewById(R.id.main_toolbar);
      entries = (LinearLayout) findViewById(R.id.main_entries);
      emptyMain = (LinearLayout) findViewById(R.id.empty_main);
      drawerListNoticias = (LinearLayout) findViewById(R.id.drawer_list_noticias);
      drawerListOpinion = (LinearLayout) findViewById(R.id.drawer_list_opinion);
      drawerListDeportes = (LinearLayout) findViewById(R.id.drawer_list_deportes);
      drawerListVidayEstilo = (LinearLayout) findViewById(R.id.drawer_list_vida_y_estilo);
    }

    public void populateLists() {
      int count = 0;
      for (Producer producer : producers) {
        final View root = getLayoutInflater().inflate(
            R.layout.list_singleline_avatar_text_icon,
            drawerListNoticias,
            false);
        root.setId(0);
        TextView item_text = (TextView) root.findViewById(R.id.item_text);
        final ImageView item_action = (ImageView) root.findViewById(R.id.item_action);

        item_text.setText(producer.getProducerName());
        if (producer.isActive())
          item_action.setImageResource(R.drawable.ic_remove_black_24dp);
        else
          item_action.setImageResource(R.drawable.ic_add_black_24dp);
        root.setTag(producer);
        final int position = count;
        root.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Producer producer = (Producer) v.getTag();
            ImageView item_action = (ImageView) v.findViewById(R.id.item_action);
            producer.setActive(!producer.isActive());
            if (producer.isActive()) {
              item_action.setImageResource(R.drawable.ic_remove_black_24dp);
              producer.setEntryDB(entryDB);
              producer.setFetchTime(FETCH_TIME);
              producer.setRunning(true);
              producer.start();
              producerDB.saveProducer(producer);
              viewHolder.emptyMain.setVisibility(View.GONE);
            } else {
              producer.setRunning(false);
              producer.interrupt();
              producerDB.saveProducer(producer);
              producer = producerDB.getProducers().get(position);
              producers.set(position, producer);
              root.setTag(producer);
              item_action.setImageResource(R.drawable.ic_add_black_24dp);
            }
          }
        });

        switch (producer.getProducerType()) {
          case Constants.NOTICIAS:
            drawerListNoticias.addView(root);
            break;
          case Constants.OPINION:
            drawerListOpinion.addView(root);
            break;
          case Constants.DEPORTES:
            drawerListDeportes.addView(root);
            break;
          case Constants.VIDA_Y_ESTILO:
            drawerListVidayEstilo.addView(root);
            break;
        }
        count += 1;
      }
    }

    public void setCustomDrawerToggle() {
      drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, 0, 0) {
        public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
        public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        public void onDrawerSlide(View drawerView, float slideOffset) {
          super.onDrawerSlide(drawerView, 0);
        }
      };
      drawerToggle.setDrawerIndicatorEnabled(true);
      drawerLayout.setDrawerListener(drawerToggle);
    }

    public void setCustomActionBar() {
      setSupportActionBar(viewHolder.toolbar);
      getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_title_main));
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setCustomStatusBar() {
      getWindow().setFlags(
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
          WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
      );
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
      SystemBarTintManager tintManager = new SystemBarTintManager(MainActivity.this);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setTintColor(Color.TRANSPARENT);
    }

    public void refreshAnimation() {
      int refreshTime = 750;
      objectAnimator = ObjectAnimator.ofFloat(findViewById(R.id.menu_item_refresh), "rotation", 0.0f, 360f);
      objectAnimator.setDuration(refreshTime);
      objectAnimator.setRepeatCount(2);
      objectAnimator.start();
    }

    public void createCard(Entry entry) {

      View card = getLayoutInflater().inflate(R.layout.entry, entries, false);
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
        Picasso.with(MainActivity.this).load(entry.getImgurl()).into(media);
      } else {
        media.setVisibility(View.GONE);
      }

      card.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Entry entry = (Entry) v.getTag();
          EntryDialogFragment edf = EntryDialogFragment.newInstance(entry);
          edf.show(getSupportFragmentManager(), "");
        }
      });

      if (entries.getChildCount()>0)
        entries.addView(card, 0);
      else
        entries.addView(card);
      Log.d(TAG, "CARD created with title:  " + entry.getTitle());
    }

  }

}

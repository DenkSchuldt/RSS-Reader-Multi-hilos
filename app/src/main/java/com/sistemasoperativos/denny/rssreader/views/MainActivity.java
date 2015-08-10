
package com.sistemasoperativos.denny.rssreader.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.database.db.EntryDB;
import com.sistemasoperativos.denny.rssreader.database.db.ProducerDB;
import com.sistemasoperativos.denny.rssreader.fragments.EntriesFragment;
import com.sistemasoperativos.denny.rssreader.interfaces.OnEntryEvent;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;
import com.sistemasoperativos.denny.rssreader.utils.SlidingTabLayout;
import com.sistemasoperativos.denny.rssreader.views.adapters.ViewPagerAdapter;

import java.util.ArrayList;


public class MainActivity extends Base implements OnEntryEvent {

  private EntryDB entryDB;
  private ProducerDB producerDB;

  private ViewHolder viewHolder;
  private ViewPagerAdapter adapter;
  private ObjectAnimator objectAnimator;
  private ActionBarDrawerToggle drawerToggle;

  private boolean running;
  private Thread consumer;
  private ArrayList<Producer> producers;

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
    running = true;

    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();

    populateNavigationDrawer();
    setCustomDrawerToggle();
    setToolbar();
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
        refreshAnimation();
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
      case R.id.menu_item_settings:
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
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

  @Override
  public void updateContent(String tag) {
    if (tag.equals(Constants.SAVED_NEWS)) {
      startProducers();
      startConsumer();
    }
  }

  public void startProducers() {
    for (Producer producer: producers) {
      if (producer.isActive() && !producer.isAlive()) {
        producer.setEntryDB(entryDB);
        producer.setFetchTime(FETCH_TIME);
        producer.setRunning(running);
        producer.start();
      }
    }
  }

  /*public void checkEmptyList() {
    if (entryDB.getEntries().isEmpty()) {
      viewHolder.emptyMain.setVisibility(View.VISIBLE);
    } else {
      viewHolder.emptyMain.setVisibility(View.GONE);
    }
  }*/

  public void startConsumer() {
    consumer = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("STARTED CONSUMER: " + consumer.getId());
        while (running) {
          final ArrayList<Entry> received = entryDB.getEntries();
          for (final Entry entry : received) {
            if (!entry.isEmpty()) {
              if (entry.isScheduled()) {
                EntriesFragment entriesFragment = adapter.getFragments().get(1);
                entriesFragment.updateContent(entry);
              } else {
                EntriesFragment entriesFragment = adapter.getFragments().get(0);
                entriesFragment.updateContent(entry);
              }
            }
          }
        }
        System.out.println("TERMINATED CONSUMER");
      }
    });
    consumer.start();
  }

  public void populateNavigationDrawer() {
    int count = 0;
    for (Producer producer : producers) {
      final View root = getLayoutInflater().inflate(
          R.layout.list_singleline_avatar_text_icon,
          viewHolder.drawerListNoticias,
          false);
      ImageView item_icon = (ImageView) root.findViewById(R.id.item_icon);
      TextView item_text = (TextView) root.findViewById(R.id.item_text);
      final ImageView item_action = (ImageView) root.findViewById(R.id.item_action);

      if (producer.getProducerName().equals(getString(R.string.feed_noticias_migrantes))) {
        item_icon.setImageResource(R.drawable.ic_migrantes_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_entrevistas))) {
        item_icon.setImageResource(R.drawable.ic_entrevistas_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_viva_alborada))) {
        item_icon.setImageResource(R.drawable.ic_viva_alborada_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_viva_norte))) {
        item_icon.setImageResource(R.drawable.ic_viva_norte_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_viva_sambo))) {
        item_icon.setImageResource(R.drawable.ic_viva_sambo_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_politica))) {
        item_icon.setImageResource(R.drawable.ic_politica_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_economia))) {
        item_icon.setImageResource(R.drawable.ic_economia_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_ecuador))) {
        item_icon.setImageResource(R.drawable.ic_ecuador_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_internacional))) {
        item_icon.setImageResource(R.drawable.ic_internacional_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_gran_guayaquil))) {
        item_icon.setImageResource(R.drawable.ic_gran_guayaquil_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_informes))) {
        item_icon.setImageResource(R.drawable.ic_informes_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_seguridad))) {
        item_icon.setImageResource(R.drawable.ic_seguridad_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_noticias_viva))) {
        item_icon.setImageResource(R.drawable.ic_viva_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_opinion_columnistas))) {
        item_icon.setImageResource(R.drawable.ic_columnistas_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_opinion_editoriales))) {
        item_icon.setImageResource(R.drawable.ic_editoriales_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_opinion_cartas_al_director))) {
        item_icon.setImageResource(R.drawable.ic_cartas_al_director_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_opinion_caricaturas))) {
        item_icon.setImageResource(R.drawable.ic_caricaturas_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_opinion_foro_de_lectores))) {
        item_icon.setImageResource(R.drawable.ic_foro_de_lectores_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_deportes_futbol))) {
        item_icon.setImageResource(R.drawable.ic_futbol_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_deportes_campeonato))) {
        item_icon.setImageResource(R.drawable.ic_campeonato_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_deportes_tenis))) {
        item_icon.setImageResource(R.drawable.ic_tenis_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_deportes_otros_deportes))) {
        item_icon.setImageResource(R.drawable.ic_otros_deportes_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_deportes_columnistas_deportes))) {
        item_icon.setImageResource(R.drawable.ic_columnistas_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_intercultural))) {
        item_icon.setImageResource(R.drawable.ic_intercultural_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_tendencias))) {
        item_icon.setImageResource(R.drawable.ic_tendencias_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_tecnologia))) {
        item_icon.setImageResource(R.drawable.ic_tecnologia_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_cultura))) {
        item_icon.setImageResource(R.drawable.ic_cultura_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_ecologia))) {
        item_icon.setImageResource(R.drawable.ic_ecologia_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_cine_y_tv))) {
        item_icon.setImageResource(R.drawable.ic_cine_y_tv_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_musica))) {
        item_icon.setImageResource(R.drawable.ic_musica_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_salud))) {
        item_icon.setImageResource(R.drawable.ic_salud_white_48dp);
      } else if (producer.getProducerName().equals(getString(R.string.feed_vida_y_estilo_gente))) {
        item_icon.setImageResource(R.drawable.ic_gente_white_48dp);
      }

      item_text.setText(producer.getProducerName());
      if (producer.isActive())
        item_action.setImageResource(R.drawable.ic_remove_white_24dp);
      else
        item_action.setImageResource(R.drawable.ic_add_white_24dp);
      root.setTag(producer);
      final int position = count;
      root.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Producer producer = (Producer) v.getTag();
          final ImageView item_action = (ImageView) v.findViewById(R.id.item_action);
          producer.setActive(!producer.isActive());
          if (producer.isActive()) {
            changeDrawerOptionIcon(item_action, true);
            producer.setEntryDB(entryDB);
            producer.setFetchTime(FETCH_TIME);
            producer.setRunning(true);
            producer.start();
            producerDB.saveProducer(producer);
            //viewHolder.emptyMain.setVisibility(View.GONE);
          } else {
            changeDrawerOptionIcon(item_action, false);
            producer.setRunning(false);
            producer.interrupt();
            producerDB.saveProducer(producer);
            producer = producerDB.getProducers().get(position);
            producers.set(position, producer);
            root.setTag(producer);
          }
        }
      });

      switch (producer.getProducerType()) {
        case Constants.NOTICIAS:
          viewHolder.drawerListNoticias.addView(root);
          break;
        case Constants.OPINION:
          viewHolder.drawerListOpinion.addView(root);
          break;
        case Constants.DEPORTES:
          viewHolder.drawerListDeportes.addView(root);
          break;
        case Constants.VIDA_Y_ESTILO:
          viewHolder.drawerListVidayEstilo.addView(root);
          break;
      }
      count += 1;
    }
  }

  public void setCustomDrawerToggle() {
    drawerToggle = new ActionBarDrawerToggle(MainActivity.this, viewHolder.drawerLayout, viewHolder.toolbar, 0, 0) {
      public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
      public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
      public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, 0);
      }
    };
    drawerToggle.setDrawerIndicatorEnabled(true);
    viewHolder.drawerLayout.setDrawerListener(drawerToggle);
  }

  public void setToolbar() {
    setSupportActionBar(viewHolder.toolbar);
    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
  }

  public void refreshAnimation() {
    int refreshTime = 750;
    objectAnimator = ObjectAnimator.ofFloat(findViewById(R.id.menu_item_refresh), "rotation", 0.0f, 360f);
    objectAnimator.setDuration(refreshTime);
    objectAnimator.setRepeatCount(2);
    objectAnimator.start();
  }

  public void changeDrawerOptionIcon(final ImageView imageView, final boolean plus) {
    int refreshTime = 100;
    objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0.0f, 90f);
    objectAnimator.setDuration(refreshTime);
    objectAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (plus) {
          imageView.setImageResource(R.drawable.ic_remove_white_24dp);
        } else {
          imageView.setImageResource(R.drawable.ic_add_white_24dp);
        }
        imageView.setRotation(180f);
      }
    });
    objectAnimator.start();
  }

  public int readFromSharedPreferences() {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    int time = sharedPref.getInt(getString(R.string.shared_preferences_settings_time), 1);
    return time;
  }

  /**
   *
   */
  private class ViewHolder {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SlidingTabLayout tabs;
    private ViewPager pager;
    private LinearLayout drawerListNoticias;
    private LinearLayout drawerListOpinion;
    private LinearLayout drawerListDeportes;
    private LinearLayout drawerListVidayEstilo;

    public void findActivityViews() {
      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      tabs = (SlidingTabLayout) findViewById(R.id.tabs);
      tabs.setDistributeEvenly(true);
      pager = (ViewPager) findViewById(R.id.pager);
      adapter =  new ViewPagerAdapter(getSupportFragmentManager());
      pager.setAdapter(adapter);
      tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
        @Override
        public int getIndicatorColor(int position) {
          return getResources().getColor(R.color.white);
        }
      });
      tabs.setViewPager(pager);
      drawerListNoticias = (LinearLayout) findViewById(R.id.drawer_list_noticias);
      drawerListOpinion = (LinearLayout) findViewById(R.id.drawer_list_opinion);
      drawerListDeportes = (LinearLayout) findViewById(R.id.drawer_list_deportes);
      drawerListVidayEstilo = (LinearLayout) findViewById(R.id.drawer_list_vida_y_estilo);
    }

  }

}

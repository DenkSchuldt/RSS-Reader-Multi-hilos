package com.sistemasoperativos.denny.rssreader.views;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.pkmmte.view.CircularImageView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.database.db.ProducerDB;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

  private ViewHolder viewHolder;
  private ActionBarDrawerToggle drawerToggle;
  private ProducerDB producerDB;

  private ArrayList<Producer> producers;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    DBHelper helper = OpenHelperManager.getHelper(MainActivity.this, DBHelper.class);
    producerDB = new ProducerDB(helper);
    producers = producerDB.getProducers();

    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();
    viewHolder.populateLists();
    viewHolder.setCustomDrawerToggle();
    viewHolder.setCustomActionBar();
    viewHolder.setCustomStatusBar();
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


  /**
   *
   */
  private class ViewHolder {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private LinearLayout drawerListNews;
    private ObjectAnimator objectAnimator;

    public void findActivityViews() {
      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      drawerListNews = (LinearLayout) findViewById(R.id.drawer_list_news);
    }

    public void populateLists() {
      for (Producer producer : producers) {
        View root = getLayoutInflater().inflate(
            R.layout.list_singleline_avatar_text_icon,
            drawerListNews,
            false);
        root.setId(0);
        CircularImageView item_avatar = (CircularImageView) root.findViewById(R.id.item_avatar);
        TextView item_text = (TextView) root.findViewById(R.id.item_text);
        final ImageView item_action = (ImageView) root.findViewById(R.id.item_action);

        switch (producer.getName()) {
          case Constants.ELUNIVERSO:
            item_avatar.setImageResource(R.drawable.eluniverso_logo);
            break;
          case Constants.BBC:
            item_avatar.setImageResource(R.drawable.bbc_logo);
            break;
          case Constants.CNN:
            item_avatar.setImageResource(R.drawable.cnn_logo);
            break;
          case Constants.TELEGRAPH:
            item_avatar.setImageResource(R.drawable.telegraph_logo);
            break;
        }

        item_text.setText(producer.getName());
        if (producer.isActive())
          item_action.setImageResource(R.drawable.ic_remove_black_24dp);
        else
          item_action.setImageResource(R.drawable.ic_add_black_24dp);
        root.setTag(producer);
        root.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Producer producer = (Producer) v.getTag();
            ImageView item_action = (ImageView) v.findViewById(R.id.item_action);
            producer.setActive(!producer.isActive());
            if (producer.isActive())
              item_action.setImageResource(R.drawable.ic_remove_black_24dp);
            else
              item_action.setImageResource(R.drawable.ic_add_black_24dp);
            producerDB.saveProducer(producer);
          }
        });

        switch (producer.getType()) {
          case Constants.NEWS:
            drawerListNews.addView(root);
            break;
        }

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
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.start();
    }
  }

}

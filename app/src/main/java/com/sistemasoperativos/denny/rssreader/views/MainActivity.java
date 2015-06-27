package com.sistemasoperativos.denny.rssreader.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.views.adapters.ProducersAdapter;


public class MainActivity extends AppCompatActivity {

  private ObjectAnimator actionRefresh;
  private ViewHolder viewHolder;
  private ActionBarDrawerToggle drawerToggle;
  private ProducersAdapter producersAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();
    setCustomDrawerToggle();
    setCustomActionBar();
    setCustomStatusBar();
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
      case R.id.action_refresh:
        animateIcon(findViewById(R.id.action_refresh));
        return true;
      case R.id.action_scheduled:
        Intent scheduled = new Intent(this, ScheduledActivity.class);
        startActivity(scheduled);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        return true;
      case R.id.action_settings:
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

  public void setCustomDrawerToggle() {
    drawerToggle = new ActionBarDrawerToggle(
      this,
      viewHolder.drawerLayout,
      viewHolder.toolbar,
      0,
      0) {
        public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
        public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, 0);
      }
    };
    drawerToggle.setDrawerIndicatorEnabled(true);
    viewHolder.drawerLayout.setDrawerListener(drawerToggle);
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
    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setTintColor(Color.TRANSPARENT);
  }

  public void createProducer() {}

  public void deleteProducer() {}

  public void animateIcon(final View view) {
    int refreshTime = 750;
    actionRefresh = ObjectAnimator.ofFloat(view, "rotation", 0.0f, 360f);
    actionRefresh.setDuration(refreshTime);
    actionRefresh.setRepeatCount(ObjectAnimator.INFINITE);
    actionRefresh.start();
  }

  /**
   *
   */
  private class ViewHolder {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ListView news_listview;

    public void findActivityViews() {
      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      news_listview = (ListView) findViewById(R.id.news_list_view);
      producersAdapter = new ProducersAdapter(
        MainActivity.this,
        getResources().getStringArray(R.array.drawer_items_news)
      );
      news_listview.setAdapter(producersAdapter);
      news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          boolean status = (boolean) view.getTag();
          ImageView imageView = (ImageView) view.findViewById(R.id.item_action);
          if(status) {
            imageView.setImageResource(R.drawable.ic_add_black_24dp);
            deleteProducer();
          } else {
            imageView.setImageResource(R.drawable.ic_remove_black_24dp);
            createProducer();
          }
          view.setTag(!status);
        }
      });
    }

  }

}

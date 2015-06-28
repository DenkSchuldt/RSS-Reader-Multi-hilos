package com.sistemasoperativos.denny.rssreader.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.OnSettingsEvent;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.dialogfragments.FetchDialogFragment;

/**
 * Created by denny on 27/06/15.
 */
public class SettingsActivity extends AppCompatActivity implements OnSettingsEvent {

  public static final String SETTINGS_FETCH_TIME = "settingsFetchTime";

  private ViewHolder viewHolder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    viewHolder = new ViewHolder();
    viewHolder.findActivityViews();
    setCustomActionBar();
    setCustomStatusBar();
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

  @Override
  public void onSettingsFetchTime() {
    updateFetchTime();
  }

  public void setCustomActionBar() {
    setSupportActionBar(viewHolder.toolbar);
    getSupportActionBar().setTitle(getResources().getString(R.string.actionbar_title_settings));
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

  public void updateFetchTime() {
    int time = readFromSharedPreferences();
    if (time == 1)
      viewHolder.fetchTime.setText(time + " " + getResources().getString(R.string.settings_time_unit_singular));
    else
      viewHolder.fetchTime.setText(time + " " + getResources().getString(R.string.settings_time_unit_plural));
  }

  public int readFromSharedPreferences() {
    SharedPreferences sharedPref = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);
    int time = sharedPref.getInt(getString(R.string.shared_preferences_settings_time), 5);
    return time;
  }

  /**
  *
  */
  private class ViewHolder implements View.OnClickListener{

    private Toolbar toolbar;
    private LinearLayout fetchContentTime;
    private TextView fetchTime;

    public void findActivityViews() {
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      fetchContentTime = (LinearLayout) findViewById(R.id.settings_fetch_content_time);
      fetchContentTime.setOnClickListener(this);
      fetchTime = (TextView) findViewById(R.id.settings_fetch_time);
      updateFetchTime();
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.settings_fetch_content_time:
          FetchDialogFragment fdf = FetchDialogFragment.newInstance(readFromSharedPreferences());
          fdf.show(getSupportFragmentManager(), "");
          break;
      }
    }

  }

}

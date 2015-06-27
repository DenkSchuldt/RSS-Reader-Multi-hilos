package com.sistemasoperativos.denny.rssreader.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.R;

/**
 * Created by denny on 27/06/15.
 */
public class SettingsActivity extends AppCompatActivity {

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

  /**
  *
  */
  private class ViewHolder {

    private Toolbar toolbar;

    public void findActivityViews() {
      toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
  }

}

package com.sistemasoperativos.denny.rssreader.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sistemasoperativos.denny.rssreader.R;

/**
 * Created by Denny on 8/8/2015.
 */
public class Base extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setCustomStatusBar();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
  }

  public void setCustomStatusBar() {
    getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    );
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    SystemBarTintManager tintManager = new SystemBarTintManager(Base.this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setTintColor(getResources().getColor(R.color.primary_dark));
  }

}

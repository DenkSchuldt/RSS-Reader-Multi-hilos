package com.sistemasoperativos.denny.rssreader.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.sistemasoperativos.denny.rssreader.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by denny on 6/11/15.
 */
public class SplashActivity extends Activity {

  private static final int SPLASH_DELAY = 3000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_splash);
    splash();
  }

  public void splash() {
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
      }
    };
    Timer timer = new Timer();
    timer.schedule(task, SPLASH_DELAY);
  }

}

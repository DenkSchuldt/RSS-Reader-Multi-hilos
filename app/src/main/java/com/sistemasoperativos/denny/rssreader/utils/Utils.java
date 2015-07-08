package com.sistemasoperativos.denny.rssreader.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by denny on 07/07/15.
 */
public class Utils {

  public static Point getDisplaySize(Activity activity) {
    Display display = activity.getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size;
  }

  public static boolean orientationIsPortrait(Activity activity) {
    return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
  }

  public static boolean orientationIsLandscape(Activity activity) {
    return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
  }

}

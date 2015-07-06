package com.sistemasoperativos.denny.rssreader.network;

import android.os.AsyncTask;

import com.sistemasoperativos.denny.rssreader.models.Feed;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by denny on 05/07/15.
 */
public class GetFeeds {

  OkHttpClient client = new OkHttpClient();

  public String getFeeds(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = client.newCall(request).execute();
    return response.body().string();
  }

}

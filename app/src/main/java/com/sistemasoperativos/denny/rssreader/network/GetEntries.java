package com.sistemasoperativos.denny.rssreader.network;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by denny on 05/07/15.
 */
public class GetEntries {

  OkHttpClient client = new OkHttpClient();

  public String getEntries(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = client.newCall(request).execute();
    return response.body().string();
  }

}

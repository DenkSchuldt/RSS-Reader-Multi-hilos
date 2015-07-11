package com.sistemasoperativos.denny.rssreader.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

/**
 * Created by denny on 07/07/15.
 */
public class WebActivity extends AppCompatActivity {

  private Entry entry;
  private ViewHolder viewHolder;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    entry = getEntryFromIntent();
    setContentView(R.layout.activity_web);
    viewHolder = new ViewHolder();
    viewHolder.findViews();
    updateContent();
  }

  /**
   *
   */
  public Entry getEntryFromIntent() {
    return (Entry) getIntent().getSerializableExtra(Constants.ENTRY);
  }

  /**
   *
   */
  public void updateContent() {
    viewHolder.title.setText(entry.getTitle());
    initWeb();
  }

  /**
   *
   */
  public void initWeb() {
    viewHolder.web.getSettings().setJavaScriptEnabled(true);
    final Activity activity = this;
    viewHolder.web.setWebViewClient(new WebViewClient() {
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
      }
    });
    viewHolder.web.loadUrl(entry.getUrl());
  }

  /**
   *
   */
  public class ViewHolder implements View.OnClickListener {

    public ImageView close;
    public TextView title;
    public WebView web;

    public void findViews() {
      close = (ImageView) findViewById(R.id.web_close);
      title = (TextView) findViewById(R.id.web_title);
      web = (WebView) findViewById(R.id.web);

      close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      finish();
    }
  }

}

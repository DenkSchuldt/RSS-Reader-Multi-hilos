package com.sistemasoperativos.denny.rssreader.models;

import com.j256.ormlite.field.DatabaseField;
import com.sistemasoperativos.denny.rssreader.views.SettingsActivity;

import java.io.Serializable;

/**
 * Created by denny on 6/11/15.
 */
public class Entry implements Serializable {

  public static final String ID = "_id";
  public static final String SCHEDULED = "scheduled";
  public static final String TITLE = "title";
  public static final String URL = "url";
  public static final String DESCRIPTION = "description";
  public static final String IMGURL = "imgurl";
  public static final String VIDEOURL = "videourl";
  public static final String PUBDATE = "pubdate";
  public static final String SOURCE = "source";

  @DatabaseField(generatedId = true, columnName = ID)
  private int id;
  @DatabaseField(columnName = SCHEDULED)
  private boolean scheduled;
  @DatabaseField(columnName = TITLE)
  private String title;
  @DatabaseField(columnName = URL)
  private String url;
  @DatabaseField(columnName = DESCRIPTION)
  private String description;
  @DatabaseField(columnName = IMGURL)
  private String imgurl;
  @DatabaseField(columnName = VIDEOURL)
  private String videourl;
  @DatabaseField(columnName = PUBDATE)
  private String pubDate;
  @DatabaseField(columnName = SOURCE)
  private String source;

  public Entry() {
    this.id = 0;
    this.scheduled =false;
    this.title = "";
    this.url = "";
    this.description = "";
    this.imgurl = "";
    this.videourl = "";
    this.pubDate = "";
    this.source = "";
  }

  public Entry(
      boolean scheduled,
      String title,
      String url,
      String description,
      String imgurl,
      String videourl,
      String pubDate,
      String source) {
    this.scheduled = scheduled;
    this.title = title;
    this.url = url;
    this.description = description;
    this.imgurl = imgurl;
    this.videourl = videourl;
    this.pubDate = pubDate;
    this.source = source;
  }

  public Entry(
      int id,
      boolean scheduled,
      String title,
      String url,
      String description,
      String imgurl,
      String videourl,
      String pubDate,
      String source) {
    this.id = id;
    this.scheduled = scheduled;
    this.title = title;
    this.url = url;
    this.description = description;
    this.imgurl = imgurl;
    this.videourl = videourl;
    this.pubDate = pubDate;
    this.source = source;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public boolean isScheduled() {
    return scheduled;
  }
  public void setScheduled(boolean scheduled) {
    this.scheduled = scheduled;
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getImgurl() {
    return imgurl;
  }
  public void setImgurl(String imgurl) {
    this.imgurl = imgurl;
  }

  public String getVideourl() {
    return videourl;
  }
  public void setVideourl(String videourl) {
    this.videourl = videourl;
  }

  public String getPubDate() {
    return pubDate;
  }
  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }

  public String getSource() {
    return source;
  }
  public void setSource(String source) {
    this.source = source;
  }

  public boolean isEmpty () {
    return title.isEmpty() &&
        url.isEmpty() &&
        description.isEmpty();
  }

  @Override
  public String toString() {
    return "Entry: {" + "\n" +
        "title: " + title + "\n" +
        "URL: " + url + "\n" +
        "description: " + description + "\n" +
        "ImgUrl: " + imgurl + "\n" +
        "PubDate: " + pubDate + "\n" +
        "Source: " + source + "\n" +
        "Scheduled: " + scheduled + "\n" +
        "}";
  }
}

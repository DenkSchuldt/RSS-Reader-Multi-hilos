package com.sistemasoperativos.denny.rssreader.models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by denny on 6/11/15.
 */
public class Feed {

  private static final String ID = "_id";
  private static final String SCHEDULED = "scheduled";
  private static final String TITLE = "title";
  private static final String URL = "url";
  private static final String DESCRIPTION = "description";
  private static final String IMGURL = "imgurl";
  private static final String VIDEOURL = "videourl";
  private static final String PUBDATE = "pubdate";

  @DatabaseField(generatedId = true, columnName = ID)
  private int id;
  @DatabaseField(columnName = SCHEDULED)
  private int scheduled;
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

  public Feed() {
    this.id = 0;
    this.scheduled = 0;
    this.title = "";
    this.url = "";
    this.description = "";
    this.imgurl = "";
    this.videourl = "";
    this.pubDate = "";
  }

  public Feed(
      int scheduled,
      String title,
      String url,
      String description,
      String imgurl,
      String videourl,
      String pubDate) {
    this.scheduled = scheduled;
    this.title = title;
    this.url = url;
    this.description = description;
    this.imgurl = imgurl;
    this.videourl = videourl;
    this.pubDate = pubDate;
  }

  public Feed(
      int id,
      int scheduled,
      String title,
      String url,
      String description,
      String imgurl,
      String videourl,
      String pubDate) {
    this.id = id;
    this.scheduled = scheduled;
    this.title = title;
    this.url = url;
    this.description = description;
    this.imgurl = imgurl;
    this.videourl = videourl;
    this.pubDate = pubDate;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public int getScheduled() {
    return scheduled;
  }
  public void setScheduled(int scheduled) {
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

}

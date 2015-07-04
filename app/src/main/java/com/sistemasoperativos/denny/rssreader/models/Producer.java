package com.sistemasoperativos.denny.rssreader.models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by denny on 6/11/15.
 */
public class Producer {

  private static final String ID = "_id";
  private static final String URL = "url";
  private static final String TYPE = "type";
  private static final String ACTIVE = "active";

  @DatabaseField(generatedId = true, columnName = ID)
  private int id;
  @DatabaseField(columnName = URL)
  private String url;
  @DatabaseField(columnName = TYPE)
  private int type;
  @DatabaseField(columnName = ACTIVE)
  private boolean active;

  public Producer() {
    this.id = 0;
    this.url = "";
    this.type = -1;
    this.active = false;
  }

  public Producer(String url, int type, boolean active) {
    this.url = url;
    this.type = type;
    this.active = active;
  }

  public Producer(int id, String url, int type, boolean active) {
    this.id = id;
    this.url = url;
    this.type = type;
    this.active = active;
  }

  public int getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public int getType() {
    return type;
  }

  public boolean isActive() {
    return active;
  }
  public void setActive(boolean active) {
    this.active = active;
  }


  @Override
  public String toString() {
    return "Producer: {\n" +
        "Url: " + this.url + "\n" +
        "Type: " + this.type + "\n" +
        "Active: " + this.active + "\n" +
        "}\n\n";
  }
}

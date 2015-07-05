package com.sistemasoperativos.denny.rssreader.models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by denny on 6/11/15.
 */
public class Producer {

  public static final String ID = "_id";
  public static final String URL = "url";
  public static final String NAME = "name";
  public static final String TYPE = "type";
  public static final String ACTIVE = "active";

  @DatabaseField(generatedId = true, columnName = ID)
  private int id;
  @DatabaseField(columnName = URL)
  private String url;
  @DatabaseField(columnName = NAME)
  private String name;
  @DatabaseField(columnName = TYPE)
  private String type;
  @DatabaseField(columnName = ACTIVE)
  private boolean active;

  public Producer() {
    this.id = 0;
    this.url = "";
    this.name = "";
    this.type = "";
    this.active = false;
  }

  public Producer(String url, String name, String type, boolean active) {
    this.url = url;
    this.name = name;
    this.type = type;
    this.active = active;
  }

  public Producer(int id, String url, String name, String type, boolean active) {
    this.id = id;
    this.url = url;
    this.name = name;
    this.type = type;
    this.active = active;
  }

  public int getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public String getName() {
    return name;
  }

  public String getType() {
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
        "Name: " + this.name + "\n" +
        "Type: " + this.type + "\n" +
        "Active: " + this.active + "\n" +
        "}\n\n";
  }
}

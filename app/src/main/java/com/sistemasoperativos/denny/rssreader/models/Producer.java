package com.sistemasoperativos.denny.rssreader.models;

import android.os.DropBoxManager;

import com.j256.ormlite.field.DatabaseField;
import com.sistemasoperativos.denny.rssreader.database.db.EntryDB;
import com.sistemasoperativos.denny.rssreader.network.GetEntries;
import com.sistemasoperativos.denny.rssreader.parsers.BBCParser;
import com.sistemasoperativos.denny.rssreader.parsers.ElUniversoParser;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by denny on 6/11/15.
 */
public class Producer extends Thread {

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

  private int fetchTime;
  private EntryDB entryDB;

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

  public int getProducerId() {
    return id;
  }

  public String getProducerUrl() {
    return url;
  }

  public String getProducerName() {
    return name;
  }

  public String getProducerType() {
    return type;
  }


  public void setFetchTime(int fetchTime) {
    this.fetchTime = fetchTime;
  }

  public void setEntryDB(EntryDB entryDB) {
    this.entryDB = entryDB;
  }

  public boolean isActive() {
    return active;
  }
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public void run() {
    super.run();
    ArrayList<Entry> entries = new ArrayList<>();
    try {
      GetEntries get = new GetEntries();
      String xml = get.getEntries(url);
      InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
      switch (name) {
        case Constants.ELUNIVERSO:
          entries = new ElUniversoParser().parse(is);
          break;
        case Constants.BBC:
          entries = new BBCParser().parse(is);
          break;
        case Constants.CNN:
          break;
        case Constants.TELEGRAPH:
          break;
      }
      for (Entry entry : entries) {
        entryDB.saveEntry(entry);
      }
      int sleep = fetchTime * 1000 * 60;
      Thread.sleep(sleep);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
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

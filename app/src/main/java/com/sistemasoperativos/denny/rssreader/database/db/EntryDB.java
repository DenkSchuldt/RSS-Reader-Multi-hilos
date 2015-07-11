package com.sistemasoperativos.denny.rssreader.database.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.models.Entry;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by denny on 04/07/15.
 */
public class EntryDB {

  private static final String TAG = "EntryDB";
  private Dao<Entry, Integer> entryDao;

  public EntryDB(DBHelper dbHelper) {
    try {
      this.entryDao = dbHelper.getEntryDao();
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get EntryDao.");
      e.printStackTrace();
    }
  }

  public boolean saveEntry(Entry entry) {
    try {
      entryDao.createOrUpdate(entry);
      Log.d(TAG, "SAVED Entry: " + entry);
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not save Entry: " + entry);
      e.printStackTrace();
    }
    return false;
  }

  public boolean deleteEntry(Entry entry) {
    try {
      entryDao.delete(entry);
      Log.d(TAG, "DELETED Entry: " + entry);
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not delete Entry: " + entry);
      e.printStackTrace();
    }
    return false;
  }

  public ArrayList<Entry> getEntries() {
    try {
      return new ArrayList<>(entryDao.queryForAll());
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get Entries");
      e.printStackTrace();
    }
    return new ArrayList<Entry>();
  }

  public ArrayList<Entry> getScheduledEntries() {
    ArrayList<Entry> entries = new ArrayList<>();
    QueryBuilder b = entryDao.queryBuilder();
    try {
      b.where().eq(Entry.SCHEDULED, Boolean.TRUE);
      entries.addAll(entryDao.query(b.prepare()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return entries;
  }

}

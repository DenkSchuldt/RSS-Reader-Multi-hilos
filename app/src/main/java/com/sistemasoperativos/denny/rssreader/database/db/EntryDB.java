package com.sistemasoperativos.denny.rssreader.database.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.models.Entry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

  public synchronized boolean saveEntry(Entry entry) {
    try {
      entryDao.createOrUpdate(entry);
      Log.d(TAG, "SAVED Entry: " + entry.getTitle());
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not save Entry: " + entry.getTitle());
      e.printStackTrace();
    }
    return false;
  }

  public synchronized boolean deleteEntry(Entry entry) {
    try {
      entryDao.delete(entry);
      Log.d(TAG, "DELETED Entry: " + entry.getTitle());
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not delete Entry: " + entry.getTitle());
      e.printStackTrace();
    }
    return false;
  }

  public synchronized Entry getEntry() {
    Entry entry = new Entry();
    try {
      List<Entry> entries = entryDao.query(entryDao.queryBuilder().orderBy(Entry.ID, false).limit(1L).prepare());
        if(!entries.isEmpty())
          entry = entries.get(0);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return entry;
  }

  public synchronized ArrayList<Entry> getEntries() {
    ArrayList<Entry> entries = new ArrayList<>();
    try {
      entries.addAll(entryDao.queryForAll());
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get Entries");
      e.printStackTrace();
    }
    return entries;
  }

  public synchronized void deleteEntries(ArrayList<Entry> entries) {
    try {
      for (Entry entry : entries) {
        entryDao.delete(entry);
        System.out.println("Deleted entry: " + entry.getTitle());
      }
    }catch (SQLException e) {
      e.printStackTrace();
      Log.d(TAG, "ERROR: Could not get delete Entries");
    }
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

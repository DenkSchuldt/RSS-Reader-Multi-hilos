package com.sistemasoperativos.denny.rssreader.database.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.models.Feed;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by denny on 04/07/15.
 */
public class FeedDB {

  private static final String TAG = "FeedDB";
  private Dao<Feed, Integer> feedDao;

  public FeedDB(DBHelper dbHelper) {
    try {
      this.feedDao = dbHelper.getFeedDao();
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get FeedDao.");
      e.printStackTrace();
    }
  }

  public boolean saveFeed(Feed feed) {
    try {
      feedDao.createOrUpdate(feed);
      Log.d(TAG, "SAVED Feed: " + feed);
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not save Feed: " + feed);
      e.printStackTrace();
    }
    return false;
  }

  public boolean deleteFeed(Feed feed) {
    try {
      feedDao.delete(feed);
      Log.d(TAG, "DELETED Feed: " + feed);
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not delete Feed: " + feed);
      e.printStackTrace();
    }
    return false;
  }

  public ArrayList<Feed> getFeeds() {
    try {
      return new ArrayList<>(feedDao.queryForAll());
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get Feeds");
      e.printStackTrace();
    }
    return new ArrayList<Feed>();
  }

  public ArrayList<Feed> getScheduledFeeds() {
    ArrayList<Feed> feeds = new ArrayList<>();
    QueryBuilder b = feedDao.queryBuilder();
    try {
      b.where().eq(Feed.SCHEDULED, Boolean.TRUE);
      feeds.addAll(feedDao.query(b.prepare()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return feeds;
  }

}

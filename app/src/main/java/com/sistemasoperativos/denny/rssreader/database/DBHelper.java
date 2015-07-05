package com.sistemasoperativos.denny.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sistemasoperativos.denny.rssreader.models.Feed;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import java.sql.SQLException;

/**
 * Created by denny on 04/07/15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

  private static final String TAG = "DBHelper";

  private static final String DATABASE_NAME = "sistemasoperativos_rssreader.db";
  private static final int DATABASE_VERSION = 1;

  private Dao<Feed, Integer> feedDao;
  private Dao<Producer, Integer> producerDao;

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Feed.class);
      TableUtils.createTable(connectionSource, Producer.class);

      Producer eluniverso = new Producer(
          Constants.ELUNIVERSO_URL,
          Constants.ELUNIVERSO,
          false);
      Producer bbc = new Producer(
          Constants.BBC_URL,
          Constants.BBC,
          false);
      Producer cnn = new Producer(
          Constants.CNN_URL,
          Constants.CNN,
          false);
      Producer telegraph = new Producer(
          Constants.TELEGRAPH_URL,
          Constants.TELEGRAPH,
          false);
      producerDao.createOrUpdate(eluniverso);
      producerDao.createOrUpdate(bbc);
      producerDao.createOrUpdate(cnn);
      producerDao.createOrUpdate(telegraph);

    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not create Database " + DATABASE_NAME);
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    onCreate(db, connectionSource);
  }

  public Dao<Feed, Integer> getFeedDao() throws SQLException {
    if (feedDao == null) {
      feedDao = getDao(Feed.class);
    }
    return feedDao;
  }

  public Dao<Producer, Integer> getProducerDao() throws SQLException {
    if (producerDao == null) {
      producerDao = getDao(Producer.class);
    }
    return producerDao;
  }

  @Override
  public void close() {
    super.close();
    feedDao = null;
    producerDao = null;
  }

}

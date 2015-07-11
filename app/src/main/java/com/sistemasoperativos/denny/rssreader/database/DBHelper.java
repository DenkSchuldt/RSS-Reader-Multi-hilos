package com.sistemasoperativos.denny.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sistemasoperativos.denny.rssreader.models.Entry;
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

  private Dao<Entry, Integer> entryDao;
  private Dao<Producer, Integer> producerDao;

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Entry.class);
      TableUtils.createTable(connectionSource, Producer.class);

      Producer eluniverso = new Producer(
          Constants.ELUNIVERSO_URL,
          Constants.ELUNIVERSO,
          Constants.NEWS,
          false);
      Producer bbc = new Producer(
          Constants.BBC_URL,
          Constants.BBC,
          Constants.NEWS,
          false);
      Producer cnn = new Producer(
          Constants.CNN_URL,
          Constants.CNN,
          Constants.NEWS,
          false);
      Producer telegraph = new Producer(
          Constants.TELEGRAPH_URL,
          Constants.TELEGRAPH,
          Constants.NEWS,
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

  public Dao<Entry, Integer> getEntryDao() throws SQLException {
    if (entryDao == null) {
      entryDao = getDao(Entry.class);
    }
    return entryDao;
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
    entryDao = null;
    producerDao = null;
  }

}

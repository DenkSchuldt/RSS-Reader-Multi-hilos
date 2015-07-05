package com.sistemasoperativos.denny.rssreader.database.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.sistemasoperativos.denny.rssreader.database.DBHelper;
import com.sistemasoperativos.denny.rssreader.models.Producer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denny on 04/07/15.
 */
public class ProducerDB {

  private static final String TAG = "ProducerDB";
  private Dao<Producer, Integer> producerDao;

  public ProducerDB(DBHelper dbHelper) {
    try {
      this.producerDao = dbHelper.getProducerDao();
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get ProducerDao.");
      e.printStackTrace();
    }
  }

  public boolean saveProducer(Producer producer) {
    try {
      producerDao.createOrUpdate(producer);
      Log.d(TAG, "SAVED Producer: " + producer);
      return true;
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not save Producer: " + producer);
      e.printStackTrace();
    }
    return false;
  }

  public ArrayList<Producer> getProducers() {
    try {
      return new ArrayList<>(producerDao.queryForAll());
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get Producers");
      e.printStackTrace();
    }
    return new ArrayList<Producer>();
  }

  public ArrayList<Producer> getProducersByType(String type) {
    try {
      QueryBuilder<Producer, Integer> queryBuilder = producerDao.queryBuilder();
      queryBuilder.where().eq(Producer.TYPE, type);
      List<Producer> producersList = queryBuilder.query();
      return new ArrayList<>(producersList);
    } catch (SQLException e) {
      Log.d(TAG, "ERROR: Could not get Producers by type " + type);
      e.printStackTrace();
    }
    return new ArrayList<Producer>();
  }

}

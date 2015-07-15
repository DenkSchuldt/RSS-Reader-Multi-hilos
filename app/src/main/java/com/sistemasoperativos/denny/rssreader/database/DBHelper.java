package com.sistemasoperativos.denny.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sistemasoperativos.denny.rssreader.R;
import com.sistemasoperativos.denny.rssreader.models.Entry;
import com.sistemasoperativos.denny.rssreader.models.Producer;
import com.sistemasoperativos.denny.rssreader.utils.Constants;

import java.sql.SQLException;

/**
 * Created by denny on 04/07/15.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

  private Context context;
  private static final String TAG = "DBHelper";

  private static final String DATABASE_NAME = "sistemasoperativos_rssreader.db";
  private static final int DATABASE_VERSION = 1;

  private Dao<Entry, Integer> feedDao;
  private Dao<Producer, Integer> producerDao;

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Entry.class);
      TableUtils.createTable(connectionSource, Producer.class);

      Producer migrantes = new Producer(
          Constants.MIGRANTES_URL,
          context.getString(R.string.feed_noticias_migrantes),
          Constants.NOTICIAS,
          false);
      Producer entrevistas = new Producer(
          Constants.ENTREVISTAS_URL,
          context.getString(R.string.feed_noticias_entrevistas),
          Constants.NOTICIAS,
          false);
      Producer vivaAlborada = new Producer(
          Constants.VIVA_ALBORADA_URL,
          context.getString(R.string.feed_noticias_viva_alborada),
          Constants.NOTICIAS,
          false);
      Producer vivaNorte = new Producer(
          Constants.VIVA_NORTE_URL,
          context.getString(R.string.feed_noticias_viva_norte),
          Constants.NOTICIAS,
          false);
      Producer vivaSambo = new Producer(
          Constants.VIVA_SAMBO_URL,
          context.getString(R.string.feed_noticias_viva_sambo),
          Constants.NOTICIAS,
          false);
      Producer politica = new Producer(
          Constants.POLITICA_URL,
          context.getString(R.string.feed_noticias_politica),
          Constants.NOTICIAS,
          false);
      Producer economia = new Producer(
          Constants.ECONOMIA_URL,
          context.getString(R.string.feed_noticias_economia),
          Constants.NOTICIAS,
          false);
      Producer ecuador = new Producer(
          Constants.ECUADOR_URL,
          context.getString(R.string.feed_noticias_ecuador),
          Constants.NOTICIAS,
          false);
      Producer internacional = new Producer(
          Constants.INTERNACIONAL_URL,
          context.getString(R.string.feed_noticias_internacional),
          Constants.NOTICIAS,
          false);
      Producer granGuayaquil = new Producer(
          Constants.GRAN_GUAYAQUIL_URL,
          context.getString(R.string.feed_noticias_gran_guayaquil),
          Constants.NOTICIAS,
          false);
      Producer informes = new Producer(
          Constants.INFORMES_URL,
          context.getString(R.string.feed_noticias_informes),
          Constants.NOTICIAS,
          false);
      Producer seguridad = new Producer(
          Constants.SEGURIDAD_URL,
          context.getString(R.string.feed_noticias_seguridad),
          Constants.NOTICIAS,
          false);
      Producer viva = new Producer(
          Constants.VIVA_URL,
          context.getString(R.string.feed_noticias_viva),
          Constants.NOTICIAS,
          false);

      Producer columnistas = new Producer(
          Constants.COLUMNISTAS_URL,
          context.getString(R.string.feed_opinion_columnistas),
          Constants.OPINION,
          false);
      Producer editoriales = new Producer(
          Constants.EDITORIALES_URL,
          context.getString(R.string.feed_opinion_editoriales),
          Constants.OPINION,
          false);
      Producer cartasAlDirector = new Producer(
          Constants.CARTAS_AL_DIRECTOR_URL,
          context.getString(R.string.feed_opinion_cartas_al_director),
          Constants.OPINION,
          false);
      Producer caricaturas = new Producer(
          Constants.CARICATURAS_URL,
          context.getString(R.string.feed_opinion_caricaturas),
          Constants.OPINION,
          false);
      Producer foroDeLectores = new Producer(
          Constants.FORO_DE_LECTORES_URL,
          context.getString(R.string.feed_opinion_foro_de_lectores),
          Constants.OPINION,
          false);

      Producer futbol = new Producer(
          Constants.FORO_DE_LECTORES_URL,
          context.getString(R.string.feed_deportes_futbol),
          Constants.DEPORTES,
          false);
      Producer campeonato = new Producer(
          Constants.FORO_DE_LECTORES_URL,
          context.getString(R.string.feed_deportes_campeonato),
          Constants.DEPORTES,
          false);
      Producer tenis = new Producer(
          Constants.FORO_DE_LECTORES_URL,
          context.getString(R.string.feed_deportes_tenis),
          Constants.DEPORTES,
          false);
      Producer otrosDeportes = new Producer(
          Constants.FORO_DE_LECTORES_URL,
          context.getString(R.string.feed_deportes_otros_deportes),
          Constants.DEPORTES,
          false);
      Producer columnistasDeportes = new Producer(
          Constants.FORO_DE_LECTORES_URL,
          context.getString(R.string.feed_deportes_columnistas_deportes),
          Constants.DEPORTES,
          false);

      Producer intercultural = new Producer(
          Constants.INTERCULTURAL_URL,
          context.getString(R.string.feed_vida_y_estilo_intercultural),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer tendencias = new Producer(
          Constants.TENDENCIAS_URL,
          context.getString(R.string.feed_vida_y_estilo_tendencias),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer tecnologia = new Producer(
          Constants.TECNOLOGIA_URL,
          context.getString(R.string.feed_vida_y_estilo_tecnologia),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer cultura = new Producer(
          Constants.CULTURAL_URL,
          context.getString(R.string.feed_vida_y_estilo_cultura),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer ecologia = new Producer(
          Constants.ECOLOGIA_URL,
          context.getString(R.string.feed_vida_y_estilo_ecologia),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer cineyTv = new Producer(
          Constants.CINE_Y_TV_URL,
          context.getString(R.string.feed_vida_y_estilo_cine_y_tv),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer musica = new Producer(
          Constants.MUSICA_URL,
          context.getString(R.string.feed_vida_y_estilo_musica),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer salud = new Producer(
          Constants.SALUD_URL,
          context.getString(R.string.feed_vida_y_estilo_salud),
          Constants.VIDA_Y_ESTILO,
          false);
      Producer gente = new Producer(
          Constants.GENTE_URL,
          context.getString(R.string.feed_vida_y_estilo_gente),
          Constants.VIDA_Y_ESTILO,
          false);

      producerDao.createOrUpdate(migrantes);
      producerDao.createOrUpdate(entrevistas);
      producerDao.createOrUpdate(vivaAlborada);
      producerDao.createOrUpdate(vivaNorte);
      producerDao.createOrUpdate(vivaSambo);
      producerDao.createOrUpdate(politica);
      producerDao.createOrUpdate(economia);
      producerDao.createOrUpdate(ecuador);
      producerDao.createOrUpdate(internacional);
      producerDao.createOrUpdate(granGuayaquil);
      producerDao.createOrUpdate(informes);
      producerDao.createOrUpdate(seguridad);
      producerDao.createOrUpdate(viva);

      producerDao.createOrUpdate(columnistas);
      producerDao.createOrUpdate(editoriales);
      producerDao.createOrUpdate(cartasAlDirector);
      producerDao.createOrUpdate(caricaturas);
      producerDao.createOrUpdate(foroDeLectores);

      producerDao.createOrUpdate(futbol);
      producerDao.createOrUpdate(campeonato);
      producerDao.createOrUpdate(tenis);
      producerDao.createOrUpdate(otrosDeportes);
      producerDao.createOrUpdate(columnistasDeportes);

      producerDao.createOrUpdate(intercultural);
      producerDao.createOrUpdate(tendencias);
      producerDao.createOrUpdate(tecnologia);
      producerDao.createOrUpdate(cultura);
      producerDao.createOrUpdate(ecologia);
      producerDao.createOrUpdate(cineyTv);
      producerDao.createOrUpdate(musica);
      producerDao.createOrUpdate(salud);
      producerDao.createOrUpdate(gente);

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
    if (feedDao == null) {
      feedDao = getDao(Entry.class);
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

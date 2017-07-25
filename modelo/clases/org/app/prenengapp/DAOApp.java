package org.app.prenengapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by dervis on 08/12/16.
 */
public class DAOApp extends Application {

    static ReporteDao reporteDao;

    public ReporteDao getUsuarioDao(){
        return reporteDao;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "preveng", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        reporteDao = daoSession.getReporteDao();
    }
}

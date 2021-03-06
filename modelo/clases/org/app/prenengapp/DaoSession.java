package org.app.prenengapp;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.prenengapp.Reporte;

import org.app.prenengapp.ReporteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig reporteDaoConfig;

    private final ReporteDao reporteDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        reporteDaoConfig = daoConfigMap.get(ReporteDao.class).clone();
        reporteDaoConfig.initIdentityScope(type);

        reporteDao = new ReporteDao(reporteDaoConfig, this);

        registerDao(Reporte.class, reporteDao);
    }
    
    public void clear() {
        reporteDaoConfig.getIdentityScope().clear();
    }

    public ReporteDao getReporteDao() {
        return reporteDao;
    }

}

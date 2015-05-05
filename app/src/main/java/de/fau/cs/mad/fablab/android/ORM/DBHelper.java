package de.fau.cs.mad.fablab.android.ORM;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.fau.cs.mad.fablab.android.R;


/**
 * This Class: creates, upgrades and Manages the Database and the Data Access Object
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ORMTest.db";
    // --> Increment DB_Version-number on changes
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<DBObject, Integer> ORMObjectDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }


    //Create DB
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, DBObject.class);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    //Drop & Create DB
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, DBObject.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }




    public RuntimeExceptionDao<DBObject, Integer> getDBObjectDao() {
        if (ORMObjectDao == null)
            ORMObjectDao = getRuntimeExceptionDao(DBObject.class);
        return ORMObjectDao;
    }

    @Override
    public void close() {
        super.close();
        ORMObjectDao = null;
    }
}
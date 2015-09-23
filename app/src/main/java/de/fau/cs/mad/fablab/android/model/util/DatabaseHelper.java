package de.fau.cs.mad.fablab.android.model.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.fau.cs.mad.fablab.android.model.entities.AutoCompleteWords;
import de.fau.cs.mad.fablab.android.model.entities.Cart;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.core.Product;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "fablab.db";
    private static final int DATABASE_VERSION = 34;

    private static DatabaseHelper instance;
    private RuntimeExceptionDao<Cart, Long> mCartDao;
    private RuntimeExceptionDao<Product, Long> mProductDao;
    private RuntimeExceptionDao<AutoCompleteWords, Long> mAutoCompleteWordsDao;
    private RuntimeExceptionDao<News, Long> mNewsDao;
    private RuntimeExceptionDao<ICal, Long> mICalDao;
    private RuntimeExceptionDao<Project, Long> mProjectDao;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Cart.class);
            TableUtils.createTable(connectionSource, CartEntry.class);
            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, AutoCompleteWords.class);
            TableUtils.createTable(connectionSource, News.class);
            TableUtils.createTable(connectionSource, ICal.class);
            TableUtils.createTable(connectionSource, Project.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Cart.class, true);
            TableUtils.dropTable(connectionSource, CartEntry.class, true);
            TableUtils.dropTable(connectionSource, Product.class, true);
            TableUtils.dropTable(connectionSource, AutoCompleteWords.class, true);
            TableUtils.dropTable(connectionSource, News.class, true);
            TableUtils.dropTable(connectionSource, ICal.class, true);
            TableUtils.dropTable(connectionSource, Project.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't drop database", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<Cart, Long> getCartDao() {
        if (mCartDao == null) {
            mCartDao = getRuntimeExceptionDao(Cart.class);
        }
        return mCartDao;
    }

    public RuntimeExceptionDao<Product, Long> getProductDao() {
        if (mProductDao == null) {
            mProductDao = getRuntimeExceptionDao(Product.class);
        }
        return mProductDao;
    }

    public RuntimeExceptionDao<AutoCompleteWords, Long> getAutoCompleteWordsDao() {
        if (mAutoCompleteWordsDao == null) {
            mAutoCompleteWordsDao = getRuntimeExceptionDao(AutoCompleteWords.class);
        }
        return mAutoCompleteWordsDao;
    }

    public RuntimeExceptionDao<News, Long> getNewsDao()
    {
        if(mNewsDao == null)
        {
            mNewsDao = getRuntimeExceptionDao(News.class);
        }
        return mNewsDao;
    }

    public RuntimeExceptionDao<ICal, Long> getICalDao()
    {
        if(mICalDao == null)
        {
            mICalDao = getRuntimeExceptionDao(ICal.class);
        }
        return mICalDao;
    }

    public RuntimeExceptionDao<Project, Long> getProjectDao()
    {
        if(mProjectDao == null)
        {
            mProjectDao = getRuntimeExceptionDao(Project.class);
        }
        return mProjectDao;
    }

    @Override
    public void close() {
        super.close();
        mAutoCompleteWordsDao = null;
        mCartDao = null;
        mProductDao = null;
        mNewsDao = null;
        mICalDao = null;
        mProjectDao = null;
    }
}

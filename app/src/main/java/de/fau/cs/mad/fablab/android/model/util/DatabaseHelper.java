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
import de.fau.cs.mad.fablab.rest.core.Product;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "fablab.db";
    private static final int DATABASE_VERSION = 27;

    private static DatabaseHelper instance;
    private RuntimeExceptionDao<Cart, Long> mCartDao;
    private RuntimeExceptionDao<Product, String> mProductDao;
    private RuntimeExceptionDao<AutoCompleteWords, Long> mAutoCompleteWordsDao;

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

    public RuntimeExceptionDao<Product, String> getProductDao() {
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

    @Override
    public void close() {
        super.close();
        mAutoCompleteWordsDao = null;
        mCartDao = null;
        mProductDao = null;
    }
}

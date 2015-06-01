package de.fau.cs.mad.fablab.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.productsearch.AutoCompleteWords;
import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "fablab.db";
    private static final int DATABASE_VERSION = 13;
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static DatabaseHelper instance;
    private RuntimeExceptionDao<CartEntry, Long> cartEntryDao;
    private RuntimeExceptionDao<AutoCompleteWords, Long> autoCompleteWordsDao;
    private RuntimeExceptionDao<Cart, Long> cartDao;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
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
            TableUtils.createTable(connectionSource, CartEntry.class);
            TableUtils.createTable(connectionSource, AutoCompleteWords.class);
            TableUtils.createTable(connectionSource, Cart.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, CartEntry.class, true);
            TableUtils.dropTable(connectionSource, AutoCompleteWords.class, true);
            TableUtils.dropTable(connectionSource, Cart.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't drop database", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<CartEntry, Long> getCartEntryDao() {
        if (cartEntryDao == null) {
            cartEntryDao = getRuntimeExceptionDao(CartEntry.class);
        }
        return cartEntryDao;
    }

    public RuntimeExceptionDao<AutoCompleteWords, Long> getAutoCompleteWordsDao() {
        if (autoCompleteWordsDao == null) {
            autoCompleteWordsDao = getRuntimeExceptionDao(AutoCompleteWords.class);
        }
        return autoCompleteWordsDao;
    }

    public RuntimeExceptionDao<Cart, Long> getCartDao() {
        if (cartDao == null) {
            cartDao = getRuntimeExceptionDao(Cart.class);
        }
        return cartDao;
    }

    @Override
    public void close() {
        super.close();
        cartEntryDao = null;
        autoCompleteWordsDao = null;
        cartDao = null;
    }
}

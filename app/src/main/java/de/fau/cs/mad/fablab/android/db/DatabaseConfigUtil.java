package de.fau.cs.mad.fablab.android.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import de.fau.cs.mad.fablab.android.basket.BasketItem;


public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            BasketItem.class,
    };

    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}

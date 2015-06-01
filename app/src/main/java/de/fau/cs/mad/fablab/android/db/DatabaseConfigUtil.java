package de.fau.cs.mad.fablab.android.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import de.fau.cs.mad.fablab.rest.core.Cart;
import de.fau.cs.mad.fablab.rest.core.CartEntry;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            CartEntry.class, Cart.class,
    };

    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}

package de.fau.cs.mad.fablab.android.ORM;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Created by EE on 04.05.15.
 *
 * Generates CONFIG AND R.raw
 */


public class DBConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            DBObject.class,
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}


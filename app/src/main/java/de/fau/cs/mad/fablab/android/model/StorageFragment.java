package de.fau.cs.mad.fablab.android.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/***
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment{

    private static NewsStorage newsStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static void initializeStorage(){
        newsStorage = new NewsStorage();
    }

    public static NewsStorage getNewsStorage(){
        return newsStorage;
    }
}

package de.fau.cs.mad.fablab.android.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/***
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private NavigationDrawerStorage navigationDrawerStorage;
    private NewsStorage newsStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        newsStorage = new NewsStorage();
        navigationDrawerStorage = new NavigationDrawerStorage();
    }

    public NewsStorage getNewsStorage(){
        return newsStorage;
    }

    public NavigationDrawerStorage getNavigationDrawerStorage() {
        return navigationDrawerStorage;
    }

}

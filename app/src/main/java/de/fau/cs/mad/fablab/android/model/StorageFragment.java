package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/***
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {

    private static Context applicationContext;

    private static NewsStorage newsStorage;
    private static NavigationDrawerStorage navigationDrawerStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void initializeStorage(AppCompatActivity context){
        newsStorage = new NewsStorage();
        navigationDrawerStorage = new NavigationDrawerStorage();
        applicationContext = context.getApplication();
    }

    public static Context getApplicationContext(){
        return applicationContext;
    }

    public static NewsStorage getNewsStorage(){
        return newsStorage;
    }

    public static NavigationDrawerStorage getNavigationDrawerStorage() {
        return navigationDrawerStorage;
    }

}

package de.fau.cs.mad.fablab.android.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/***
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {

    private static Context applicationContext;
    private static Context activityContext;
    private static FragmentManager fragmentManager;

    private static NewsStorage newsStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static void initializeStorage(ActionBarActivity context){
        newsStorage = new NewsStorage();
        applicationContext = context.getApplicationContext();
        activityContext = context;
        fragmentManager = context.getSupportFragmentManager();
    }

    public static Context getApplicationContext(){
        return applicationContext;
    }

    public static Context getContext(){
        return activityContext;
    }

    public static FragmentManager getActivityFragmentManager(){
        return fragmentManager;
    }

    public static NewsStorage getNewsStorage(){
        return newsStorage;
    }
}

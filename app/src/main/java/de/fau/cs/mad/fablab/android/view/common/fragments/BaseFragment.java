package de.fau.cs.mad.fablab.android.view.common.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/***
 * Base class for all Fragments
 */
public abstract class BaseFragment extends Fragment{
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}

package de.fau.cs.mad.fablab.android.view.fragments;

import javax.inject.Inject;


public class ICalAndNewsFragmentViewModel {

    Listener mListener;

    @Inject
    public ICalAndNewsFragmentViewModel() {

    }

    public void setListener(Listener listener){
        this.mListener = listener;
    }


    public interface Listener{

    }
}

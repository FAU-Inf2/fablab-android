package de.fau.cs.mad.fablab.android.viewmodel.common;

public abstract class BaseViewModel<T> {

    protected BaseViewModel(){}

    public abstract void setData(T data);

    public interface Listener {

    }
}

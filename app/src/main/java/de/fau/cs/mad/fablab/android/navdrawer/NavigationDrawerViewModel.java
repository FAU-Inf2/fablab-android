package de.fau.cs.mad.fablab.android.navdrawer;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;

public class NavigationDrawerViewModel extends BaseViewModel {

    private NavigationDrawerModel model;
    private NavigationDrawerLauncher viewLauncher;

    @Inject
    public NavigationDrawerViewModel(NavigationDrawerLauncher viewLauncher) {
        this.model = new NavigationDrawerModel();
        this.viewLauncher = viewLauncher;
    }

    public NavigationDrawerLauncher getLauncher() {
        return viewLauncher;
    }

    public void setData(int itemId) {
        model.setItemId(itemId);
    }

    @Override
    public void setData(Object data) {
    }

    public int getItemId() {
        return model.getItemId();
    }
}

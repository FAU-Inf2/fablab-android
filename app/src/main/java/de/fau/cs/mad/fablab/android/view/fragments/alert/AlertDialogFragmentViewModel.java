package de.fau.cs.mad.fablab.android.view.fragments.alert;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.DrupalModel;

public class AlertDialogFragmentViewModel {

    private DrupalModel mDrupalModel;

    @Inject
    public AlertDialogFragmentViewModel(DrupalModel drupalModel) {
        mDrupalModel = drupalModel;
    }
}

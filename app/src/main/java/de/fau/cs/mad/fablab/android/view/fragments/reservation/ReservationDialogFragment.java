package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class ReservationDialogFragment extends BaseDialogFragment
        implements ReservationDialogFragmentViewModel.Listener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_reservation_dialog, container, false);
    }
}

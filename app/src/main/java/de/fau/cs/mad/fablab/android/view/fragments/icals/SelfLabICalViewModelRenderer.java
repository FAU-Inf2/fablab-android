package de.fau.cs.mad.fablab.android.view.fragments.icals;

import de.fau.cs.mad.fablab.android.R;

public class SelfLabICalViewModelRenderer extends ICalViewModelRenderer {
    @Override
    public void render() {
        super.render();
        dates_cv.setCardBackgroundColor(getRootView().getResources().getColor(R.color.card_selflab));
    }
}

package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class ICalViewModelRenderer extends Renderer<ICalViewModel> {
    @InjectView(R.id.dates_card)
    CardView dates_cv;
    @InjectView(R.id.title_dates_entry)
    TextView title_tv;
    @InjectView(R.id.date_dates_entry)
    TextView date_tv;
    @InjectView(R.id.time_dates_entry)
    TextView time_tv;
    @InjectView(R.id.location_dates_entry)
    TextView location_tv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.inject(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.dates_entry, viewGroup, false);
    }

    @Override
    public void render() {
        ICalViewModel viewModel = getContent();

        new ViewCommandBinding().bind(getRootView(), viewModel.getShowDialogCommand());

        Resources res = getRootView().getResources();

        int visibleCardsCount = res.getInteger(R.integer.visible_date_cards);
        int cardVerticalMargin = res.getDimensionPixelSize(R.dimen.card_vertical_margin);
        int cardHorizontalMargin = res.getDimensionPixelSize(R.dimen.card_horizontal_margin);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                (res.getDisplayMetrics().widthPixels / visibleCardsCount)
                        - (cardHorizontalMargin * 2), RecyclerView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(cardHorizontalMargin, cardVerticalMargin, cardHorizontalMargin,
                cardVerticalMargin);
        getRootView().setLayoutParams(layoutParams);

        title_tv.setText(viewModel.getTitle());
        date_tv.setText(viewModel.getDate());
        time_tv.setText(viewModel.getTime());
        location_tv.setText(viewModel.getLocation());
    }
}

package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;

public class ToolUsageViewModelRenderer extends Renderer<ToolUsageViewModel> {
    @Bind(R.id.toolusage_entry)
    CardView entry_cv;
    @Bind(R.id.toolusage_entry_user)
    TextView user_tv;
    @Bind(R.id.toolusage_entry_project)
    TextView project_tv;
    @Bind(R.id.toolusage_entry_duration)
    TextView duration_tv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.toolusage_entry, viewGroup, false);
    }

    @Override
    public void render() {
        ToolUsageViewModel viewModel = getContent();

        user_tv.setText(viewModel.getUser());
        project_tv.setText(viewModel.getProject());
        duration_tv.setText(viewModel.getDuration());
        if(viewModel.isOwnId()) {
            entry_cv.setCardBackgroundColor(0xFFBBFFBB);
        } else {
            entry_cv.setCardBackgroundColor(0xFFFFFFFF);
        }
    }
}

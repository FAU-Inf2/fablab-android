package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.content.res.Resources;
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
        Resources res = this.getRootView().getResources();
        user_tv.setText(viewModel.getUser());
        project_tv.setText(viewModel.getProject());
        duration_tv.setText(viewModel.getDuration());

        if(viewModel.isPast()) {
            entry_cv.setAlpha(0.28f);
        }

        if(viewModel.isOwnId()) {
            if(viewModel.isNow()) {
                user_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_my_now));
                project_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_my_now_light));
                duration_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_my_now));
                user_tv.setTextColor(res.getColor(R.color.toolUsage_text_now));
                project_tv.setTextColor(res.getColor(R.color.toolUsage_text_now));
                duration_tv.setTextColor(res.getColor(R.color.toolUsage_text_now));
            } else {
                user_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_my_notnow));
                project_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_my_notnow_light));
                duration_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_my_notnow));
                user_tv.setTextColor(res.getColor(R.color.toolUsage_text_notnow));
                project_tv.setTextColor(res.getColor(R.color.toolUsage_text_notnow));
                duration_tv.setTextColor(res.getColor(R.color.toolUsage_text_notnow));
            }
        } else {
            if(viewModel.isNow()) {
                user_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_others_now));
                project_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_others_now_light));
                duration_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_others_now));
                user_tv.setTextColor(res.getColor(R.color.toolUsage_text_now));
                project_tv.setTextColor(res.getColor(R.color.toolUsage_text_now));
                duration_tv.setTextColor(res.getColor(R.color.toolUsage_text_now));
            } else {
                user_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_others_notnow));
                project_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_others_notnow_light));
                duration_tv.setBackgroundColor(res.getColor(R.color.toolUsage_background_others_notnow));
                user_tv.setTextColor(res.getColor(R.color.toolUsage_text_notnow));
                project_tv.setTextColor(res.getColor(R.color.toolUsage_text_notnow));
                duration_tv.setTextColor(res.getColor(R.color.toolUsage_text_notnow));
            }
        }
    }
}

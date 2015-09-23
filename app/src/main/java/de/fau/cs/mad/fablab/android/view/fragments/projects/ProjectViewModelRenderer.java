package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;

public class ProjectViewModelRenderer extends Renderer<ProjectViewModel> {

    @Bind(R.id.project_entry_title)
    TextView mProjectEntryTitleTV;
    @Bind(R.id.project_entry_short_description)
    TextView mProjectEntryShortDescriptionTV;

    @Override
    protected void setUpView(View view)
    {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void hookListeners(View view)
    {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.project_entry, viewGroup, false);
    }

    @Override
    public void render() {

        ProjectViewModel viewModel = getContent();

        mProjectEntryTitleTV.setText(viewModel.getTitle());
        mProjectEntryShortDescriptionTV.setText(viewModel.getShortDescription());
    }
}

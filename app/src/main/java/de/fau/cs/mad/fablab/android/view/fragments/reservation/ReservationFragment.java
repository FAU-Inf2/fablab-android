package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pedrogomez.renderers.RVRendererAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.SpinnerCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.ToolUsage;

public class ReservationFragment extends BaseFragment implements ReservationFragmentViewModel.Listener {
    @Bind(R.id.reservation_recycler_view)
    RecyclerView toolUsage_rv;
    @Bind(R.id.reservation_tool_spinner)
    Spinner mToolSpinner;
    @Bind(R.id.reservation_fragment_add_button)
    Button mAddButton;


    @Inject
    ReservationFragmentViewModel mViewModel;

    private RVRendererAdapter<ToolUsageViewModel> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        toolUsage_rv.setLayoutManager(layoutManager);

        List<FabTool> mTools = mViewModel.getTools();
        if (mTools.isEmpty()) {
            // TODO Fehlermeldung
        } else {
            List<String> mToolNames = new ArrayList<>();
            for (FabTool f : mTools) {
                mToolNames.add(f.getTitle());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.spinner_item, mToolNames);
            mToolSpinner.setAdapter(adapter);

            mToolSpinner.setVisibility(View.VISIBLE);
        }

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new ToolUsageViewModelRendererBuilder(), mViewModel.getToolUsageViewModelCollection());
        toolUsage_rv.setAdapter(mAdapter);

        new ViewCommandBinding().bind(mAddButton, mViewModel.getAddCommand());
        new SpinnerCommandBinding().bind(mToolSpinner, mViewModel.getToolChangedCommand());
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        setNavigationDrawerSelection(R.id.drawer_item_reservation);
    }

    @Override
    public void onAdd() {
        ReservationDialogFragment fragment = new ReservationDialogFragment();
        fragment.show(getFragmentManager(), "ReservationDialogFragment");

    }

    @Override
    public void onToolChanged(int parameter) {
        String text;

        text = mToolSpinner.getSelectedItem().toString();
        long id = -1;
        for(FabTool f : mViewModel.getTools()) {
            if(f.getTitle().equals(text)) {
                id = f.getId();
                break;
            }
        }

        List<ToolUsage> usages_tmp = mViewModel.getToolUsages(id);
    }
    @Override
    public void onToolListChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
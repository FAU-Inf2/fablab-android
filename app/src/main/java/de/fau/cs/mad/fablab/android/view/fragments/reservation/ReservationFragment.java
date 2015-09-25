package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import de.fau.cs.mad.fablab.android.model.events.ReservationChangedEvent;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.SpinnerCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.SwipeableRecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.cs.mad.fablab.rest.core.User;
import de.greenrobot.event.EventBus;

public class ReservationFragment extends BaseFragment implements ReservationFragmentViewModel.Listener {
    @Bind(R.id.reservation_recycler_view)
    RecyclerView toolUsage_rv;
    @Bind(R.id.reservation_tool_spinner)
    Spinner mToolSpinner;
    @Bind(R.id.reservation_fragment_add_button)
    Button mAddButton;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    ReservationFragmentViewModel mViewModel;

    private RVRendererAdapter<ToolUsageViewModel> mAdapter;
    private EventBus mEventBus = EventBus.getDefault();

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

        if(getArguments() != null) {
            User user = (User) getArguments().getSerializable(getResources().getString(R.string.key_user));
            mViewModel.setUser(user);
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        toolUsage_rv.setLayoutManager(layoutManager);

        List<FabTool> mTools = mViewModel.getTools();
        if (mTools.isEmpty()) {
            getFragmentManager().popBackStack();
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
        new SwipeableRecyclerViewCommandBinding().bind(toolUsage_rv,
                mViewModel.getRemoveReservationCommand());

        mSwipeRefreshLayout.setOnRefreshListener(mViewModel);

        new ViewCommandBinding().bind(mAddButton, mViewModel.getAddCommand());
        new SpinnerCommandBinding().bind(mToolSpinner, mViewModel.getToolChangedCommand());
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        setNavigationDrawerSelection(R.id.drawer_item_reservation);

        mViewModel.getToolChangedCommand().execute(0);
        mEventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onAdd() {
        ReservationDialogFragment fragment = new ReservationDialogFragment();
        if(mViewModel.getUser() != null) {
            fragment.setUser(mViewModel.getUser());
        }
        fragment.setTool(mViewModel.getTool());
        fragment.show(getFragmentManager(), "ReservationDialogFragment");

    }

    @Override
    public void onToolChanged(int parameter) {
        String text;

        // TODO Wait until available in better way (handler or something)
        while(mToolSpinner == null) { }
        text = mToolSpinner.getSelectedItem().toString();
        long id = -1;
        for(FabTool f : mViewModel.getTools()) {
            if(f.getTitle().equals(text)) {
                id = f.getId();
                mViewModel.setTool(f);
                break;
            }
        }

        mViewModel.getToolUsages(id);
    }

    @Override
    public void onToolListChanged() {
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void onEvent(ReservationChangedEvent event) {
        onToolChanged(0);
    }
}
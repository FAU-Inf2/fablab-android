package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrogomez.renderers.RVRendererAdapter;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.greenrobot.event.EventBus;

/***
 * The view of all Icals
 */
public class ICalFragment extends BaseFragment implements ICalFragmentViewModel.Listener {
    @InjectView(R.id.ical_recycler_view)
    RecyclerView ical_rv;

    @Inject
    ICalFragmentViewModel mViewModel;

    private RVRendererAdapter<ICalViewModel> mAdapter;

    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }

        ical_rv.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new ICalViewModelRendererBuilder(), mViewModel.getICalViewModelCollection());
        ical_rv.setAdapter(mAdapter);

        mViewModel.setListener(this);
        mViewModel.initialize();

        //bind the getGetICalCommand to the recyclerView
        new RecyclerViewCommandBinding().bind(ical_rv, mViewModel.getGetICalCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dates, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
        mViewModel.resume();
    }

    @Override
    public void onDataInserted(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onAllDataRemoved(int itemCount) {
        mAdapter.notifyItemRangeRemoved(0, itemCount);
    }

    //new
    public void onEvent(ICalClickedEvent event)
    {
        ICalDetailsDialogFragment fragment = new ICalDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ICalDetailsDialogFragmentViewModel.KEY_TITLE, event.getTitle());
        args.putIntArray(ICalDetailsDialogFragmentViewModel.KEY_START_DATE, event.getStartDate());
        args.putIntArray(ICalDetailsDialogFragmentViewModel.KEY_END_DATE, event.getEndDate());
        args.putIntArray(ICalDetailsDialogFragmentViewModel.KEY_START_TIME, event.getStartTime());
        args.putIntArray(ICalDetailsDialogFragmentViewModel.KEY_END_TIME, event.getEndTime());
        args.putString(ICalDetailsDialogFragmentViewModel.KEY_LOCATION, event.getLocation());
        args.putString(ICalDetailsDialogFragmentViewModel.KEY_DESCRIPTION, event.getDescription());
        args.putBoolean(ICalDetailsDialogFragmentViewModel.KEY_ALLDAY, event.isAllday());
        fragment.setArguments(args);
        fragment.show(getFragmentManager(), "ICalDetailsDialogFragment");
    }
}

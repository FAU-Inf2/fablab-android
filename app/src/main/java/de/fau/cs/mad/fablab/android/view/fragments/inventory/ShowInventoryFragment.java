package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pedrogomez.renderers.RVRendererAdapter;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class ShowInventoryFragment extends BaseFragment implements ShowInventoryFragmentViewModel.Listener{

    @Bind(R.id.inventory_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.inventory_progress_bar)
    ProgressBar mInventoryProgressBar;

    @Inject
    ShowInventoryFragmentViewModel mViewModel;

    private RVRendererAdapter<InventoryViewModel> mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //create and set the layoutmanager needed by recyclerview
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new InventoryViewModelRendererBuilder(), mViewModel.getInventoryViewModelCollection());
        mRecyclerView.setAdapter(mAdapter);

        mViewModel.setListener(this);
        mInventoryProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_inventory, container, false);
    }

    @Override
    public void onAllDataRemoved(int itemCount)
    {
        mAdapter.notifyItemRangeRemoved(0, itemCount);
    }

    @Override
    public void onDataChanged()
    {
        mAdapter.notifyDataSetChanged();
        mInventoryProgressBar.setVisibility(View.GONE);
    }
}

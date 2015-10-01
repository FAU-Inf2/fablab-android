package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrogomez.renderers.RVRendererAdapter;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.greenrobot.event.EventBus;

public class CartChooserFragment extends BaseFragment implements
        CartChooserFragmentViewModel.Listener{

    @Bind(R.id.projects_choose_cart_recycler_view)
    RecyclerView mCartRecyclerView;

    @Inject
    CartChooserFragmentViewModel mViewModel;

    private RVRendererAdapter<CartViewModel> mAdapter;
    private EventBus mEventBus = EventBus.getDefault();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_choose_cart, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCartRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new CartViewModelRendererBuilder(),
                mViewModel.getCartViewModelCollection());
        mCartRecyclerView.setAdapter(mAdapter);

        mViewModel.setListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.update();
        mEventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onDataChanged()
    {
        mAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEvent(CartClickedEvent event) {
        EditProjectFragment fragment = new EditProjectFragment();
        Bundle args = new Bundle();
        args.putSerializable(getResources().getString(R.string.key_project), null);
        args.putSerializable(getResources().getString(R.string.key_cart), event.getCart());
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).addToBackStack(null).commit();
    }
}

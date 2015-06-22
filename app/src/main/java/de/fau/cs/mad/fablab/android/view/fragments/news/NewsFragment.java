package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.greenrobot.event.EventBus;

public class NewsFragment extends BaseFragment implements NewsFragmentViewModel.Listener {
    @InjectView(R.id.news_recycler_view)
    RecyclerView news_recycler_view;

    @Inject
    NewsFragmentViewModel viewModel;

    private RVRendererAdapter<NewsViewModel> mAdapter;
    private ListAdapteeCollection<NewsViewModel> mNewsViewModelCollection;

    private EventBus mEventBus;

    public NewsFragment() {
        mNewsViewModelCollection = new ListAdapteeCollection<>();
        mEventBus = EventBus.getDefault();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //create and set the layoutmanager needed by recyclerview
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_recycler_view.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new NewsViewModelRendererBuilder(), mNewsViewModelCollection);
        news_recycler_view.setAdapter(mAdapter);

        viewModel.setListener(this);

        //bind the getGetNewsCommand to the recyclerView
        new RecyclerViewCommandBinding().bind(news_recycler_view, viewModel.getGetNewsCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
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
    }

    @Override
    public void onItemAdded(NewsViewModel viewModel) {
        mNewsViewModelCollection.add(viewModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataPrepared(List<NewsViewModel> viewModels) {
        mNewsViewModelCollection.addAll(viewModels);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void onEvent(NewsClickedEvent event) {
        NewsDetailsDialogFragment dialog = new NewsDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString(NewsDetailsDialogViewModel.KEY_TITLE, event.getTitle());
        args.putString(NewsDetailsDialogViewModel.KEY_TEXT, event.getText());
        args.putString(NewsDetailsDialogViewModel.KEY_IMAGE_LINK, event.getImageLink());
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "NewsDetailsDialogFragment");
    }
}


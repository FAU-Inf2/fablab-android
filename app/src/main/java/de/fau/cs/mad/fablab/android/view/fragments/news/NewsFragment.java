package de.fau.cs.mad.fablab.android.view.fragments.news;

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
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewDeltaCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icalandnews.NewsListChangeEvent;
import de.greenrobot.event.EventBus;

public class NewsFragment extends BaseFragment implements NewsFragmentViewModel.Listener {

    @InjectView(R.id.news_recycler_view)
    RecyclerView news_rv;

    @Inject
    NewsFragmentViewModel mViewModel;

    private RVRendererAdapter<NewsViewModel> mAdapter;

    private EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //create and set the layoutmanager needed by recyclerview
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_rv.setLayoutManager(layoutManager);

        mAdapter = new RVRendererAdapter<>(getLayoutInflater(savedInstanceState),
                new NewsViewModelRendererBuilder(), mViewModel.getNewsViewModelCollection());
        news_rv.setAdapter(mAdapter);

        mViewModel.setListener(this);
        mViewModel.initialize();

        //bind the getGetNewsCommand to the recyclerView
        new RecyclerViewCommandBinding().bind(news_rv, mViewModel.getGetNewsCommand());
        new RecyclerViewDeltaCommandBinding().bind(news_rv, mViewModel.getNewsScrollingCommand());
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
        mViewModel.resume();
    }

    @Override
    public void onDataInserted(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        mEventBus.post(new NewsListChangeEvent(mAdapter.getItemCount()));
    }

    @Override
    public void onAllDataRemoved(int itemCount) {
        mAdapter.notifyItemRangeRemoved(0, itemCount);
        mEventBus.post(new NewsListChangeEvent(mAdapter.getItemCount()));
    }

    public void resetPointer() {
        news_rv.scrollToPosition(0);
    }

    public int getSize() {
        return mAdapter.getItemCount();
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

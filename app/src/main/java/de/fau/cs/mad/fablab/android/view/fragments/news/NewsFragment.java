package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.RecyclerViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.recyclerview.NewsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment implements NewsFragmentViewModel.Listener{

    @InjectView(R.id.news_recycler_view)
    RecyclerView news_recycler_view;

    @Inject
    NewsFragmentViewModel viewModel;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.setListener(this);

        //create and set the layoutmanager needed by recyclerview
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_recycler_view.setLayoutManager(layoutManager);
        news_recycler_view.setAdapter(new NewsAdapter(viewModel, (MainActivity) getActivity()));

        //bind the getNewsCommand to the recyclerView
        new RecyclerViewCommandBinding().bind(news_recycler_view, viewModel.getNewsCommand());
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListDataChanged() {
        news_recycler_view.getAdapter().notifyDataSetChanged();
    }
}


package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.viewmodel.NewsFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment{

    @InjectView(R.id.news_recycler_view)
    RecyclerView news_recycler_view;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, rootView);

        NewsFragmentViewModel viewModel = new NewsFragmentViewModel(getActivity());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_recycler_view.setLayoutManager(layoutManager);
        news_recycler_view.setAdapter(new NewsAdapter());

        bindRecyclerView(news_recycler_view, viewModel.getNewsCommand());

        return rootView;
    }
}

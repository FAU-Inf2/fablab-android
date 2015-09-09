package de.fau.cs.mad.fablab.android.view.fragments.icalandnews;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.greenrobot.event.EventBus;

public class ICalAndNewsFragment extends BaseFragment {

    @Bind(R.id.fragment_ical)
    FrameLayout ical_fl;

    private float mTranslationY;

    @Bind(R.id.fragment_news)
    FrameLayout news_fl;

    private NewsFragment newsFragment;

    private EventBus mEventBus = EventBus.getDefault();

    @Inject
    public ICalAndNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_ical_and_news, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mEventBus.register(this);
        if(newsFragment != null)
            newsFragment.resetPointer();

        mTranslationY = 0;
        setDisplayOptions(R.id.drawer_item_news, true, true, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String TAG_ICAL_FRAGMENT = "tag_ical_fragment";
        final String TAG_NEWS_FRAGMENT = "tag_news_fragment";

        ICalFragment iCalFragment = (ICalFragment) getChildFragmentManager().findFragmentByTag(
                TAG_ICAL_FRAGMENT);
        if (iCalFragment == null) {
            getChildFragmentManager().beginTransaction().add(R.id.fragment_ical, new ICalFragment(),
                    TAG_ICAL_FRAGMENT).commit();
        }

        if(isLandscpape()) {
            int visibleCardsCount = getResources().getInteger(R.integer.visible_date_cards);
            int frame_width = (getResources().getDisplayMetrics().widthPixels / visibleCardsCount);
            RelativeLayout.LayoutParams layoutParamsIcal = new RelativeLayout.LayoutParams(frame_width, RelativeLayout.LayoutParams.MATCH_PARENT);
            ical_fl.setLayoutParams(layoutParamsIcal);
            RelativeLayout.LayoutParams layoutParamsNews = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParamsNews.setMargins(0, 0, frame_width, 0);
            news_fl.setLayoutParams(layoutParamsNews);
        }

        newsFragment = (NewsFragment) getChildFragmentManager().findFragmentByTag(
                TAG_NEWS_FRAGMENT);
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
            getChildFragmentManager().beginTransaction().add(R.id.fragment_news, newsFragment,
                    TAG_NEWS_FRAGMENT).commit();
        }
    }

    public void onEvent(NewsListScrollingEvent event) {
        if(!isLandscpape()) {
            mTranslationY = mTranslationY - event.getDelta().getDy();
            ical_fl.setTranslationY(mTranslationY);
        }
    }

    private boolean isLandscpape() {
        return (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }
}

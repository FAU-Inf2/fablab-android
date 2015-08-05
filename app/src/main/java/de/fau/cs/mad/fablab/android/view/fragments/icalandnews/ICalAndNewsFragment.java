package de.fau.cs.mad.fablab.android.view.fragments.icalandnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.InjectView;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.greenrobot.event.EventBus;

public class ICalAndNewsFragment extends BaseFragment {

    @InjectView(R.id.fragment_ical)
    FrameLayout ical_fl;
    float mTranslationY;


    private EventBus mEventBus = EventBus.getDefault();

    @Inject
    public ICalAndNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ical_and_news, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
        setDisplayOptions(R.id.drawer_item_news, true, true);
        ical_fl.setTranslationY(mTranslationY);

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

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mTranslationY = sharedPref.getFloat("saved_translation_y", 0);

        ICalFragment iCalFragment = (ICalFragment) getChildFragmentManager().findFragmentByTag(
                TAG_ICAL_FRAGMENT);
        if (iCalFragment == null) {
            getChildFragmentManager().beginTransaction().add(R.id.fragment_ical, new ICalFragment(),
                    TAG_ICAL_FRAGMENT).commit();
        }

        if(mTranslationY > 0) mTranslationY = 0;
        ical_fl.setTranslationY(mTranslationY);

        NewsFragment newsFragment = (NewsFragment) getChildFragmentManager().findFragmentByTag(
                TAG_NEWS_FRAGMENT);
        if (newsFragment == null) {
            getChildFragmentManager().beginTransaction().add(R.id.fragment_news, new NewsFragment(),
                    TAG_NEWS_FRAGMENT).commit();
        }
    }

    public void onEvent(NewsListScrollingEvent event){
        mTranslationY = ical_fl.getTranslationY() - event.getDelta().getDy();
        ical_fl.setTranslationY(mTranslationY);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("saved_translation_y", mTranslationY);
        editor.commit();
    }
}

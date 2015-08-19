package de.fau.cs.mad.fablab.android.view.fragments.icalandnews;

import android.content.Context;
import android.content.SharedPreferences;
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
    private float mTranslationYReal;
    private float news_fl_height;

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
        return inflater.inflate(R.layout.fragment_ical_and_news, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
        setDisplayOptions(R.id.drawer_item_news, true, true);
        if(!isLandscpape()) {
            ical_fl.setTranslationY(mTranslationY);
        }
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
        mTranslationYReal = sharedPref.getFloat("saved_translation_y_real", 0);
        news_fl_height = sharedPref.getFloat("saved_news_fl_height", -1);

        ICalFragment iCalFragment = (ICalFragment) getChildFragmentManager().findFragmentByTag(
                TAG_ICAL_FRAGMENT);
        if (iCalFragment == null) {
            getChildFragmentManager().beginTransaction().add(R.id.fragment_ical, new ICalFragment(),
                    TAG_ICAL_FRAGMENT).commit();
        }

        if(!isLandscpape()) {
            if (mTranslationY > 0) mTranslationY = 0;
            ical_fl.setTranslationY(mTranslationY);
        } else {
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
            getChildFragmentManager().beginTransaction().add(R.id.fragment_news, new NewsFragment(),
                    TAG_NEWS_FRAGMENT).commit();
        }
    }

    public void onEvent(NewsListScrollingEvent event) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        mTranslationY = sharedPref.getFloat("saved_translation_y", 0);
        mTranslationYReal = sharedPref.getFloat("saved_translation_y_real", 0);
        news_fl_height = sharedPref.getFloat("saved_news_fl_height", -1);
        mTranslationYReal = mTranslationYReal - event.getDelta().getDy();

        if(news_fl_height == -1 && !isLandscpape()) {
            if(newsFragment != null) {
                newsFragment.resetPointer();
            }

            news_fl_height = news_fl.getHeight() - getResources().getDimension(R.dimen.news_activity_news_padding_top) - getResources().getDimension(R.dimen.card_vertical_margin);

            editor.putFloat("saved_news_fl_height", news_fl_height);
            editor.commit();

            mEventBus.post(new NewsListChangeEvent(newsFragment.getSize()));
        }

        if(!isLandscpape()) {
            mTranslationY = mTranslationY - event.getDelta().getDy();
            ical_fl.setTranslationY(mTranslationY);
            mTranslationYReal = mTranslationY;
        } else {
            if(news_fl_height != -1) {
                int maxPixelHeight = sharedPref.getInt("saved_translation_maxpixelheight", 0);

                if (mTranslationYReal >= -maxPixelHeight)
                    mTranslationY = mTranslationYReal;
                else
                    mTranslationY = -maxPixelHeight;
            }
        }

        if(event.getDelta().getDy() != 0 && news_fl_height != -1) {
            editor.putFloat("saved_translation_y", mTranslationY);
            editor.putFloat("saved_translation_y_real", mTranslationYReal);
        }

        editor.commit();
    }

    public void onEvent(NewsListChangeEvent event) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        news_fl_height = sharedPref.getFloat("saved_news_fl_height", -1);

        if(news_fl_height != -1) {
            int maxPixelHeight = sharedPref.getInt("saved_translation_maxpixelheight", 0);
            int a = (int) ((event.getSize() * (getResources().getDimension(R.dimen.card_icon_size) +
                        getResources().getDimension(R.dimen.card_vertical_margin) * 2)) - news_fl_height);

            if (a > maxPixelHeight)
                editor.putInt("saved_translation_maxpixelheight", a);

            editor.commit();
        }
    }

    private boolean isLandscpape() {
        return (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }
}

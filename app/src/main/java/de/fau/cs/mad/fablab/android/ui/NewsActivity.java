package de.fau.cs.mad.fablab.android.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_news)
public class NewsActivity extends RoboActionBarActivity {

    @InjectView(R.id.shopping_cart_FAM) FloatingActionMenu shoppingCartButton;
    @InjectView(R.id.search_FAB) FloatingActionButton searchButton;
    @InjectView(R.id.scan_FAB) FloatingActionButton scanButton;
    @InjectView(R.id.news) RecyclerView news;
    @InjectView(R.id.dates_view_pager) ViewPager datesViewPager;
    private DatesSlidePagerAdapter datesSlidePagerAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    //private RecyclerView.LayoutManager datesLayoutManager;
    private NewsAdapter newsAdapter;
    //private DatesAdapter datesAdapter;

    private AppbarDrawerInclude appbarDrawer;

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    static final String LIST= "LIST";
    static final String POSITION = "POSITION";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();

        //no animations of the shopping cart when clicked
        shoppingCartButton.setIconAnimated(false);

        //get news and set them
        newsLayoutManager = new LinearLayoutManager(this);
        List<String> testNews = new ArrayList<>();
        testNews.add("News1");
        testNews.add("News2");
        testNews.add("News3");
        newsAdapter = new NewsAdapter(testNews);
        news.setLayoutManager(newsLayoutManager);
        news.setAdapter(newsAdapter);

        //datesLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);

        ArrayList<String> testDates = new ArrayList<>();
        testDates.add("Selflab");
        testDates.add("Open Lab");
        testDates.add("Versammlung");
        /*
        datesAdapter = new DatesAdapter(testDates);
        dates.setLayoutManager(datesLayoutManager);
        dates.setAdapter(datesAdapter);
        */


        datesSlidePagerAdapter = new DatesSlidePagerAdapter(getSupportFragmentManager(), testDates);
        datesViewPager.setAdapter(datesSlidePagerAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        appbarDrawer.startTimer();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        appbarDrawer.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        appbarDrawer.startTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_opened) {
            appbarDrawer.updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, appbarDrawer.openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

        private List<String> news = new ArrayList<String>();

        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsViewHolder holder, int position) {
            holder.setNews(news.get(position), "texttexttexttexttexttexttexttexttexttexttexttexttexttexttext" +
                    "texttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttextext");
        }

        @Override
        public int getItemCount() {
            return news.size();
        }

        public NewsAdapter(List<String> newsList)
        {
            this.news = newsList;
        }

        public class NewsViewHolder extends RecyclerView.ViewHolder {

            private final View view;
            private final TextView titleView, subTitleView;
            private final ImageView iconView;

            public NewsViewHolder(View view) {
                super(view);
                this.view = view;
                this.titleView = (TextView) view.findViewById(R.id.title_news_entry);
                this.subTitleView = (TextView) view.findViewById(R.id.text_news_entry);
                this.iconView = (ImageView) view.findViewById(R.id.icon_news_entry);
            }

            public void setNews(String title, String subtitle)
            {
                this.titleView.setText(title);
                this.subTitleView.setText(subtitle);
                //this.iconView.setImageDrawable(image.getDrawable());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView title = (TextView) v.findViewById(R.id.title_news_entry);
                        TextView text = (TextView) v.findViewById(R.id.text_news_entry);
                        ImageView image = (ImageView) v.findViewById(R.id.icon_news_entry);
                        Bundle args = new Bundle();
                        args.putString(TITLE, title.getText().toString());
                        args.putString(TEXT, text.getText().toString());
                        args.putParcelable(IMAGE, ((BitmapDrawable) image.getDrawable()).getBitmap());
                        NewsDialog dialog = new NewsDialog();
                        dialog.setArguments(args);
                        dialog.show(getSupportFragmentManager(), "news dialog");
                    }
                });
            }
        }
    }

    private class DatesSlidePagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> dates = new ArrayList<>();
        public DatesSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public DatesSlidePagerAdapter(FragmentManager fm, ArrayList<String> dates)
        {
            super(fm);
            this.dates = dates;
        }

        @Override
        public Fragment getItem(int position) {
                int arrayPosition = position *2;
                Bundle args = new Bundle();
                args.putStringArrayList(LIST, dates);
                args.putInt(POSITION, arrayPosition);
                DatesFragment datesFragment = new DatesFragment();
                datesFragment.setArguments(args);
                return datesFragment;

        }

        @Override
        public int getCount() {
            return (int)(dates.size()+1)/2;
        }
    }


    /*
    public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.DatesViewHolder> {

        private List<String> dates = new ArrayList<String>();

        @Override
        public DatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_entry, parent, false);
            return new DatesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DatesViewHolder holder, int position) {
            holder.setDates(dates.get(position), "29.05.2015", "10.00");
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }

        public DatesAdapter(List<String> datesList)
        {
            this.dates = datesList;
        }

        public class DatesViewHolder extends RecyclerView.ViewHolder {

            private final View view;
            private final TextView titleView, dateView, timeView;

            public DatesViewHolder(View view) {
                super(view);
                this.view = view;
                this.titleView = (TextView) view.findViewById(R.id.title_dates_entry);
                this.dateView = (TextView) view.findViewById(R.id.date_dates_entry);
                this.timeView = (TextView) view.findViewById(R.id.time_dates_entry);
            }

            public void setDates(String title, String date, String time)
            {
                this.titleView.setText(title);
                this.dateView.setText(date);
                this.timeView.setText(time);
            }
        }
    }
    */
}

package de.fau.cs.mad.fablab.android.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.fablab.android.FabButton;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_news)
public class NewsActivity extends RoboActionBarActivity {

    @InjectView(R.id.news) RecyclerView news;
    @InjectView(R.id.dates_view_pager) ViewPager datesViewPager;
    private DatesSlidePagerAdapter datesSlidePagerAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private NewsAdapter newsAdapter;

    private AppbarDrawerInclude appbarDrawer;

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    static final String ICAL1 = "ICAL1";
    static final String ICAL2 = "ICAL2";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();

        // init Floating Action Menu
        FabButton.MYFABUTTON.init(findViewById(android.R.id.content));

        //get news and set them
        newsLayoutManager = new LinearLayoutManager(this);
        List<String> testNews = new ArrayList<>();
        testNews.add("News1");
        testNews.add("News2");
        testNews.add("News3");
        List<News> listNews = new ArrayList<>();
        News news1 = new News(); news1.setLinkToPreviewImage(null);news1.setTitle("News 1"); news1.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        listNews.add(news1);
        News news2 = new News(); news2.setLinkToPreviewImage(null);news2.setTitle("News 2"); news2.setDescription("blablablablabla");
        listNews.add(news2);
        News news3 = new News(); news3.setLinkToPreviewImage("http://www.jpl.nasa.gov/spaceimages/images/mediumsize/PIA17011_ip.jpg");news3.setTitle("News 3"); news3.setDescription("texttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttexttext");
        listNews.add(news3);
        newsAdapter = new NewsAdapter(listNews);
        news.setLayoutManager(newsLayoutManager);
        news.setAdapter(newsAdapter);

        List<ICal> listDates = new ArrayList<>();
        ICal date1 = new ICal(); date1.setLocation("Fablab"); date1.setSummery("OpenLab");
        listDates.add(date1);
        ICal date2 = new ICal(); date2.setLocation("Fablab"); date2.setSummery("SelfLab");
        listDates.add(date2);
        ICal date3 = new ICal(); date3.setLocation("Cafete"); date3.setSummery("Kaffeetrinken");
        listDates.add(date3);
        ICal date4 = new ICal(); date4.setLocation("ziemlich lange location"); date4.setSummery("ziemlich langer Veranstaltungstitel");
        listDates.add(date4);
        ICal date5 = new ICal(); date5.setLocation("CIP"); date5.setSummery("Test ungerade");
        listDates.add(date5);

        datesSlidePagerAdapter = new DatesSlidePagerAdapter(getSupportFragmentManager(), listDates);
        datesViewPager.setAdapter(datesSlidePagerAdapter);


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

        private List<News> news = new ArrayList<>();

        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsViewHolder holder, int position) {
            holder.setNews(news.get(position));
        }

        @Override
        public int getItemCount() {
            return news.size();
        }

        public NewsAdapter(List<News> newsList)
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

            public void setNews(News news)
            {
                this.titleView.setText(news.getTitle());
                this.subTitleView.setText(news.getDescription());
                if(news.getLinkToPreviewImage() != null) {
                    new DownloadImageTask(iconView)
                            .execute(news.getLinkToPreviewImage());
                }

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

        List<ICal> dates = new ArrayList<>();
        public DatesSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public DatesSlidePagerAdapter(FragmentManager fm, List<ICal> dates)
        {
            super(fm);
            this.dates = dates;
        }

        @Override
        public Fragment getItem(int position) {
            int arrayPosition = position *2;

            Bundle args = new Bundle();
            args.putSerializable(ICAL1, dates.get(arrayPosition));
            if(arrayPosition+1 < dates.size())
            {
                args.putSerializable(ICAL2, dates.get(arrayPosition+1));
            }
            DatesFragment datesFragment = new DatesFragment();
            datesFragment.setArguments(args);
            return datesFragment;

        }

        @Override
        public int getCount() {
            return (dates.size()+1)/2;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

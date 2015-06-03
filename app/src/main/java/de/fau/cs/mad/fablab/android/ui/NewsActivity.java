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
import android.text.Html;
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
import de.fau.cs.mad.fablab.android.cart.CartSingleton;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.android.productsearch.AutoCompleteHelper;
import de.fau.cs.mad.fablab.rest.NewsApiClient;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.fau.cs.mad.fablab.rest.core.News;
import de.fau.cs.mad.fablab.rest.myapi.NewsApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_news)
public class NewsActivity extends RoboActionBarActivity {

    @InjectView(R.id.cart_recycler_view) RecyclerView cart_rv;
    @InjectView(R.id.news) RecyclerView news_rv;
    @InjectView(R.id.dates_view_pager) ViewPager datesViewPager;
    private DatesSlidePagerAdapter datesSlidePagerAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private NewsAdapter newsAdapter;
    private UiUtils uiUtils;

    private List<News> newsList;
    private NewsApi newsApi;

    private AppbarDrawerInclude appbarDrawer;

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    static final String ICAL1 = "ICAL1";
    static final String ICAL2 = "ICAL2";


    //This callback is used for product Search.
    private Callback<List<News>> newsCallback = new Callback<List<News>>() {
        @Override
        public void success(List<News> news, Response response) {
            if (news.isEmpty()) {
                Toast.makeText(getBaseContext(), R.string.product_not_found, Toast.LENGTH_LONG).show();
            }
            newsList.clear();
            for (News singleNews : news) {
                singleNews.setDescription(uiUtils.processNewsText(singleNews.getDescription()));
                newsList.add(singleNews);
            }

            newsAdapter = new NewsAdapter(newsList);
            news_rv.setAdapter(newsAdapter);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getBaseContext(), R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
            newsAdapter = new NewsAdapter(newsList);
            news_rv.setAdapter(newsAdapter);
        }
    };


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiUtils = new UiUtils();

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();

        // init db and cart - always do this on app start
        CartSingleton.MYCART.init(getApplication());

        // init cart panel
        CartSingleton.MYCART.setSlidingUpPanel(this, findViewById(android.R.id.content), true);

        // init Floating Action Menu
        FabButton.MYFABUTTON.init(findViewById(android.R.id.content), this);

        //get news and set them
        newsLayoutManager = new LinearLayoutManager(getApplicationContext());
        news_rv.setLayoutManager(newsLayoutManager);
        newsList = new ArrayList<>();
        newsApi = new NewsApiClient(this).get();
        newsApi.findAll(newsCallback);


        List<ICal> listDates = new ArrayList<>();
        ICal date1 = new ICal(); date1.setLocation("Fablab"); date1.setSummery("OpenLab");
        listDates.add(date1);
        ICal date2 = new ICal(); date2.setLocation("Fablab"); date2.setSummery("SelfLab");
        listDates.add(date2);
        ICal date3 = new ICal(); date3.setLocation("Cafete"); date3.setSummery("Kaffeetrinken");
        listDates.add(date3);
        ICal date4 = new ICal(); date4.setLocation("ziemlich lange location"); date4.setSummery("ziemlich langer Veranstaltungstitel");
        listDates.add(date4);
        ICal date5 = new ICal();
        date5.setLocation("CIP");
        date5.setSummery("Test ungerade");
        listDates.add(date5);

        datesSlidePagerAdapter = new DatesSlidePagerAdapter(getSupportFragmentManager(), listDates);
        datesViewPager.setAdapter(datesSlidePagerAdapter);

        //Load Autocompleteionwords
        AutoCompleteHelper.getInstance().loadProductNames(this);
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
        CartSingleton.MYCART.setSlidingUpPanel(this, findViewById(android.R.id.content), true);
        appbarDrawer.startTimer();

        //Load Autocompleteionwords
        AutoCompleteHelper.getInstance().loadProductNames(this);
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
            private String description;

            public NewsViewHolder(View view) {
                super(view);
                this.view = view;
                this.titleView = (TextView) view.findViewById(R.id.title_news_entry);
                this.subTitleView = (TextView) view.findViewById(R.id.text_news_entry);
                this.iconView = (ImageView) view.findViewById(R.id.icon_news_entry);
            }

            public void setNews(News news)
            {
                description = news.getDescription();
                this.titleView.setText(news.getTitle());
                this.subTitleView.setText(Html.fromHtml(description));
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
                        args.putString(TEXT, description);
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

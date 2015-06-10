package de.fau.cs.mad.fablab.android.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.NewsStorage;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.model.events.NewsUpdateEvent;
import de.fau.cs.mad.fablab.android.ui.NewsDialog;
import de.fau.cs.mad.fablab.rest.core.News;
import de.greenrobot.event.EventBus;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    static final String IMAGE = "IMAGE";
    static final String TITLE = "TITLE";
    static final String TEXT = "TEXT";

    NewsStorage storage;

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_entry, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setNews(storage.getAllNews().get(position));
    }

    @Override
    public int getItemCount() {
        return storage.getAllNews().size();
    }

    public NewsAdapter()
    {
        EventBus.getDefault().register(this);
        storage = StorageFragment.getNewsStorage();
    }

    public void onEvent(NewsUpdateEvent event) {
        notifyDataSetChanged();
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
            this.titleView.setText(news.getTitle());
            this.subTitleView.setText(news.getDescriptionShort());
            if(news.getLinkToPreviewImage() != null) {
                new DownloadImageTask(iconView)
                        .execute(news.getLinkToPreviewImage());
            }
            description = news.getDescription();

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
                    //TODO re enable dialog show here!
                }
            });
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
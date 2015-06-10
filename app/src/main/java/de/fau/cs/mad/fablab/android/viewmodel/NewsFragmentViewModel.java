package de.fau.cs.mad.fablab.android.viewmodel;

import android.content.Context;

import de.fau.cs.mad.fablab.android.model.NewsModel;

public class NewsFragmentViewModel{

    private Context context;
    private NewsModel model;

    private Command commandGetNews = new Command() {
        @Override
        public void execute(Object parameter) {
            model.fetchNextNews();
        }
    };

    public NewsFragmentViewModel(Context c){
        this.context = c;
        this.model = new NewsModel(context);
    }

    public Command getNewsCommand(){
        return commandGetNews;
    }
}

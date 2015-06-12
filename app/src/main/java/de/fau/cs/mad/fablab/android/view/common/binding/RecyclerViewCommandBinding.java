package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class RecyclerViewCommandBinding extends BaseBinding {

    private static final String LOG_TAG = "RecyclerViewCommand";

    private final RecyclerView recyclerView;
    private final Command<?> command;

    private LinearLayoutManager layoutManager;

    public RecyclerViewCommandBinding(RecyclerView recyclerView, final Command command){
        this.recyclerView = recyclerView;
        this.command = command;

        this.layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

        this.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if ( (visibleItemCount+pastVisiblesItems) >= totalItemCount) {
                        Log.d(LOG_TAG, "End of List, Fetching next entries");
                        command.execute(null);
                    }
            }
        });
    }
}

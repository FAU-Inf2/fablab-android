package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class RecyclerViewCommandBinding extends RecyclerView.OnScrollListener
        implements Binding<RecyclerView, Void> {
    private Command<Void> mCommand;

    @Override
    public void bind(RecyclerView view, Command<Void> command) {
        mCommand = command;
        view.addOnScrollListener(this);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

        if ((visibleItemCount + pastVisibleItems) >= totalItemCount && mCommand.isExecutable()) {
            mCommand.execute(null);
        }
    }
}

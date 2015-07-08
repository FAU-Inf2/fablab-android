package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.v7.widget.RecyclerView;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class RecyclerViewDeltaCommandBinding  extends RecyclerView.OnScrollListener
        implements Binding<RecyclerView, RecyclerViewDeltaCommandBinding.RecyclerViewDelta> {

    private Command<RecyclerViewDelta> mCommand;


    @Override
    public void bind(RecyclerView recyclerView, Command<RecyclerViewDelta> command) {
        recyclerView.addOnScrollListener(this);
        mCommand = command;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mCommand.execute(new RecyclerViewDelta(dx, dy));
    }

    public class RecyclerViewDelta{
        int mDy;
        int mDx;

        public RecyclerViewDelta(int dx, int dy){
            this.mDx = dx;
            this.mDy = dy;
        }

        public int getDx(){
            return mDx;
        }

        public int getDy(){
            return mDy;
        }
    }
}

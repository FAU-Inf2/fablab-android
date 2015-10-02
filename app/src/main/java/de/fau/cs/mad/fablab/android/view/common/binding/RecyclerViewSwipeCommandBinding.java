package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class RecyclerViewSwipeCommandBinding implements Binding<RecyclerView, Integer> {

    private Command<Integer> mCommand;

    @Override
    public void bind(RecyclerView recyclerView, Command<Integer> command) {

        mCommand = command;

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                mCommand.execute(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

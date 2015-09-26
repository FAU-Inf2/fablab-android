package de.fau.cs.mad.fablab.android.view.common.binding;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class SwipeableRecyclerViewCommandBinding implements Binding<RecyclerView, Integer>,
        SwipeableRecyclerViewTouchListener.SwipeListener {
    private Command<Integer> mCommand;

    @Override
    public void bind(RecyclerView view, Command<Integer> command) {
        mCommand = command;

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(view, this) {
                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        // Workaround library bug
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        EventBus.getDefault().post(e);
                        super.onTouchEvent(rv, e);
                    }
                };
        view.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public boolean canSwipe(int position) {
        return mCommand.isExecutable();
    }

    @Override
    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mCommand.execute(position);
        }
    }

    @Override
    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mCommand.execute(position);
        }
    }
}

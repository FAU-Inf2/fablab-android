package de.fau.cs.mad.fablab.android.view.fragments.icals;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class ICalFragmentViewModel implements ObservableArrayList.Listener<ICal> {
    private ICalModel mModel;
    private Listener mListener;

    private ListAdapteeCollection<ICalViewModel> mICalViewModelCollection;

    private Command<Void> mCommandGetICals = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            mModel.fetchNextICals();
        }
    };

    @Inject
    public ICalFragmentViewModel(ICalModel model) {
        mModel = model;
        mModel.getICalsList().setListener(this);
        mICalViewModelCollection = new ListAdapteeCollection<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getGetICalCommand() {
        return mCommandGetICals;
    }

    @Override
    public void onItemAdded(ICal newItem) {
        mICalViewModelCollection.add(new ICalViewModel(newItem));
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    @Override
    public void onItemRemoved(ICal removedItem) {

    }

    public AdapteeCollection<ICalViewModel> getICalViewModelCollection() {
        return mICalViewModelCollection;
    }

    public void initialize() {
        if (mListener != null) {
            for (ICal iCal : mModel.getICalsList()) {
                mICalViewModelCollection.add(new ICalViewModel(iCal));
            }
            mListener.onDataChanged();
        }
    }

    public interface Listener {
        void onDataChanged();
    }
}

package de.fau.cs.mad.fablab.android.view.fragments.icals;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.Collection;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class ICalFragmentViewModel extends BaseViewModel<ICal> {
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
    public void onAllItemsAdded(Collection<? extends ICal> collection) {
        addItems(collection);
    }

    private void addItems(Collection<? extends ICal> icals) {
        int positionStart = mICalViewModelCollection.size();
        int count = 0;
        for (ICal newItem : icals) {
            mICalViewModelCollection.add(new ICalViewModel(newItem));
            count++;
        }
        if (mListener != null && count > 0) {
            mListener.onDataInserted(positionStart, count);
        }
    }

    public AdapteeCollection<ICalViewModel> getICalViewModelCollection() {
        return mICalViewModelCollection;
    }

    public void initialize() {
        addItems(mModel.getICalsList());
    }

    public interface Listener {
        void onDataInserted(int positionStart, int itemCount);
    }
}

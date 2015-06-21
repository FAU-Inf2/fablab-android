package de.fau.cs.mad.fablab.android.view.fragments.icals;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseAdapterViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class ICalViewPagerFragmentViewModel extends BaseAdapterViewModel<ICal>{

    private Listener listener;
    private ICalModel model;

    private Command commandGetICal = new Command() {
        @Override
        public void execute(Object parameter) {
            model.fetchNextICals();

        }
    };

    @Inject
    public ICalViewPagerFragmentViewModel(ICalModel model){
        this.model = model;
        model.getICalsList().setListener(this);
    }


    public void setListener(Listener listener){
        super.setListener(listener);
        this.listener = listener;
    }

    @Override
    public List<ICal> getData() {
        return model.getICalsList();
    }

    public Command getGetICalCommand(){
        return commandGetICal;
    }

    public interface Listener extends BaseAdapterViewModel.Listener{

    }
}

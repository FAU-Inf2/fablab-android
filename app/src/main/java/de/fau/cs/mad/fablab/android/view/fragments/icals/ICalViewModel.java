package de.fau.cs.mad.fablab.android.view.fragments.icals;

import java.text.DateFormat;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;
import de.greenrobot.event.EventBus;

public class ICalViewModel {
    private ICal mICal;

    private Command<Void> mShowDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new ICalClickedEvent(getTitle(), getDate(), getTime(),
                    getLocation(), mICal.getDescription()));
        }
    };

    public ICalViewModel(ICal iCal) {
        mICal = iCal;
    }

    public Command<Void> getShowDialogCommand() {
        return mShowDialogCommand;
    }

    public String getTitle() {
        return mICal.getSummery();
    }

    public String getDate() {
        return DateFormat.getDateInstance().format(mICal.getDtstartAsDate());
    }

    public String getTime() {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(mICal.getDtstartAsDate());
    }

    public String getLocation() {
        return mICal.getLocation();
    }
}

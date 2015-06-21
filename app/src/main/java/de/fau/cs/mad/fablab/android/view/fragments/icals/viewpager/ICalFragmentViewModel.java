package de.fau.cs.mad.fablab.android.view.fragments.icals.viewpager;

import java.util.Calendar;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalViewLauncher;
import de.fau.cs.mad.fablab.android.viewmodel.common.BaseViewModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.ICal;

public class ICalFragmentViewModel extends BaseViewModel {

    @Inject
    ICalDetailFragmentViewModel detailFragmentViewModel;
    @Inject
    ICalViewLauncher viewLauncher;
    private ICal iCalLeft;
    private ICal iCalRight;

    private Command showDetailsDialogLeftCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            showDialog(true);
        }
    };

    private Command showDetailsDialogRightCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            showDialog(false);
        }
    };

    @Inject
    public ICalFragmentViewModel(ICalViewLauncher viewLauncher)
    {
        this.viewLauncher = viewLauncher;
    }

    private void showDialog(boolean left)
    {
        ICal ical = left ? iCalLeft : iCalRight;
        Calendar cal = Calendar.getInstance();
        cal.setTime(ical.getDtstartAsDate());
        String date = (Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "." + Integer.toString(cal.get(Calendar.MONTH)+1) + "." + Integer.toString(cal.get(Calendar.YEAR)));
        String time = (Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cal.get(Calendar.MINUTE) + 100).substring(1));
        detailFragmentViewModel.show(ical.getSummery(), date, time, ical.getLocation());
    }

    private Command dismissDialogCommand = new Command() {
        @Override
        public void execute(Object parameter) {
            viewLauncher.dismissDialog();
        }
    };

    public Command getShowDetailsDialogLeftCommand()
    {
        return showDetailsDialogLeftCommand;
    }

    public Command getShowDetailsDialogRightCommand()
    {
        return showDetailsDialogRightCommand;
    }

    public Command getDismissDialogCommand()
    {
        return dismissDialogCommand;
    }

    public int getEntryCount()
    {
        int count = 0;
        if(iCalLeft != null)
        {
            count++;
        }
        if(iCalRight != null)
        {
            count++;
        }

        return count;
    }

    public void setItems(ICal iCalLeft, ICal iCalRight) {
        this.iCalLeft = iCalLeft;
        this.iCalRight = iCalRight;
    }

    public ICal getLeftItem()
    {
        return iCalLeft;
    }

    public ICal getRightItem()
    {
        return iCalRight;
    }
}

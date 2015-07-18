package de.fau.cs.mad.fablab.android.view.fragments.icals;

import android.os.Bundle;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ICalDetailsDialogFragmentViewModel {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_TIME = "key_time";
    public static final String KEY_LOCATION = "key_location";
    public static final String KEY_DESCRIPTION = "key_description";


    Listener mListener;

    private String mTitle;
    private String mDate;
    private String mTime;
    private String mLocation;
    private String mDescription;

    private Command<Void> dismissDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    @Inject
    public ICalDetailsDialogFragmentViewModel() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getDismissDialogCommand()
    {
        return dismissDialogCommand;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate()
    {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getDescription() {
        return mDescription;
    }

    public void initialize(Bundle args) {
        mTitle = args.getString(KEY_TITLE);
        mDate = args.getString(KEY_DATE);
        mTime = args.getString(KEY_TIME);
        mLocation = args.getString(KEY_LOCATION);
        mDescription = args.getString(KEY_DESCRIPTION);
    }

    public interface Listener {
        void onDismiss();
    }

}

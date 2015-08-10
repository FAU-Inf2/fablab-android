package de.fau.cs.mad.fablab.android.view.fragments.about;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class AboutFragmentViewModel {

    private Listener mListener;

    private Command<Void> mToggleVersionCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.toggleVersion();
            }
        }
    };

    private Command<Void> mToggleAboutCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.toggleAbout();
            }
        }
    };

    private Command<Void> mToggleOpenSourceLicensesCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if (mListener != null) {
                mListener.toggleOpenSourceLicenses();
            }
        }
    };

    @Inject
    public AboutFragmentViewModel()
    {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Void> getToggleVersionCommand() {
        return mToggleVersionCommand;
    }

    public Command<Void> getToggleAboutCommand() {
        return mToggleAboutCommand;
    }

    public Command<Void> getToggleOpenSourceLicensesCommand() {
        return mToggleOpenSourceLicensesCommand;
    }

    public interface Listener
    {
        void toggleVersion();

        void toggleAbout();

        void toggleOpenSourceLicenses();
    }
}

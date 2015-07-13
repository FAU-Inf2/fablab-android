package de.fau.cs.mad.fablab.android.viewmodel.common.commands;

public abstract class Command<T> {

    private CommandListener mCommandListener;
    private boolean mIsAvailable = true;
    private boolean mIsExecutable = true;

    public void setListener(CommandListener commandListener) {
        mCommandListener = commandListener;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        if (isAvailable != mIsAvailable) {
            mIsAvailable = isAvailable;
            if (mCommandListener != null) {
                mCommandListener.onIsAvailableChanged(isAvailable);
            }
        }
    }

    public boolean isExecutable() {
        return mIsExecutable && isAvailable();
    }

    public void setIsExecutable(boolean isExecutable) {
        if (isExecutable != mIsExecutable) {
            mIsExecutable = isExecutable;
            if (mCommandListener != null) {
                mCommandListener.onIsExecutableChanged(isExecutable);
            }
        }
    }

    public abstract void execute(T parameter);
}

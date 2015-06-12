package de.fau.cs.mad.fablab.android.viewmodel.common;

public abstract class Command<T> {

    private CommandListener commandListener;
    private boolean isAvailable = true;
    private boolean isExecutable = true;

    public void setListener(CommandListener commandListener)
    {
        this.commandListener = commandListener;
    }

    public boolean isAvailable()
    {
        return isAvailable;
    }

    public boolean isExecutable()
    {
        return isExecutable && isAvailable();
    }

    public void setAvailable(boolean isAvailable)
    {
        if(isAvailable != this.isAvailable)
        {
            this.isAvailable = isAvailable;
            commandListener.onIsAvailableChanged(isAvailable);
        }

    }

    public void setIsExecutable(boolean isExecutable) {
        if(isExecutable != this.isExecutable)
        {
            this.isExecutable = isExecutable;
            commandListener.onIsExecutableChanged(isExecutable);
        }
    }

    public abstract void execute(T parameter);
}




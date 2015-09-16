package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.UserModel;
import de.fau.cs.mad.fablab.android.model.events.NavigationEventInventory;
import de.fau.cs.mad.fablab.android.model.events.UserRetrievedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Roles;
import de.greenrobot.event.EventBus;

public class InventoryLoginFragmentViewModel {

    private Listener mListener;
    private UserModel mModel;
    private EventBus mEventBus = EventBus.getDefault();

    private final Command<Void> mLoginCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mModel.getUser(mListener.getUsername(), mListener.getPassword());
            }
        }
    };

    private final Command<Void> mLoginQrCodeScannerCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onLoginQrCodeClicked();
            }
        }
    };

    @Inject
    public InventoryLoginFragmentViewModel(UserModel model)
    {
        mModel = model;
        mEventBus.register(this);
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public Command<Void> getLoginCommand()
    {
        return mLoginCommand;
    }

    public Command<Void> getLoginQrCodeScannerCommand()
    {
        return mLoginQrCodeScannerCommand;
    }

    public void onEvent(UserRetrievedEvent event) {
        if(mListener != null) {
            if (!event.getUser().getRoles().contains(Roles.INVENTORY)) {
                mListener.userUnauthorized();
            }
            else
            {
                mEventBus.post(new NavigationEventInventory(event.getUser()));
            }
            EventBus.getDefault().cancelEventDelivery(event);
        }
    }

    public interface Listener {

        String getUsername();

        String getPassword();

        void userUnauthorized();

        void onLoginQrCodeClicked();
    }
}

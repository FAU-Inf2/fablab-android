package de.fau.cs.mad.fablab.android.view.fragments.projects;

import de.fau.cs.mad.fablab.android.model.entities.Cart;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class CartViewModel {

    private Cart mCart;

    private Command<Void> mCreateProjectFromCartCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new CartClickedEvent(mCart));
        }
    };

    public CartViewModel(Cart cart)
    {
        mCart = cart;
    }

    public Command<Void> getCreateProjectFromCartCommand()
    {
        return mCreateProjectFromCartCommand;
    }

    public int getItemCount()
    {
        return mCart.getEntries().size();
    }

    public double getCartPrice()
    {
        return mCart.getTotalPrice();
    }
}

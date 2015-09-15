package de.fau.cs.mad.fablab.android.view.cartpanel;

import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.util.Formatter;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class CartEntryViewModel {
    private CartEntry mCartEntry;

    private final Command<Void> mShowDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new CartEntryClickedEvent(mCartEntry));
        }
    };

    public CartEntryViewModel(CartEntry cartEntry) {
        this(cartEntry, true);
    }

    public CartEntryViewModel(CartEntry cartEntry, boolean isEditable) {
        mCartEntry = cartEntry;
        if (!isEditable) {
            mShowDialogCommand.setIsExecutable(false);
        }
    }

    public Command<Void> getShowDialogCommand() {
        return mShowDialogCommand;
    }

    public CartEntry getCartEntry() {
        return mCartEntry;
    }

    public String getProductName() {
        return Formatter.formatProductName(mCartEntry.getProduct().getName())[0];
    }

    public String getProductDetails() {
        return Formatter.formatProductName(mCartEntry.getProduct().getName())[1];
    }

    public String getUnit() {
        return mCartEntry.getProduct().getUnit();
    }

    public boolean isDecimalAmount() {
        return mCartEntry.getProduct().getUom().getRounding() != 1.0;
    }

    public double getAmount() {
        return mCartEntry.getAmount();
    }

    public double getTotalPrice() {
        return mCartEntry.getTotalPrice();
    }
}

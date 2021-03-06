package de.fau.cs.mad.fablab.android.view.fragments.cart;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.entities.CartEntry;
import de.fau.cs.mad.fablab.android.view.cartpanel.CartEntryUpdatedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class AddToCartDialogFragmentViewModel
{
    public static final String KEY_CART_ENTRY = "cart_entry";
    private static final String KEY_AMOUNT = "amount";

    @Inject
    CartModel mCartModel;

    private CartEntry mCartEntry;
    private double mAmount;

    private Listener mListener;

    private final Command<Void> mAddToCartCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter)
        {
            if (mAmount != 0)
            {
                mCartEntry.setAmount(mAmount);
                mCartModel.addEntry(mCartEntry);
            }
            if (mListener != null)
            {
                mListener.onDismiss();
            }
        }
    };

    private final Command<Void> mUpdateCommand = new Command<Void>()
    {
        @Override
        public void execute(Void parameter)
        {
            if (mAmount != 0)
            {
                mCartEntry.setAmount(mAmount);
                mCartModel.updateEntry(mCartEntry);
                EventBus.getDefault().post(new CartEntryUpdatedEvent(mCartEntry));
            } else
            {
                mCartModel.removeEntry(mCartEntry);
            }
            if (mListener != null)
            {
                mListener.onDismiss();
            }
        }
    };

    private final Command<String> mChangeAmountCommand = new Command<String>()
    {
        @Override
        public void execute(String parameter)
        {
            double value = 0;
            if (!(parameter.equals("") || parameter.equals(".")))
            {
                value = Double.parseDouble(parameter);
            }
            int tmp = 100;
            double rounding = mCartEntry.getProduct().getUom().getRounding();
            mAmount = ((rounding*tmp) * Math.ceil(Math.round(value/rounding)))/tmp;
            if (mListener != null)
            {
                mListener.onUpdatePriceAndAmount(getPriceTotal(), (value != mAmount) ? mAmount : -1);
            }
        }
    };

    private final Command<Void> mIncreaseAmountByOneCommand = new Command<Void>()
    {
        @Override
        public void execute(Void editText)
        {
            int tmp = 100;
            double rounding = mCartEntry.getProduct().getUom().getRounding();
            mAmount = mAmount + rounding;
            mAmount = ((rounding*tmp) * Math.ceil(Math.round(mAmount/rounding)))/tmp;
            mListener.onChangeEditText( mAmount);
        }
    };

    private final Command<Void> mDecreaseAmountByOneCommand = new Command<Void>()
    {
        @Override
        public void execute(Void editText)
        {
            int tmp = 100;
            double rounding = mCartEntry.getProduct().getUom().getRounding();

            if((mAmount - rounding) >= 0)
            {
                mAmount = mAmount - rounding;
                mAmount = ((rounding*100) * Math.ceil(Math.round(mAmount/rounding)))/tmp;
                mListener.onChangeEditText(mAmount);
            }
        }
    };

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public Command<Void> getAddToCartCommand()
    {
        return mAddToCartCommand;
    }

    public Command<Void> getUpdateCartEntryCommand()
    {
        return mUpdateCommand;
    }

    public Command<String> getChangeAmountCommand()
    {
        return mChangeAmountCommand;
    }

    public Command<Void> getIncreaseAmountByOneCommand()
    {
        return mIncreaseAmountByOneCommand;
    }

    public Command<Void> getDecreaseAmountByOneCommand()
    {
        return mDecreaseAmountByOneCommand;
    }

    public String getName()
    {
        return mCartEntry.getProduct().getName();
    }

    public double getPrice()
    {
        return mCartEntry.getProduct().getPrice();
    }

    public String getUnit()
    {
        return mCartEntry.getProduct().getUnit();
    }

    public boolean isDecimalAmount()
    {
        return mCartEntry.getProduct().getUom().getRounding() != 1;
    }

    public double getAmount()
    {
        return mAmount;
    }

    public double getPriceTotal()
    {
        return mAmount * getPrice();
    }

    public boolean isUpdate()
    {
        return mCartEntry.getAmount() != 0;
    }

    public void saveState(Bundle outState)
    {
        outState.putDouble(KEY_AMOUNT, mAmount);
    }

    public void restoreState(Bundle arguments, Bundle savedInstanceState)
    {
        mCartEntry = (CartEntry) arguments.getSerializable(KEY_CART_ENTRY);
        if (savedInstanceState != null)
        {
            mAmount = savedInstanceState.getDouble(KEY_AMOUNT);
        } else
        {
            mAmount = mCartEntry != null ? mCartEntry.getAmount() : 0;
        }
    }

    public interface Listener
    {
        void onDismiss();

        void onUpdatePriceAndAmount(double priceTotal, double amount);

        void onChangeEditText(double amountNotRounded);
    }
}

package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.InventoryModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.InventoryItem;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.core.User;

public class AddToInventoryDialogFragmentViewModel {

    public static final String KEY_PRODUCT = "product";
    private static final String KEY_AMOUNT = "amount";
    public static final String KEY_USER = "user";

    private Product mProduct;
    private double mAmount;

    private Listener mListener;
    private InventoryModel mModel;
    private User mUser;

    private final Command<Void> mAddToInventoryCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {

            mModel.sendInventoryItem(createInventoryItem(), mUser.getUsername(), mUser.getPassword());

            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    };

    private final Command<Integer> mChangeAmountCommand = new Command<Integer>() {
        @Override
        public void execute(Integer parameter) {
            mAmount = parameter;
        }
    };

    @Inject
    public AddToInventoryDialogFragmentViewModel(InventoryModel model)
    {
        mModel = model;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setUser(User user)
    {
        mUser = user;
    }

    public User getUser()
    {
        return mUser;
    }

    public String getName() {
        return mProduct.getName();
    }

    public double getAmount() {
        return mAmount;
    }

    public Command<Void> getAddToInventoryCommand()
    {
        return mAddToInventoryCommand;
    }

    public Command<Integer> getChangeAmountCommand() {
        return mChangeAmountCommand;
    }

    public void saveState(Bundle outState) {
        outState.putDouble(KEY_AMOUNT, mAmount);
    }

    public void restoreState(Bundle arguments, Bundle savedInstanceState) {
        mProduct = (Product) arguments.getSerializable(KEY_PRODUCT);
        mUser = (User) arguments.getSerializable(KEY_USER);
        if (savedInstanceState != null) {
            mAmount = savedInstanceState.getDouble(KEY_AMOUNT);
        } else {
            mAmount = 1;
        }
    }

    private InventoryItem createInventoryItem()
    {
        return new InventoryItem(mUser.getUsername(), mListener.getUUID(), mProduct.getName(), mProduct.getProductId(), mAmount);
    }

    public interface Listener {
        void onDismiss();

        String getUUID();
    }
}

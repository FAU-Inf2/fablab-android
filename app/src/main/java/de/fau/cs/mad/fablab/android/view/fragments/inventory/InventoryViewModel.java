package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import java.util.Date;

import de.fau.cs.mad.fablab.rest.core.InventoryItem;

public class InventoryViewModel {

    private InventoryItem mInventoryItem;

    public InventoryViewModel(InventoryItem item)
    {
        mInventoryItem = item;
    }

    public String getName()
    {
        return mInventoryItem.getProductName();
    }

    public String getID()
    {
        return mInventoryItem.getProductId();
    }

    public String getUser()
    {
        return mInventoryItem.getUserName();
    }

    public double getAmount()
    {
        return mInventoryItem.getAmount();
    }

    public Date getUpdatedAt()
    {
        return mInventoryItem.getUpdated_at();
    }
}

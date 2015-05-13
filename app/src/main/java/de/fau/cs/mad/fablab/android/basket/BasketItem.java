package de.fau.cs.mad.fablab.android.basket;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "basket")
public class BasketItem {
    @DatabaseField(id = true)
    private int productId;

    @DatabaseField
    private int quantity;

    public BasketItem() {

    }

    public BasketItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

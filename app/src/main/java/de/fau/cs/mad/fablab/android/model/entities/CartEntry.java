package de.fau.cs.mad.fablab.android.model.entities;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.fau.cs.mad.fablab.rest.core.Product;

/**
 * This class represents an entry in a shopping cart, specifying how many products are in a cart
 */

@Entity
@Table(name = "cart_entry")
public class CartEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @DatabaseField(foreign = true, canBeNull = false)
    private Cart cart;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Product product;

    @Column(name = "amount")
    private double amount;

    // required no-arg constructor for ORM lite
    public CartEntry() {
    }

    public CartEntry(Product product, double amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return product.getPrice() * amount;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other != null && getClass() == other.getClass()
                && id == ((CartEntry) other).id);
    }
}

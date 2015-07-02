package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

public class ProductSearchViewModel {

    private Product mProduct;

    private Command<Void> mShowDialogCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new ProductClickedEvent(mProduct));
        }
    };

    public ProductSearchViewModel(Product product) {
        mProduct = product;
    }

    public Command<Void> getShowDialogCommand() {
        return mShowDialogCommand;
    }

    public String getName() {
        return mProduct.getName();
    }

    public String getPrice() {
        return mProduct.getPrice()+"";
    }

    public String getUnit() {
        return mProduct.getUnit();
    }
}

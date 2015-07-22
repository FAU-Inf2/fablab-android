package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.regex.Pattern;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.events.ProductFoundEvent;
import de.fau.cs.mad.fablab.android.model.events.ProductNotFoundEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

public class BarcodeScannerFragmentViewModel {
    @Inject
    ProductModel mProductModel;

    private Listener mListener;

    private EventBus mEventBus = EventBus.getDefault();

    private final Pattern mBarcodePattern = Pattern.compile("200(00000)?\\d{5}");
    private final Command<Result> mProcessBarcodeCommand = new Command<Result>() {
        @Override
        public void execute(Result result) {
            mProcessBarcodeCommand.setIsExecutable(false);

            String barcode = result.getText();
            boolean isEan8 = BarcodeFormat.EAN_8.equals(result.getBarcodeFormat());
            boolean isEan13 = BarcodeFormat.EAN_13.equals(result.getBarcodeFormat());

            if ((isEan8 || isEan13) && mBarcodePattern.matcher(barcode).matches()) {
                String productId;
                if (isEan13) {
                    productId = barcode.substring(8, 12);
                } else {
                    productId = barcode.substring(3, 7);
                }

                mProductModel.findProductById(productId);
            } else {
                mProcessBarcodeCommand.setIsExecutable(true);
                if (mListener != null) {
                    mListener.onShowInvalidBarcodeMessage();
                }
            }
        }
    };

    @Inject
    public BarcodeScannerFragmentViewModel() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Result> getProcessBarcodeCommand() {
        return mProcessBarcodeCommand;
    }

    public void resume() {
        mProcessBarcodeCommand.setIsAvailable(true);
        mEventBus.register(this);
    }

    public void pause() {
        mProcessBarcodeCommand.setIsAvailable(false);
        mEventBus.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(ProductFoundEvent event) {
        mProcessBarcodeCommand.setIsExecutable(true);
        if (mListener != null) {
            mListener.onShowAddToCartDialog(event.getProduct());
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ProductNotFoundEvent event) {
        mProcessBarcodeCommand.setIsExecutable(true);
        if (mListener != null) {
            mListener.onShowProductNotFoundMessage();
        }
    }

    public interface Listener {
        void onShowAddToCartDialog(Product product);
        void onShowProductNotFoundMessage();
        void onShowInvalidBarcodeMessage();
    }
}

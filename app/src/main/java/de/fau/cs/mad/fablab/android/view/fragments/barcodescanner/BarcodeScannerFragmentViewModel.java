package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.regex.Pattern;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

public class BarcodeScannerFragmentViewModel {
    @Inject
    ProductModel mProductModel;

    private Listener mListener;

    private EventBus mEventBus;

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

                Product product = mProductModel.findProductById(productId);
                mProcessBarcodeCommand.setIsExecutable(true);
                if (product != null) {
                    mEventBus.post(new ProductFoundEvent(product));
                } else {
                    if (mListener != null) {
                        mListener.onShowProductNotFoundMessage();
                    }
                }
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
        mEventBus = EventBus.getDefault();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Result> getProcessBarcodeCommand() {
        return mProcessBarcodeCommand;
    }

    public void resume() {
        mProcessBarcodeCommand.setIsAvailable(true);
    }

    public void pause() {
        mProcessBarcodeCommand.setIsAvailable(false);
    }

    public interface Listener {
        void onShowProductNotFoundMessage();
        void onShowInvalidBarcodeMessage();
    }
}

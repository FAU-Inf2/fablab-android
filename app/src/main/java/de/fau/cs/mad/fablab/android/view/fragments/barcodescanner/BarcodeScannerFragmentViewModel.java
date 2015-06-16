package de.fau.cs.mad.fablab.android.view.fragments.barcodescanner;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.regex.Pattern;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BarcodeScannerFragmentViewModel {
    @Inject
    ProductApi mProductApi;
    @Inject
    BarcodeScannerViewLauncher mViewLauncher;

    private Listener mListener;

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

                mProductApi.findById(productId, new Callback<Product>() {
                    @Override
                    public void success(Product product, Response response) {
                        mProcessBarcodeCommand.setIsExecutable(true);
                        mViewLauncher.showAddToCartDialogFragment(product);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProcessBarcodeCommand.setIsExecutable(true);
                        if (mListener != null) {
                            mListener.onShowProductNotFoundMessage();
                        }
                    }
                });
            } else {
                mProcessBarcodeCommand.setIsExecutable(true);
                if (mListener != null) {
                    mListener.onShowInvalidBarcodeMessage();
                }
            }
        }
    };

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

package de.fau.cs.mad.fablab.android.view.fragments.checkout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.regex.Pattern;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class QrCodeScannerFragmentViewModel {
    private Listener mListener;

    private Pattern mQrCodePattern = Pattern.compile("(FAU)(-)?\\d{1,19}");
    private final Command<Result> mProcessQrCodeCommand = new Command<Result>() {
        @Override
        public void execute(Result result) {
            mProcessQrCodeCommand.setIsExecutable(false);

            String qrCodeText = result.getText();

            if (mQrCodePattern.matcher(qrCodeText).matches()
                    && BarcodeFormat.QR_CODE.equals(result.getBarcodeFormat())) {
                mProcessQrCodeCommand.setIsExecutable(true);
                if (mListener != null) {
                    mListener.onStartCheckout(qrCodeText);
                }
            } else {
                mProcessQrCodeCommand.setIsExecutable(true);
                if (mListener != null) {
                    mListener.onShowInvalidQrCodeMessage();
                }
            }
        }
    };

    @Inject
    public QrCodeScannerFragmentViewModel() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public Command<Result> getProcessQrCodeCommand() {
        return mProcessQrCodeCommand;
    }

    public void pause() {
        mProcessQrCodeCommand.setIsAvailable(false);
    }

    public void resume() {
        mProcessQrCodeCommand.setIsAvailable(true);
    }

    public interface Listener {
        void onShowInvalidQrCodeMessage();

        void onStartCheckout(String cartCode);
    }
}
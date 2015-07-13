package de.fau.cs.mad.fablab.android.view.fragments.checkout;

public class QrCodeScannedEvent {
    private final String mQrCodeText;

    public QrCodeScannedEvent(String qrCodeText) {
        mQrCodeText = qrCodeText;
    }

    public String getQrCodeText() {
        return mQrCodeText;
    }
}

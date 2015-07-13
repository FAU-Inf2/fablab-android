package de.fau.cs.mad.fablab.android.view.common.binding;

import com.google.zxing.Result;

import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.CommandListener;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerViewCommandBinding implements Binding<ZXingScannerView, Result>,
        CommandListener, ZXingScannerView.ResultHandler {
    private Command<Result> mCommand;
    private ZXingScannerView mScannerView;

    @Override
    public void bind(ZXingScannerView view, Command<Result> command) {
        mCommand = command;
        mScannerView = view;

        mCommand.setListener(this);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onIsAvailableChanged(boolean newValue) {
        if (newValue) {
            mScannerView.startCamera();
        } else {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onIsExecutableChanged(boolean newValue) {
        mScannerView.setResultHandler(newValue ? this : null);
    }

    @Override
    public void handleResult(Result result) {
        if (mCommand.isExecutable()) {
            mCommand.execute(result);
        }
        mScannerView.startCamera();
    }
}

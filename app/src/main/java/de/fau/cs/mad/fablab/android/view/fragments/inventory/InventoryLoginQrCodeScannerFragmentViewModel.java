package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.UserModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class InventoryLoginQrCodeScannerFragmentViewModel {

    private Listener mListener;
    private UserModel mUserModel;

    private final Command<Result> mProcessQrCodeCommand = new Command<Result>() {
        @Override
        public void execute(Result result) {
            mProcessQrCodeCommand.setIsExecutable(false);
            try {
                JSONObject obj = new JSONObject(result.getText());
                String username = obj.getString("username");
                String password = obj.getString("password");
                mUserModel.getUser(username, password);
            } catch (JSONException e) {
                mProcessQrCodeCommand.setIsExecutable(true);
                if (mListener != null) {
                    mListener.onShowInvalidQrCodeMessage();
                }
            }
        }
    };

    @Inject
    public InventoryLoginQrCodeScannerFragmentViewModel(UserModel userModel) {
        mUserModel = userModel;
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
    }
}

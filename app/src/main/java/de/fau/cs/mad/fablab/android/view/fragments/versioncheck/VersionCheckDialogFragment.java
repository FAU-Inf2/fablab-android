package de.fau.cs.mad.fablab.android.view.fragments.versioncheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class VersionCheckDialogFragment extends BaseDialogFragment {
    private static final String KEY_UPDATE_REQUIRED = "key_update_availability";
    private static final String KEY_UPDATE_MESSAGE = "key_update_message";

    public static VersionCheckDialogFragment newInstance(boolean updateRequired,
                                                         String updateMessage) {
        VersionCheckDialogFragment fragment = new VersionCheckDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(KEY_UPDATE_REQUIRED, updateRequired);
        args.putString(KEY_UPDATE_MESSAGE, updateMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setTitle(getArguments().getBoolean(KEY_UPDATE_REQUIRED)
                ? getString(R.string.version_check_update_required)
                : getString(R.string.version_check_update_optional));
        builder.setMessage(getArguments().getString(KEY_UPDATE_MESSAGE));
        AlertDialog dialog = builder.create();
        dialog.show();

        UiUtils.changeDialogTitleColor(dialog);
        return dialog;
    }
}

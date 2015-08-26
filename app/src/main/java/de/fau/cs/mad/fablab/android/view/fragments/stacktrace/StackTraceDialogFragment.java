package de.fau.cs.mad.fablab.android.view.fragments.stacktrace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.MailModel;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class StackTraceDialogFragment extends BaseDialogFragment {
    private static final String KEY_STACK_TRACE = "key_stack_trace";

    @Inject
    MailModel mModel;

    public static StackTraceDialogFragment newInstance(String stackTrace) {
        StackTraceDialogFragment fragment = new StackTraceDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_STACK_TRACE, stackTrace);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", mModel.getFablabMailAddress(), null));
                String subject = getString(R.string.stacktrace_messaging_subject);
                String body = getArguments().getString(KEY_STACK_TRACE) + "\n\n";

                sendIntent.putExtra(Intent.EXTRA_TEXT, body);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                startActivity(Intent.createChooser(sendIntent, getString(
                        R.string.stacktrace_messaging_chooser_title)));
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setTitle(getString(R.string.stacktrace_messaging_dialog_title));
        builder.setMessage(getString(R.string.stacktrace_messaging_dialog_text));
        AlertDialog dialog = builder.create();
        dialog.show();

        UiUtils.changeDialogTitleColor(dialog);
        return dialog;
    }
}

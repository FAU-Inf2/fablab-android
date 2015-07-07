package de.fau.cs.mad.fablab.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.fau.cs.mad.fablab.android.R;

public class StackTraceReporter {
    public static void reportStackTraceIfAvailable(final Activity activity) {
        // see if stracktrace file is available
        String line;
        String trace = "";
        boolean traceAvailable = true;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.openFileInput("stack.trace")));
            while ((line = reader.readLine()) != null) {
                trace += line + "\n";
            }
        } catch (IOException ioe) {
            traceAvailable =false;
        }

        // if yes ask user to send stacktrace
        if (traceAvailable) {

            final String traceFinal = trace;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setPositiveButton(R.string.stacktrace_messaging_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", " fablab@i2.cs.fau.de", null));
                    String subject = activity.getString(R.string.stacktrace_messaging_subject);
                    String body = traceFinal + "\n\n";

                    sendIntent.putExtra(Intent.EXTRA_TEXT, body);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                    activity.startActivity(Intent.createChooser(sendIntent, activity.getString(R.string.stacktrace_messaging_chooser_title)));
                }
            });
            builder.setNegativeButton(R.string.stacktrace_messaging_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            builder.setTitle(activity.getString(R.string.stacktrace_messaging_dialog_title));
            builder.setMessage(activity.getString(R.string.stacktrace_messaging_dialog_text));
            AlertDialog dialog = builder.create();
            dialog.show();

            UiUtils.changeDialogTitleColor(dialog);
            activity.deleteFile("stack.trace");
        }
    }
}

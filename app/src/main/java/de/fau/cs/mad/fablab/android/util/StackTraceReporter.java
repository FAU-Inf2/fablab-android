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
import de.fau.cs.mad.fablab.android.model.util.RestClient;
import de.fau.cs.mad.fablab.rest.myapi.DataApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StackTraceReporter {

    private static String mMailAddress = "";
    private static DataApi mDataApi;
    private static Activity mActivity;
    private static String mTrace;

    private static Callback<String> mMailAddressCallback = new Callback<String>() {
        @Override
        public void success(String address, Response response) {
            mMailAddress = address;
            final String traceFinal = mTrace;
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setPositiveButton(R.string.stacktrace_messaging_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", mMailAddress, null));
                    String subject = mActivity.getString(R.string.stacktrace_messaging_subject);
                    String body = traceFinal + "\n\n";

                    sendIntent.putExtra(Intent.EXTRA_TEXT, body);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                    mActivity.startActivity(Intent.createChooser(sendIntent, mActivity.getString(R.string.stacktrace_messaging_chooser_title)));
                }
            });
            builder.setNegativeButton(R.string.stacktrace_messaging_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            builder.setTitle(mActivity.getString(R.string.stacktrace_messaging_dialog_title));
            builder.setMessage(mActivity.getString(R.string.stacktrace_messaging_dialog_text));
            AlertDialog dialog = builder.create();
            dialog.show();

            UiUtils.changeDialogTitleColor(dialog);
        }

        @Override
        public void failure(RetrofitError error) {
        }
    };


    public static void reportStackTraceIfAvailable(final Activity activity) {
        System.out.println("REPORT STACKTRACE IF AVAILABLE REACHED");
        mActivity = activity;
        // see if stracktrace file is available
        String line;
        mTrace = "";
        boolean traceAvailable = true;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.openFileInput("stack.trace")));
            while ((line = reader.readLine()) != null) {
                mTrace += line + "\n";
            }
        } catch (IOException ioe) {
            traceAvailable =false;
        }

        // if yes ask user to send stacktrace
        if (traceAvailable) {

            mDataApi = new RestClient(activity.getApplicationContext(), true).getDataApi();
            mDataApi.getFabLabMailAddress(mMailAddressCallback);

            activity.deleteFile("stack.trace");
        }
    }
}



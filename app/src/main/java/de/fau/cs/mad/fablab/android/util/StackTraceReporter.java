package de.fau.cs.mad.fablab.android.util;

import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.fau.cs.mad.fablab.android.view.fragments.stacktrace.StackTraceDialogFragment;

public class StackTraceReporter {
    public static void reportStackTraceIfAvailable(final AppCompatActivity activity) {
        // see if stacktrace file is available
        String trace = "";
        boolean traceAvailable = true;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.openFileInput(
                    "stack.trace")));
            String line;
            while ((line = reader.readLine()) != null) {
                trace += line + "\n";
            }
        } catch (IOException ioe) {
            traceAvailable = false;
        }

        // if yes ask user to send stacktrace
        if (traceAvailable) {
            StackTraceDialogFragment.newInstance(trace).show(activity.getSupportFragmentManager(),
                    "stacktrace");

            activity.deleteFile("stack.trace");
        }
    }
}

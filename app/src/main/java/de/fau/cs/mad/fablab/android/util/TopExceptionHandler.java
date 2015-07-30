package de.fau.cs.mad.fablab.android.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.io.FileOutputStream;
import java.io.IOException;

public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;
    private Activity app = null;

    public TopExceptionHandler(Activity app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    public void uncaughtException(Thread t, Throwable e) {

        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString() + "\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i = 0; i < arr.length; i++) {
            report += "    " + arr[i].toString() + "\n";
        }
        report += "-------------------------------\n\n";

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause
        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if (cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report += "    " + arr[i].toString() + "\n";
            }
        }
        report += "-------------------------\n\n";

        report += "--------- System properties ---------\n\n";
        report += "Device name: " + getDeviceName() + "\n";
        report += "Android version: " + getAndroidVersion() + "\n";
        report += "-------------------------------------\n";

        try {
            FileOutputStream trace = app.openFileOutput(
                    "stack.trace", Context.MODE_PRIVATE);
            trace.write(report.getBytes());
            trace.close();
        } catch (IOException ioe) {
            // ...
        }

        defaultUEH.uncaughtException(t, e);
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion + " (" + release +")";
    }
}

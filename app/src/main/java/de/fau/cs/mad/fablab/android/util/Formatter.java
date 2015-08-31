package de.fau.cs.mad.fablab.android.util;

import android.text.Html;

public class Formatter {
    public static String formatPrice(double price) {
        return String.format("%.2f", price) + Html.fromHtml("&nbsp;€");
    }

    public static String[] formatProductName(String productName) {
        String[] splitProductName = productName.split(" ");
        String formattedProductName = "";
        String formattedProductDetail = "";
        for (int i = 0; i < splitProductName.length; i++) {
            if (i == 0) {
                formattedProductName = splitProductName[i];
            } else {
                formattedProductDetail += splitProductName[i]
                        + " ";
            }
        }
        return new String[]{formattedProductName, formattedProductDetail};
    }

    public static String formatTime(long time) {
        if (time < 0) {
            return "";
        } else if (time < 60) {
            return time + "m";
        } else if (time < (60 * 24)) {
            long hours = time / 60;
            long minutes = time % 60;
            if (minutes == 0) {
                return hours + "h";
            } else {
                return hours + "h " + minutes + "m";
            }
        } else {
            return "";
        }
    }
}

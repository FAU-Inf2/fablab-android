package de.fau.cs.mad.fablab.android.util;

import android.text.Html;

public class Formatter {
    public static String formatPrice(double price) {
        return String.format("%.2f", price) + Html.fromHtml("&nbsp;€");
    }
}

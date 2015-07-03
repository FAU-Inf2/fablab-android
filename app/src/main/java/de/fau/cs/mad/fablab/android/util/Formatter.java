package de.fau.cs.mad.fablab.android.util;

import android.text.Html;

public class Formatter {
    public static String formatPrice(double price) {
        return String.format("%.2f", price) + Html.fromHtml("&nbsp;€");
    }

    public static String[] formatProductName(String productName){
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
}

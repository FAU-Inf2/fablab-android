package de.fau.cs.mad.fablab.android.productMap;


import android.location.Location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LocationParser
{
    //String teststring = "tats\u00e4chliche Lagerorte / FAU FabLab / Elektrowerkstatt / Regal / Kiste Spaxschrauben";

    private LocationParser(){   }

    public static ProductLocation getLocation(String locationString)
    {
        ProductLocation productLocation = null;

        if(locationString != null)
        {
            String splitedString[] = splitLocationString(deleteWhitespaces(locationString));

            String identificationCode = extractIdentificationCode(splitedString[splitedString.length - 1]);

            if (identificationCode.equals(""))
            {
                for (ProductLocation loc : ProductLocation.values())
                {
                    if (splitedString[splitedString.length - 1].equals(loc.getLocationName()))
                    {
                        productLocation = loc;
                    }
                }
                // maybe the second last location name is known
                if (productLocation == null && splitedString.length > 2)
                    for (ProductLocation loc : ProductLocation.values())
                    {
                        if (splitedString[splitedString.length - 2].equals(loc.getLocationName()))
                        {
                            productLocation = loc;
                        }
                    }
            }


        }

        return productLocation;
    }

    private static String deleteWhitespaces(String locationString)
    {
        String result = locationString.replaceAll(" / ", "/");
        return result;
    }

    private static String[] splitLocationString(String locationString)
    {
        String[] parts = locationString.split("/");
        return parts;
    }

    private static String extractIdentificationCode(String lastSplitString)
    {
        String result = "";
        String regex = "\\s\\([A-Z]\\d*(\\.\\d)?\\)"; // ...Regal (K) oder ...Halter (E6.1) oder .. (D1) oder ... (W10)

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(lastSplitString);

        if(m.find())
            result = m.group();

        return result;
    }

    private static ProductLocation convertIdentificationCodeToLocation(String identificationCode)
    {
        ProductLocation productLocation = null;
        for (ProductLocation loc : ProductLocation.values())
        {
            if(identificationCode.equals(loc.getIdentificationCode()))
            {
                productLocation = loc;
            }
        }
        return productLocation;
    }

}

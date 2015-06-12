package de.fau.cs.mad.fablab.android.productMap;


public final class LocationParser
{
    //String teststring = "tats\u00e4chliche Lagerorte / FAU FabLab / Elektrowerkstatt / Regal / Kiste Spaxschrauben";
    private LocationParser(){   }

    public static ProductLocation getLocation(String locationString)
    {
        ProductLocation productLocation = null;

        if(locationString != null)
        {
            String result[] = splitLocationString(deleteWhitespaces(locationString));
            for (ProductLocation loc : ProductLocation.values())
            {
                if (result[result.length - 1].equals(loc.getLocationName()))
                {
                    productLocation = loc;
                }
            }
            // maybe the second last location name is known
            if(productLocation == null && result.length > 2)
            for (ProductLocation loc : ProductLocation.values())
            {
                if(result[result.length - 2].equals(loc.getLocationName()))
                {
                    productLocation = loc;
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


}

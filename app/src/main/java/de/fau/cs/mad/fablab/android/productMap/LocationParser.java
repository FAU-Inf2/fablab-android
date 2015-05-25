package de.fau.cs.mad.fablab.android.productMap;

/**
 * Created by Michael on 22.05.2015.
 */
public final class LocationParser
{
    String teststring = "tatsächliche Lagerorte / FAU FabLab / Elektrowerkstatt / Regal / Kiste Spaxschrauben";
    private LocationParser(){   }

    public static ProductLocation getLocation(String locationString)
    {
        ProductLocation productLocation = null;
        String result[] = splitLocationString(deleteWhitespaces(locationString));
        for(ProductLocation loc : ProductLocation.values())
        {
            if(result[result.length - 1].equals(loc.getLocationName()))
            {
                productLocation = loc;
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

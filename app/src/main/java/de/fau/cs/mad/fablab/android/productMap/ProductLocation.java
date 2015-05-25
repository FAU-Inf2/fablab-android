package de.fau.cs.mad.fablab.android.productMap;

/**
 * Created by Michael on 19.05.2015.
 */
public enum ProductLocation
{
    //Level 0
    FABLAB(FablabView.MAIN_ROOM, null, 0, 0, 0, 0, "FAU Fablab"),
    //BASEMENT_STOCK(),

    //Level 1
    ELECTRIC_WORKSHOP(FablabView.ELECTRIC_WORKSHOP, FABLAB, 0, 0, 300, 50, "Elektrowerkstatt"),
    //CHEMISTRY_BENCH(),
    //ENGINE_LATHE(),

    //SCREW_SHELF(),
    //WORK_BENCH(),

    //VITRINE(),
    //CUTTER_BOX(),


    //Level 2
    //ACRYLIC_GLAS_SHELF(),
    SHELF(FablabView.ELECTRIC_WORKSHOP, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX() + 300, ELECTRIC_WORKSHOP.getMainPositionY(), "Regal"),

    //Level 3
    BOX_SPAX_SCREW(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Spaxschrauben");


    private FablabView fablabView;

    private ProductLocation parent;

    private int viewPositionX;
    private int viewPositionY;

    private int mainPositionX;
    private int mainPositionY;

    private String locationName;

    private ProductLocation(FablabView fablabView, ProductLocation parent, int viewPositionX, int viewPositionY, int mainPositionX, int mainPositionY, String locationName)
    {
        this.fablabView = fablabView;

        this.parent = parent;

        this.viewPositionX = viewPositionX;
        this.viewPositionY = viewPositionY;

        this.mainPositionX = mainPositionX;
        this.mainPositionY = mainPositionY;

        this.locationName = locationName;
    }

    public int getViewPositionX() {
        return viewPositionX;
    }
    public int getViewPositionY() {
        return viewPositionY;
    }
    public int getMainPositionX() {
        return mainPositionX;
    }
    public int getMainPositionY() {
        return mainPositionY;
    }
    public FablabView getView() {
        return fablabView;
    }
    public String getLocationName() {
        return  locationName;
    }
}

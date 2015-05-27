package de.fau.cs.mad.fablab.android.productMap;


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
    ACRYLIC_GLAS_SHELF(FablabView.ACRYLIC_GLAS_SHELF, ELECTRIC_WORKSHOP, 0, 0, 0, 0,"Plexiglasregal" ),
    SHELF(FablabView.ELECTRIC_WORKSHOP, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX() + 300, ELECTRIC_WORKSHOP.getMainPositionY(), "Regal"),

    //Level 3
    BOX_SPAX_SCREW(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Spaxschrauben"),
    ACRYLIC_GLAS(FablabView.ACRYLIC_GLAS_SHELF, ACRYLIC_GLAS_SHELF, 0.20, 0.65, 0.60, 0.45, "Plexiglas");


    private FablabView fablabView;

    private ProductLocation parent;

    /*
    ######################
    #          y         #
    #                    #      x coordinate to the right and
    #          |         #      y coordinate to the bottom
    #         \|/  x --> #
    #                    #      in BOTH! views. Main and specific view
    ######################
    */
    private double viewPositionX;
    private double viewPositionY;

    private double mainPositionX;
    private double mainPositionY;

    private String locationName;

    private ProductLocation(FablabView fablabView, ProductLocation parent, double viewPositionX, double viewPositionY, double mainPositionX, double mainPositionY, String locationName)
    {
        this.fablabView = fablabView;

        this.parent = parent;

        this.viewPositionX = viewPositionX;
        this.viewPositionY = viewPositionY;

        this.mainPositionX = mainPositionX;
        this.mainPositionY = mainPositionY;

        this.locationName = locationName;
    }

    public double getViewPositionX() {
        return viewPositionX;
    }
    public double getViewPositionY() {
        return viewPositionY;
    }
    public double getMainPositionX() {
        return mainPositionX;
    }
    public double getMainPositionY() {
        return mainPositionY;
    }
    public FablabView getView() {
        return fablabView;
    }
    public String getLocationName() {
        return  locationName;
    }
}

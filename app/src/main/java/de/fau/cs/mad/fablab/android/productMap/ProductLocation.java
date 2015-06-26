package de.fau.cs.mad.fablab.android.productMap;


public enum ProductLocation
{
    //Level 0
    FABLAB(FablabView.MAIN_ROOM, null, 0, 0, 0, 0, "FAU Fablab", ""),
    //BASEMENT_STOCK(),

    //Level 1
    ELECTRIC_WORKSHOP(FablabView.ELECTRIC_WORKSHOP, FABLAB, 0, 0, 0.5, 0.25, "Elektrowerkstatt", ""),

    //Level 2
    BOX_REFLOW_OVEN(null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Kiste 'Zubehör Reflow-Ofen'", ""),

    //Level 2
    DRAWER_PAPER_FOILS(null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Schublade 'Buntes Papier/Overheadfolie/Laminierfolie", ""),

    //Level 2
    BOX_TSHIRTS(null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Kiste T-Shirts", ""),

    //Level 2
    ACRYLIC_GLAS_SHELF(FablabView.ACRYLIC_GLAS_SHELF, ELECTRIC_WORKSHOP, 0, 0, 0, 0,"Plexiglasregal" ,"" ),
    //Level 3
    ACRYLIC_GLAS(FablabView.ACRYLIC_GLAS_SHELF, ACRYLIC_GLAS_SHELF, 0.20, 0.65, 0.60, 0.45, "Plexiglas" ,""),

    //Level 2
    SHELF(FablabView.ELECTRIC_WORKSHOP, ELECTRIC_WORKSHOP, 0, 0, 0.9, 0.25, "Regal" , ""),
    //Level 3
    BOX_SPAX_SCREWS(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Spaxschrauben", ""),
    BOX_CONNECTORS(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Buchsen und Stecker", ""),
    BOX_SOCKET_STRIPS(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Stift- und Buchsenleisten", ""),
    BOX_POWER_ADAPTERS(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Netzteile", ""),
    SMD_RESISTORS(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "SMD-Widerstand Sortiment 0805", ""),


    //CHEMISTRY_BENCH(),
    //Level 1
    ENGINE_LATHE(null, FABLAB, 0, 0, 0, 0, "Drehbanktisch", ""),

    //Level 2
    ENGINE_LATHE_DRAWER(null, ENGINE_LATHE, 0, 0, ENGINE_LATHE.getMainPositionX(), ENGINE_LATHE.getMainPositionY(), "Drehbankschublade", ""),

    //SCREW_SHELF(),

    //Level 1
    MAIN_ROOM(FablabView.MAIN_ROOM, FABLAB, 0, 0, 0, 0, "Hauptraum", ""),

    //Level 2
    WORK_BENCH(null, FABLAB, 0, 0, 0.5, 0.55, "Werkbank", ""),
    //Level 3
    DRAWER_10(null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 10", ""),
    DRAWER_14(null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 14", ""),
    DRAWER_15(null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 15", ""),
    DRAWER_16(null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 16", ""),
    DRAWER_17(null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 17", ""),
    DRAWER_18(null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 18", "");


    //VITRINE(),
    //CUTTER_BOX(),


    //Level 2


    //Level 3




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
    private String identificationCode;

    private ProductLocation(FablabView fablabView, ProductLocation parent, double viewPositionX, double viewPositionY, double mainPositionX, double mainPositionY, String locationName, String identificationCode)
    {
        this.fablabView = fablabView;

        this.parent = parent;

        this.viewPositionX = viewPositionX;
        this.viewPositionY = viewPositionY;

        this.mainPositionX = mainPositionX;
        this.mainPositionY = mainPositionY;

        this.locationName = locationName;
        this.identificationCode = identificationCode;
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
    public String getIdentificationCode() {return identificationCode;}
}

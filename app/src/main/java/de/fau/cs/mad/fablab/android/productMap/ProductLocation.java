package de.fau.cs.mad.fablab.android.productMap;


public enum ProductLocation
{
    //Level 0
    FABLAB(FablabView.MAIN_ROOM, null, 0, 0, 0, 0, "FAU Fablab", ""),
    //BASEMENT_STOCK(),

    //Level 1
    ELECTRIC_WORKSHOP(FablabView.ELECTRIC_WORKSHOP, FABLAB, 0, 0, 0.5, 0.25, "Elektrowerkstatt", "(E)"),

    //Level 2
    DRAWER_RUBBER           (FablabView.ELECTRIC_WORKSHOP, ELECTRIC_WORKSHOP, 0, 0, 0, 0, "Schublade Moosgummi", ""),

    //Level 2
    BOX_KABEL_FOUND         (FablabView.ELECTRIC_WORKSHOP, ELECTRIC_WORKSHOP, 0, 0, 0, 0, "Fundkabel", ""),

    //Level 2
    BOX_REFLOW_OVEN         (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Kiste 'Zubeh\u00F6r Reflow-Ofen'", ""),

    //Level 2
    SHELF_FOILS             (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Folienrack", ""),

    //Level 2
    DRAWER_PAPER_FOILS      (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Schublade 'Buntes Papier/Overheadfolie/Laminierfolien", ""),

    //Level 2
    BOX_TSHIRTS             (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Kiste T-Shirts", ""),

    //Level 2
    DRAWER_MAGAZINE         (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Schubladenmagazin", "(S)"),
    //Level 3
    SCREW_ASSORTMENT_ABOVE      (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Schraubensortiment oben", "(S3)"),
    SCREW_ASSORTMENT_MIDDLE     (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Schraubensortiment mitte", "(S2)"),
    SCREW_ASSORTMENT_BELOW      (null, ELECTRIC_WORKSHOP, 0, 0, ELECTRIC_WORKSHOP.getMainPositionX(), ELECTRIC_WORKSHOP.getMainPositionY(), "Schraubensortiment unten", "(S3)"),

    //Level 2
    ACRYLIC_GLAS_SHELF      (FablabView.ACRYLIC_GLAS_SHELF, ELECTRIC_WORKSHOP, 0, 0, 0.4, 0.4, "Acrylregal" ,"" ),
    //Level 3
    ACRYLIC_GLAS            (FablabView.ACRYLIC_GLAS_SHELF, ACRYLIC_GLAS_SHELF, 0.20, 0.65, 0.60, 0.45, "Plexiglas" ,""),
    BOX_STAMPS              (FablabView.ELECTRIC_WORKSHOP, ACRYLIC_GLAS_SHELF, 0, 0, ACRYLIC_GLAS_SHELF.getMainPositionX(), ACRYLIC_GLAS_SHELF.getMainPositionY(), "Kiste Stempel", ""),

    //Level 2
    SHELF                   (FablabView.ELECTRIC_WORKSHOP, ELECTRIC_WORKSHOP, 0, 0, 0.9, 0.25, "Regal" , "(K)"),
    //Level 3
    BOX_POWER_ADAPTERS          (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Netzteile", ""),
    BOX_CONNECTORS              (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Buchsen und Stecker", ""),
    BOX_SEMICONDUCTORS          (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Halbleiter", "(E1.1"),
    BOX_LEDS                    (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste LEDs", "(E2.1"),
    BOX_SOCKET_STRIPS           (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Stift- und Buchsenleisten", "(E4.1)"),
    BOX_JACKS                   (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Buchsen und Stecker", "(E5.1)"),
    BOX_BATTERIES               (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Batterien und -Halter", "(E6.1)"),
    SHELF_SHRINKING_HOSES       (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Regal-Fach Schrumpfschl\u00E4uche", "(E7.1)"),
    BOX_DIODES                  (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Dioden", "(E9.1"),
    BOX_CAPACITOR               (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Kondensatoren", "(E10.1"),
    BOX_FERRULES                (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Aderendh\u00FClsen, Kabelschuhe, L\u00FCsterklemmen", "(E13.1"),
    BOX_SPAX_SCREWS             (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Spaxschrauben", "(E14.1)"),
    BOX_DOWEL                   (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste D\u00FCbel, Winkel", "(E15.1)"),
    BOX_STRAND_SUPPLIES         (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Nachschub Litze", "(E16.1)"),
    BOX_PERIPHERY_1             (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Peripherie 1/2", "(E1.2)"),
    BOX_PERIPHERY_2             (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Peripherie 2/2", "(E2.2)"),
    BOX_INDUCTORS               (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Spulen, Quarz", "(E3.2"),
    SMD_RESISTORS               (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "SMD-Widerstand Sortiment 0805", "(E4.2)"),
    BOX_CASING                  (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Geh\u00E4usebau", "(E14.2"),
    BOX_COOLER_1                (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste K\u00FChler, L\u00FCfter Teil 1/2", "(E15.2)"),
    BOX_IC_1                    (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste IC 1/2", "(E1.3)"),
    BOX_IC_2                    (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste IC 2/2", "(E2.3)"),
    BOX_IC_SOCKET               (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste IC-Sockel", "(E3.3)"),
    CAPACITOR_GREEN             (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Gr\u00FCnes Kondensatorsortiment", "(E4.3)"),
    SHELF_LED                   (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "LED Fach", "(E12.3)"),
    BOX_POWER_ADAPTERS_12V      (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Netzteile 12V", "(E13.3"),
    BOX_NEON_SIGN_HOLDER_RGB_205(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste RGB-Leuchtschildhalter 205mm", "(E15.3)"),
    BOX_NEON_SIGN_HOLDER_RGB_155(FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste RGB-Leuchtschildhalter 155mm", "(E16.3)"),
    BOX_NEON_SIGN_HOLDER        (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Leuchtschildhalter", "(E17.3)"),
    BOX_POWER_ADAPTERS_LEFTOVER (FablabView.ELECTRIC_WORKSHOP, SHELF, 0, 0, SHELF.getMainPositionX(), SHELF.getMainPositionY(), "Kiste Netzteil sonstige", "(E14.4)"),

    //Level 1
    CHEMISTRY_BENCH     (null, FABLAB, 0, 0, 0.1, 0.9, "Chemietisch", ""),

    //Level 2
    PLATINE_MANUFACTURE     (null, CHEMISTRY_BENCH, 0, 0, CHEMISTRY_BENCH.getMainPositionX(), CHEMISTRY_BENCH.getMainPositionY(), "Platinenfertigung", "(C1)"),
    CHEMISTRY               (null, CHEMISTRY_BENCH, 0, 0, CHEMISTRY_BENCH.getMainPositionX(), CHEMISTRY_BENCH.getMainPositionY(), "Chemie, KSS, Papierrollen", "(C4)"),

    //Level 1
    SHELF_MASCHINERY    (null, FABLAB, 0, 0, 0, 0, "Maschinenregal neben Hauptt\u00FCre", ""),

    //Level 2
    RIVETS                  (null, SHELF_MASCHINERY, 0, 0, 0, 0, "Nietensortiment", ""),

    //Level 1
    ENGINE_LATHE        (null, FABLAB, 0, 0, 0, 0, "Drehbanktisch", ""),

    //Level 2
    ENGINE_LATHE_DRAWER     (null, ENGINE_LATHE, 0, 0, ENGINE_LATHE.getMainPositionX(), ENGINE_LATHE.getMainPositionY(), "Drehbankschublade", "(D1)"),

    //SCREW_SHELF(),

    //Level 1
    MAIN_ROOM           (FablabView.MAIN_ROOM, FABLAB, 0, 0, 0, 0, "Hauptraum", ""),

    //Level 2
    WORK_BENCH              (null, MAIN_ROOM, 0, 0, 0.5, 0.55, "Werkbank", "(W)"),
    //Level 3
    DRAWER_3                    (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 3", "(W3)"),
    DRAWER_5                    (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 5", "(W5)"),
    DRAWER_9                    (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 9", "(W9)"),
    DRAWER_10                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 10", "(W10)"),
    DRAWER_14                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 14", "(W14)"),
    DRAWER_15                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 15", "(W15)"),
    DRAWER_16                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 16", "(W16)"),
    DRAWER_17                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 17", "(W17)"),
    DRAWER_18                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 18", "(W18)"),
    DRAWER_19                   (null, WORK_BENCH, 0, 0, WORK_BENCH.getMainPositionX(), WORK_BENCH.getMainPositionY(), "Schublade 19", "(W19)"),

    //Level 2
    DRAWER_MAGAZINE_MM      (null, MAIN_ROOM, 0, 0, 0, 0, "Schubladenmagazin neben Fr\u00e4se", "");


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

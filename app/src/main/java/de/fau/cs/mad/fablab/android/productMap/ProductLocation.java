package de.fau.cs.mad.fablab.android.productMap;

/**
 * Created by Michael on 19.05.2015.
 */
public enum ProductLocation
{
    BOX_SPAX_SCREW(FablabView.ELECTRIC_WORKSHOP, 250, 1100, 80, 80, "Box Spax Screw");


    private FablabView fablabView;

    //TODO: coordinates ralativ to view
    private int positionX;
    private int positionY;

    private int mainPositionX;
    private int mainPositionY;

    private String locationName;

    private ProductLocation(FablabView fablabView, int positionX, int positionY, int mainPositionX, int mainPositionY, String locationName)
    {
        this.fablabView = fablabView;

        this.positionX = positionX;
        this.positionY = positionY;

        this.mainPositionX = mainPositionX;
        this.mainPositionY = mainPositionY;

        this.locationName = locationName;
    }

    public int getPositionX() {
        return positionX;
    }
    public int getPositionY() {
        return positionY;
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

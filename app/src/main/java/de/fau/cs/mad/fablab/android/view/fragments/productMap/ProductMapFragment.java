package de.fau.cs.mad.fablab.android.view.fragments.productMap;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.productMap.DrawingActivity;
import de.fau.cs.mad.fablab.android.productMap.FablabView;
import de.fau.cs.mad.fablab.android.productMap.LocationParser;
import de.fau.cs.mad.fablab.android.productMap.ProductLocation;
import de.fau.cs.mad.fablab.android.productsearch.ProductSearchActivity;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class ProductMapFragment extends BaseFragment
        implements ProductMapFragmentViewModel.Listener
{
    @Inject
    ProductMapFragmentViewModel viewModel;

    private boolean isMainViewActive ;

    private DrawingActivity drawView;
    private ProductLocation productLocation;

    public ProductMapFragment(){}


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        viewModel.setListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_product_map);

        //get location
        String locationString = "tats\u00e4chliche Lagerorte / FAU FabLab / Elektrowerkstatt / Regal / Kiste Spaxschrauben"; //getIntent().getStringExtra(ProductSearchActivity.KEY_LOCATION);
        isMainViewActive = true;


        productLocation = LocationParser.getLocation(locationString);


        createView(getActivity());

    }


    public void createView(Context context)
    {

        //assert productLocation != null;

        try
        {

            drawView = new DrawingActivity(context, FablabView.MAIN_ROOM, productLocation.getMainPositionX(), productLocation.getMainPositionY(), productLocation.getLocationName());
            drawView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (isMainViewActive == true)
                    {
                        drawView.setDrawingParameter(productLocation.getView(), productLocation.getViewPositionX(), productLocation.getViewPositionY());
                        drawView.invalidate();
                        isMainViewActive = false;
                    } else
                    {
                        drawView.setDrawingParameter(FablabView.MAIN_ROOM, productLocation.getMainPositionX(), productLocation.getMainPositionY());
                        drawView.invalidate();
                        isMainViewActive = true;
                    }
                }
            });
            throw new IllegalArgumentException("ob das wohl geht");
        }
        catch(IllegalArgumentException exception)
        {
            new AlertDialog.Builder(context)
                    .setTitle("test")
                    .setMessage("something went wrong");
        }

        getActivity().setContentView(drawView);

    }

    @Override
    public void onLocationNotFound()
    {

    }
}

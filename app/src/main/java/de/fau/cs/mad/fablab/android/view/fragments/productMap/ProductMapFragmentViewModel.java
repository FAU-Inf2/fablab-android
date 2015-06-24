package de.fau.cs.mad.fablab.android.view.fragments.productMap;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.productMap.LocationParser;
import de.fau.cs.mad.fablab.android.productMap.ProductLocation;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ProductMapFragmentViewModel
{
    private Listener myListener;
    private ProductLocation productLocation;

//    private final Command<String> myProcessProductMapCommand = new Command<String>()
//    {
//        @Override
//        public void execute(String parameter)
//        {
//            myProcessProductMapCommand.setIsExecutable(false);
//
//            productLocation = productLocation = LocationParser.getLocation(parameter);
//
//            if(productLocation != null)
//                //TODO: Create map
//            else
//                myListener.onLocationNotFound();
//        }
//    }

    public void setListener(Listener listener) { myListener = listener;}

    public interface Listener
    {
        void onLocationNotFound();
    }

    //public Command<String> getProcessProductMapCommand() {return myProcessProductMapCommand; }

}

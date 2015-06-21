package de.fau.cs.mad.fablab.android.model;

import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.ICal;

/***
 * This class acts as a storage for our fetched news
 * //TODO persistence!
 */
public class ICalStorage {

    private ObservableArrayList<ICal> iCals;

    public ICalStorage(){
            iCals = new ObservableArrayList<>();
        }

    public ICal getICal(int position){
            return iCals.get(position);
        }

    public void addICal(ICal iCal){
            this.iCals.add(iCal);
        }

    public ObservableArrayList<ICal> getAllICals(){
            return iCals;
        }
}

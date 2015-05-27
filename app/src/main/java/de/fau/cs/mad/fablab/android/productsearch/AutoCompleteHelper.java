package de.fau.cs.mad.fablab.android.productsearch;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;
import de.fau.cs.mad.fablab.android.db.DatabaseHelper;
import de.fau.cs.mad.fablab.rest.ProductApiClient;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AutoCompleteHelper{

    private static AutoCompleteHelper instance;

    private RuntimeExceptionDao<AutoCompleteWords, Long> dao;

    public static AutoCompleteHelper getInstance () {
        if (AutoCompleteHelper.instance == null) {
            AutoCompleteHelper.instance = new AutoCompleteHelper();
        }
        return AutoCompleteHelper.instance;
    }


    /**

     This Method is called on:
     -Start of device
     -resume of device
     -start product search activity

     -> It gets all product names from the server, creates autocomplete words and stores them in the DB
     -->All 24h (changeable) updates are requestet

     */

    public void loadProductNames(Context c){
        dao = DatabaseHelper.getHelper(c).getAutoCompleteWordsDao();
        AutoCompleteWords words = dao.queryForId((long)0);

        if(words == null  || words.needsRefresh()){
            ProductApi api = new ProductApiClient(c).get();
            api.findAllNames(new Callback<List<String>>() {
                @Override
                public void success(List<String> strings, Response response) {
                    createAutocompleteStrings(strings);
                }

                @Override
                public void failure(RetrofitError error) {
                    //error -> Autocomplete doesn't work... but everything else is still fine
                    System.out.println(error);
                }
            });
        }
    }

    public void forceReloadProductNames(Context c) {
        dao = DatabaseHelper.getHelper(c).getAutoCompleteWordsDao();
        if(dao.queryForId((long) 0) != null)
            dao.deleteById((long)0);
        loadProductNames(c);
    }

    private void createAutocompleteStrings(List<String> strings){
        List<String> tempList = new ArrayList<>();
        if(strings != null) {
            //This loops remove chars like (,),- and short words (length < 3) and afterwards it checks for save values and creates a String[]
            for (int i = 0; i < strings.size(); i++) {
                String[] temp = strings.get(i).replace("(", " ").replace(")", " ").replace("_", " ").replace("-", " ").split(" ");

                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].length() > 2) {
                        boolean found = false;
                        for (String s : tempList)
                            if (s.toLowerCase().equals(temp[j].toLowerCase()))
                                found = true;

                        if (!found)
                            tempList.add(temp[j]);
                    }
                }
            }

            AutoCompleteWords w = dao.queryForId((long) 0);
            if(w == null)
                dao.create(new AutoCompleteWords(tempList.toArray(new String[tempList.size()])));
            else{
                w.setPossibleAutoCompleteWords(tempList.toArray(new String[tempList.size()]));
                dao.update(w);
            }
        }
    }


    public String[] getPossibleAutoCompleteWords(){
        AutoCompleteWords w = dao.queryForId((long)0);
        if(w != null)
            return w.getPossibleAutoCompleteWords();
        return new String[0];
    }
}

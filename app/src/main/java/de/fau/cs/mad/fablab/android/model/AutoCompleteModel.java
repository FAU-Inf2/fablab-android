package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.fablab.android.model.entities.AutoCompleteWords;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AutoCompleteModel {

    private RuntimeExceptionDao<AutoCompleteWords, Long> mAutoCompleteWordsDao;
    private ProductApi mProductApi;

    private Callback<List<String>> mProductNamesCallback = new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                createAutoCompleteWords(strings);
            }

            @Override
            public void failure(RetrofitError error) {
                //autocomplete does not work
            }
    };

    public AutoCompleteModel(RuntimeExceptionDao<AutoCompleteWords, Long> autoCompleteWordsDao,
                             ProductApi productApi) {
        mAutoCompleteWordsDao = autoCompleteWordsDao;
        mProductApi = productApi;
    }

    public void loadProductNames(){
        AutoCompleteWords autoCompleteWords = mAutoCompleteWordsDao.queryForId((long)0);
        if(autoCompleteWords == null || autoCompleteWords.needsRefresh()){
            mProductApi.findAllNames(mProductNamesCallback);
        }
    }

    public void forceReloadProductNames() {
        if(mAutoCompleteWordsDao.queryForId((long)0) != null) {
            mAutoCompleteWordsDao.deleteById((long)0);
        }
        loadProductNames();
    }

    public String[] getAutoCompleteWords(){
        AutoCompleteWords autoCompleteWords = mAutoCompleteWordsDao.queryForId((long)0);
        if(autoCompleteWords != null) {
            return autoCompleteWords.getPossibleAutoCompleteWords();
        }
        return new String[0];
    }

    private void createAutoCompleteWords(List<String> strings){

        List<String> tempList = new ArrayList<>();
        if(strings != null) {
            //This loops remove chars like (,),- and short words (length < 3) and afterwards
            //it checks for save values and creates a String[]
            for (int i = 0; i < strings.size(); i++) {
                String[] temp = strings.get(i).replace(",", " ").replace("(", " ").replace(")", " ")
                        .replace("_", " ").replace("-", " ").split(" ");
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

            AutoCompleteWords autoCompleteWords = mAutoCompleteWordsDao.queryForId((long)0);
            if(autoCompleteWords == null)
                mAutoCompleteWordsDao.create(new AutoCompleteWords(tempList.toArray(
                        new String[tempList.size()])));
            else{
                autoCompleteWords.setPossibleAutoCompleteWords(tempList.toArray(
                        new String[tempList.size()]));
                mAutoCompleteWordsDao.update(autoCompleteWords);
            }
        }

    }

}

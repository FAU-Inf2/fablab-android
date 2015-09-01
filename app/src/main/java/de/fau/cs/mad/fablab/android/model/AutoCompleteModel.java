package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.fau.cs.mad.fablab.android.model.entities.AutoCompleteWords;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AutoCompleteModel {

    private RuntimeExceptionDao<AutoCompleteWords, Long> mAutoCompleteWordsDao;
    private ProductApi mProductApi;

    private Callback<List<String>> mAutocompleteCallback = new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                mAutoCompleteWordsDao.createOrUpdate(new AutoCompleteWords(strings.toArray(
                        new String[strings.size()])));
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

        loadProductNames();
    }

    public void loadProductNames(){
        AutoCompleteWords autoCompleteWords = mAutoCompleteWordsDao.queryForId(0L);
        if(autoCompleteWords == null || autoCompleteWords.needsRefresh()){
            mProductApi.getAutoCompletions(mAutocompleteCallback);
        }
    }

    public void forceReloadProductNames() {
        if(mAutoCompleteWordsDao.queryForId(0L) != null) {
            mAutoCompleteWordsDao.deleteById(0L);
        }
        loadProductNames();
    }

    public String[] getAutoCompleteWords(){
        AutoCompleteWords autoCompleteWords = mAutoCompleteWordsDao.queryForId(0L);
        if(autoCompleteWords != null) {
            return autoCompleteWords.getPossibleAutoCompleteWords();
        }
        return new String[0];
    }
}

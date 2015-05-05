package de.fau.cs.mad.fablab.android.ORM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.R;


public class DBObjectView extends OrmLiteBaseActivity<DBHelper> {

    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("CALLED", "CALLED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbobject_view);
        populateListView();
        registerClickCallback();
    }

    private void populateListView(){
        RuntimeExceptionDao<DBObject, Integer> dao = getHelper().getDBObjectDao();
        List<DBObject> dbObjects = dao.queryForAll();
        String[] items = new String[dbObjects.size()];

        for(int i = 0; i < dbObjects.size(); i++){
            items[i] = dbObjects.get(i).getTitle();
        }

        ((ListView) findViewById(R.id.lvORMObjects)).setAdapter(new ArrayAdapter<String>(this, R.layout.textview_dbobject_view_list_item, items));

    }

    private void registerClickCallback(){
        ListView list = (ListView) findViewById(R.id.lvORMObjects);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DBObjectDetailsView.class);
                intent.putExtra("DBObject", getHelper().getDBObjectDao().queryForAll().get(position));
                startActivityForResult(intent, 1);
            }
        });
    }

    public void createNewObjectButtonClicked(View view){
        Intent intent = new Intent(getApplicationContext(), DBObjectDetailsView.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    public void backButtonClicked(View v){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        populateListView();
    }
}

package de.fau.cs.mad.fablab.android.ORM;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Random;

import de.fau.cs.mad.fablab.android.R;


public class DBObjectDetailsView extends OrmLiteBaseActivity<DBHelper> {


    private DBObject item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbobject_details_view);
        Object obj = getIntent().getSerializableExtra("DBObject");


        if(obj == null) {
            findViewById(R.id.btnDelete).setVisibility(View.INVISIBLE);
            item = null;
        }else{
            item = (DBObject) obj;
            TextView title = (TextView) findViewById(R.id.etTitle);
            TextView text = (TextView) findViewById(R.id.etText);
            TextView date = (TextView) findViewById(R.id.tvCreated);
            TextView modified = (TextView) findViewById(R.id.tvModified);

            title.setText(item.getTitle());
            text.setText(item.getText());
            date.setText("Created: " + item.getCreated());
            modified.setText("Last Change: " + item.getModified());
        }
    }

    public void backButtonClicked(View v) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void deleteButtonClicked(View v) {
        getHelper().getDBObjectDao().delete(item);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void saveButtonClicked(View v) {
        RuntimeExceptionDao<DBObject, Integer> dao = getHelper().getDBObjectDao();
        String title = ((EditText) findViewById(R.id.etTitle)).getText().toString();
        String text = ((EditText) findViewById(R.id.etText)).getText().toString();

        if(item == null){
            item = new DBObject(new Random().nextInt(),title,text);
            dao.create(item);
        }else{
            item.setTitle(title);
            item.setText(text);
            dao.update(item);
        }

        Intent intent = new Intent(getApplicationContext(),DBObjectView.class);
        setResult(RESULT_OK, intent);
        finish();
    }


}

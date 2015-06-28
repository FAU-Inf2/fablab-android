package de.fau.cs.mad.fablab.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public abstract class BaseActivity extends ActionBarActivity {

    protected abstract void baseOnCreate(Bundle savedInstanceState);
    protected abstract void baseSetContentView();
    protected void baseOnDestroy() { }
    protected boolean baseOnCreateOptionsMenu(Menu menu) { return true; }

    @Override
    final public void onCreate(Bundle savedInstanceState) {
        baseSetContentView();

        baseOnCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
    }

    public void initFabButton() {

    }

    public void initCartPanel(boolean show) {

    }

    @Override
    final public void onDestroy() {
        baseOnDestroy();
        super.onDestroy();
    }

    @Override
    final public boolean onCreateOptionsMenu(Menu menu) {
        baseOnCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    final public void onPause() {
        baseOnPause();
        super.onPause();
    }

    @Override
    final public void onResume() {
        baseOnResume();
        super.onResume();
    }

    protected void baseOnPause() {
    }

    protected void baseOnResume() {
    }
}

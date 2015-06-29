package de.fau.cs.mad.fablab.android.about;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class AboutActivity extends ActionBarActivity{

    private AppbarDrawerInclude appbarDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView textView_about = (TextView)findViewById(R.id.textView_about);
        TextView textView_libraries = (TextView)findViewById(R.id.textView_used_libraries);

        textView_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAboutDialog();
            }
        });

        textView_libraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLibariesDialog();
            }
        });

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        appbarDrawer.startTimer();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        appbarDrawer.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        appbarDrawer.startTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_opened) {
            appbarDrawer.updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, appbarDrawer.openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openAboutDialog(){


        final TextView textView = new TextView(this);
        textView.setPadding(20, 20, 20, 20);
        textView.setText(Html.fromHtml(getResources().getString(R.string.about_content)));
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // this is important to make the links clickable
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.about_title))
                .setPositiveButton("Close", null)
                .setView(textView)
                .create();


        alertDialog.show();
    }

    public void openLibariesDialog()
    {
        final Notices notices = new Notices();
        notices.addNotice(new Notice("Test 1", "http://example.org", "Example Person", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Test 2", "http://example.org", "Example Person 2", new GnuLesserGeneralPublicLicense21()));

        new LicensesDialog.Builder(this)
                .setNotices(notices)
                .setTitle(R.string.used_libraries)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }
}

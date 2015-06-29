package de.fau.cs.mad.fablab.android.about;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
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
import de.psdev.licensesdialog.licenses.License;
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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.about_title))
                .setPositiveButton("Close", null)
                .setView(textView)
                .create();

        alertDialog.show();


        setDialogColors(alertDialog);
    }

    private void setDialogColors(Dialog dialog){
        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) dialog.findViewById(textViewId);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));

        int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(dividerId);
        if(divider != null){
            divider.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void openLibariesDialog()
    {
        final Notices notices = new Notices();
        notices.addNotice(new Notice("jackson-databind", "https://github.com/FasterXML/jackson-databind/", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("retrofit", "https://github.com/square/retrofit", "Copyright 2013 Square, Inc.", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("okhttp", "https://github.com/square/okhttp", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("okio", "https://github.com/square/okio", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("ormlite", "http://ormlite.com/", null, null));
        notices.addNotice(new Notice("zxing", "https://github.com/dm77/barcodescanner", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("AndroidSlidingUpPanel", "https://github.com/umano/AndroidSlidingUpPanel", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("SwipeableRecyclerView", "https://github.com/brnunes/SwipeableRecyclerView", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("CircleImageView", "https://github.com/hdodenhof/CircleImageView", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("FloatingActionButton", "https://github.com/Clans/FloatingActionButton", "Copyright 2015 Dmytro Tarianyk", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("RoboGuice", "https://github.com/roboguice/roboguice/", "Copyright 2009-2014 roboguice committers", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("EventBus", "https://github.com/greenrobot/EventBus", null, new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Picasso", "https://github.com/square/picasso", "Copyright 2013 Square, Inc.", new ApacheSoftwareLicense20()));

        LicensesDialog dialog = new LicensesDialog.Builder(this)
                .setNotices(notices)
                .setTitle(R.string.used_libraries)
                .setIncludeOwnLicense(true)
                .build();

        Dialog licenseDialog = dialog.show();

        setDialogColors(licenseDialog);

    }
}

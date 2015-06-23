package de.fau.cs.mad.fablab.android.navdrawer;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean loggedIn = true;

    private NavigationDrawer navdrawer;
    private ActionBarActivity mainActivity;

    public void setMainActivity(ActionBarActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int holderID;

        TextView navdrawer_textView;
        ImageView navdrawer_imageView;
        ImageView navdrawer_icon;
        TextView navdrawer_name;
        TextView navdrawer_email;


        EditText drupalLoginUsername;
        EditText drupalLoginPassword;
        Button drupalLoginButton;



        public ViewHolder(final View itemView,int ViewType, boolean loggedIn) {
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                navdrawer_textView = (TextView) itemView.findViewById(R.id.navdrawer_rowText);
                navdrawer_imageView = (ImageView) itemView.findViewById(R.id.navdrawer_rowIcon);
                holderID = 1;
            } else {
                if(loggedIn) {
                    navdrawer_name = (TextView) itemView.findViewById(R.id.navdrawer_name);
                    navdrawer_email = (TextView) itemView.findViewById(R.id.navdrawer_email);
                    navdrawer_icon = (ImageView) itemView.findViewById(R.id.navdrawer_circleView);

                } else {
                    drupalLoginUsername = (EditText) itemView.findViewById(R.id.drupal_login_username);
                    drupalLoginPassword = (EditText) itemView.findViewById(R.id.drupal_login_password);
                    drupalLoginButton = (Button) itemView.findViewById(R.id.drupal_login_button);
                    drupalLoginButton.setOnClickListener(new DrupalLogin(drupalLoginPassword, drupalLoginUsername));
                }
                holderID = 0;
            }
        }
    }

    class DrupalLogin implements View.OnClickListener {
        EditText drupalLoginUsername;
        EditText drupalLoginPassword;

        public DrupalLogin(EditText drupalLoginUsername, EditText drupalLoginPassword) {
            this.drupalLoginPassword = drupalLoginPassword;
            this.drupalLoginUsername = drupalLoginUsername;
        }

        @Override
        public void onClick(View v) {
            /* Function to be changed to really fit to the drupal login with timeout and
            loading icon, maybe then animating the logged in screen income.
             */
            if(drupalLoginUsername.getText().toString().equals("admin")
                    && drupalLoginPassword.getText().toString().equals("admin")) {
                navdrawer.setEmail("logged@in.fab");
                navdrawer.setName("Logged in User");
                navdrawer.setIcon(R.drawable.avatar);

                // Inflating new Layout
                RelativeLayout layout = (RelativeLayout) mainActivity.findViewById(R.id.navdrawer_header_login);
                LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout.removeAllViews();
                layout.addView(layoutInflater.inflate(R.layout.navdrawer_header_loggedin, null));
                TextView navdrawer_name = (TextView) mainActivity.findViewById(R.id.navdrawer_name);
                TextView navdrawer_email = (TextView) mainActivity.findViewById(R.id.navdrawer_email);
                ImageView navdrawer_icon = (ImageView) mainActivity.findViewById(R.id.navdrawer_circleView);
                navdrawer_name.setText(navdrawer.getName());
                navdrawer_email.setText(navdrawer.getEmail());
                navdrawer_icon.setImageDrawable(mainActivity.getResources().getDrawable(navdrawer.getIcon()));

                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(), 0);
                } catch(NullPointerException e) {
                    Log.i("KEYBOARD", "NullPointerException on hiding keyboard");
                }

                loggedIn = true;
            }
        }
    }

    public NavigationDrawerAdapter(NavigationDrawer navdrawer) {
        this.navdrawer = navdrawer;
    }

    public NavigationDrawerAdapter(NavigationDrawer navdrawer, boolean loggedIn) {
        this.navdrawer = navdrawer;
        this.loggedIn = loggedIn;
    }

    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navdrawer_item_row,parent, false);
            return new ViewHolder(v,viewType, loggedIn);
        } else if(viewType == TYPE_HEADER) {
            View v;
            if(!loggedIn) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navdrawer_header_login, parent, false);
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navdrawer_header_loggedin, parent, false);
            }
            return new ViewHolder(v,viewType,loggedIn);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder holder, int position) {
        if(holder.holderID == 1) {
            holder.navdrawer_textView.setText(navdrawer.getItems().get(position - 1).getTitle());
            holder.navdrawer_imageView.setImageResource(navdrawer.getItems().get(position - 1).getIcon());
        } else {
            if(loggedIn) {
                holder.navdrawer_icon.setImageResource(navdrawer.getIcon());
                holder.navdrawer_name.setText(navdrawer.getName());
                holder.navdrawer_email.setText(navdrawer.getEmail());
            }
        }
    }

    @Override
    public int getItemCount() {
        return navdrawer.getItems().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}

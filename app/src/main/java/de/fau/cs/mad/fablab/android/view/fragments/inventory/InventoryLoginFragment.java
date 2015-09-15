package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

public class InventoryLoginFragment extends BaseFragment implements InventoryLoginFragmentViewModel.Listener{

    @Bind(R.id.inventory_login_username)
    EditText usernameET;
    @Bind(R.id.inventory_login_password)
    EditText passwordET;
    @Bind(R.id.inventory_login_login_button)
    Button loginButton;
    @Bind(R.id.inventory_login_scan_button)
    Button scanButton;

    @Inject
    InventoryLoginFragmentViewModel mViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_inventory_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);

        new ViewCommandBinding().bind(loginButton, mViewModel.getLoginCommand());
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_NAVDRAWER);
        setNavigationDrawerSelection(R.id.drawer_item_inventory);
    }

    @Override
    public String getUsername()
    {
        if(usernameET != null)
        {
             return usernameET.getText().toString();
        }
        return null;
    }

    @Override
    public String getPassword()
    {
        if(passwordET != null)
        {
           return passwordET.getText().toString();
        }
        return null;
    }

    @Override
    public void userUnauthorized() {
        Toast.makeText(getActivity(), R.string.inventory_login_unauthorized_user, Toast.LENGTH_LONG)
                .show();
    }
}

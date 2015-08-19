package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.model.events.AppBarShowDoorStateEvent;
import de.fau.cs.mad.fablab.android.model.events.AppBarShowTitleEvent;
import de.fau.cs.mad.fablab.android.view.common.binding.NumberPickerCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationEvent;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.greenrobot.event.EventBus;

public class AddToInventoryDialogFragment extends BaseDialogFragment implements AddToInventoryDialogFragmentViewModel.Listener {

    @Inject
    AddToInventoryDialogFragmentViewModel mViewModel;

    @Bind(R.id.add_to_inventory_name)
    TextView mNameTextView;
    @Bind(R.id.button_send_inventory)
    Button mButtonSend;
    @Bind(R.id.add_to_inventory_numberPicker)
    NumberPicker mNumberPicker;

    private EventBus mEventBus = EventBus.getDefault();

    public static AddToInventoryDialogFragment newInstance(Product product) {
        AddToInventoryDialogFragment dialogFragment = new AddToInventoryDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AddToInventoryDialogFragmentViewModel.KEY_PRODUCT, product);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEventBus.post(new AppBarShowDoorStateEvent(false));
        mEventBus.post(new AppBarShowTitleEvent(false));

        mViewModel.restoreState(getArguments(), savedInstanceState);

        mNameTextView.setText(mViewModel.getName());

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(100);
        mNumberPicker.setValue((int) mViewModel.getAmount());
        mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        mViewModel.setListener(this);

        new NumberPickerCommandBinding().bind(mNumberPicker, mViewModel.getChangeAmountCommand());
        new ViewCommandBinding().bind(mButtonSend, mViewModel.getAddToInventoryCommand());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_to_inventory, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.saveState(outState);
    }

    @Override
    public void onDismiss() {
        getFragmentManager().popBackStack();
        mEventBus.post(NavigationEvent.Inventory);
        mEventBus.post(new AppBarShowDoorStateEvent(true));
        mEventBus.post(new AppBarShowTitleEvent(true));
    }
}

package de.fau.cs.mad.fablab.android.cart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class CheckoutFragment extends Fragment {
    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }

    public static CheckoutFragment newInstance(boolean error) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("error", error);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        if (getArguments().getBoolean("error")) {
            ((TextView) view.findViewById(R.id.checkout_text)).setText(R.string.checkout_error);
            ((Button) view.findViewById(R.id.button_finish)).setText(android.R.string.ok);
        }
        return view;
    }
}

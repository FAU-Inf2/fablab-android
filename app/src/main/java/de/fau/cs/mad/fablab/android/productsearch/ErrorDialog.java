package de.fau.cs.mad.fablab.android.productsearch;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.fau.cs.mad.fablab.android.R;

public class ErrorDialog extends DialogFragment {

    private static final String     KEY_MESSAGE = "message";
    private String                  message;

    public static ErrorDialog newInstance(String message) {
        ErrorDialog errorDialog = new ErrorDialog();

        //supply message as an argument
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_MESSAGE, message);
        errorDialog.setArguments(arguments);

        return errorDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get error message
        message = getArguments().getString(KEY_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get gui elements
        View view = inflater.inflate(R.layout.error_dialog, container, false);
        TextView title = (TextView) view.findViewById(R.id.error_dialog_title);
        Button button = (Button) view.findViewById(R.id.error_dialog_button);

        //set message as title
        title.setText(message);

        //set listener for click events
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //not canceled on touch outside
        dialog.setCanceledOnTouchOutside(false);
        //window without title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

}

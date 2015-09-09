package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ProductSearchView extends SearchView {
    @Bind(android.support.v7.appcompat.R.id.search_src_text)
    SearchView.SearchAutoComplete mSearchAutoComplete;

    private Command<String> mCommand;

    public ProductSearchView(Context context) {
        this(context, null);
    }

    public ProductSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ButterKnife.bind(getRootView());
        mSearchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = mSearchAutoComplete.getAdapter().getItem(position).toString();
                mSearchAutoComplete.setText(text);
                mSearchAutoComplete.setSelection(text.length());

                if (mCommand.isExecutable()) {
                    mCommand.execute(text);
                }
            }
        });
        setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mCommand.isExecutable()) {
                    mCommand.execute(query);
                }
                return true;
            }
        });
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        mSearchAutoComplete.setAdapter(adapter);
    }

    public void setCommand(Command<String> command) {
        mCommand = command;
    }
}

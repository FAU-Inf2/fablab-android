package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.content.Context;
import android.util.AttributeSet;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;

public class AlphabeticSectionTitleIndicator extends SectionTitleIndicator<String> {

    public AlphabeticSectionTitleIndicator(Context context) {
        super(context);
    }

    public AlphabeticSectionTitleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphabeticSectionTitleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSection(String section) {
        setTitleText(section);
    }
}
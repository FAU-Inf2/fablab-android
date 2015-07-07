package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.view.LayoutInflater;
import android.widget.SectionIndexer;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;

public class ProductRVRendererAdapter extends RVRendererAdapter implements SectionIndexer {

    private final String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ProductRVRendererAdapter(LayoutInflater layoutInflater, RendererBuilder rendererBuilder,
                                    AdapteeCollection collection) {
        super(layoutInflater, rendererBuilder, collection);
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[SECTIONS.length()];
        for (int i = 0; i < SECTIONS.length(); i++)
            sections[i] = String.valueOf(SECTIONS.charAt(i));
        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        if(position < getItemCount()) {
            for (int i = 1; i < SECTIONS.length(); i++) {
                if (String.valueOf(((ProductSearchViewModel)getItem(position)).getUnformattedName()
                        .charAt(0)).equals(String.valueOf(SECTIONS.charAt(i)))) {
                    return i;
                }
            }
        }
        return 0;
    }

}

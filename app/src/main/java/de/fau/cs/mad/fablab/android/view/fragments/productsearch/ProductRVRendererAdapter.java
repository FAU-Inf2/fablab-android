package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.view.LayoutInflater;
import android.widget.SectionIndexer;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;

import java.text.Collator;
import java.util.Locale;

public class ProductRVRendererAdapter extends RVRendererAdapter<ProductSearchViewModel>
        implements SectionIndexer {

    private final String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ProductRVRendererAdapter(LayoutInflater layoutInflater,
                                    RendererBuilder<ProductSearchViewModel> rendererBuilder,
                                    AdapteeCollection<ProductSearchViewModel> collection) {
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
        if(getItemCount() > 0) {
            if(position >= getItemCount()) {
                position = getItemCount() -1;
            }
            Collator collator = Collator.getInstance(Locale.GERMAN);
            collator.setStrength(Collator.PRIMARY);
            for (int i = 1; i < SECTIONS.length(); i++) {
                if(collator.compare(String.valueOf((getItem(position)).getUnformattedName()
                        .charAt(0)), String.valueOf(SECTIONS.charAt(i))) == 0) {
                    return i;
                }
            }
        }
        return 0;
    }

}

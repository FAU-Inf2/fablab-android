package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import android.view.LayoutInflater;
import android.widget.SectionIndexer;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;

import java.text.Collator;
import java.util.Locale;

public class InventoryItemRVRendererAdapter extends RVRendererAdapter<InventoryViewModel>
        implements SectionIndexer {

    private final String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public InventoryItemRVRendererAdapter(LayoutInflater layoutInflater,
                                    RendererBuilder<InventoryViewModel> rendererBuilder,
                                    AdapteeCollection<InventoryViewModel> collection) {
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
                if(collator.compare(String.valueOf((getItem(position)).getName()
                        .charAt(0)), String.valueOf(SECTIONS.charAt(i))) == 0) {
                    return i;
                }
            }
        }
        return 0;
    }
}

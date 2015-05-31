package de.fau.cs.mad.fablab.android.productsearch;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import java.util.ArrayList;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.rest.core.Product;

public class ProductAdapter extends ArrayAdapter<Product> implements SectionIndexer {

    private final String SECTIONS = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public ProductAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_entry,
                parent, false);

        //get gui elements
        CardView productCardView = (CardView) view.findViewById(R.id.product_card_view);
        ImageView productPhoto = (ImageView) view.findViewById(R.id.product_photo);
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);

        //set product photo
        productPhoto.setImageResource(R.drawable.no_image_avl);

        //format and set product name
        String[] splitProductName = getItem(position).getName().split(" ");
        String formattedProductName = "";
        for (int i = 0; i < splitProductName.length; i++) {
            if (i == 0) {
                formattedProductName += view.getResources().getString(R.string.bold_start)
                        + splitProductName[i]
                        + view.getResources().getString(R.string.bold_end)
                        + view.getResources().getString(R.string.non_breaking_space);
            } else {
                formattedProductName += splitProductName[i]
                        + view.getResources().getString(R.string.non_breaking_space);
            }
        }
        productName.setText(Html.fromHtml(formattedProductName));

        //format and set product price
        String formattedProductPrice = String.format("%.2f", getItem(position).getPrice())
                + view.getResources().getString(R.string.non_breaking_space)
                + view.getResources().getString(R.string.currency);
        productPrice.setText(Html.fromHtml(formattedProductPrice));

        //handling for unsaleable products
        //for testing: price > 250 -> unsaleable
        if(getItem(position).getPrice() > 250) {
            productCardView.setCardBackgroundColor(getContext().getResources()
                    .getColor(R.color.spinner_background));
            productPhoto.setAlpha(0.5f);
            productName.setTextColor(getContext().getResources().getColor(R.color
                    .primary_text_disabled_material_light));
            productPrice.setTextColor(getContext().getResources().getColor(R.color
                    .primary_text_disabled_material_light));
            productPrice.setText(Html.fromHtml("-,- €"));
        }

        return view;
    }

    public ArrayList<Product> getAllItems() {
        ArrayList<Product> allItems = new ArrayList<Product>();
        for (int i = 0; i < getCount(); i++) {
            allItems.add(getItem(i));
        }
        return allItems;
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
        //loop backwards over sections (if there is no item for the current section,
        //the previous section will be selected)
        for (int i = sectionIndex; i >= 0; i--) {
            //loop forward over entries
            for (int j = 0; j < getCount(); j++) {

                //alphabetic sections
                if (i != 0) {
                    if (String.valueOf(getItem(j).getName().charAt(0))
                            .equals(String.valueOf(SECTIONS.charAt(i)))) {
                        return j;
                    }

                    //numeric section
                } else {
                    for (int k = 0; k <= 9; k++) {
                        if (String.valueOf(getItem(j).getName().charAt(0))
                                .equals(String.valueOf(k))) {
                            return j;
                        }
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}

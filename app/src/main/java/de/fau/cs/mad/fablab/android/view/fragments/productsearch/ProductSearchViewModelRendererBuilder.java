package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;


public class ProductSearchViewModelRendererBuilder extends RendererBuilder<ProductSearchViewModel> {

    public ProductSearchViewModelRendererBuilder(){
        Collection<Renderer<ProductSearchViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new ProductSearchViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(ProductSearchViewModel productSearchViewModel) {
        return ProductSearchViewModelRenderer.class;
    }
}

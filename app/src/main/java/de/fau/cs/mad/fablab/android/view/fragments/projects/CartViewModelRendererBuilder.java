package de.fau.cs.mad.fablab.android.view.fragments.projects;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class CartViewModelRendererBuilder extends RendererBuilder<CartViewModel> {

    public CartViewModelRendererBuilder() {
        Collection<Renderer<CartViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new CartViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(CartViewModel cartViewModel) {
        return CartViewModelRenderer.class;
    }
}

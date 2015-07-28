package de.fau.cs.mad.fablab.android.view.cartpanel;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class CartEntryViewModelRendererBuilder extends RendererBuilder<CartEntryViewModel> {
    public CartEntryViewModelRendererBuilder() {
        Collection<Renderer<CartEntryViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new CartEntryViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(CartEntryViewModel cartEntryViewModel) {
        return CartEntryViewModelRenderer.class;
    }
}


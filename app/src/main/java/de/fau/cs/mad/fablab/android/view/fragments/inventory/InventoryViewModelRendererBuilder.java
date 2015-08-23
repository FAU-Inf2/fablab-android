package de.fau.cs.mad.fablab.android.view.fragments.inventory;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class InventoryViewModelRendererBuilder extends RendererBuilder<InventoryViewModel> {

    public InventoryViewModelRendererBuilder() {
        Collection<Renderer<InventoryViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new InventoryViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(InventoryViewModel inventoryViewModel) {
        return InventoryViewModelRenderer.class;
    }
}

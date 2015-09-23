package de.fau.cs.mad.fablab.android.view.fragments.reservation;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class ToolUsageViewModelRendererBuilder extends RendererBuilder<ToolUsageViewModel> {
    public ToolUsageViewModelRendererBuilder() {
        Collection<Renderer<ToolUsageViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new ToolUsageViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(ToolUsageViewModel newsViewModel) {
        return ToolUsageViewModelRenderer.class;
    }
}

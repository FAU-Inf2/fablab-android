package de.fau.cs.mad.fablab.android.view.fragments.news;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class NewsViewModelRendererBuilder extends RendererBuilder<NewsViewModel> {
    public NewsViewModelRendererBuilder() {
        Collection<Renderer<NewsViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new NewsViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(NewsViewModel newsViewModel) {
        return NewsViewModelRenderer.class;
    }
}

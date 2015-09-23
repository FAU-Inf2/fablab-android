package de.fau.cs.mad.fablab.android.view.fragments.projects;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectViewModelRendererBuilder extends RendererBuilder<ProjectViewModel> {

    public ProjectViewModelRendererBuilder() {
        Collection<Renderer<ProjectViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new ProjectViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(ProjectViewModel projectViewModel) {
        return ProjectViewModelRenderer.class;
    }
}

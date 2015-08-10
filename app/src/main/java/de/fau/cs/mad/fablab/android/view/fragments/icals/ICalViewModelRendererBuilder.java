package de.fau.cs.mad.fablab.android.view.fragments.icals;

import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import java.util.ArrayList;
import java.util.Collection;

public class ICalViewModelRendererBuilder extends RendererBuilder<ICalViewModel> {
    public ICalViewModelRendererBuilder() {
        Collection<Renderer<ICalViewModel>> prototypes = new ArrayList<>();
        prototypes.add(new ICalViewModelRenderer());
        prototypes.add(new OpenLabICalViewModelRenderer());
        prototypes.add(new SelfLabICalViewModelRenderer());

        setPrototypes(prototypes);
    }

    @Override
    protected Class getPrototypeClass(ICalViewModel iCalViewModel) {
        String title = iCalViewModel.getTitle().trim();
        if (splitString(title).equalsIgnoreCase("openlab")) {
            return OpenLabICalViewModelRenderer.class;
        } else if (splitString(title).equalsIgnoreCase("selflab")) {
            return SelfLabICalViewModelRenderer.class;
        } else {
            return ICalViewModelRenderer.class;
        }


    }
    private String splitString(String completeString)
    {
        // because of: e.g. OpenLab OHNE Lasercutter
        return completeString.split(" ")[0];
    }


}

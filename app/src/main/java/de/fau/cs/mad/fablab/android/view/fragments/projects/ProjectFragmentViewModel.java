package de.fau.cs.mad.fablab.android.view.fragments.projects;

import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;

public class ProjectFragmentViewModel {

    private Listener mListener;
    private ProjectModel mModel;
    private CartModel mCartModel;
    private ListAdapteeCollection<ProjectViewModel> mProjectViewModelCollection;

    private Command<Void> mNewProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null) {
                mListener.onNewProjectClicked();
            }
        }
    };

    private Command<Void> mNewProjectFromCartCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            if(mListener != null)
            {
                if(mCartModel.getAllPaidCarts().isEmpty())
                {
                    mListener.noCartsAvailable();
                }
                else
                {
                    mListener.showCartChooser();
                }
            }
        }
    };

    @Inject
    ProjectFragmentViewModel(ProjectModel model, CartModel cartModel)
    {
        mModel = model;
        mCartModel = cartModel;
        mProjectViewModelCollection = new ListAdapteeCollection<>();
        update();
    }

    public Command<Void> getNewProjectCommand()
    {
        return mNewProjectCommand;
    }

    public Command<Void> getNewProjectFromCartCommand()
    {
        return mNewProjectFromCartCommand;
    }

    public AdapteeCollection<ProjectViewModel> getProjectViewModelCollection() {
        return mProjectViewModelCollection;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void update()
    {
        List<Project> projects = mModel.getAllProjects();
        mProjectViewModelCollection.clear();
        for(Project p : projects)
        {
            mProjectViewModelCollection.add(new ProjectViewModel(p));
        }
        if (mListener != null) {
            mListener.onDataChanged();
        }
    }

    public interface Listener{
        void onNewProjectClicked();
        void onDataChanged();
        void noCartsAvailable();
        void showCartChooser();
    }
}

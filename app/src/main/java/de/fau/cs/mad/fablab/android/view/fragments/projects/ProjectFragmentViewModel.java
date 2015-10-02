package de.fau.cs.mad.fablab.android.view.fragments.projects;

import com.pedrogomez.renderers.ListAdapteeCollection;

import java.util.List;

import javax.inject.Inject;

import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.ProjectModel;
import de.fau.cs.mad.fablab.android.model.events.ProjectDeletedEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class ProjectFragmentViewModel {

    private Listener mListener;
    private ProjectModel mModel;
    private CartModel mCartModel;
    private ListAdapteeCollection<ProjectViewModel> mProjectViewModelCollection;
    private EventBus mEventBus = EventBus.getDefault();

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

    private Command<Integer> mConfirmDeletionCommand = new Command<Integer>() {
        @Override
        public void execute(Integer position) {
            if(mListener != null)
            {
                mListener.confirmDeletion(position);
            }
        }
    };

    @Inject
    ProjectFragmentViewModel(ProjectModel model, CartModel cartModel)
    {
        mModel = model;
        mCartModel = cartModel;
        mProjectViewModelCollection = new ListAdapteeCollection<>();
        mEventBus.register(this);
        update();
    }

    public Command<Integer> getConfirmDeletionCommand()
    {
        return mConfirmDeletionCommand;
    }

    public Command<Void> getNewProjectCommand()
    {
        return mNewProjectCommand;
    }

    public Command<Void> getNewProjectFromCartCommand()
    {
        return mNewProjectFromCartCommand;
    }

    public ListAdapteeCollection<ProjectViewModel> getProjectViewModelCollection() {
        return mProjectViewModelCollection;
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    public void deleteProject(Project project)
    {
        mModel.deleteProject(project);
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

    @SuppressWarnings("unused")
    public void onEvent(ProjectDeletedEvent event) {
        if(mListener != null)
        {
            if(event.getState())
            {
                update();
            }
            else
            {
                mListener.deleteFailure();
                mListener.onDataChanged();
            }
        }
    }

    public interface Listener{
        void onNewProjectClicked();
        void onDataChanged();
        void noCartsAvailable();
        void showCartChooser();
        void confirmDeletion(int position);
        void deleteFailure();
    }
}

package de.fau.cs.mad.fablab.android.view.fragments.projects;

import de.fau.cs.mad.fablab.android.viewmodel.common.Project;
import de.fau.cs.mad.fablab.android.viewmodel.common.commands.Command;
import de.greenrobot.event.EventBus;

public class ProjectViewModel {

    private Project mProject;

    private Command<Void> mShowProjectCommand = new Command<Void>() {
        @Override
        public void execute(Void parameter) {
            EventBus.getDefault().post(new ProjectClickedEvent(mProject));
        }
    };

    public ProjectViewModel(Project project)
    {
        mProject = project;
    }

    public String getTitle()
    {
        return mProject.getProjectFile().getFilename();
    }

    public String getShortDescription()
    {
        return mProject.getProjectFile().getDescription();
    }

    public Command<Void> getShowProjectCommand()
    {
        return mShowProjectCommand;
    }
}

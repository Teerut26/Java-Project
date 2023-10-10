package cs211.project.services.deleterelated;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.models.collections.CommentActivitiesEventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.datasource.ActivitiesEventFileListDatesource;
import cs211.project.services.datasource.CommentActivitiesEventFileListDatasource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;

public class DeleteRelatedOfPrimaryKeyEvent {
    private TeamFileListDatasource teamFileListDatasource;
    private ActivitiesEventFileListDatesource activitiesEventFileListDatesource;
    private ManyToManyFileListDatasource manyToManyFileListDatasource;
    private TeamCollection teamCollection;
    private ActivitiesEventCollection activitiesEventCollection;
    private ManyToManyCollection manyToManyCollection;

    public DeleteRelatedOfPrimaryKeyEvent() {
        teamFileListDatasource = new TeamFileListDatasource();
        activitiesEventFileListDatesource = new ActivitiesEventFileListDatesource();
        manyToManyFileListDatasource = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_EVENT);

        teamCollection = teamFileListDatasource.readData();
        activitiesEventCollection = activitiesEventFileListDatesource.readData();
        manyToManyCollection = manyToManyFileListDatasource.readData();
    }

    public void delete(Event event) {
        deleteTeam(event);
        deleteUserEventMTM(event);
    }

    private void deleteTeam(Event event) {
        TeamCollection teamCollection1 = teamCollection.findByEvent(event);
        teamCollection.removeByEvent(event);
        teamFileListDatasource.writeData(teamCollection);

        // Delete Relation User Team
        ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_TEAM);
        ManyToManyCollection manyToManyCollection1 = manyToManyFileListDatasource.readData();
        teamCollection1.getTeams().forEach(team -> {
            manyToManyCollection1.removeByB(team.getId());
        });
        manyToManyFileListDatasource.writeData(manyToManyCollection1);
    }

    private void deleteUserEventMTM(Event event) {
        manyToManyCollection.removeByB(event.getEventID());
        manyToManyFileListDatasource.writeData(manyToManyCollection);
    }

}

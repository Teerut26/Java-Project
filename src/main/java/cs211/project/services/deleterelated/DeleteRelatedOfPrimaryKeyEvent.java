package cs211.project.services.deleterelated;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.*;
import cs211.project.services.datasource.*;

public class DeleteRelatedOfPrimaryKeyEvent {
    private TeamFileListDatasource teamFileListDatasource;
    private ManyToManyFileListDatasource manyToManyFileListDatasource;
    private EventFileListDatesource eventFileListDatesource;
    private TeamCollection teamCollection;
    private ManyToManyCollection manyToManyCollection;
    private EventCollection eventCollection;

    public DeleteRelatedOfPrimaryKeyEvent() {
        teamFileListDatasource = new TeamFileListDatasource();
        manyToManyFileListDatasource = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_EVENT);
        eventFileListDatesource = new EventFileListDatesource();

        teamCollection = teamFileListDatasource.readData();
        manyToManyCollection = manyToManyFileListDatasource.readData();
        eventCollection = eventFileListDatesource.readData();
    }

    public void delete(Event event) {
        deleteTeam(event);
        deleteUserEventMTM(event);
        deleteEvent(event);
    }

    private void deleteEvent(Event event) {
        eventCollection.remove(event);
        eventFileListDatesource.writeData(eventCollection);
    }

    private void deleteTeam(Event event) {
        TeamCollection teamCollection1 = teamCollection.findByEvent(event);
        teamCollection.removeByEvent(event);
        teamFileListDatasource.writeData(teamCollection);

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

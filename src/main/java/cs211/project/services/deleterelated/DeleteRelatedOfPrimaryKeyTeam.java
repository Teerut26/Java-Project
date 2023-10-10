package cs211.project.services.deleterelated;

import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.datasource.ActivitiesTeamFileListDatesource;
import cs211.project.services.datasource.ManyToManyFileListDatasource;
import cs211.project.services.datasource.TeamFileListDatasource;

public class DeleteRelatedOfPrimaryKeyTeam {
    private TeamFileListDatasource teamFileListDatasource;
    private ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource;
    private ManyToManyFileListDatasource manyToManyFileListDatasource;
    private TeamCollection teamCollection;
    private ActivitiesTeamCollection activitiesTeamCollection;
    private ManyToManyCollection manyToManyCollection;

    public DeleteRelatedOfPrimaryKeyTeam() {
        this.teamFileListDatasource = new TeamFileListDatasource();
        this.activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
        this.manyToManyFileListDatasource = new ManyToManyFileListDatasource(new ManyToManyFileListDatasource().MTM_USER_TEAM);

        this.teamCollection = this.teamFileListDatasource.readData();
        this.activitiesTeamCollection = this.activitiesTeamFileListDatesource.readData();
        this.manyToManyCollection = this.manyToManyFileListDatasource.readData();
    }

    public void delete(Team team) {
        deleteTeam(team);
        deleteActivity(team);
        deleteUserTeamMTM(team);
    }

    private void deleteTeam(Team team) {
        this.teamCollection.remove(team);
        this.teamFileListDatasource.writeData(this.teamCollection);
    }

    private void deleteActivity(Team team) {
        activitiesTeamCollection.removeByTeam(team);
        activitiesTeamFileListDatesource.writeData(activitiesTeamCollection);

    }

    private void deleteUserTeamMTM(Team team) {
        manyToManyCollection.removeByB(team.getId());
        manyToManyFileListDatasource.writeData(manyToManyCollection);
    }
}

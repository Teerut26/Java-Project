package cs211.project.models.collections;
import cs211.project.models.Event;
import cs211.project.models.Team;
import java.util.ArrayList;

public class TeamCollection {
    private ArrayList<Team> teams;

    public TeamCollection() {
        this.teams = new ArrayList<>();
    }

    public void add(Team team) {
        if(checkIsExisted(team)) {
            return;
        }
        this.teams.add(team);
    }

    private boolean checkIsExisted(Team team) {
        for(Team team1 : this.teams) {
            if(team1.getId().equals(team.getId())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Team> getTeams() {
        return this.teams;
    }



    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public void remove(Team team) {
        this.teams.remove(team);
    }

    public Team findByName(String name) {
        for(Team team : this.teams) {
            if(team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public void update(Team team) {
        for (int i = 0; i < this.teams.size(); i++) {
            if (this.teams.get(i).getId().equals(team.getId())) {
                this.teams.set(i, team);
            }
        }
    }

    public Team findById(String id) {
        for(Team team : this.teams) {
            if(team.getId().equals(id)) {
                return team;
            }
        }
        return null;
    }


}

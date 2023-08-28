package cs211.project.models.collections;
import cs211.project.models.Team;
import java.util.ArrayList;

public class TeamCollection {
    private ArrayList<Team> teams;

    public TeamCollection() {
        this.teams = new ArrayList<>();
    }

    public void add(Team team) {
        this.teams.add(team);
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


}

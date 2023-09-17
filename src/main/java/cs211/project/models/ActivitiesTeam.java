package cs211.project.models;

import java.time.LocalDateTime;

public class ActivitiesTeam extends Activities {
    private Team team;

    public ActivitiesTeam(String id, String title, String detail, LocalDateTime startDate, LocalDateTime endDate, Team team) {
        super(id, title, detail, startDate, endDate);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

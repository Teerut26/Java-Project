package cs211.project.models;

import java.time.LocalDateTime;

public class ActivitiesTeam extends Activities {
    private Team team;

    public ActivitiesTeam(String id, String title, String detail, LocalDateTime dateStart, LocalDateTime dateEnd, String startTime, String endTime, Team team) {
        super(id, title, detail, dateStart, dateEnd,startTime, endTime);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        ActivitiesTeam that = (ActivitiesTeam) o;
        return this.getId().equals(that.getId());
    }
}

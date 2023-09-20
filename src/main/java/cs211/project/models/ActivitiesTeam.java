package cs211.project.models;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ActivitiesTeam that = (ActivitiesTeam) o;
        return team.getId().equals(that.team.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(team.getId());
    }
}

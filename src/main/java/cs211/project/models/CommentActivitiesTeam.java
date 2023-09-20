package cs211.project.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentActivitiesTeam extends Comment {
    private Team team;

    public CommentActivitiesTeam(String id, String message, User owner, Team team, LocalDateTime timeStamps) {
        super(id, message, owner, timeStamps);
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommentActivitiesTeam that = (CommentActivitiesTeam) o;
        return this.getId().equals(that.getId());
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

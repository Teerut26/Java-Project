package cs211.project.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentActivitiesTeam extends Comment {
    private ActivitiesTeam activitiesTeam;

    public CommentActivitiesTeam(String id, String message, User owner, ActivitiesTeam activitiesTeam, LocalDateTime timeStamps) {
        super(id, message, owner, timeStamps);
        this.activitiesTeam = activitiesTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommentActivitiesTeam that = (CommentActivitiesTeam) o;
        return this.getId().equals(that.getId());
    }

    public boolean equalsTeam(ActivitiesTeam that) {
        return this.getActivitiesTeam().equals(that);
    }

    public ActivitiesTeam getActivitiesTeam() {
        return activitiesTeam;
    }

    public void setActivitiesTeam(ActivitiesTeam activitiesTeam) {
        this.activitiesTeam = activitiesTeam;
    }

    @Override
    public String toString() {
        return "CommentActivitiesTeam{" +
                "activitiesTeam=" + activitiesTeam +
                ", message='" + message + '\'' +
                '}';
    }
}

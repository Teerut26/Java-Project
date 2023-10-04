package cs211.project.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentActivitiesEvent extends Comment {
    private ActivitiesEvent activitiesEvent;

    public CommentActivitiesEvent(String id, String message, User owner, ActivitiesEvent activitiesEvent, LocalDateTime timeStamps) {
        super(id, message, owner, timeStamps);
        this.activitiesEvent = activitiesEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommentActivitiesEvent that = (CommentActivitiesEvent) o;
        return this.getId().equals(that.getId());
    }

    public boolean equalsEvent(ActivitiesEvent that) {
        return this.getActivitiesEvent().equals(that);
    }

    public void setActivitiesEvent(ActivitiesEvent activitiesEvent) {
        this.activitiesEvent = activitiesEvent;
    }

    public ActivitiesEvent getActivitiesEvent() {
        return activitiesEvent;
    }
}

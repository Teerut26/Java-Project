package cs211.project.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentActivitiesEvent extends Comment {
    private Event event;

    public CommentActivitiesEvent(String id, String message, User owner, Event event, LocalDateTime timeStamps) {
        super(id, message, owner, timeStamps);
        this.event = event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommentActivitiesEvent that = (CommentActivitiesEvent) o;
        return this.getId().equals(that.getId());
    }

    public Event getEvent() {
        return event;
    }
}

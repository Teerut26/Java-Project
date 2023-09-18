package cs211.project.models;

import java.time.LocalDateTime;

public class ActivitiesEvent extends Activities {
    private Event event;

    public ActivitiesEvent(String id, String title, String detail, LocalDateTime startDate, LocalDateTime endDate, Event event) {
        super(id, title, detail, startDate, endDate);
        this.event = event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        ActivitiesEvent that = (ActivitiesEvent) o;
        return this.getId().equals(that.getId());
    }
}

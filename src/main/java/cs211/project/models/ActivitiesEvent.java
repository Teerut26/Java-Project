package cs211.project.models;

import javafx.scene.input.DataFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class ActivitiesEvent extends Activities {
    private Event event;

    public ActivitiesEvent(String id, String title, String detail, LocalDateTime dateStart, LocalDateTime dateEnd, Event event) {
        super(id, title, detail, dateStart, dateEnd);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ActivitiesEvent that = (ActivitiesEvent) o;
        return event.getEventID().equals(that.event.getEventID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(event.getEventID());
    }

}

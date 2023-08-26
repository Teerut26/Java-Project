package cs211.project.models.collections;

import cs211.project.models.Event;

import java.util.ArrayList;

public class EventCollection {
    private ArrayList<Event> events;

    public EventCollection() {
        this.events = new ArrayList<>();
    }

    public void add(Event event) {
        this.events.add(event);
    }

    public Event findByName(String name) {
        for (Event event : this.events) {
            if (event.getNameEvent().equals(name)) {
                return event;
            }
        }
        return null;
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void remove(Event event) {
        this.events.remove(event);
    }

    public Event findById(String id) {
        for (Event event : this.events) {
            if (event.getEventID().equals(id)) {
                return event;
            }
        }
        return null;
    }

}

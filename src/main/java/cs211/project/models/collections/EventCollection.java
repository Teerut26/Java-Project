package cs211.project.models.collections;

import cs211.project.models.Event;

import java.util.ArrayList;

public class EventCollection {
    private ArrayList<Event> events;

    public EventCollection() {
        this.events = new ArrayList<>();
    }

    public void add(Event event) {
        if (this.findById(event.getEventID()) == null) {
            this.events.add(event);
        }
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

    public void update(Event event) {
        for (int i = 0; i < this.events.size(); i++) {
            if (this.events.get(i).getEventID().equals(event.getEventID())) {
                this.events.set(i, event);
            }
        }
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

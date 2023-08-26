package cs211.project.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class Event {
    private String eventID;
    private String nameEvent;
    private String imageEvent;
    private String descriptionEvent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer quantityEvent;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Event(String eventID, String nameEvent, String imageEvent, String descriptionEvent, LocalDateTime startDate, LocalDateTime endDate, Integer quantityEvent) {
        this.eventID = eventID;
        this.nameEvent = nameEvent;
        this.imageEvent = imageEvent;
        this.descriptionEvent = descriptionEvent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantityEvent = quantityEvent;
    }

    public Event() {}

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public void setImageEvent(String imageEvent) {
        this.imageEvent = imageEvent;
    }

    public void setDescriptionEvent(String descriptionEvent) {
        this.descriptionEvent = descriptionEvent;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setQuantityEvent(Integer quantityEvent) {
        this.quantityEvent = quantityEvent;
    }

    public String getEventID() {
        return eventID;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public String getImageEvent() {
        return imageEvent;
    }

    public String getDescriptionEvent() {
        return descriptionEvent;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getQuantityEvent() {
        return quantityEvent;
    }

    public Integer getCurrentMemberParticipatingAmount() {
        return 20;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", nameEvent='" + nameEvent + '\'' +
                ", imageEvent='" + imageEvent + '\'' +
                ", descriptionEvent='" + descriptionEvent + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", quantityEvent=" + quantityEvent +
                '}';
    }
}

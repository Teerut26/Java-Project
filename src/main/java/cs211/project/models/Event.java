package cs211.project.models;

import cs211.project.models.collections.TeamCollection;
import cs211.project.models.collections.UserCollection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private String eventID;
    private String nameEvent;
    private String imageEvent;
    private String descriptionEvent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer quantityEvent;
    private UserCollection userInEvent = new UserCollection();
    private TeamCollection teamInEvent = new TeamCollection();
    private User owner;
    private boolean isPublic = false;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Event(String eventID, String nameEvent, String imageEvent, String descriptionEvent, LocalDateTime startDate, LocalDateTime endDate, Integer quantityEvent, boolean isPublic, User owner) {
        this.eventID = eventID;
        this.nameEvent = nameEvent;
        this.imageEvent = imageEvent;
        this.descriptionEvent = descriptionEvent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantityEvent = quantityEvent;
        this.isPublic = isPublic;
        this.owner = owner;
    }

    public Event(String eventID, String nameEvent, String imageEvent, String descriptionEvent, LocalDateTime startDate, LocalDateTime endDate, Integer quantityEvent, User owner) {
        this.eventID = eventID;
        this.nameEvent = nameEvent;
        this.imageEvent = imageEvent;
        this.descriptionEvent = descriptionEvent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantityEvent = quantityEvent;
        this.isPublic = false;
        this.owner = owner;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setUserInEvent(UserCollection userInEvent) {
        this.userInEvent = userInEvent;
    }

    public void setTeamInEvent(TeamCollection teamInEvent) {
        this.teamInEvent = teamInEvent;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public UserCollection getUserInEvent() {
        return userInEvent;
    }

    public TeamCollection getTeamInEvent() {
        return teamInEvent;
    }

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
        return this.userInEvent.getUsers().size();
    }

    public User getOwner() {
        return owner;
    }

    public void JoinEvent(User user) {
        this.userInEvent.add(user);
    }

    public void LeaveEvent(User user) {
        this.userInEvent.remove(user);
    }

    public void createTeam(Team team) {
        this.teamInEvent.add(team);
    }

    public void deleteTeam(Team team) {
        this.teamInEvent.remove(team);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean checkEventEnded() {
        return LocalDateTime.now().isAfter(this.endDate);
    }

    @Override
    public boolean equals(Object o) {
        Event that = (Event) o;
        return this.eventID.equals(that.eventID);
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

package cs211.project.models;

import cs211.project.models.collections.UserCollection;

import java.time.LocalDateTime;

public class Team {
    private String id;
    private String name;
    private Integer quantity;
    private LocalDateTime startRecruitDate;
    private LocalDateTime endRecruitDate;
    private User owner;
    private Event event;
    private UserCollection userInTeam = new UserCollection();

    public Event getEvent() {
        return event;
    }

    public Team(String id, String name, Integer quantity, LocalDateTime startRecruitDate, LocalDateTime endRecruitDate, User owner, Event event) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.startRecruitDate = startRecruitDate;
        this.endRecruitDate = endRecruitDate;
        this.owner = owner;
        this.event = event;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setUserInTeam(UserCollection userInTeam) {
        this.userInTeam = userInTeam;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getStartRecruitDate() {
        return startRecruitDate;
    }

    public LocalDateTime getEndRecruitDate() {
        return endRecruitDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setStartRecruitDate(LocalDateTime startRecruitDate) {
        this.startRecruitDate = startRecruitDate;
    }

    public void setEndRecruitDate(LocalDateTime endRecruitDate) {
        this.endRecruitDate = endRecruitDate;
    }

    public void joinTeam(User user) {
        this.userInTeam.add(user);
    }

    public void leaveTeam(User user) {
        this.userInTeam.remove(user);
    }

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public UserCollection getUserInTeam() {
        return userInTeam;
    }
}

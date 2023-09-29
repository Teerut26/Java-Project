package cs211.project.models;

import cs211.project.models.collections.UserCollection;

import java.time.LocalDateTime;
import java.util.Objects;

public class Team {
    private String id;
    private String name;
    private Integer quantity;
    private LocalDateTime startRecruitDate;
    private LocalDateTime endRecruitDate;
    private User owner;
    private Event event;

    private String joinStatus;

    private String startTimeTeam;
    private String endTimeTeam;
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
        this.joinStatus = "Not Joined";
        this.setTimeToLocalDateTime();
    }

    private void setTimeToLocalDateTime() {
        String hourStart = String.valueOf(this.startRecruitDate.getHour());
        String minuteStart = String.valueOf(this.startRecruitDate.getMinute());
        String secondStart = String.valueOf(this.startRecruitDate.getSecond());

        String hourEnd = String.valueOf(this.endRecruitDate.getHour());
        String minuteEnd = String.valueOf(this.endRecruitDate.getMinute());
        String secondEnd = String.valueOf(this.endRecruitDate.getSecond());

        this.startTimeTeam = hourStart + ":" + minuteStart + ":" + secondStart;
        this.endTimeTeam = hourEnd + ":" + minuteEnd+ ":" + secondEnd;
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

    public String getStartTimeTeam(){
        return startTimeTeam;
    }

    public String getEndTimeTeam(){
        return endTimeTeam;
    }

    public String getJoinStatus() {
        return this.joinStatus;
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

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
